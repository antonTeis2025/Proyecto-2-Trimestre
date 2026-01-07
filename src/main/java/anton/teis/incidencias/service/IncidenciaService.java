package anton.teis.incidencias.service;

import anton.teis.incidencias.entity.incidencia.Incidencia;
import anton.teis.incidencias.entity.incidencia.IncidenciaAbierta;
import anton.teis.incidencias.entity.incidencia.IncidenciaEnProceso;
import anton.teis.incidencias.entity.incidencia.IncidenciaResuelta;
import anton.teis.incidencias.entity.user.Tecnico;
import anton.teis.incidencias.repository.IncidenciaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IncidenciaService {

    @Autowired
    private IncidenciaRepository incidenciaRepository;

    /**
     * Funcion para crear nueva incidencia
     * @param incidencia
     * @return
     */
    public Incidencia abrirIncidencia(IncidenciaAbierta incidencia) {
        return incidenciaRepository.save(incidencia);
    }

    public Incidencia getById(long id) {
        return incidenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));
    }

    private void deleteById(long id) {
        incidenciaRepository.deleteById(id);
    }

    /**
     * Funcion para asignar una incidencia a un técnico
     * @param id Id Incidencia
     * @param tecnico Entidad Técnico
     * @return
     */
    @Transactional // esta anotación hace que puedas acceder a la entidad "usuario"
                //    vinculada a la propia incidencia
    public Incidencia asignarIncidencia(long id, Tecnico tecnico) {
        Incidencia incidencia = incidenciaRepository.getOne(id);
        IncidenciaEnProceso incidenciaEnProceso = new IncidenciaEnProceso();

        incidenciaEnProceso.setTecnico(tecnico);

        incidenciaEnProceso.copiarDatos(incidencia);

        deleteById(incidencia.getId());

        return incidenciaRepository.save(incidenciaEnProceso);
    }

    /**
     *  Funcion para marcar una incidencia como resuelta
     * @param id
     * @param tecnicos Entidades Técnico que resolvieron la incidencia (null para el que ya estaba)
     * @param solucion Descripción de la solución propuesta
     * @return
     */
    public Incidencia resolverIncidencia(long id, List<Tecnico> tecnicos, String solucion) {
        // obtener la incidenciaBase por ID
        Incidencia incidenciaBase = getById(id);

        // AQUÍ Hibernate ya sabe el tipo real, y la almacena en la variable incidencia
        if (!(incidenciaBase instanceof IncidenciaEnProceso incidencia)) {
            throw new IllegalStateException(
                    "Solo se puede resolver una incidencia en proceso"
            );
        }

        IncidenciaResuelta incidenciaResuelta = new IncidenciaResuelta();

        if (tecnicos == null) {
            // si no se especifica lista de técnicos poner el que ya estaba
            Tecnico t = incidencia.getTecnico();
            List<Tecnico> ts = new ArrayList<>();
            ts.add(t);
            incidenciaResuelta.setTecnicos(ts);
        } else {
            incidenciaResuelta.setTecnicos(tecnicos);
        }

        incidenciaResuelta.setSolucion(solucion);

        incidenciaResuelta.copiarDatos(incidencia);

        deleteById(incidencia.getId());

        return incidenciaRepository.save(incidenciaResuelta);

    }



}
