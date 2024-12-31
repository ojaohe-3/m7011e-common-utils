package m7011e.the_homeric_odyssey.containers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import m7011e.the_homeric_odyssey.configuration.ContainerProperties;

@Configuration
public class KeycloakTestContainer {

    @Bean
    public GenericContainer<?> keycloakContainer(ContainerProperties properties,
                                                 DynamicPropertyRegistry registry) {
        try (GenericContainer<?> keycloak = new GenericContainer<>(properties.getKeycloak().getImage())
                .withExposedPorts(8080)
                .withEnv("KEYCLOAK_ADMIN", properties.getKeycloak().getAdmin())
                .withEnv("KEYCLOAK_ADMIN_PASSWORD", properties.getKeycloak().getAdminPassword())
                .withEnv("KC_DB", "dev-file")
                .withCommand("start-dev")
                .waitingFor(Wait.forHttp("/health/ready").forPort(8080))) {

            registry.add("keycloak.auth-server-url",
                    () -> String.format("http://%s:%d", keycloak.getHost(), keycloak.getFirstMappedPort()));

            keycloak.start();
            return keycloak;
        }
    }
}