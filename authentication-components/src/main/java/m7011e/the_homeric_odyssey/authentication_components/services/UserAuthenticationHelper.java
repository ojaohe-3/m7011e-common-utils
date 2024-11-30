package m7011e.the_homeric_odyssey.authentication_components.services;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import m7011e.the_homeric_odyssey.resource_server.models.RealmUserType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import m7011e.the_homeric_odyssey.authentication_components.utils.JwtUtils;

import java.util.*;

@Service
@AllArgsConstructor
public class UserAuthenticationHelper {

    /**
     * Get all roles of the current authenticated user
     *
     * @return List of user roles
     */
    public Collection<GrantedAuthority> getCurrentUserRoles() {
        return Optional
                .ofNullable(getJwtToken())
                .map(JwtAuthenticationToken::getAuthorities)
                .orElse(List.of());
    }

    /**
     * Check if the current user has a specific role
     *
     * @param role Role to check
     * @return true if user has the role, false otherwise
     */
    public boolean hasRole(RealmUserType role) {
        return getCurrentUserRoles()
                .stream()
                .anyMatch(userRole -> userRole.getAuthority().equals(role.getRoleName()));
    }

    /**
     * Check if the current user write access on a specific role
     *
     * @param role Role to check
     * @return true if user has the role, false otherwise
     */
    public boolean hasWriteRole(RealmUserType role) {
        return JwtUtils.jwtHasWriteRole(role.getRoleName(), getCurrentUserRoles());
    }

    /**
     * Check if the current user read access on a specific role
     *
     * @param role Role to check
     * @return true if user has the role, false otherwise
     */
    public boolean hasReadRole(RealmUserType role) {
        return JwtUtils.jwtHasReadRole(role.getRoleName(), getCurrentUserRoles());
    }


    /**
     * Determines if the current user possesses read access for any role defined in the
     * RealmUserType enumeration.
     *
     * @return true if the user has read access to at least one role; false otherwise
     */
    public boolean hasAnyReadRole() {
        return Arrays.stream(RealmUserType.values())
                .anyMatch(this::hasReadRole);
    }

    /**
     * Checks if the current user has write access for any role defined in the
     * RealmUserType enumeration.
     *
     * @return true if the user has write access to at least one role; false otherwise
     */
    public boolean hasAnyWriteRole() {
        return Arrays.stream(RealmUserType.values())
                .anyMatch(this::hasWriteRole);
    }


    public Optional<String> getUserId() {
        return Optional
                .ofNullable(getJwtToken())
                .map(JwtAuthenticationToken::getTokenAttributes)
                .map(it -> it.get("sub"))
                .map(Object::toString);
    }


    public Optional<String> getUserScope() {
        return Optional
                .ofNullable(getJwtToken())
                .map(JwtAuthenticationToken::getTokenAttributes)
                .map(it -> it.get("scope"))
                .map(Object::toString);
    }

    @Nullable
    public JwtAuthenticationToken getJwtToken() {
        return (JwtAuthenticationToken) getAuthentication()
                .filter(authentication -> authentication instanceof JwtAuthenticationToken).orElse(null);
    }

    private static Optional<Authentication> getAuthentication() {
        return getContext().map(SecurityContext::getAuthentication);
    }

    private static Optional<SecurityContext> getContext() {
        return Optional.ofNullable(SecurityContextHolder.getContext());
    }
}
