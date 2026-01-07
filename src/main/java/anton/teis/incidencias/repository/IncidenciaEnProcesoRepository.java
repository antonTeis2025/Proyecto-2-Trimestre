package anton.teis.incidencias.repository;

import anton.teis.incidencias.entity.incidencia.IncidenciaEnProceso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidenciaEnProcesoRepository extends JpaRepository<IncidenciaEnProceso, Long> {
}
