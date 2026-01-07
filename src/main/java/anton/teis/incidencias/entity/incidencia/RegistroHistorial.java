package anton.teis.incidencias.entity.incidencia;

import anton.teis.incidencias.entity.user.Tecnico;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//TODO
@Entity
@Data
@NoArgsConstructor
public class RegistroHistorial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime asignado;
    @ManyToOne
    @JoinColumn(name = "id_tecnico")
    private Tecnico tecnico;

    public RegistroHistorial(LocalDateTime asignado, Tecnico tecnico) {
        this.asignado = asignado;
        this.tecnico = tecnico;
    }
}
