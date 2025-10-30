package tpintegrado_sanchez_rios;

public class Administrador {

    private String nombre;
    private String dni;
    private double sueldo;
    private String password;

    public Administrador(String nombre, String dni, double sueldo, String password) {
        if (!esNombreValido(nombre)) {
            throw new IllegalArgumentException("❌ El nombre del administrador solo puede contener letras y espacios.");
        }
        this.nombre = nombre;
        this.dni = dni;
        this.sueldo = sueldo;
        this.password = password;
    }

    // 🔍 Método de validación
    private boolean esNombreValido(String nombre) {
        return nombre != null && nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (!esNombreValido(nombre)) {
            throw new IllegalArgumentException("❌ El nombre del administrador solo puede contener letras y espacios.");
        }
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public double getSueldo() {
        return sueldo;
    }

    public void setSueldo(double sueldo) {
        this.sueldo = sueldo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void reiniciarBaseDeDatos(String pass) {
        if (this.password.equals(pass)) {
            ManejoBD.getInstance().resetDatabase();
        } else {
            System.out.println("❌ Contraseña incorrecta. No se reinició la base de datos.");
        }
    }

}
