package anton.teis.incidencias.service;

import anton.teis.incidencias.entity.incidencia.Incidencia;
import anton.teis.incidencias.entity.incidencia.IncidenciaAbierta;
import anton.teis.incidencias.entity.incidencia.IncidenciaEnProceso;
import anton.teis.incidencias.entity.user.Tecnico;
import anton.teis.incidencias.repository.IncidenciaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IncidenciaService {

    @Autowired
    private IncidenciaRepository incidenciaRepository;

    // Funcion para crear nueva incidencia
    public Incidencia abrirIncidencia(IncidenciaAbierta incidencia) {
        return incidenciaRepository.save(incidencia);
    }

    public Incidencia getById(long id) {
        return incidenciaRepository.getOne(id);
    }

    private void deleteById(long id) {
        incidenciaRepository.deleteById(id);
    }

    // Funcion para asignar una incidencia a un técnico
    @Transactional // esta anotación hace que puedas acceder a la entidad "usuario"
                //    vinculada a la propia incidencia
    public Incidencia asignarIncidencia(long id, Tecnico tecnico) {
        Incidencia incidencia = incidenciaRepository.getOne(id);
        IncidenciaEnProceso incidenciaEnProceso = new IncidenciaEnProceso();

        incidenciaEnProceso.setTecnico(tecnico);
        incidenciaEnProceso.setUsuario(incidencia.getUsuario());
        incidenciaEnProceso.setDescripcion(incidencia.getDescripcion());
        incidenciaEnProceso.setIP(incidencia.getIP());
        incidenciaEnProceso.setTipo(incidencia.getTipo());
        incidenciaEnProceso.setMomento(incidencia.getMomento());

        deleteById(incidencia.getId());

        return incidenciaRepository.save(incidenciaEnProceso);
    }



}
