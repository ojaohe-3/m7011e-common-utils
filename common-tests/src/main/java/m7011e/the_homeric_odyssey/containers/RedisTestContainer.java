package m7011e.the_homeric_odyssey.containers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import m7011e.the_homeric_odyssey.configuration.ContainerProperties;

@Configuration
public class RedisTestContainer {

    @Bean
    public GenericContainer<?> redisContainer(ContainerProperties properties,
                                              DynamicPropertyRegistry registry) {
        try (GenericContainer<?> redis = new GenericContainer<>(properties.getRedis().getImage())
                .withExposedPorts(properties.getRedis().getPort())
                .waitingFor(Wait.forLogMessage(".*Ready to accept connections.*\\n", 1))) {

            registry.add("spring.redis.host", redis::getHost);
            registry.add("spring.redis.port", redis::getFirstMappedPort);

            redis.start();
            return redis;
        }
    }
}
