package anton.teis.incidencias.entity.incidencia;

import anton.teis.incidencias.entity.user.Tecnico;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * La incidencia está siendo revisada por un técnico
 */
@Entity
@Data
@NoArgsConstructor
public class IncidenciaEnProceso extends Incidencia {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tecnico")
    // Técnico que trabaja en una incidencia (solo puede uno a la vez)
    private Tecnico tecnico;

    // TODO: impl historial al abrir una incidencia
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RegistroHistorial> historial = new ArrayList<>();


    public void setTecnico(Tecnico tecnico){
        this.tecnico = tecnico;
        this.addTecnico(tecnico);
    }

    public void addTecnico(Tecnico tecnico) {
        this.historial.add(new RegistroHistorial(
                LocalDateTime.now(),
                tecnico
        ));
    }

    public List<Tecnico> historialToTecnicoList() {

        List<RegistroHistorial> historico = this.getHistorial();
        List<Tecnico> tecnicos = new ArrayList<>();

        historico.forEach(registro -> {
            tecnicos.add(registro.getTecnico());
        });

        return tecnicos;

    }

}
