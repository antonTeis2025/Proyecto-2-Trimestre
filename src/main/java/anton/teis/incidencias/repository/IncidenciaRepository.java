package anton.teis.incidencias.repository;


import anton.teis.incidencias.entity.incidencia.Incidencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {
}
