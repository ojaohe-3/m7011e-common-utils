package m7011e.the_homeric_odyssey.authentication_components;

import m7011e.the_homeric_odyssey.authentication_components.configuration.AuthenticationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.bootstrap.BootstrapConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan
@BootstrapConfiguration
@EnableConfigurationProperties(AuthenticationProperties.class)
public class AuthenticationComponentAutoConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}