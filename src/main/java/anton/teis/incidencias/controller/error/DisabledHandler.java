package anton.teis.incidencias.controller.error;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class DisabledHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException, java.io.IOException {

        // Si el error es porque el usuario está deshabilitado (de baja)
        if (exception instanceof DisabledException) {
            setDefaultFailureUrl("/cuenta-baja"); // Redirigir a la página específica
        } else {
            // Para cualquier otro error (contraseña mal, usuario no existe), comportamiento normal
            setDefaultFailureUrl("/login?error");
        }

        super.onAuthenticationFailure(request, response, exception);
    }
}
