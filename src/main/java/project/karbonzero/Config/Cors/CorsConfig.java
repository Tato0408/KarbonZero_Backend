package project.karbonzero.Config.Cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;


@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Configuración esencial
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost");
        config.addAllowedOrigin("https://localhost");
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedOrigin("http://localhost");
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedOrigin("https://karbon-zero-front-end.vercel.app/");
        // Desarrollo local: cualquier puerto de localhost (los frontends de Vite usan 5173, 5174, ...)
        config.setAllowedOriginPatterns(Arrays.asList("http://localhost:*", "http://127.0.0.1:*"));

        /// Métodos HTTP permitidos
        config.addAllowedMethod("GET");

        /// Métodos HTTP permitidos
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("PATCH");

        /// Cabeceras permitidas
        config.addAllowedHeader("Origin");
        config.addAllowedHeader("Content-Type");
        config.addAllowedHeader("Accept");
        config.addAllowedHeader("Authorization");
        config.addAllowedHeader("X-Requested-With");
        config.addAllowedHeader("Access-Control-Request-Method");
        config.addAllowedHeader("Access-Control-Request-Headers");
        config.addAllowedHeader("Cookie");
        config.addAllowedHeader("Set-Cookie");

        config.setExposedHeaders(Arrays.asList(
                "Set-Cookie", "Cookie", "Authorization", "Content-Disposition"
        ));

        /// Tiempo de cache para preflight requests
        config.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    /// CORS PARA EL SECCONFIGGGGGGG
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedOrigin("http://localhost:4200");
        configuration.addAllowedOrigin("https://localhost");
        configuration.addAllowedOrigin("http://localhost:5173");
        configuration.addAllowedOrigin("http://localhost");
        configuration.addAllowedOrigin("https://*.herokuapp.com");
        configuration.addAllowedOrigin("https://karbon-zero-front-end.vercel.app/");
        // Desarrollo local: cualquier puerto de localhost (los frontends de Vite usan 5173, 5174, ...)
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:*", "http://127.0.0.1:*"));

        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");

        configuration.addExposedHeader("Set-Cookie");
        configuration.addExposedHeader("Cookie");
        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
