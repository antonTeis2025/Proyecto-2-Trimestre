package anton.teis.incidencias.entity.incidencia;

import anton.teis.incidencias.entity.user.Tecnico;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class IncidenciaEnProceso extends Incidencia {

    @ManyToOne
    @JoinColumn(name = "id_tecnico")
    private Tecnico tecnico;
}
