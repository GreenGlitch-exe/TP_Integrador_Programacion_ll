package tpintegrado_sanchez_rios;

public class ExcepcionStock extends Exception {

    public ExcepcionStock() {
        super("Error de stock.");
    }

    public ExcepcionStock(String msg) {
        super(msg);
    }

    public ExcepcionStock(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ExcepcionStock(Throwable cause) {
        super(cause);
    }
}


