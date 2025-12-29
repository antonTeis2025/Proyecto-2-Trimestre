package anton.teis.incidencias.entity.user;

import anton.teis.incidencias.entity.incidencia.Incidencia;
import jakarta.persistence.Entity;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

@Entity
public class Tecnico extends UsuarioBase{
    // TODO: relaciones

    // Lista de incidencias resueltas (ManyToMany)
    private List<Incidencia> resueltas;
    // Lista de incidencias en proceso (ManyToOne)
    private List<Incidencia> en_proceso;
}
