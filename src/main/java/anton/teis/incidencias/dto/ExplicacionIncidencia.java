package anton.teis.incidencias.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ExplicacionIncidencia {
    @NotNull(message = "Requerido un motivo / descripcion")
    @Size(max = 200, message = "Como máximo el motivo puede tener 200 caracteres")
    private String motivo;
}
