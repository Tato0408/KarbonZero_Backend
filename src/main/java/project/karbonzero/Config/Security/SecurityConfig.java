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
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /// Creamos una instancia de JWTCookieAuthFilter
    private final JwtCookieAuthFilter jwtCookieAuthFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    /// Constructor
    public SecurityConfig(JwtCookieAuthFilter jwtCookieAuthFilter, CorsConfigurationSource corsConfigurationSource) {
        this.jwtCookieAuthFilter = jwtCookieAuthFilter;
        this.corsConfigurationSource = corsConfigurationSource;
    }
    /// No se por que esta es mi parte favorita
    ///
    /// Configuración de seguridad HTTP, aca validamos quienes van a acceder a nuestros datos
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource)) // ← Configura CORS aquí
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/apiAuth/login", "/apiAuth/logout").permitAll()
                        /// Aca van los que piden auth
                        .requestMatchers("/apiAuth/me").authenticated()
                        ///Usuarios
                        .requestMatchers("/apiUser/getAllUsers").hasAnyRole("1", "0")
                        .requestMatchers("/apiUser/getAllRoles").hasAnyRole("2","0")
                        /// Consumos
                        .requestMatchers("/apiConsumption/getAllConsumptions").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/getConsumptionById/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/getConsumptionsByName/{name}").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/getConsumptionsByFilters").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/getConsumptionsByFiltersM").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/getConsumptionCO2/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/insertConsumption").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/updateConsumption/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/deleteConsumption/{id}").hasAnyRole("1", "0")
                        /// Recursos
                        .requestMatchers("/apiConsumption/getAllResourcesList").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/getAllResources").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/getResourceById/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/getResourcesByName/{name}").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/getResourceConsumptions/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/getResourceConsumptionsCO2/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/getAllResourcesConsumptionsCO2").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/getAllResourceConsumptionsCO2Total").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/getAllResourcePuritiesList").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/getAllResourcePurities").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/getResourcePurityById/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/getResourcePuritiesByName/{name}").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/getAllResourcePurityConsumptions/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/insertResource").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/insertResourcePurity").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/updateResource/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/updateResourcePurity/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/deleteResource/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiConsumption/deleteResourcePurity/{id}").hasAnyRole("1", "0")
                        .anyRequest().authenticated())
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtCookieAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
