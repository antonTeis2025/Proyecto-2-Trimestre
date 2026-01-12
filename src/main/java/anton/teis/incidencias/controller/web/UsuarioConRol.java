package anton.teis.incidencias.controller.web;

import anton.teis.incidencias.entity.user.Usuarios;
import lombok.Data;

@Data
public class UsuarioConRol {
    private Long id;
    private String nombre;
    private String apellidos;
    private String username;
    private boolean alta;
    private String rol;


    public UsuarioConRol(Usuarios u, String rol) {
        this.id = u.getId();
        this.nombre = u.getNombre();
        this.apellidos = u.getApellidos();
        this.username = u.getUsername();
        this.alta = u.isAlta();
        this.rol = rol;
    }
}
