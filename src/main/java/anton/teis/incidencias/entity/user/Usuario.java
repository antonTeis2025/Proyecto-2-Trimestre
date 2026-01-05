package anton.teis.incidencias.entity.user;

import anton.teis.incidencias.entity.incidencia.Incidencia;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Usuario extends UsuarioBase {

    @OneToMany(mappedBy = "usuario")
    private List<Incidencia> incidencias;
}
