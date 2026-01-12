package anton.teis.incidencias.controller.web;

import anton.teis.incidencias.dto.IncidenciaData;
import anton.teis.incidencias.entity.incidencia.Incidencia;
import anton.teis.incidencias.entity.incidencia.IncidenciaAbierta;
import anton.teis.incidencias.entity.incidencia.Tipo;
import anton.teis.incidencias.entity.user.Usuario;
import anton.teis.incidencias.entity.user.Usuarios;
import anton.teis.incidencias.service.IncidenciaService;
import anton.teis.incidencias.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/web/usuario")
public class UsuarioWebController {

    // temporal
    // todo: springsecurity
    Long usuarioId = 7L;

    @Autowired
    private IncidenciaService incidenciaService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String dashboard(Model model) {

        Usuarios usuario = usuarioService.getById(usuarioId);

        if (!(usuario instanceof Usuario)) {
            return "redirect:/web";
        }

        model.addAttribute("incidencias", incidenciaService.getIncidenciasByUsuario(usuarioId));
        model.addAttribute("usuario", usuario);
        model.addAttribute("tipos", Tipo.values());

        return "usuario/dashboard";
    }

    @GetMapping("/abrir-incidencia")
    public String abrirIncidenciaForm(Model model, HttpServletRequest request) {

        IncidenciaData incidenciaData = new IncidenciaData();
        // Obtenemos la IP local del usuario
        String ip = getUserIp(request);

        // cargamos ya la IP en el DTO
        incidenciaData.setIP(ip);

        model.addAttribute("incidenciaData", incidenciaData);
        model.addAttribute("tipos", Tipo.values());
        return "usuario/abrir-incidencia";
    }



    @PostMapping("/abrir-incidencia")
    public String abrirIncidencia(
            // @ModelAttribute @Valid IncidenciaData incidenciaData, todo: el usuario no se manda y se requiere en la validacion
            @ModelAttribute @Valid IncidenciaData incidenciaData,
            BindingResult bindingResult,
            Model model
    ) {
        // Por simplicidad, establecemos un usuario de ejemplo
        String username = usuarioService.getById(usuarioId).getUsername();
        incidenciaData.setUsername(username);

        System.out.println("Errores de validación: " + bindingResult.getAllErrors());
        // Si hay errores de validación, volvemos al formulario con los errores
        if (bindingResult.hasErrors()) {
            model.addAttribute("tipos", Tipo.values());
            return "usuario/abrir-incidencia";
        }


        try {
            IncidenciaAbierta incidencia = new IncidenciaAbierta();
            Usuario usuario = (Usuario) usuarioService.getByUsername(incidenciaData.getUsername());
            incidencia.setUsuario(usuario);
            incidencia.setDescripcion(incidenciaData.getDescripcion());
            incidencia.setIP(incidenciaData.getIP());
            incidencia.setTipo(Tipo.valueOf(incidenciaData.getTipo().toUpperCase()));
            incidencia.setMomento(LocalDateTime.now());
            incidenciaService.abrirIncidencia(incidencia);
            model.addAttribute("success", "Incidencia abierta correctamente");
            return "redirect:/web/usuario";
        } catch (Exception e) {
            model.addAttribute("error", "Error al abrir la incidencia: " + e.getMessage());
            model.addAttribute("tipos", Tipo.values());
            return "usuario/abrir-incidencia";
        }
    }

    @GetMapping("/incidencias")
    public String verIncidencias(Model model) {

        Usuarios usuario = usuarioService.getById(usuarioId);

        if (!(usuario instanceof Usuario)) {
            return "redirect:/web";
        }

        model.addAttribute("incidencias", incidenciaService.getIncidenciasByUsuario(usuarioId));
        return "usuario/dashboard";
    }

    private static @Nullable String getUserIp(HttpServletRequest request) {
        // Intentamos obtener la IP del header X-Forwarded-Forç
        // (aparese ahi cuando hay un proxy en la red)
        String ip = request.getHeader("X-Forwarded-For");

        // vemos a ver si pescamos algo
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            // si no pescamos nada obtenermos directamente la IP q nos hace la peticion
            ip = request.getRemoteAddr();
        }
        // Limpieza: A veces X-Forwarded-For devuelve varias IPs (ej: "client, proxy1, proxy2")
        // Nos quedamos con la primera
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0];
        }

        // Si estás probando en local (localhost), esto devolverá 0:0:0:0:0:0:0:1.
        // lo cambiamos a localhost de toda la puta vida
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "localhost";
        }
        return ip;
    }

}