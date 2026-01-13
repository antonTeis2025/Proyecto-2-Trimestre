package anton.teis.incidencias.controller.web;

import anton.teis.incidencias.dto.ContrasinalData;
import anton.teis.incidencias.dto.UserData;
import anton.teis.incidencias.entity.user.*;
import anton.teis.incidencias.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping({"/web/administrador", "/web/administrador/"})
public class AdministradorWebController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String dashboard(Model model) {
        List<Usuarios> usuarios = usuarioService.getAll();
        List<UsuarioConRol> usuariosConRol = usuarios.stream()
                .map(u -> new UsuarioConRol(u, getRol(u)))
                .toList();
        model.addAttribute("usuarios", usuariosConRol);
        return "administrador/dashboard";
    }

    @GetMapping({"/crear-usuario", "/crear-usuario/"})
    public String crearUsuarioForm(Model model) {
        model.addAttribute("userData", new UserData());
        model.addAttribute("tiposUsuario", new String[]{"usuario", "tecnico", "administrador"});
        return "administrador/crear-usuario";
    }

    @PostMapping({"/crear-usuario", "/crear-usuario/"})
    public String crearUsuario(@ModelAttribute @Valid UserData userData, BindingResult bindingResult, Model model) {
        // validacion nombre de usuario
        if (usuarioService.exists(userData.getUsername())) {
            bindingResult.rejectValue("username", "error.userData", "El nombre de usuario ya está en uso");
        }

        // Comprobamos si hay errores de validación (como password corta)
        if (bindingResult.hasErrors()) {
            // Recargamos la lista de roles para que el <select> no falle al pintar de nuevo
            model.addAttribute("tiposUsuario", new String[]{"usuario", "tecnico", "administrador"});
            // Retornamos la vista directamente (NO redirect) para mostrar los errores
            return "administrador/crear-usuario";
        }

        try {
            Usuarios usuario = null;

            switch (userData.getPrivilegios().toLowerCase()) {
                case "tecnico":
                    usuario = new Tecnico();
                    break;
                case "administrador":
                    usuario = new Administrador();
                    break;
                default:
                    usuario = new Usuario();
            }

            usuario.copiarDto(userData);
            usuarioService.guardar(usuario);
            return "redirect:/web/administrador?success=Usuario creado correctamente";
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear el usuario: " + e.getMessage());
            model.addAttribute("tiposUsuario", new String[]{"usuario", "tecnico", "administrador"});
            return "administrador/crear-usuario";
        }
    }

    @GetMapping({"/editar-usuario/{id}", "/editar-usuario/{id}/"})
    public String editarUsuarioForm(@PathVariable Long id, Model model) {
        try {
            Usuarios usuario = usuarioService.getById(id);
            model.addAttribute("usuario", usuario);
            UserData userData = new UserData();
            userData.setUsername(usuario.getUsername());
            userData.setNombre(usuario.getNombre());
            userData.setApellido(usuario.getApellidos());
            // Determinar el rol basado en la clase del usuario
            if (usuario instanceof Administrador) {
                userData.setPrivilegios("administrador");
            } else if (usuario instanceof Tecnico) {
                userData.setPrivilegios("tecnico");
            } else if (usuario instanceof Usuario) {
                userData.setPrivilegios("usuario");
            }
            model.addAttribute("userData", userData);
            model.addAttribute("tiposUsuario", new String[]{"usuario", "tecnico", "administrador"});
            return "administrador/editar-usuario";
        } catch (Exception e) {
            return "redirect:/web/administrador?error=" + e.getMessage();
        }
    }

    @PostMapping({"/editar-usuario/{id}", "/editar-usuario/{id}/"})
    public String editarUsuario(@PathVariable Long id, @ModelAttribute UserData userData, Model model) {
        try {
            usuarioService.update(id, userData.getUsername(), userData.getNombre(), userData.getApellido());
            return "redirect:/web/administrador?success=Usuario actualizado correctamente";
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar el usuario: " + e.getMessage());
            return editarUsuarioForm(id, model);
        }
    }

    @PostMapping({"/dar-baja/{id}", "/dar-baja/{id}/"})
    public String darDeBaja(@PathVariable Long id, Model model) {
        try {
            usuarioService.darDeBaja(id);
            return "redirect:/web/administrador?success=Usuario dado de baja correctamente";
        } catch (Exception e) {
            model.addAttribute("error", "Error al dar de baja al usuario: " + e.getMessage());
            return "redirect:/web/administrador";
        }
    }

    @PostMapping({"/reactivar/{id}", "/reactivar/{id}/"})
    public String reactivar(@PathVariable Long id, Model model) {
        try {
            usuarioService.reactivar(id);
            return "redirect:/web/administrador?success=Usuario reactivado correctamente";
        } catch (Exception e) {
            model.addAttribute("error", "Error al reactivar al usuario: " + e.getMessage());
            return "redirect:/web/administrador";
        }
    }

    private String getRol(Usuarios usuario) {
        if (usuario instanceof Usuario) return "Usuario";
        if (usuario instanceof Tecnico) return "Tecnico";
        if (usuario instanceof Administrador) return "Administrador";
        return "Desconocido";
    }

    // implementaciones cambio de contraseña

    @GetMapping({"/cambiar-contrasinal/{id}", "/cambiar-contrasinal/{id}/"})
    public String mostrarFormularioCambio(@PathVariable("id") Long id, Model model) {
        Usuarios usuario = usuarioService.getById(id);


        model.addAttribute("usuario", usuario);
        model.addAttribute("contrasinalData", new ContrasinalData());

        return "administrador/cambiar-contrasinal";
    }

    @PostMapping({"/cambiar-contrasinal/{id}", "/cambiar-contrasinal/{id}/"})
    public String actualizarContrasinal(
            @PathVariable Long id,
            @Valid @ModelAttribute("contrasinalData") ContrasinalData contrasinalData,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("usuario", usuarioService.getById(id));
            return "administrador/cambiar-contrasinal";
        }

        try {
            usuarioService.changePassword(id, contrasinalData.getPass1());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "redirect:/web/administrador?success";
    }


}