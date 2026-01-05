package anton.teis.incidencias.entity.incidencia;

import anton.teis.incidencias.entity.user.Tecnico;
import jakarta.persistence.ManyToMany;

import java.util.List;

public class IncidenciaResuelta extends Incidencia{
    // TODO: relacion ManyToMany. Esta lista permitirá ver el historial de técnicos que han trabajado en una incidencia.
    @ManyToMany
    private List<Tecnico> tecnico;
}
