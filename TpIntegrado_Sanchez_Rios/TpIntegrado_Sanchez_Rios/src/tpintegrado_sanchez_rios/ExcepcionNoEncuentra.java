package tpintegrado_sanchez_rios;

public class ExcepcionNoEncuentra extends Exception {

    public ExcepcionNoEncuentra() {
        super("Recurso no encontrado.");
    }

    public ExcepcionNoEncuentra(String msg) {
        super(msg);
    }

    public ExcepcionNoEncuentra(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ExcepcionNoEncuentra(Throwable cause) {
        super(cause);
    }
}
