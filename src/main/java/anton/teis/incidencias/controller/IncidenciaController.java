package anton.teis.incidencias.controller;

import anton.teis.incidencias.dto.IncidenciaData;
import anton.teis.incidencias.dto.UserData;
import anton.teis.incidencias.entity.incidencia.Incidencia;
import anton.teis.incidencias.entity.incidencia.IncidenciaAbierta;
import anton.teis.incidencias.entity.user.Usuario;
import anton.teis.incidencias.entity.user.Usuarios;
import anton.teis.incidencias.service.IncidenciaService;
import anton.teis.incidencias.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Controller
public class IncidenciaController {

    @Autowired
    private IncidenciaService incidenciaService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/api/incidencia/abrir")
    @ResponseBody
    private Incidencia abrirIncidencia(@ModelAttribute @Valid IncidenciaData incidenciaData, BindingResult bindingResult) {
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

        // --- Añadir el resto de campos
        i.setDescripcion(incidenciaData.getDescripcion());
        i.setIP(incidenciaData.getIP());
        i.setTipo(incidenciaData.getTipo());
        // --- Pone la fecha actual
        i.setMomento(LocalDateTime.now());

        return incidenciaService.abrirIncidencia(i);

    }
}
