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
                        // permitir recursos estáticos (css, js, imagenes)
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        // rutas protegidas por rol
                        .requestMatchers("/web/administrador/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/web/tecnico/**").hasRole("TECNICO")
                        .requestMatchers("/web/usuario/**").hasRole("USUARIO")
                        // cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .successHandler(customSuccessHandler)
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll())
                // pagina 403 cuando no puede acceder un usuario a un recurso
                .exceptionHandling((exception) -> exception
                        .accessDeniedPage("/acceso-denegado")
                );

        return http.build();
    }
}