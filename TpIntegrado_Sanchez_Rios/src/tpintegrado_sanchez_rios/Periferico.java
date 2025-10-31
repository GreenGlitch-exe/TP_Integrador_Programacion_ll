package tpintegrado_sanchez_rios;

public class Periferico extends Producto {
    private String conexion;
    private String tipo;


    public Periferico(String tipo, String descripcion, double precio, int stock, String conexion) {
        super(tipo, descripcion, precio, stock);
        this.tipo = tipo;
        this.conexion = conexion;
    }

    public String getConexion(){ return conexion; }
    public void setConexion(String conexion){ this.conexion = conexion; }

    public String getSocket(){ return tipo; }
    public void setSocket(String tipo){ this.tipo = tipo; }

    @Override
    public boolean validarDatos() {
        return super.validarDatos() && conexion != null && !conexion.isEmpty();
    }

    public void mostrarDatos() {
        System.out.println(this.toString() + " | Conexión: " + conexion + " | Tipo: " + tipo);
    }
}
