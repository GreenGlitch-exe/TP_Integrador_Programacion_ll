package tpintegrado_sanchez_rios;

public class Administrador extends Empleado {

    private String password;

    public Administrador(String nombre, String dni, double sueldo, String password) {
        super(nombre, dni, sueldo); // ✅ Hereda nombre, dni y sueldo desde Empleado
        if (!esNombreValido(nombre)) {
            throw new IllegalArgumentException("❌ El nombre del administrador solo puede contener letras y espacios.");
        }
        this.password = password;
    }

    // Método de validación
    private boolean esNombreValido(String nombre) {
        return nombre != null && nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
    }

    @Override
    public void setNombre(String nombre) {
        if (!esNombreValido(nombre)) {
            throw new IllegalArgumentException("❌ El nombre del administrador solo puede contener letras y espacios.");
        }
        super.setNombre(nombre);
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
            System.out.println("✅ Base de datos reiniciada correctamente.");
        } else {
            System.out.println("❌ Contraseña incorrecta. No se reinició la base de datos.");
        }
    }
    
    @Override
    public void mostrar() {
        System.out.println("Administrador: " + getNombre() + " | DNI: " + getDni() + " | Sueldo: " + getSueldo());
    }

}
