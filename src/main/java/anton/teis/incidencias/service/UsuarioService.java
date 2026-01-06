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
    public List<Usuarios> getAll() {
        return usuarioRepository.findAll();
    }
    public List<Tecnico> getTecnicos() {
        return usuarioRepository.findAllTecnicos();
    }
    public Usuarios getByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

}
