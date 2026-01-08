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
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UsuarioService usuarioService;




    /*
    curl.exe -X POST http://localhost:8080/api/user/create `
          -d "nombre=Juan" `
          -d "password=123456" `
          -d "privilegios=tecnico" `
          -d "username=jperez" `
          -d "apellido=Perez"
     */

    /**
     * Request tipo POST para recibir datos de un formulario (todo: formulario)
     * @param userData
     * @param bindingResult
     * @param model
     * @return
     */
    @PostMapping("/api/user/create")
    @ResponseBody // TEMPORAL -> Devolver texto plano para debugging
    public String crearUsuario(@ModelAttribute @Valid UserData userData, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "error";
        }

        try {
            // crear el modelo de usuario en función del campo privilegios
            switch (userData.getPrivilegios().toLowerCase()) {
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
                default -> { // engloba "usuario"
                    Usuario usuario = new Usuario();
                    usuario.copiarDto(userData);
                    usuarioService.guardar(usuario);
                }
            }
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }

        return "success";
    }

    /**
     * Actualiza los datos de un usuario. NO cambia la contraseña (todo: hacer endpoint cambio contraseña)
     * Campos: username, nombre, apellidos
     *
     * @param id
     * @param userData
     * @param bindingResult
     * @return
     */
    @PutMapping("/api/user/update/{id}")
    @ResponseBody
    public String actualizarUsuario(@PathVariable Long id, @ModelAttribute @Valid UserData userData, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "error";
        }

        try {
            // Buscar usuario por ID
            Usuarios u = usuarioService.getById(id);
            if (u == null) {
                return "error: Usuario " + id + " no encontrado";
            }
            // verificar que haya username, nombre y apellido, campos necesarios para actualizar
            if (userData.getUsername() == null || userData.getUsername().isBlank() ||
                    userData.getNombre() == null || userData.getNombre().isBlank() ||
                    userData.getApellido() == null || userData.getApellido().isBlank()) {
                return "ERROR: Los campos username, nombre y apellido son obligatorios.";
            }
            // actualizar datos xd
            usuarioService.update(id, userData.getUsername(), userData.getNombre(), userData.getApellido());

        } catch (Exception e) {
            return "error: " + e.getMessage();
        }

        return "success";
    }



    /*
            TODO
                - Cambiar contraseña (esperar a springsecurity)
                - Dar de baja un usuario
                - Comprobar si contraseña es correcta
                - Lista de usuarios Json
                - Usuario por ID
                - Usuario por username
     */


}
