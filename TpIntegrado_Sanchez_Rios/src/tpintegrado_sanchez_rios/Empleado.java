package tpintegrado_sanchez_rios;

public abstract class Empleado extends Persona {

    private double sueldo;

    public Empleado() {
        super();
    }

    public Empleado(String nombre, String dni, double sueldo) {
        super(nombre, dni);
        this.sueldo = sueldo;
    }

    public double getSueldo() {
        return sueldo;
    }

    public void setSueldo(double sueldo) {
        this.sueldo = sueldo;
    }
}
