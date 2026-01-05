package anton.teis.incidencias.repository;

import anton.teis.incidencias.entity.user.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuarios,Long> {
}
