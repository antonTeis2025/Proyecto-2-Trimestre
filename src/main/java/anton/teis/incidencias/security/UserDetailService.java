package anton.teis.incidencias.security;

import anton.teis.incidencias.entity.user.Administrador;
import anton.teis.incidencias.entity.user.Tecnico;
import anton.teis.incidencias.entity.user.Usuarios;
import anton.teis.incidencias.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // buscar usuario en el service
        Usuarios u = usuarioService.getByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("El usuario no existe");
        }

        // obtener el rol en string
        String rol = "USUARIO";
        if (u instanceof Tecnico) {
            rol = "TECNICO";
        } else if (u instanceof Administrador) {
            rol = "ADMINISTRADOR";
        }

        // todo: quitar noop con BCryptPasswordEncoder
        return User.builder()
                .username(username)
                .password("{noop}" + u.getPassword())
                .roles(rol)
                .build();
    }
}
