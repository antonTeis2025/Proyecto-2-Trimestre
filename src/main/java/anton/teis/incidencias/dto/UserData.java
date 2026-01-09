package anton.teis.incidencias.dto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserData {
    @Size(max=20, message = "El nombre de usuario no puede tener más de 20 caracteres")
    private String username;

    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;
    private String nombre;
    private String apellido;

    private String privilegios; // "usuario", "tecnico", "administrador"
}
