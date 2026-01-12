package anton.teis.incidencias.service;

import anton.teis.incidencias.entity.user.Administrador;
import anton.teis.incidencias.entity.user.Tecnico;
import anton.teis.incidencias.entity.user.Usuario;
import anton.teis.incidencias.entity.user.Usuarios;
import anton.teis.incidencias.repository.UsuarioRepository;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Metodos para crear los 3 tipos de usuario
    public Usuarios guardar(Usuarios u) {
        return usuarioRepository.save(u);
    }

    // Metodos para leer de la base de datos de usuarios
    public Usuarios getById(long id) {
        return usuarioRepository.findById(id).get();
    }
    public Tecnico getTecnicoById(long id) {
        return usuarioRepository.findTecnicoById(id);
    }
    public List<Usuarios> getAll() {
        return usuarioRepository.findAll();
    }
    public List<Usuarios> getActivos() {
        return usuarioRepository.findAll().stream().filter(Usuarios::isAlta).toList();
    }
    public List<Tecnico> getTecnicos() {
        return usuarioRepository.findAllTecnicos().stream().filter(Tecnico::isAlta).toList();
    }
    public Usuarios getByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    // Metodos para actualizar datos de los usuarios
    public Usuarios update(
            long id,
            String username,
            String nombre,
            String apellidos
    ) {
        Usuarios old = usuarioRepository.findById(id).get();
        old.setUsername(username);
        old.setNombre(nombre);
        old.setApellidos(apellidos);

        return usuarioRepository.save(old);
    }
    public boolean checkPassword(long id, String password) { // verificar si la contraseña antigua es correcta antes de cambiarla
        Usuarios u = usuarioRepository.findById(id).get();
        return u.getPassword().equals(password);
    }
    // TODO: Implementar cambio de contraseña con el cifrador de spring security
    public Usuarios changePassword(long id, String newPassword) {
        Usuarios old = usuarioRepository.findById(id).get();
        old.setPassword(newPassword);
        return usuarioRepository.save(old);
    }
    public Usuarios darDeBaja(long id) {
        Usuarios u = usuarioRepository.findById(id).get();
        u.setAlta(false);
        return usuarioRepository.save(u);
    }
    public Usuarios reactivar(long id) {
        Usuarios u = usuarioRepository.findById(id).get();
        u.setAlta(true);
        return usuarioRepository.save(u);
    }

    public boolean exists(String username) {
        return usuarioRepository.existsByUsername(username);
    }

}
