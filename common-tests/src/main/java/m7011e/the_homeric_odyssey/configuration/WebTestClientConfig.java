package m7011e.the_homeric_odyssey.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;

@Configuration
public class WebTestClientConfig {

    @Bean
    public WebTestClient webTestClient(@Value("${local.server.port}") Integer port) {
        return WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .responseTimeout(Duration.ofSeconds(30))
                .build();
    }
}
