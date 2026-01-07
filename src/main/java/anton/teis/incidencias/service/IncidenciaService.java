package anton.teis.incidencias.service;

import anton.teis.incidencias.entity.incidencia.*;
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

        Incidencia saved = incidenciaRepository.save(incidenciaEnProceso);

        deleteById(incidencia.getId());

        return saved;
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

        // todo: refactor para el historico
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

    @Transactional
    public Incidencia cerrarIncidencia(long id, List<Tecnico> tecnicos, String motivo) {
        // obtener la incidenciaBase por ID
        Incidencia incidenciaBase = getById(id);

        // AQUÍ Hibernate ya sabe el tipo real, y la almacena en la variable incidencia
        if (!(incidenciaBase instanceof IncidenciaEnProceso incidencia)) {
            throw new IllegalStateException(
                    "Solo se puede cerrar una incidencia en proceso"
            );
        }

        IncidenciaCerrada incidenciaCerrada = new IncidenciaCerrada();
        incidenciaCerrada.setMotivo(motivo);
        incidenciaCerrada.copiarDatos(incidencia);

        // 1️⃣ Guardar primero sin técnicos
        incidenciaCerrada = incidenciaRepository.saveAndFlush(incidenciaCerrada);

        // todo: refactor para el historico
        if (tecnicos == null) {
            // si no se especifica lista de técnicos poner el que ya estaba
            Tecnico t = incidencia.getTecnico();

            List<Tecnico> ts = new ArrayList<>();
            ts.add(t);
            incidenciaCerrada.setTecnicos(ts);
        } else {
            incidenciaCerrada.setTecnicos(tecnicos);
        }

        // 3️⃣ Guardar nuevamente
        incidenciaCerrada = incidenciaRepository.saveAndFlush(incidenciaCerrada);

        // se borra la incidencia en proceso
        deleteById(incidencia.getId());

        return incidenciaCerrada;
    }



}
