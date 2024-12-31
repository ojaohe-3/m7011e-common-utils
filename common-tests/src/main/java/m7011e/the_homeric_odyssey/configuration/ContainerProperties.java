package m7011e.the_homeric_odyssey.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "test.containers")
@Getter
@Setter
public class ContainerProperties {
    PostgresProperties postgres = new PostgresProperties();
    RedisProperties redis = new RedisProperties();
    KeycloakProperties keycloak = new KeycloakProperties();

    @Getter
    @Setter
    public static class PostgresProperties {
        String image = "postgres:15-alpine";
    }

    @Getter
    @Setter
    public static class RedisProperties {
        String image = "redis:7-alpine";
        Integer port = 6379;
    }

    @Getter
    @Setter
    public static class KeycloakProperties {
        String image = "quay.io/keycloak/keycloak:21.1";
        String admin = "admin";
        String adminPassword = "admin";
    }

}
