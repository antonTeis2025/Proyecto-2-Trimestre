package anton.teis.incidencias.entity.incidencia;

import anton.teis.incidencias.entity.user.Tecnico;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

import java.util.List;

/**
 * Este tipo de incidencias no se pudieron resolver y se cerraron
 */
@Entity
public class IncidenciaCerrada extends Incidencia{
    // Esta lista permitirá ver el historial de técnicos que han trabajado en una incidencia.
    @ManyToMany
    @JoinTable(
            name = "incidencia_tecnico",
            joinColumns = @JoinColumn(name = "id_incidencia"),
            inverseJoinColumns = @JoinColumn(name = "id_tecnico")
    )
    private List<Tecnico> tecnico;
    // aqui se describe porque la incidencia se ha cerrado
    private String problema;
}
