package anton.teis.incidencias.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(){
        super("Recurso no encontrado");
    }
}
