package anton.teis.incidencias.controller;

import anton.teis.incidencias.dto.UserData;
import anton.teis.incidencias.entity.user.Administrador;
import anton.teis.incidencias.entity.user.Tecnico;
import anton.teis.incidencias.entity.user.Usuario;
import anton.teis.incidencias.entity.user.Usuarios;
import anton.teis.incidencias.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UsuarioService usuarioService;


    // Request tipo POST para recibir datos de un formulario (todo: formulario)
    @PostMapping
    public String crearUsuario(@ModelAttribute @Valid UserData userData, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "error";
        }

        // crear el modelo de usuario en función del campo privilegios
        switch (userData.getPrivilegios()) {
            case "tecnico" -> {
                Tecnico tecnico = new Tecnico();
                tecnico.copiarDto(userData);
                usuarioService.guardar(tecnico);
            }
            case "administrador" -> {
                Administrador administrador = new Administrador();
                administrador.copiarDto(userData);
                usuarioService.guardar(administrador);
            }
            default -> { // engloba usuario
                Usuario usuario = new Usuario();
                usuario.copiarDto(userData);
                usuarioService.guardar(usuario);
            }
        }

        return "success";
    }

}
