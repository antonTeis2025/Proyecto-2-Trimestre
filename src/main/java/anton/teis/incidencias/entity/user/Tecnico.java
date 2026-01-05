package anton.teis.incidencias.entity.user;

import anton.teis.incidencias.entity.incidencia.Incidencia;
import anton.teis.incidencias.entity.incidencia.IncidenciaResuelta;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

@Entity
@DiscriminatorValue("tecnico")
public class Tecnico extends UsuarioBase{
    // TODO: relaciones

    // Lista de incidencias resueltas Y Cerradas (ManyToMany)
    @ManyToMany(mappedBy = "tecnico")
    private List<Incidencia> resueltas;
    // Lista de incidencias en proceso (ManyToOne)
    @OneToMany(mappedBy = "tecnico")
    private List<Incidencia> en_proceso;
}
