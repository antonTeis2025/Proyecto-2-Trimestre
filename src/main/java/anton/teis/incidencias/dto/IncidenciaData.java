package anton.teis.incidencias.dto;


import anton.teis.incidencias.entity.incidencia.Tipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class IncidenciaData {
    private String username;
    @NotBlank(message = "Requerida una descripcion")
    @Size(max = 200, message = "Como máximo la descripción ha de tener 200 caracteres")
    private String descripcion;
    @NotBlank(message = "Requerida una IP")
    private String IP;
    @NotNull(message = "El tipo de incidencia es obligatorio y debe ser uno de los permitidos (OTRO, HARDWARE, SOFTWARE, RED, ERROR)")
    private String tipo;
}
