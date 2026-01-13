package anton.teis.incidencias.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class BaseController {

    @GetMapping
    public String index(Authentication authentication) {
        // usuario no autenticado -> login
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        // dependiendo del rol, se le manda a una pagina u otra
        var authorities = authentication.getAuthorities();

        // administrador
        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR")) ||
                authorities.contains(new SimpleGrantedAuthority("ADMINISTRADOR"))) {
            return "redirect:/web/administrador";
        }

        // tecnico
        if (authorities.contains(new SimpleGrantedAuthority("ROLE_TECNICO")) ||
                authorities.contains(new SimpleGrantedAuthority("TECNICO"))) {
            return "redirect:/web/tecnico";
        }

        // usuario
        if (authorities.contains(new SimpleGrantedAuthority("ROLE_USUARIO")) ||
                authorities.contains(new SimpleGrantedAuthority("USUARIO"))) {
            return "redirect:/web/usuario";
        }

        // si algo falla se le desloguea
        return "redirect:/logout";
    }
}