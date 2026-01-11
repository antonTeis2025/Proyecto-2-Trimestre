package anton.teis.incidencias.repository.incidencia;

import anton.teis.incidencias.entity.incidencia.IncidenciaAbierta;
import anton.teis.incidencias.entity.incidencia.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IncidenciaAbiertaRepository extends JpaRepository<IncidenciaAbierta, Long> {
    List<IncidenciaAbierta> findByTipo(Tipo tipo);
}
