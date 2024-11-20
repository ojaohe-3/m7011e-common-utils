import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.cloud.bootstrap.BootstrapConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@AutoConfiguration
@EnableWebSecurity
@BootstrapConfiguration
public class ExceptionHandilingAutoConfiguration {
}
