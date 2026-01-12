package anton.teis.incidencias.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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
                        // usamos el login por defecto de Spring.
                        // al loguearse exitosamente, redirigimos a una ruta base
                        // todo: hacer que segun con que usuario te logguees salga una ruta u otra
                        .defaultSuccessUrl("/web/usuario", true)
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }
}