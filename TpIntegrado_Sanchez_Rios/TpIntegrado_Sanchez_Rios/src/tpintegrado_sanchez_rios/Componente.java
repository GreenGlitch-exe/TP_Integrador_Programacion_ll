package tpintegrado_sanchez_rios;

public class Componente extends Producto {
    private String socket;

    public Componente(String tipo, String descripcion, double precio, int stock, String socket) {
        super(tipo, descripcion, precio, stock);
        this.socket = socket;
    }

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    @Override
    public boolean validarDatos() {
        return (getDescripcion() != null && !getDescripcion().isEmpty()
                && getPrecio() > 0
                && getStock() >= 0
                && socket != null && !socket.isEmpty());
    }

    @Override
    public String toString() {
        return super.toString() + " | Socket: " + socket;
    }

    public void mostrarDatos(String prefix) {
        System.out.println(prefix + " " + this.toString());
    }
}
