package anton.teis.incidencias.controller.api;

import anton.teis.incidencias.dto.ExplicacionIncidencia;
import anton.teis.incidencias.dto.IncidenciaData;
import anton.teis.incidencias.entity.incidencia.Incidencia;
import anton.teis.incidencias.entity.incidencia.IncidenciaAbierta;
import anton.teis.incidencias.entity.incidencia.IncidenciaEnProceso;
import anton.teis.incidencias.entity.incidencia.Tipo;
import anton.teis.incidencias.entity.user.Tecnico;
import anton.teis.incidencias.entity.user.Usuario;
import anton.teis.incidencias.entity.user.Usuarios;
import anton.teis.incidencias.service.IncidenciaService;
import anton.teis.incidencias.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class IncidenciaController {

    @Autowired
    private IncidenciaService incidenciaService;

    @Autowired
    private UsuarioService usuarioService;


    /**
     * Obtiene un listado de todas las incidencias
     * @return
     */
    @GetMapping("/api/incidencia/all")
    @ResponseBody
    public Object getAll() {
        try {
            return incidenciaService.getAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtiene una incidencia por su ID
     * @param id
     * @return
     */
    @GetMapping("/api/incidencia/{id}")
    @ResponseBody
    public Object getById(@PathVariable long id) {
        try {
            return incidenciaService.getById(id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Obtiene todas las incidencias en estado abiertas
     * @return
     */
    @GetMapping("/api/incidencia/abiertas")
    @ResponseBody
    public Object getAbiertas() {
        try {
            return incidenciaService.getAllAbiertas();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtiene todas las incidencias en estado abiertas de un tipo determinado
     * @return
     */
    @GetMapping("/api/incidencia/abiertas/{tipo}")
    @ResponseBody
    public Object getAbiertasByTipo(@PathVariable String tipo) {
        // Verificamos que el tipo esté dentro del ENUM
        Tipo t;
        try {
            t = Tipo.valueOf(tipo.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El tipo solo puede ser: OTRO, HARDWARE, SOFTWARE, RED, ERROR");
        }

        return incidenciaService.getAbiertasByTipo(t);

    }

    /**
     * Obtiene todas las incidencias en proceso
     * @return
     */
    @GetMapping("/api/incidencia/enproceso")
    @ResponseBody
    public Object getEnProceso() {
        try {
            return incidenciaService.getAllEnProceso();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtiene todas las incidencias resueltas
     * @return
     */
    @GetMapping("/api/incidencia/resueltas")
    @ResponseBody
    public Object getResueltas() {
        try {
            return incidenciaService.getAllResueltas();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtiene todas las incidencias cerradas
     * @return
     */
    @GetMapping("/api/incidencia/cerradas")
    @ResponseBody
    public Object getCerradas() {
        try {
            return incidenciaService.getAllCerradas();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Abre una incidencia nueva
     * @param incidenciaData
     * @param bindingResult
     * @return
     */
    @PostMapping("/api/incidencia/abrir")
    @ResponseBody
    public Object abrirIncidencia(@ModelAttribute @Valid IncidenciaData incidenciaData, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException("Error");
        }

        // Verificar que existe el usuaruio
        if (incidenciaData.getUsername().isBlank()) {
            throw new RuntimeException("Error, el usuario no ha sido especificado");
        }

        IncidenciaAbierta i = new IncidenciaAbierta();

        // --- Añadir usuario
        // validar que exista el usuario y obtenerlo
        Usuarios u = usuarioService.getByUsername(incidenciaData.getUsername());
        if (u == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        // valida que el usuario sea un usuario normal
        if (u instanceof anton.teis.incidencias.entity.user.Usuario) {
            // 3. Cast y asignación
            i.setUsuario((Usuario) u);
        } else {
            throw new RuntimeException("Los técnicos y administradores no pueden poner incidencias");
        }

        // --- VALIDACIÓN MANUAL DEL ENUM ---
        try {
            i.setTipo(Tipo.valueOf(incidenciaData.getTipo().toUpperCase()));
        } catch (IllegalArgumentException e) {
            return "Error: El tipo '" + incidenciaData.getTipo() + "' no es válido. Debe ser: OTRO, HARDWARE, SOFTWARE, RED o ERROR";
        }

        // --- Añadir el resto de campos
        i.setDescripcion(incidenciaData.getDescripcion());
        i.setIP(incidenciaData.getIP());
        // --- Pone la fecha actual
        i.setMomento(LocalDateTime.now());

        return incidenciaService.abrirIncidencia(i);
    }

    /**
     * Asigna una incidencia abierta o en proceso a un técnico
     * @param id_incidencia
     * @param id_tecnico
     * @return
     */
    @PostMapping("/api/incidencia/pasar/{id_incidencia}/{id_tecnico}")
    @ResponseBody
    public Object asignarIncidencia(@PathVariable long id_incidencia,@PathVariable long id_tecnico) {

        IncidenciaEnProceso incidenciaEnProceso;
        Incidencia i = null;
        try {
            i = incidenciaService.getById(id_incidencia);
        } catch (Exception e) {
            throw new RuntimeException("La incidencia no existe");
        }

        Usuarios u = usuarioService.getById(id_tecnico);
        Tecnico t;

        // Valida que el id sea de un técnico
        if (!(u instanceof Tecnico)) {
            throw new IllegalArgumentException("El usuario a asignar debe ser un técnico");
        } else {
            t = (Tecnico) u;
        }

        // valida que la incidencia sea abierta o en proceso
        if (i instanceof IncidenciaAbierta) {
            // logica si es una incidencia abierta
            return incidenciaService.asignarIncidencia(id_incidencia, t);
        } else if (i instanceof IncidenciaEnProceso) {
            // logica si es una incidencia en proceso
            return incidenciaService.pasarIncidencia(id_incidencia, t);
        } else {
            throw new IllegalArgumentException("La incidencia debe estar abierta o en proceso");
        }

    }

    /**
     * Resuelve una incidencia y le da una descripción de la solución
     * @param explicacionIncidencia
     * @param id
     * @return
     */
    @PostMapping("/api/incidencia/resolver/{id}")
    @ResponseBody
    public Object resolverIncidencia(
            @ModelAttribute @Valid ExplicacionIncidencia explicacionIncidencia,
            @PathVariable long id
            )
    {
        Incidencia i;
        // conseguimos la incidencia con el id
        try {
            i = incidenciaService.getById(id);
        } catch (Exception e) {
            throw new RuntimeException("La incidencia no existe");
        }

        // Verificamos que la incidencia sea una incidenciaEnProceso
        if (!(i instanceof IncidenciaEnProceso)) {
            throw new IllegalArgumentException("Solo se pueden resolver incidencias en proceso");
        }

        return incidenciaService.resolverIncidencia(id, explicacionIncidencia.getMotivo());

    }

    @PostMapping("/api/incidencia/cerrar/{id}")
    @ResponseBody
    public Object cerrarIncidencia(
            @ModelAttribute @Valid ExplicacionIncidencia explicacionIncidencia,
            @PathVariable long id
    ) {
        // conseguir la incidencia y verificar que existe
        Incidencia i;
        try {
            i = incidenciaService.getById(id);
        } catch (Exception e) {
            throw new RuntimeException("No se ha encontrado la incidencia");
        }

        // verificar que el tipo de incidencia es en proceso
        if (!(i instanceof IncidenciaEnProceso)) {
            throw new IllegalArgumentException("Solo se pueden cerrar incidencias en proceso");
        }

        return incidenciaService.cerrarIncidencia(id, explicacionIncidencia.getMotivo());

    }

    /**
     * Obtiene todas las incidencias en las que ha trabajdo / esta trabajando un tecnico
     * @param id
     * @return
     */
    @GetMapping("/api/incidencia/tecnico/{id}")
    @ResponseBody
    public Object incidenciasTecnico(@PathVariable long id) {

        // Verifica si el usuario es tecnico
        Usuarios u = usuarioService.getById(id);
        if (!(u instanceof Tecnico)) {
            throw new IllegalArgumentException("El usuario ha de ser un tecnico");
        }

        return incidenciaService.getIncidenciasByTecnico(id);

    }

    /**
     * Obtiene todas las incidencias reportadas por un usuario y separadas en tipo
     * @param id
     * @return
     */
    @GetMapping("/api/incidencia/usuario/{id}")
    @ResponseBody
    public Object incidenciasUsuario(@PathVariable long id) {
        // Verifica que sea un usuario normal
        Usuarios u = usuarioService.getById(id);
        if (!(u instanceof Usuario)) {
            throw new IllegalArgumentException("Solamente los usuarios normales pueden tener incidencias");
        }

        return incidenciaService.getIncidenciasByUsuario(id);

    }
}
