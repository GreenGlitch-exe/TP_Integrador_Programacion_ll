package tpintegrado_sanchez_rios;

public class ExcepcionValorEntrada extends Exception {

    public ExcepcionValorEntrada() {
        super("Entrada inválida detectada.");
    }

    public ExcepcionValorEntrada(String msg) {
        super(msg);
    }

    public ExcepcionValorEntrada(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ExcepcionValorEntrada(Throwable cause) {
        super(cause);
    }
}

