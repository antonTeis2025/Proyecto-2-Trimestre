package anton.teis.incidencias.controller.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController {

    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "error/403";
    }
}