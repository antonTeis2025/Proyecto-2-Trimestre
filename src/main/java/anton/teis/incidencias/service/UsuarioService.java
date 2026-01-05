package anton.teis.incidencias.service;

import anton.teis.incidencias.entity.user.Administrador;
import anton.teis.incidencias.entity.user.Tecnico;
import anton.teis.incidencias.entity.user.Usuario;
import anton.teis.incidencias.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Metodos para crear los 3 tipos de usuario
    public Usuario crearUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
        return usuario;
    }
    public Tecnico crearTecnico(Tecnico tecnico) {
        usuarioRepository.save(tecnico);
        return tecnico;
    }
    public Administrador crearAdministrador(Administrador administrador) {
        usuarioRepository.save(administrador);
        return administrador;
    }

}
