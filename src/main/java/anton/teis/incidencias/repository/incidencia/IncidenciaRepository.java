package anton.teis.incidencias.repository.incidencia;


import anton.teis.incidencias.entity.incidencia.Incidencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {

}
