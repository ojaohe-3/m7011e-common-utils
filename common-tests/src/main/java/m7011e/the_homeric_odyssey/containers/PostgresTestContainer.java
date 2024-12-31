package m7011e.the_homeric_odyssey.containers;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;
import m7011e.the_homeric_odyssey.configuration.ContainerProperties;

@Configuration
public class PostgresTestContainer {

    @Bean
    public PostgreSQLContainer<?> postgresContainer(ContainerProperties properties,
                                                    DynamicPropertyRegistry registry) {
        try (PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(properties.getPostgres().getImage())) {

            registry.add("spring.datasource.url", postgres::getJdbcUrl);
            registry.add("spring.datasource.username", postgres::getUsername);
            registry.add("spring.datasource.password", postgres::getPassword);

            postgres.start();
            return postgres;
        }
    }
}
