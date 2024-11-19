package services;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import utils.JwtUtils;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserAuthenticationHelper {

    private final JwtUserService jwtUserService;

    /**
     * Get all roles of the current authenticated user
     *
     * @return List of user roles
     */
    public List<String> getCurrentUserRoles() {
        return Optional
                .ofNullable(getJwtToken())
                .map(JwtAuthenticationToken::getToken)
                .map(jwtUserService::getRoles)
                .orElse(List.of());
    }

    /**
     * Check if the current user has a specific role
     *
     * @param role Role to check
     * @return true if user has the role, false otherwise
     */
    public boolean hasRole(String role) {
        return getCurrentUserRoles()
                .stream()
                .anyMatch(userRole -> userRole.equalsIgnoreCase(role));
    }

    /**
     * Check if the current user write access on a specific role
     *
     * @param role Role to check
     * @return true if user has the role, false otherwise
     */
    public boolean hasWriteRole(String role) {
        return JwtUtils.jwtHasWriteRole(role, getCurrentUserRoles());
    }

    /**
     * Check if the current user read access on a specific role
     *
     * @param role Role to check
     * @return true if user has the role, false otherwise
     */
    public boolean hasReadRole(String role) {
        return JwtUtils.jwtHasReadRole(role, getCurrentUserRoles());
    }

    @Nullable
    private static JwtAuthenticationToken getJwtToken() {
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
