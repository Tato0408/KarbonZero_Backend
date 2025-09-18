package project.karbonzero.Config.App;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.karbonzero.Utils.JWTUtils;
import project.karbonzero.Utils.JwtCookieAuthFilter;

@Configuration
public class AppConfig {
    @Bean
    public JwtCookieAuthFilter jwtCookieAuthFilter(JWTUtils jwtUtils){
        return new JwtCookieAuthFilter(jwtUtils);
    }
}
