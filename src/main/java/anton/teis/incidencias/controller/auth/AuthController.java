package anton.teis.incidencias.controller.auth;

import anton.teis.incidencias.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Al hacer un post a /api/auth/login con username y passwd en json
     * devuelve un jwt valido con una validez de 10 dias
     * @param credenciales
     * @return
     */
    @PostMapping("/api/auth/login")
    public Map<String, String> login(@RequestBody Map<String, String> credenciales) {
        String username = credenciales.get("username");
        String password = credenciales.get("password");

        // autentica con spring segurity
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        // si la autenticacion funciona, genera el token
        if (authentication.isAuthenticated()) {
            String token = jwtUtils.generateToken(username);
            return Map.of("token", token); // { "token": "" }
        } else {
            throw new RuntimeException("Credenciales inválidas");
        }
    }

    @GetMapping("cuenta-baja")
    public String cuentaDeshabilitada() {
        return "error/deshabilitado";
    }
}
