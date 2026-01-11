package anton.teis.incidencias.controller.web;

import anton.teis.incidencias.dto.IncidenciaData;
import anton.teis.incidencias.entity.incidencia.IncidenciaAbierta;
import anton.teis.incidencias.entity.incidencia.Tipo;
import anton.teis.incidencias.entity.user.Usuario;
import anton.teis.incidencias.entity.user.Usuarios;
import anton.teis.incidencias.service.IncidenciaService;
import anton.teis.incidencias.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/web/usuario")
public class UsuarioWebController {

    @Autowired
    private IncidenciaService incidenciaService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String dashboard(Model model) {
        // Por simplicidad, usamos un usuario de ejemplo con ID 1
        Long usuarioId = 1L;
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
    public String abrirIncidenciaForm(Model model) {
        model.addAttribute("incidenciaData", new IncidenciaData());
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
        incidenciaData.setUsername("pedro");

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
        Long usuarioId = 1L;
        Usuarios usuario = usuarioService.getById(usuarioId);

        if (!(usuario instanceof Usuario)) {
            return "redirect:/web";
        }

        model.addAttribute("incidencias", incidenciaService.getIncidenciasByUsuario(usuarioId));
        return "usuario/dashboard";
    }
}