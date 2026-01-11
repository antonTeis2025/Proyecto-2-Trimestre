package anton.teis.incidencias.dto;

import anton.teis.incidencias.entity.incidencia.IncidenciaCerrada;
import anton.teis.incidencias.entity.incidencia.IncidenciaEnProceso;
import anton.teis.incidencias.entity.incidencia.IncidenciaResuelta;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class IncidenciasTecnicoResponse {
    private List<IncidenciaEnProceso> enProceso;
    private List<IncidenciaResuelta> resueltas;
    private List<IncidenciaCerrada> cerradas;
}

