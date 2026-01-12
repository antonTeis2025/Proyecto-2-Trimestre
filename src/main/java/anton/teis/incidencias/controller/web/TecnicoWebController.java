package anton.teis.incidencias.controller.web;

import anton.teis.incidencias.dto.ExplicacionIncidencia;
import anton.teis.incidencias.entity.incidencia.*;
import anton.teis.incidencias.entity.user.Tecnico;
import anton.teis.incidencias.entity.user.Usuarios;
import anton.teis.incidencias.service.IncidenciaService;
import anton.teis.incidencias.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/web/tecnico")
public class TecnicoWebController {

    // Por simplicidad, usamos un técnico de ejemplo con ID 2
    // todo: manejarlo con springSecurity
    Long tecnicoId = 2L;

    @Autowired
    private IncidenciaService incidenciaService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String dashboard(Model model) {

        Usuarios usuario = usuarioService.getById(tecnicoId);

        if (!(usuario instanceof Tecnico)) {
            return "redirect:/web";
        }

        model.addAttribute("incidencias", incidenciaService.getIncidenciasByTecnico(tecnicoId));
        model.addAttribute("tecnico", usuario);

        return "tecnico/dashboard";
    }

    @GetMapping("/incidencias-disponibles")
    public String incidenciasDisponibles(Model model) {
        List<IncidenciaAbierta> incidenciasAbiertas = incidenciaService.getAllAbiertas();
        List<IncidenciaEnProceso> incidenciasEnProceso = incidenciaService.getAllOtrosTecnicos(tecnicoId);

        model.addAttribute("abiertas", incidenciasAbiertas);
        model.addAttribute("enProceso", incidenciasEnProceso);
        return "tecnico/incidencias-disponibles";
    }

    @PostMapping("/asignar-incidencia/{id}")
    public String asignarIncidencia(@PathVariable Long id, Model model) {
        Long tecnicoId = 2L;

        try {
            Tecnico tecnico = usuarioService.getTecnicoById(tecnicoId);
            Incidencia incidencia = incidenciaService.getById(id);

            if (incidencia instanceof IncidenciaAbierta) {
                incidenciaService.asignarIncidencia(id, tecnico);
            } else if (incidencia instanceof IncidenciaEnProceso) {
                incidenciaService.pasarIncidencia(id, tecnico);
            }

            model.addAttribute("success", "Incidencia asignada correctamente");
        } catch (Exception e) {
            model.addAttribute("error", "Error al asignar la incidencia: " + e.getMessage());
        }

        return "redirect:/web/tecnico/incidencias-disponibles";
    }

    @GetMapping("/resolver-incidencia/{id}")
    public String resolverIncidenciaForm(@PathVariable Long id, Model model) {
        try {
            Incidencia incidencia = incidenciaService.getById(id);
            if (!(incidencia instanceof IncidenciaEnProceso)) {
                throw new IllegalArgumentException("Solo se pueden resolver incidencias en proceso");
            }
            model.addAttribute("incidencia", incidencia);
            model.addAttribute("explicacion", new ExplicacionIncidencia()); // ← ¡Importante!
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/web/tecnico";
        }
        return "tecnico/resolver-incidencia";
    }

    @PostMapping("/resolver-incidencia/{id}")
    public String resolverIncidencia(
            @PathVariable Long id,
            @Valid @ModelAttribute("explicacion") ExplicacionIncidencia explicacion,
            BindingResult bindingResult,
            Model model) {

        // 1. Si hay errores de validación, volver a cargar TODO el modelo
        if (bindingResult.hasErrors()) {
            model.addAttribute("incidencia", incidenciaService.getById(id)); // ← Obligatorio
            model.addAttribute("explicacion", explicacion); // ← Obligatorio (con los datos ya introducidos)
            return "tecnico/resolver-incidencia"; // ← No redirect
        }

        // 2. Si no hay errores, procesar
        try {
            incidenciaService.resolverIncidencia(id, explicacion.getMotivo());
            return "redirect:/web/tecnico?success=Resuelta";
        } catch (Exception e) {
            model.addAttribute("error", "Error: " + e.getMessage());
            model.addAttribute("incidencia", incidenciaService.getById(id));
            model.addAttribute("explicacion", explicacion);
            return "tecnico/resolver-incidencia";
        }
    }

    @GetMapping("/cerrar-incidencia/{id}")
    public String cerrarIncidenciaForm(@PathVariable Long id, Model model) {
        try {
            Incidencia incidencia = incidenciaService.getById(id);
            if (!(incidencia instanceof IncidenciaEnProceso)) {
                throw new IllegalArgumentException("Solo se pueden cerrar incidencias en proceso");
            }
            model.addAttribute("incidencia", incidencia);
            model.addAttribute("explicacion", new ExplicacionIncidencia()); // ← Obligatorio
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/web/tecnico";
        }
        return "tecnico/cerrar-incidencia";
    }

    @PostMapping("/cerrar-incidencia/{id}")
    public String cerrarIncidencia(
            @PathVariable Long id,
            @Valid @ModelAttribute("explicacion") ExplicacionIncidencia explicacion,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            // ← ¡Clave! Volver a cargar TODO el modelo
            model.addAttribute("incidencia", incidenciaService.getById(id));
            model.addAttribute("explicacion", explicacion); // ← con los datos ya introducidos
            return "tecnico/cerrar-incidencia"; // ← no redirect
        }

        try {
            incidenciaService.cerrarIncidencia(id, explicacion.getMotivo());
            return "redirect:/web/tecnico?success=Incidencia cerrada correctamente";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cerrar la incidencia: " + e.getMessage());
            model.addAttribute("incidencia", incidenciaService.getById(id));
            model.addAttribute("explicacion", explicacion);
            return "tecnico/cerrar-incidencia";
        }
    }

    @GetMapping("/historial")
    public String historial(Model model) {
        Long tecnicoId = 2L;
        model.addAttribute("incidencias", incidenciaService.getIncidenciasByTecnico(tecnicoId));
        return "tecnico/historial";
    }
}