package tpintegrado_sanchez_rios;

public class Periferico extends Producto implements Vendible {

    private String conexion;

    public Periferico(String tipo, String descripcion, double precio, int stock, String conexion) {
        super(tipo, descripcion, precio, stock);
        this.conexion = conexion;
    }

    public String getConexion() { return conexion; }
    public void setConexion(String conexion) { this.conexion = conexion; }

    public boolean validarDatos(boolean requiereConexion) {
        boolean baseValida = super.validarDatos();
        return baseValida && (!requiereConexion || (conexion != null && !conexion.isEmpty()));
    }

    public void mostrarDatos() {
        System.out.println(this.toString() + " | Conexión: " + conexion);
    }
}
