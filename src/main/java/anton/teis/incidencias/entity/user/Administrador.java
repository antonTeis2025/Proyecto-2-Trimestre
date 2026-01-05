package anton.teis.incidencias.entity.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("administrador")
public class Administrador extends Usuarios {
}
