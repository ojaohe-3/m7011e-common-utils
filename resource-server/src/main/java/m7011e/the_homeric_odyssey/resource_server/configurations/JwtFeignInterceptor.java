package m7011e.the_homeric_odyssey.resource_server.configurations;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtFeignInterceptor implements RequestInterceptor {

    /**
     * Attaches an Authorization header with a Bearer token to the given request template,
     * derived from the current security context's authentication with JWT credentials.
     *
     * @param requestTemplate the request template to which the Authorization header is to be added
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getCredentials() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getCredentials();
            requestTemplate.header(
                    "Authorization",
                    "Bearer " + jwt.getTokenValue()
            );
        }
    }
}
