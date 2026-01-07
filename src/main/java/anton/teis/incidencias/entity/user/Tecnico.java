package anton.teis.incidencias.entity.user;

import anton.teis.incidencias.entity.incidencia.IncidenciaCerrada;
import anton.teis.incidencias.entity.incidencia.IncidenciaEnProceso;
import anton.teis.incidencias.entity.incidencia.IncidenciaResuelta;
import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("tecnico")
public class Tecnico extends Usuarios {
    // TODO: relaciones

    // Lista de incidencias resueltas (ManyToMany)
    @ManyToMany(mappedBy = "tecnicos")
    private List<IncidenciaResuelta> resueltas;

    // Lista de incidencias cerradas (ManyToMany)
    @ManyToMany(mappedBy = "tecnicos")
    private List<IncidenciaCerrada> cerradas;

    // Lista de incidencias en proceso (ManyToOne)
    @OneToMany(mappedBy = "tecnico")
    private List<IncidenciaEnProceso> en_proceso;
}
