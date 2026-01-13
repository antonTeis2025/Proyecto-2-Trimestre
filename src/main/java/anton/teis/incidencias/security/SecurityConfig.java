package anton.teis.incidencias.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationSuccessHandler customSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        // 1. Recursos estáticos
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                        // =======================================================
                        //  PROTECCIÓN DE API
                        // =======================================================
                        // solo los usuarios pueden abrir incidencias
                        .requestMatchers("/api/incidencia/abrir").hasRole("USUARIO")

                        // la parte de incidencias solamente para tecnicos y admins
                        .requestMatchers("/api/incidencia/**").hasAnyRole("TECNICO", "ADMINISTRADOR")
                        // la parte de usuarios solo para admins
                        .requestMatchers("/api/user/**").hasRole("ADMINISTRADOR")

                        // =======================================================
                        // 3. PROTECCIÓN WEB
                        // =======================================================
                        .requestMatchers("/web/administrador/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/web/tecnico/**").hasRole("TECNICO")
                        .requestMatchers("/web/usuario/**").hasRole("USUARIO")

                        // Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .successHandler(customSuccessHandler)
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll())
                .exceptionHandling((exception) -> exception
                        .accessDeniedPage("/acceso-denegado")
                )
                // deshabilitar sesiones csrf para la API
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers("/api/**")
                );

        return http.build();
    }
}