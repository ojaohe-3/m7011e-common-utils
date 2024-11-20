import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.bootstrap.BootstrapConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@BootstrapConfiguration
@EnableAutoConfiguration
public class AuthenticationComponentAutoConfiguration {
}