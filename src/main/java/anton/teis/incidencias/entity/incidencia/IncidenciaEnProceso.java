package anton.teis.incidencias.entity.incidencia;

import anton.teis.incidencias.entity.user.Tecnico;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


/**
 * La incidencia está siendo revisada por un técnico
 */
@Entity
public class IncidenciaEnProceso extends Incidencia {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tecnico")
    // Técnico que trabaja en una incidencia (solo puede uno a la vez)
    private Tecnico tecnico;
}
