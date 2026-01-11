package anton.teis.incidencias.repository.incidencia;

import anton.teis.incidencias.entity.incidencia.IncidenciaCerrada;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidenciaCerradaRepository extends JpaRepository<IncidenciaCerrada, Long> {
}
