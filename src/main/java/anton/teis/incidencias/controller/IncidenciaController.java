package anton.teis.incidencias.controller;

import anton.teis.incidencias.dto.IncidenciaData;
import anton.teis.incidencias.dto.UserData;
import anton.teis.incidencias.entity.incidencia.Incidencia;
import anton.teis.incidencias.entity.incidencia.IncidenciaAbierta;
import anton.teis.incidencias.entity.incidencia.IncidenciaEnProceso;
import anton.teis.incidencias.entity.incidencia.Tipo;
import anton.teis.incidencias.entity.user.Tecnico;
import anton.teis.incidencias.entity.user.Usuario;
import anton.teis.incidencias.entity.user.Usuarios;
import anton.teis.incidencias.service.IncidenciaService;
import anton.teis.incidencias.service.UsuarioService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Controller
public class IncidenciaController {

    /*
        TODO:
            - Endpoints para leer los datos
                -/all
                - /id
                - /abiertas
                - /cerradas
                - /enproceso
                - /resueltas
                - /tecnico/id
                    Devuelve en las que está trabajando y en las que ha participado
     */

    @Autowired
    private IncidenciaService incidenciaService;

    @Autowired
    private UsuarioService usuarioService;

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
        Incidencia i = incidenciaService.getById(id_incidencia);

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

}
