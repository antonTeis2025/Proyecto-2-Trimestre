package anton.teis.incidencias.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String targetUrl = "";

        // consigo el rol del usuario que acaba de iniciar sesión
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // depende del rol que tenga le mando a una ruta
        for (GrantedAuthority grantedAuthority : authorities) {
            String authorityName = grantedAuthority.getAuthority();

            // Nota: Spring Security suele añadir el prefijo "ROLE_" automáticamente
            if (authorityName.equals("ROLE_ADMINISTRADOR") || authorityName.equals("ADMINISTRADOR")) {
                targetUrl = "/web/administrador";
                break;
            } else if (authorityName.equals("ROLE_TECNICO") || authorityName.equals("TECNICO")) {
                targetUrl = "/web/tecnico";
                break;
            } else if (authorityName.equals("ROLE_USUARIO") || authorityName.equals("USUARIO")) {
                targetUrl = "/web/usuario";
                break;
            }
        }

        // si no tiene rol (muy extraño) le mando al login otra vez
        if (targetUrl.isEmpty()) {
            throw new IllegalStateException("El usuario no tiene un rol válido asignado para redirección");
        }

        // Realizamos la redirección
        if (response.isCommitted()) {
            return;
        }
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }
}