package anton.teis.incidencias.entity.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

// TODO la tabla se implementará de forma SingleTable
@Entity
public abstract class UsuarioBase {
    @Id
    private Long id;

    private String nombre;
    private String apellidos;

    private String username;
    private String password;

}
