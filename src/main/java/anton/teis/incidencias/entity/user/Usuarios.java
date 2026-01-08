package anton.teis.incidencias.entity.user;

import anton.teis.incidencias.dto.UserData;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
@DiscriminatorColumn(name = "privilegios")
public abstract class Usuarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellidos;

    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    private boolean alta = true; // si no esta dado de alta no aparecerá en el front-end

    public void copiarDto(UserData userData) {
        this.setNombre(userData.getNombre());
        this.setApellidos(userData.getApellido());
        this.setUsername(userData.getUsername());
        this.setPassword(userData.getPassword());
    }

}
