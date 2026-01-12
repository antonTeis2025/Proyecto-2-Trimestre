package anton.teis.incidencias.repository.incidencia;

import anton.teis.incidencias.entity.incidencia.IncidenciaAbierta;
import anton.teis.incidencias.entity.incidencia.IncidenciaEnProceso;
import anton.teis.incidencias.entity.user.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IncidenciaEnProcesoRepository extends JpaRepository<IncidenciaEnProceso, Long> {
    List<IncidenciaEnProceso> findByTecnico_Id(Long id);
    List<IncidenciaEnProceso> findByUsuario(Usuario usuario);

    @Query("SELECT i FROM IncidenciaEnProceso i WHERE i.tecnico.id <> :idTecnico")
    List<IncidenciaEnProceso> findAllOtrosTecnicos(@Param("idTecnico") Long idTecnico);

}
