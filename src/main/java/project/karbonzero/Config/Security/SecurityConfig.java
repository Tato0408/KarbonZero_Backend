package project.karbonzero.Config.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project.karbonzero.Utils.JwtCookieAuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /// Creamos una instancia de JWTCookieAuthFilter
    private final JwtCookieAuthFilter jwtCookieAuthFilter;

    /// Constructor
    public SecurityConfig(JwtCookieAuthFilter jwtCookieAuthFilter) {
        this.jwtCookieAuthFilter = jwtCookieAuthFilter;
    }
    /// No se por que esta es mi parte favorita
    ///
    /// Configuración de seguridad HTTP, aca validamos quienes van a acceder a nuestros datos
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        ///Aqui van todos los endPoints públicos que no requieren de un JWT
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,
                                "/apiAuth/login",
                                "/apiAuth/logout")
                        .permitAll()
                        /// Aca van los que piden auth
                        .requestMatchers("/apiAuth/me").authenticated()
                        .requestMatchers("/apiUser/getAllUsers").hasAnyRole("1", "0")
                        .requestMatchers("/apiUser/getAllRoles").hasAnyRole("2","0")
                        .anyRequest().authenticated())
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtCookieAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // Exponer el AuthenticationManager como bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
