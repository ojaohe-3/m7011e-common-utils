package m7011e.the_homeric_odyssey.resource_server.configurations;

import jakarta.validation.constraints.NotNull;
import m7011e.the_homeric_odyssey.resource_server.models.RealmUserType;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    public static final String ROLES = "roles";
    public static final String RESOURCE_ACCESS = "resource_access";
    public static final String REALM_ACCESS = "realm_access";
    private final String clientId;

    public CustomJwtAuthenticationConverter(String clientId) {
        this.clientId = clientId;
    }


    @Override
    public AbstractAuthenticationToken convert(@NotNull Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
        return new JwtAuthenticationToken(jwt, authorities);
    }

    /**
     * Extracts authorities from the given JWT by aggregating both realm and resource roles.
     *
     * @param jwt the JSON Web Token from which to extract roles as granted authorities
     * @return a collection of granted authorities derived from the roles present in the JWT
     */
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        return Stream.of(
                        extractRealmRoles(jwt),
                        extractResourceRoles(jwt)
                )
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    /**
     * Extracts realm roles from the given JSON Web Token (JWT) and converts them into a collection of granted authorities.
     * The roles are filtered and mapped to corresponding `GrantedAuthority` objects based on the enumerated type `RealmUserType`.
     *
     * @param jwt the JSON Web Token from which to extract realm roles
     * @return a collection of granted authorities derived from the realm roles present in the JWT
     */
    private Collection<GrantedAuthority> extractRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim(REALM_ACCESS);
        if (realmAccess == null) {
            return List.of();
        }

        List<String> realmRoles = Optional.ofNullable((List<String>) realmAccess.get(ROLES)).orElse(List.of());
        return realmRoles.stream()
                .map(String::toUpperCase)
                .filter(roleName -> EnumUtils.isValidEnum(RealmUserType.class, roleName))
                .map(RealmUserType::valueOf)
                .map(roleName -> new SimpleGrantedAuthority(roleName.getRoleName()))
                .collect(Collectors.toList());
    }

    /**
     * Extracts resource roles from the given JSON Web Token (JWT) and converts them into a collection
     * of granted authorities. The roles are filtered and mapped to corresponding `GrantedAuthority`
     * objects based on the enumerated type `RealmUserType`.
     *
     * @param jwt the JSON Web Token from which to extract resource roles
     * @return a collection of granted authorities derived from the resource roles present in the JWT
     */
    private Collection<GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim(RESOURCE_ACCESS);
        if (resourceAccess == null) {
            return List.of();
        }
        Map<String, Object> clientResource = (Map<String, Object>) resourceAccess.get(clientId);
        var clientRoles = (List<String>) clientResource.get(ROLES);
        return clientRoles.stream()
                .map(String::toUpperCase)
                .filter(roleName -> EnumUtils.isValidEnum(RealmUserType.class, roleName))
                .map(RealmUserType::valueOf)
                .map(roleName -> new SimpleGrantedAuthority(roleName.getRoleName()))
                .collect(Collectors.toList());
    }
}
