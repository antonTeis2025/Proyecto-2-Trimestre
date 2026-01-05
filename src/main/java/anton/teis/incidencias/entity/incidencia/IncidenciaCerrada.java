package anton.teis.incidencias.entity.incidencia;

import anton.teis.incidencias.entity.user.Tecnico;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;

import java.util.List;

@Entity
public class IncidenciaCerrada extends Incidencia{
    // Esta lista permitirá ver el historial de técnicos que han trabajado en una incidencia.
    @JoinTable(
            name = "incidencia_tecnico",
            joinColumns = @JoinColumn(name = "id_incidencia"),
            inverseJoinColumns = @JoinColumn(name = "id_tecnico")
    )
    private List<Tecnico> tecnico;
}
