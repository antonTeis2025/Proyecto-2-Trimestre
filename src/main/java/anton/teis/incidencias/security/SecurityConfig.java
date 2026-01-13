package anton.teis.incidencias.security;


import anton.teis.incidencias.controller.error.DisabledHandler;
import anton.teis.incidencias.security.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private anton.teis.incidencias.security.jwt.JwtFilter jwtFilter; // filtro jwt de la API

    @Autowired
    private AuthenticationSuccessHandler customSuccessHandler;

    @Autowired
    private DisabledHandler disabledHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // =======================================================
    // SEGURIDAD DE LA API
    // =======================================================
    @Bean
    @Order(1) // se evalua primero
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**") // solamente aplica a la API
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // endpoint para obtener el token (publico)
                        .requestMatchers("/api/auth/**").permitAll()

                        // los usuarios solo podran abrir incidencias
                        .requestMatchers("/api/incidencia/abrir").hasRole("USUARIO")
                        // los tecnicos y administradores pueden gestionar las incidencias
                        .requestMatchers("/api/incidencia/**").hasAnyRole("TECNICO", "ADMINISTRADOR")
                        // solo administradores pueden gestionar los usuarios
                        .requestMatchers("/api/user/**").hasRole("ADMINISTRADOR")
                        .anyRequest().authenticated()
                )
                // Añadimos el filtro JWT antes del filtro de usuario/contraseña
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // =======================================================
    // SEGURIDAD PARA WEB
    // =======================================================
    @Bean
    @Order(2) // se valida despues de la API
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Recursos estáticos
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/cuenta-baja").permitAll()
                        // reglas de acceso a la web
                        .requestMatchers("/web/administrador/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/web/tecnico/**").hasRole("TECNICO")
                        .requestMatchers("/web/usuario/**").hasRole("USUARIO")

                        // login y acceso denegado públicos
                        .requestMatchers("/login", "/acceso-denegado").permitAll()

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // si el login es correcto, se maneja para saber a donde redireccionar
                        .successHandler(customSuccessHandler)
                        .failureHandler(disabledHandler)
                        .permitAll()
                )
                .logout(logout -> logout.permitAll())
                // pagina 403 para cuando se deniega el acceso
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/acceso-denegado")
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}