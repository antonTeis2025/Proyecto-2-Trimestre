package anton.teis.incidencias.repository.incidencia;

import anton.teis.incidencias.entity.incidencia.IncidenciaCerrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IncidenciaCerradaRepository extends JpaRepository<IncidenciaCerrada, Long> {

    @Query("SELECT i FROM IncidenciaCerrada i JOIN FETCH i.tecnicos t WHERE t.id = :id")
    List<IncidenciaCerrada> findByTecnicoId(long id);

}
