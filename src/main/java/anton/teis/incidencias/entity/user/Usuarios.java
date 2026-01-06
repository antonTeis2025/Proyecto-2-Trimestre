package anton.teis.incidencias.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
@DiscriminatorColumn(name = "privilegios")
public abstract class Usuarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellidos;

    private String username;
    private String password;

    /**
     * Constructor para cuando se actualiza un usuario
     */
    public Usuarios(
            String nombre,
            String apellidos,
            String username
    ) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.username = username;
    }
}
