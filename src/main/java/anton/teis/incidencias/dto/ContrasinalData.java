package anton.teis.incidencias.dto;

import anton.teis.incidencias.anotations.match.FieldsValueMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@FieldsValueMatch(
        field = "pass1",
        fieldMatch = "pass2",
        message = "Las contraseñas deben coincidir"
)
public class ContrasinalData {
    @Size(min = 8, message = "La contraseña debe tener minimo 8 caracteres")
    @NotBlank(message = "Debes poner una contraseña")
    private String pass1;
    @Size(min = 8, message = "La contraseña debe tener minimo 8 caracteres")
    @NotBlank(message = "Debes confirmar la contraseña")
    private String pass2;
}
