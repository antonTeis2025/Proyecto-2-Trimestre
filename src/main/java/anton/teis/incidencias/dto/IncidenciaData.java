package anton.teis.incidencias.dto;


import anton.teis.incidencias.entity.incidencia.Tipo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class IncidenciaData {
    private String username;
    @NotNull
    @Size(min = 1, max = 200, message = "Como máximo la descripción ha de tener 200 caracteres")
    private String descripcion;
    private String IP;
    @NotNull(message = "El tipo de incidencia es obligatorio y debe ser uno de los permitidos (OTRO, HARDWARE, SOFTWARE, RED, ERROR)")
    private Tipo tipo;
}
