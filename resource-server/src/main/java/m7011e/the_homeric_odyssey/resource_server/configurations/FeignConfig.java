package m7011e.the_homeric_odyssey.resource_server.configurations;

import feign.RequestInterceptor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "m7011e.the_homeric_odyssey.resource_server")
public class FeignConfig {

    private final JwtFeignInterceptor jwtFeignInterceptor;

    public FeignConfig(JwtFeignInterceptor jwtFeignInterceptor) {
        this.jwtFeignInterceptor = jwtFeignInterceptor;
    }

    @Bean
    public RequestInterceptor jwtRequestInterceptor() {
        return jwtFeignInterceptor;
    }
}