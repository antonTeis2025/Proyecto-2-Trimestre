package anton.teis.incidencias.repository.incidencia;

import anton.teis.incidencias.entity.incidencia.IncidenciaAbierta;
import anton.teis.incidencias.entity.incidencia.IncidenciaResuelta;
import anton.teis.incidencias.entity.user.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IncidenciaResueltaRepository extends JpaRepository<IncidenciaResuelta, Long> {

    @Query("SELECT i FROM IncidenciaResuelta i JOIN FETCH i.tecnicos t WHERE t.id = :id")
    List<IncidenciaResuelta> findByTecnicoId(long id);
    List<IncidenciaResuelta> findByUsuario(Usuario usuario);


}
