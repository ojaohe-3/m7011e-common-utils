package m7011e.the_homeric_odyssey.authentication_components.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@ConfigurationProperties(prefix = "security.authentication")
@PropertySource("classpath:security.yaml")
@Getter
@Setter
public class AuthenticationProperties {
    private SystemUser systemUser = new SystemUser();

    private String url;

    private String clientId;

    private String clientSecret;

    @Getter
    @Setter
    public static class SystemUser {
        private String username;
        private String password;
    }
}
