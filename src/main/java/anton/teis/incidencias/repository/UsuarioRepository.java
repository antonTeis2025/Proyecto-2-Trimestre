package anton.teis.incidencias.repository;

import anton.teis.incidencias.entity.user.Tecnico;
import anton.teis.incidencias.entity.user.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuarios,Long> {

    Usuarios findByUsername(String username);
    @Query("SELECT u FROM Usuarios u WHERE TYPE(u) = Tecnico")
    List<Tecnico> findAllTecnicos();
}
