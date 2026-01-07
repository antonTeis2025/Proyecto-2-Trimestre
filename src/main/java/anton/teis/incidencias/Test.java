package anton.teis.incidencias;

import anton.teis.incidencias.entity.incidencia.IncidenciaAbierta;
import anton.teis.incidencias.entity.incidencia.Tipo;
import anton.teis.incidencias.entity.user.Administrador;
import anton.teis.incidencias.entity.user.Tecnico;
import anton.teis.incidencias.entity.user.Usuario;
import anton.teis.incidencias.entity.user.Usuarios;
import anton.teis.incidencias.service.IncidenciaService;
import anton.teis.incidencias.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Test implements CommandLineRunner {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private IncidenciaService incidenciaService;

    // con esta funcion inserto datos de prueba para el desarollo de la app
    private void testSetup() {
        testCrearUsuarios();
        long id = testAbrirIncidencia();
        testAsignarIncidencia(id);
    }

    private void testCrearUsuarios() {
        System.out.println("🔧 === Creando usuarios ===");

        // 1. Guardar un Usuario normal
        Usuario u = new Usuario();
        u.setNombre("Pedro");
        u.setApellidos("García");
        u.setUsername("pedro");
        u.setPassword("123");
        usuarioService.guardar(u);

        // 2. Guardar un Técnico
        Tecnico t = new Tecnico();
        t.setNombre("Laura");
        t.setApellidos("Fernández");
        t.setUsername("laura_tec");
        t.setPassword("456");
        usuarioService.guardar(t);

        // 3. Guardar un Administrador
        Administrador a = new Administrador();
        a.setNombre("Jefe");
        a.setApellidos("Del Sistema");
        a.setUsername("admin");
        a.setPassword("admin123");
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
        System.out.println(usuarioService.checkPassword(1, "asd123"));

    }

    private void testAltaYBaja(long id) {
        System.out.println("------ Dando de baja y reactivando técnico ------");
        usuarioService.darDeBaja(id);
        usuarioService.getTecnicos().forEach(System.out::println);
        usuarioService.reactivar(id);
        usuarioService.getTecnicos().forEach(System.out::println);
    }

    private long testAbrirIncidencia() {
        System.out.println("------ Abriendo incidencia ------");
        IncidenciaAbierta incidencia = new IncidenciaAbierta();

        incidencia.setIP("127.0.0.1");
        incidencia.setTipo(Tipo.OTRO);
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
        incidenciaService.resolverIncidencia(id, null, "Conectar cable de rede");

    }

    private void testCerrarIncidencia(long id) {
        System.out.println("--------- Cerrando incidencia ------------");
        incidenciaService.cerrarIncidencia(id, null, "no se que mas hacer");
    }

    /*
            TESTS (todo):
                X Resolver incidencia
                X Cerrar incidencia
                - Re-abrir incidencia cerrada
     */


    @Override
    public void run(String... args) throws Exception {
        // testCrearUsuarios();
        long id = testAbrirIncidencia();
        long id2 = testAsignarIncidencia(id);
        System.out.println(id2);
        // testSetup();
        // testResolverIncidencia(2);
        // testCerrarIncidencia(id2);
    }
}
