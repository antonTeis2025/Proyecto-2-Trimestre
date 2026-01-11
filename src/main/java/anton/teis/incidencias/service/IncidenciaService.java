package anton.teis.incidencias.service;

import anton.teis.incidencias.entity.incidencia.*;
import anton.teis.incidencias.entity.user.Tecnico;
import anton.teis.incidencias.repository.IncidenciaEnProcesoRepository;
import anton.teis.incidencias.repository.IncidenciaRepository;
import anton.teis.incidencias.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IncidenciaService {

    @Autowired
    private IncidenciaRepository incidenciaRepository;
    @Autowired
    private IncidenciaEnProcesoRepository incidenciaEnProcesoRepository;
    @Autowired
    private UsuarioService usuarioService;

    /**
     * Funcion para crear nueva incidencia
     * @param incidencia
     * @return
     */
    public Incidencia abrirIncidencia(IncidenciaAbierta incidencia) {
        return incidenciaRepository.save(incidencia);
    }

    public List<Incidencia> getAll() {
        return incidenciaRepository.findAll();
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
     * @param solucion Descripción de la solución propuesta
     * @return
     */
    public Incidencia resolverIncidencia(long id, String solucion) {
        // obtener la incidenciaBase por ID
        Incidencia incidenciaBase = getById(id);

        // AQUÍ Hibernate ya sabe el tipo real, y la almacena en la variable incidencia
        if (!(incidenciaBase instanceof IncidenciaEnProceso incidencia)) {
            throw new IllegalStateException(
                    "Solo se puede resolver una incidencia en proceso"
            );
        }

        IncidenciaResuelta incidenciaResuelta = new IncidenciaResuelta();

        incidenciaResuelta.setTecnicos(incidencia.historialToTecnicoList());

        incidenciaResuelta.setSolucion(solucion);

        incidenciaResuelta.copiarDatos(incidencia);

        deleteById(incidencia.getId());

        return incidenciaRepository.save(incidenciaResuelta);

    }

    @Transactional
    public Incidencia cerrarIncidencia(long id, String motivo) {
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

        incidenciaCerrada.setTecnicos(incidencia.historialToTecnicoList());

        // se borra la incidencia en proceso
        deleteById(incidencia.getId());

        return incidenciaRepository.save(incidenciaCerrada);
    }

    /**
     * Pasa la incidencia a otro técnico
     * @param id_incidencia
     * @param tecnico
     * @return
     */
    @Transactional
    public Incidencia pasarIncidencia(long id_incidencia, Tecnico tecnico) {

        IncidenciaEnProceso ip = incidenciaEnProcesoRepository.getById(id_incidencia);
        ip.setTecnico(tecnico);
        return incidenciaEnProcesoRepository.save(ip);
    }


}
