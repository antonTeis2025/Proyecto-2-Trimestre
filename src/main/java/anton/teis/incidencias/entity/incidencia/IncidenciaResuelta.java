package anton.teis.incidencias.entity.incidencia;

import anton.teis.incidencias.entity.user.Tecnico;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

import java.util.List;

public class IncidenciaResuelta extends Incidencia{
    // Lista de técnicos que han trabajado en cierta incidencia
    @ManyToMany
    @JoinTable(
            name = "incidencia_tecnico",
            joinColumns = @JoinColumn(name = "id_incidencia"),
            inverseJoinColumns = @JoinColumn(name = "id_tecnico")
    )
    private List<Tecnico> tecnico;
}
