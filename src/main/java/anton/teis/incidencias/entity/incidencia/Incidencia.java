package anton.teis.incidencias.entity.incidencia;

import anton.teis.incidencias.entity.user.Usuario;
import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

@Entity
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Incidencia {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    private String descripcion;
    private String IP;
    private Tipo tipo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime momento;

    /**
     * Esta funcion copia los datos base de otra incidencia
     * @param i Incidencia de la que se quiere copiar
     */
    public void copiarDatos(Incidencia i) {
        this.setUsuario(i.getUsuario());
        this.setDescripcion(i.getDescripcion());
        this.setIP(i.getIP());
        this.setTipo(i.getTipo());
        this.setMomento(i.getMomento());
    }

}
