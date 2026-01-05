package anton.teis.incidencias.entity.incidencia;

import anton.teis.incidencias.entity.user.Tecnico;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
public class IncidenciaCerrada extends Incidencia{
    // TODO: relacion ManyToMany. Esta lista permitirá ver el historial de técnicos que han trabajado en una incidencia.
    private List<Tecnico> tecnico;
}
