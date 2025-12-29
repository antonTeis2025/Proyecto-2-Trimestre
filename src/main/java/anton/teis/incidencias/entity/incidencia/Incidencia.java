package anton.teis.incidencias.entity.incidencia;

import anton.teis.incidencias.entity.user.Usuario;
import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

// TODO la herencia se implementará de forma Table per class

@Entity
@Data
public abstract class Incidencia {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    private String descripcion;
    private String IP;
    private Tipo tipo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime momento;

}
