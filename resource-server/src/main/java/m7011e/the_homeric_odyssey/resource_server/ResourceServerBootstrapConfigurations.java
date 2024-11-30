package m7011e.the_homeric_odyssey.resource_server;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.cloud.bootstrap.BootstrapConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@AutoConfiguration
@EnableWebSecurity
@EnableMethodSecurity
@BootstrapConfiguration
@ComponentScan()
@PropertySource(value = "classpath:security.yaml")
@PropertySource(value = "classpath:resource-server.yaml")
public class ResourceServerBootstrapConfigurations {
}
