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
                        /// Recursos/getAllResourcesSP
                        .requestMatchers("/apiResource/getAllResourcesList").hasAnyRole("1", "0")
                        .requestMatchers("/apiResource/getAllResources").hasAnyRole("1", "0")
                        .requestMatchers("/apiResource/getResourceById/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiResource/getResourcesByName/{name}").hasAnyRole("1", "0")
                        .requestMatchers("/apiResource/getResourceConsumptions/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiResource/getResourceConsumptionsCO2/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiResource/getAllResourcesConsumptionsCO2").hasAnyRole("1", "0")
                        .requestMatchers("/apiResource/getAllResourceConsumptionsCO2Total").hasAnyRole("1", "0")
                        .requestMatchers("/apiResource/getAllResourcePuritiesList").hasAnyRole("1", "0")
                        .requestMatchers("/apiResource/getAllResourcePurities").hasAnyRole("1", "0")
                        .requestMatchers("/apiResource/getResourcePurityById/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiResource/getResourcePuritiesByName/{name}").hasAnyRole("1", "0")
                        .requestMatchers("/apiResource/getAllResourcePurityConsumptions/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiResource/insertResource").hasAnyRole("1", "0")
                        .requestMatchers("/apiResource/insertResourcePurity").hasAnyRole("1", "0")
                        .requestMatchers("/apiResource/updateResource/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiResource/updateResourcePurity/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiResource/deleteResource/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiResource/deleteResourcePurity/{id}").hasAnyRole("1", "0")
                        /// MeasuresgetAllMeasuresList
                        .requestMatchers("/apiMeasure/getAllMeasuresList").hasAnyRole("1", "0")
                        .requestMatchers("/apiMeasure/getAllMeasures").hasAnyRole("1", "0")
                        .requestMatchers("/apiMeasure/getMeasureById/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiMeasure/getMeasuresByName/{name}").hasAnyRole("1", "0")
                        .requestMatchers("/apiMeasure/getAllMeasureUnitsList").hasAnyRole("1", "0")
                        .requestMatchers("/apiMeasure/getAllMeasureUnits").hasAnyRole("1", "0")
                        .requestMatchers("/apiMeasure/getMeasureUnitById/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiMeasure/getMeasureUnitsByName/{name}").hasAnyRole("1", "0")
                        .requestMatchers("/apiMeasure/getAllConversionUnits").hasAnyRole("1", "0")
                        .requestMatchers("/apiMeasure/getConversionUnitById/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiMeasure/getConversionUnitsByName/{name}").hasAnyRole("1", "0")
                        .requestMatchers("/apiMeasure/convertMeasureUnit").hasAnyRole("1", "0")
                        .requestMatchers("/apiMeasure/insertMeasure").hasAnyRole("1", "0")
                        .requestMatchers("/apiMeasure/insertMeasureUnit").hasAnyRole("1", "0")
                        .requestMatchers("/apiMeasure/insertConversionUnit").hasAnyRole("1", "0")
                        .requestMatchers("/apiMeasure/updateMeasure/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiMeasure/updateMeasureUnit/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiMeasure/updateConversionUnit/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiMeasure/deleteMeasure/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiMeasure/deleteMeasureUnit/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiMeasure/deleteConversionUnit/{id}").hasAnyRole("1", "0")
                        /// Users
                        .requestMatchers("/apiUser/confirmPassword").hasAnyRole("1", "0")
                        .requestMatchers("/apiUser/getAllUsers").hasAnyRole("1", "0")
                        .requestMatchers("/apiUser/getUserById/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiUser/getUserByEmail/{email}").hasAnyRole("1", "0")
                        .requestMatchers("/apiUser/getUsersByUsername/{username}").hasAnyRole("1", "0")
                        .requestMatchers("/apiUser/getUserConsumptions/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiUser/getAllRolesList").hasAnyRole("1", "0")
                        .requestMatchers("/apiUser/getAllRoles").hasAnyRole("1", "0")
                        .requestMatchers("/apiUser/getRolById/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiUser/insertUser").hasAnyRole("1", "0")
                        .requestMatchers("/apiUser/insertRol").hasAnyRole("1", "0")
                        .requestMatchers("/apiUser/updateUser/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiUser/updateRol/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiUser/deleteUser/{id}").hasAnyRole("1", "0")
                        .requestMatchers("/apiUser/deleteRol/{id}").hasAnyRole("1", "0")
                        /// Claudinary
                        .requestMatchers("/api/image/upload").hasAnyRole("1", "0")
                        .requestMatchers("/api/image/upload-to-folder").hasAnyRole("1", "0")
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
