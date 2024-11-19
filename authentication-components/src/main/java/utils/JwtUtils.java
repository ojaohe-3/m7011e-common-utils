package utils;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class JwtUtils {

    public boolean jwtHasWriteRole(String role, List<String> roles) {
        String matchRole = role.toLowerCase();
        return roles.stream().anyMatch(r -> matchRole.equals(r.toLowerCase()))
                && roles.contains("write");
    }


    public boolean jwtHasReadRole(String role, List<String> roles) {
        String matchRole = role.toLowerCase();
        return roles.stream().anyMatch(r -> matchRole.equals(r.toLowerCase()))
                && roles.contains("read");
    }
}
