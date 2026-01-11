package anton.teis.incidencias.controller.api;

import anton.teis.incidencias.dto.UserData;
import anton.teis.incidencias.entity.user.Administrador;
import anton.teis.incidencias.entity.user.Tecnico;
import anton.teis.incidencias.entity.user.Usuario;
import anton.teis.incidencias.entity.user.Usuarios;
import anton.teis.incidencias.exceptions.NotFoundException;
import anton.teis.incidencias.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Object crearUsuario(@ModelAttribute @Valid UserData userData, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "error";
        }

        long id = -1;

        try {
            // crear el modelo de usuario en función del campo privilegios
            switch (userData.getPrivilegios().toLowerCase()) {
                case "tecnico" -> {
                    Tecnico tecnico = new Tecnico();
                    tecnico.copiarDto(userData);
                    return usuarioService.guardar(tecnico);
                }
                case "administrador" -> {
                    Administrador administrador = new Administrador();
                    administrador.copiarDto(userData);
                    return usuarioService.guardar(administrador);
                }
                default -> { // engloba "usuario"
                    Usuario usuario = new Usuario();
                    usuario.copiarDto(userData);
                    return usuarioService.guardar(usuario);
                }
            }
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }

    }

    /*
    curl.exe -X PUT http://localhost:8080/api/user/update/8 `
          -d "username=jperez_nuevo" `
          -d "nombre=Juan" `
          -d "apellido=Perez"
     */
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
    public Object actualizarUsuario(@PathVariable Long id, @ModelAttribute @Valid UserData userData, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "error";
        }

        try {
            // Buscar usuario por ID
            Usuarios u = usuarioService.getById(id);
            if (u == null) {
                throw new NotFoundException();
            }
            // verificar que haya username, nombre y apellido, campos necesarios para actualizar
            if (userData.getUsername() == null || userData.getUsername().isBlank() ||
                    userData.getNombre() == null || userData.getNombre().isBlank() ||
                    userData.getApellido() == null || userData.getApellido().isBlank()) {
                return "ERROR: Los campos username, nombre y apellido son obligatorios.";
            }
            // actualizar datos xd
            return usuarioService.update(id, userData.getUsername(), userData.getNombre(), userData.getApellido());

        } catch (Exception e) {
            return "error: " + e.getMessage();
        }

    }

    // curl.exe -X POST http://localhost:8080/api/user/disable/8
    /**
     * Desactiva un usuario para que no aparezca listado
     * @param id
     * @return
     */
    @PostMapping("/api/user/disable/{id}")
    @ResponseBody
    public Object darDeBaja(@PathVariable long id) {
        try {
            usuarioService.darDeBaja(id);
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }

        return "success";
    }

    // curl.exe -X POST http://localhost:8080/api/user/enable/8
    /**
     * Reactiva un usuario que estaba de baja
     * @param id
     * @return
     */
    @PostMapping("/api/user/enable/{id}")
    @ResponseBody
    public Object reactivar(@PathVariable long id) {
        try {
            return usuarioService.reactivar(id);
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    /**
     * Lista todos los usuarios
     * @return
     */
    @GetMapping("/api/user/all")
    @ResponseBody
    public ResponseEntity<List<Usuarios>> getAll() {
        List<Usuarios> u = usuarioService.getAll();
        if (u.isEmpty() || u == null) {
            throw new NotFoundException();
        }
        return ResponseEntity.ok(u);
    }

    /**
     * Lista los usuarios activos
     * @return
     */
    @GetMapping("/api/user/active")
    @ResponseBody
    public ResponseEntity<List<Usuarios>> getActive() {
        List<Usuarios> u = usuarioService.getActivos();
        if (u.isEmpty() || u == null) {
            throw new NotFoundException();
        }
        return ResponseEntity.ok(u);
    }

    /**
     * Obtiene un usuario por su ID
     * @param id
     * @return
     */
    @GetMapping("/api/user/{id}")
    @ResponseBody
    public ResponseEntity<Usuarios> getById(@PathVariable long id) {
        Usuarios u = usuarioService.getById(id);
        if (u == null) {
            throw new NotFoundException();
        }
        return ResponseEntity.ok(u);
    }

    /**
     * Obtiene un usuario por su nombre de usuario
     * @param name
     * @return
     */
    @GetMapping("/api/user/name/{name}")
    @ResponseBody
    public ResponseEntity<Usuarios> getByName(@PathVariable String name) {
        Usuarios u = usuarioService.getByUsername(name);
        if (u == null) {
            throw new NotFoundException();
        }
        return ResponseEntity.ok(u);
    }


    /*
            TODO
                - Manejo de errores (no encuentra usuario con ID X)
                - Cambiar contraseña (esperar a springsecurity)
     */


}
