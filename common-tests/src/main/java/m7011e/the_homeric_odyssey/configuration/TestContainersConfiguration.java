package m7011e.the_homeric_odyssey.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import m7011e.the_homeric_odyssey.containers.KeycloakTestContainer;
import m7011e.the_homeric_odyssey.containers.PostgresTestContainer;
import m7011e.the_homeric_odyssey.containers.RedisTestContainer;

@Configuration
@EnableConfigurationProperties(ContainerProperties.class)
@Import({
        PostgresTestContainer.class,
        RedisTestContainer.class,
        KeycloakTestContainer.class,
        WebTestClientConfig.class
})
public class TestContainersConfiguration {
}