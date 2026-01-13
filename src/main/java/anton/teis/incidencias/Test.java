package anton.teis.incidencias;

import anton.teis.incidencias.entity.incidencia.Incidencia;
import anton.teis.incidencias.entity.incidencia.IncidenciaAbierta;
import anton.teis.incidencias.entity.incidencia.IncidenciaEnProceso;
import anton.teis.incidencias.entity.incidencia.Tipo;
import anton.teis.incidencias.entity.user.Administrador;
import anton.teis.incidencias.entity.user.Tecnico;
import anton.teis.incidencias.entity.user.Usuario;
import anton.teis.incidencias.entity.user.Usuarios;
import anton.teis.incidencias.repository.incidencia.IncidenciaAbiertaRepository;
import anton.teis.incidencias.repository.incidencia.IncidenciaCerradaRepository;
import anton.teis.incidencias.repository.incidencia.IncidenciaEnProcesoRepository;
import anton.teis.incidencias.repository.incidencia.IncidenciaResueltaRepository;
import anton.teis.incidencias.service.IncidenciaService;
import anton.teis.incidencias.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Test implements CommandLineRunner {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private IncidenciaService incidenciaService;
    @Autowired
    private IncidenciaEnProcesoRepository incidenciaEnProcesoRepository;
    @Autowired
    private IncidenciaResueltaRepository incidenciaResueltaRepository;
    @Autowired
    private IncidenciaCerradaRepository incidenciaCerradaRepository;
    @Autowired
    private IncidenciaAbiertaRepository incidenciaAbiertaRepository;

    // con esta funcion inserto datos de prueba para el desarollo de la app
    private void testSetup() {
        testCrearUsuarios();
        long id = testAbrirIncidencia(Tipo.OTRO);
        testAsignarIncidencia(id);
    }

    private void crearUsuariosPrueba() {
        String passwordComun = "asd123..";

        // --- USUARIOS ---
        Usuario u = new Usuario();
        u.setUsername("user1");
        u.setPassword(passwordComun);
        u.setNombre("Juan");
        u.setApellidos("Pérez");
        usuarioService.guardar(u);

        Usuario u2 = new Usuario();
        u2.setUsername("user2");
        u2.setPassword(passwordComun);
        u2.setNombre("María");
        u2.setApellidos("García");
        usuarioService.guardar(u2);

        // --- TÉCNICOS ---
        Tecnico t = new Tecnico();
        t.setUsername("tecnico1");
        t.setPassword(passwordComun);
        t.setNombre("Roberto");
        t.setApellidos("Sánchez");
        usuarioService.guardar(t);

        Tecnico t2 = new Tecnico();
        t2.setUsername("tecnico2");
        t2.setPassword(passwordComun);
        t2.setNombre("Lucía");
        t2.setApellidos("Martín");
        usuarioService.guardar(t2);

        // --- ADMINISTRADOR ---
        Administrador a = new Administrador();
        a.setUsername("admin");
        a.setPassword(passwordComun);
        a.setNombre("Admin");
        a.setApellidos("Sistema");
        usuarioService.guardar(a);

        System.out.println("Usuarios de prueba creados exitosamente.");
    }

    private void testCrearUsuarios() {
        System.out.println("🔧 === Creando usuarios ===");

        // 1. Guardar un Usuario normal
        Usuario u = new Usuario();
        u.setNombre("Pedro");
        u.setApellidos("García");
        u.setUsername("user2");
        u.setPassword("abcd1234");
        usuarioService.guardar(u);

//        // 2. Guardar un Técnico
//        Tecnico t = new Tecnico();
//        t.setNombre("Laura");
//        t.setApellidos("Fernández");
//        t.setUsername("tec");
//        t.setPassword("abcd1234");
//        usuarioService.guardar(t);
//
//        // 2. Guardar un Técnico
//        Tecnico t2 = new Tecnico();
//        t2.setNombre("Laura");
//        t2.setApellidos("Fernández");
//        t2.setUsername("tec2");
//        t2.setPassword("abcd1234");
//        usuarioService.guardar(t2);


        // 3. Guardar un Administrador
        Administrador a = new Administrador();
        a.setNombre("Jefe");
        a.setApellidos("Del Sistema");
        a.setUsername("admin2");
        a.setPassword("abcd1234");
        usuarioService.guardar(a);

        System.out.println("✅ ¡Usuarios guardados! Revisa tu base de datos.");
    }

    private void testLeerUsuarios() {
        Usuarios u1 = usuarioService.getById(1);
        Usuarios u2 = usuarioService.getById(2);
        System.out.println("----------------------------");
        System.out.println(u1.toString());
        System.out.println("----------------------------");
        System.out.println(u2.toString());
        System.out.println("----------------------------");
        // lee todos los usuarios tipo tecnicos
        usuarioService.getTecnicos().forEach(System.out::println);
        // getAll
        usuarioService.getAll().forEach(System.out::println);
        // getByUsername
        System.out.println(usuarioService.getByUsername("laura_tec").toString());
    }

    private void testActualizarUsuarios(long id) {
        System.out.println("------ Modificando usuario ------");
        Usuarios u1 = usuarioService.getById(id);
        System.out.println("----------------------------");
        System.out.println(u1.toString());
        System.out.println("----------------------------");
        usuarioService.update(1,
                "jdacosta",
                "juan",
                "dacosta"
        );
        usuarioService.changePassword(1, "asd123");

    }

    private void testAltaYBaja(long id) {
        System.out.println("------ Dando de baja y reactivando técnico ------");
        usuarioService.darDeBaja(id);
        usuarioService.getTecnicos().forEach(System.out::println);
        usuarioService.reactivar(id);
        usuarioService.getTecnicos().forEach(System.out::println);
    }

    private long testAbrirIncidencia(Tipo t) {
        System.out.println("------ Abriendo incidencia ------");
        IncidenciaAbierta incidencia = new IncidenciaAbierta();

        incidencia.setIP("127.0.0.1");
        incidencia.setMomento(LocalDateTime.now());
        incidencia.setTipo(t);
        incidencia.setDescripcion("No me va esto que esta pasando");
        incidencia.setUsuario((Usuario) usuarioService.getById(1));

        incidenciaService.abrirIncidencia(incidencia);

        return incidencia.getId();

    }

    private long testAsignarIncidencia(long id) {

        System.out.println("-------- Asignando incidencia ---------");

        Tecnico t = usuarioService.getTecnicos().getFirst();

        return incidenciaService.asignarIncidencia(id, t).getId();

    }

    private void testResolverIncidencia(long id) {
        System.out.println("-------- Resolviendo incidencia ---------");
        Incidencia resuelta = incidenciaService.resolverIncidencia(id,  "Conectar cable de rede");
        System.out.println(resuelta.toString());
        System.out.println(resuelta.getMomento());

    }

    private void testCerrarIncidencia(long id) {
        System.out.println("--------- Cerrando incidencia ------------");
        incidenciaService.cerrarIncidencia(id, "no se que mas hacer");
    }

    private void testHistoricos() {
        testCrearUsuarios();

        long id = testAbrirIncidencia(Tipo.OTRO);
        long id2 = testAsignarIncidencia(id);

        incidenciaService.pasarIncidencia(id2, usuarioService.getTecnicos().get(1));

        testResolverIncidencia(id2);
    }

    @Override
    public void run(String... args) throws Exception {

        // crearUsuariosPrueba();
         // testCrearUsuarios();
//
//        testAbrirIncidencia(Tipo.ERROR);
//        testAbrirIncidencia(Tipo.ERROR);
//
//        testAbrirIncidencia(Tipo.HARDWARE);
//
//        incidenciaAbiertaRepository.findByTipo(Tipo.ERROR).forEach(System.out::println);
        //        incidenciaResueltaRepository.findByTecnicoId(2L).forEach(System.out::println);
//        incidenciaCerradaRepository.findByTecnicoId(3L).forEach(System.out::println);
//        long id1 = testAbrirIncidencia();
//        testAsignarIncidencia(id1);
//        incidenciaEnProcesoRepository.findByTecnico_Id(2L).forEach(System.out::println);
        // testSetup();
        // testResolverIncidencia(2);
        // testCerrarIncidencia(id2);
    }
}
