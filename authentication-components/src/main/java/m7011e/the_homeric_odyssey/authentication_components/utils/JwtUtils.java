package m7011e.the_homeric_odyssey.authentication_components.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class JwtUtils {

    public boolean jwtHasWriteRole(String role, Collection<GrantedAuthority> roles) {
        String matchRole = role.toUpperCase();
        return roles.stream().anyMatch(r -> r.getAuthority().equals(matchRole))
                && roles.stream().anyMatch(r -> r.getAuthority().equals("WRITE"));
    }


    public boolean jwtHasReadRole(String role, Collection<GrantedAuthority> roles) {
        String matchRole = role.toUpperCase();
        return roles.stream().anyMatch(r -> r.getAuthority().equals(matchRole))
                && roles.stream().anyMatch(r -> r.getAuthority().equals("READ"));

    }
}
