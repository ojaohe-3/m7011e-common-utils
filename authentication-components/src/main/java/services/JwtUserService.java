package services;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class JwtUserService {

    @Value("{security.authentication.client-id}")
    private String clientId;



    public List<String> getRoles(Jwt jwt) {
        String KEYCLOAK_ROLE_CLAIM = "realm_access.roles";
        List<String> claimedRoles = Optional.ofNullable(jwt.getClaimAsStringList(KEYCLOAK_ROLE_CLAIM)).orElse(List.of());

        Set<String> roles = Optional.ofNullable(jwt.getClaimAsMap("resource_access"))
                .map(access -> {
                            Set<?> clientRoles = (Set<?>) Optional.ofNullable(access.get(clientId)).orElse(Set.of());
                            return clientRoles.stream().map(Object::toString).collect(Collectors.toSet());
                        }
                )
                .orElse(Set.of());
        return Stream.concat(claimedRoles.stream(), roles.stream()).collect(Collectors.toList());
    }

    /**
     * Get claim userId from jwt token.
     * @param jwt
     * @return
     */
    public Optional<String> getUserId(@NonNull Jwt jwt) {
        return Optional.ofNullable(jwt.getClaimAsString("sub"));
    }

    /**
     * Get claim email from jwt token.
     * @param jwt
     * @return
     */
    public Optional<String> getEmail(@NonNull Jwt jwt) {
        return Optional.ofNullable(jwt.getClaimAsString("email"));
    }


}
