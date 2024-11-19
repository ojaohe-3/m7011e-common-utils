package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

@Service
@PropertySource("classpath:security.yaml")
public class SystemUserAuthenticationService {

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    @Value("${security.authentication.system-user.username}")
    private String systemUsername;

    @Value(value = "${security.authentication.system-user.password}")
    private String systemPassword;

    @Value("${oauth2.token-uri}")
    private String tokenUri;

    private OAuth2AccessToken currentSystemToken;

    private Instant lastTokenFetchTime;

    public SystemUserAuthenticationService(
            RestTemplate restTemplate,
            ObjectMapper objectMapper
    ) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Get a valid system user access token
     *
     * @return Valid OAuth2 Access Token
     */
    public synchronized OAuth2AccessToken getSystemUserToken() {
        if (currentSystemToken != null && !isTokenExpired(currentSystemToken)) {
            return currentSystemToken;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // TODO fix for auth-server
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");
        map.add("client_id", systemUsername);
        map.add("client_secret", systemPassword);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            String responseBody = restTemplate.postForObject(tokenUri, request, String.class);
            var responseMap = objectMapper.readValue(responseBody, Map.class);
            currentSystemToken = new OAuth2AccessToken(
                    OAuth2AccessToken.TokenType.BEARER,
                    (String) responseMap.get("access_token"),
                    Instant.now(),
                    Instant.now().plusSeconds(Long.parseLong(responseMap.get("expires_in").toString()))
            );

            lastTokenFetchTime = Instant.now();
            return currentSystemToken;
        } catch (Exception e) {
            throw new SystemTokenFetchException("Failed to obtain system user token", e);
        }
    }

    /**
     * Check if a token is expired
     *
     * @param token OAuth2 Access Token
     * @return true if token is expired, false otherwise
     */
    private boolean isTokenExpired(OAuth2AccessToken token) {
        // Add a buffer of 5 minutes to account for network/processing delays
        Instant expirationWithBuffer = token.getExpiresAt().minusSeconds(300);
        return Instant.now().isAfter(expirationWithBuffer);
    }

    /**
     * Execute a privileged operation with system user token
     *
     * @param privilegedOperation The operation to execute
     * @param <T>                 Return type of the operation
     * @return Result of the privileged operation
     */
    public <T> T executeWithSystemToken(Supplier<T> privilegedOperation) {
        OAuth2AccessToken systemToken = getSystemUserToken();

        Authentication originalAuth = SecurityContextHolder.getContext().getAuthentication();
        try {
            OAuth2AuthenticationToken systemAuth = createSystemAuthentication(systemToken);
            SecurityContextHolder.getContext().setAuthentication(systemAuth);

            return privilegedOperation.get();
        } finally {
            SecurityContextHolder.getContext().setAuthentication(originalAuth);
        }
    }

    /**
     * Create OAuth2 Authentication token for system user
     *
     * @param token System user access token
     * @return OAuth2AuthenticationToken
     */
    private OAuth2AuthenticationToken createSystemAuthentication(OAuth2AccessToken token) {
        return new OAuth2AuthenticationToken(
                new OAuth2User() {
                    @Override
                    public Map<String, Object> getAttributes() {
                        return Collections.singletonMap("token", token.getTokenValue());
                    }

                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        return Collections.singletonList(
                                new SimpleGrantedAuthority("ROLE_SYSTEM")
                        );
                    }

                    @Override
                    public String getName() {
                        return systemUsername;
                    }
                },
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_SYSTEM")),
                systemUsername
        );
    }

    public static class SystemTokenFetchException extends RuntimeException {
        public SystemTokenFetchException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}