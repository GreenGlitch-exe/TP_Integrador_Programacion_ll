package tpintegrado_sanchez_rios;

import java.util.ArrayList;

public class Cliente extends Persona {

    private ArrayList<Factura> compras = new ArrayList<>();

    public Cliente() {
    }

    public Cliente(String nombre, String dni) {
        super(nombre, dni);
        if (!esNombreValido(nombre)) {
            System.out.println("❌ Nombre inválido: solo letras y espacios permitidos.");
        }
    }

    private boolean esNombreValido(String nombre) {
        return nombre != null && nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
    }

    public boolean setNombreValidado(String nombre) {
        if (!esNombreValido(nombre)) {
            System.out.println("❌ Nombre inválido: solo letras y espacios permitidos.");
            return false;
        }
        setNombre(nombre);
        return true;
    }

    public ArrayList<Factura> getCompras() {
        return compras;
    }

    public void agregarCompra(Factura f) {
        compras.add(f);
    }

    public static void mostrarClientes(ArrayList<Cliente> clientes) {
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados");
        } else {
            System.out.println("Lista de clientes:");
            for (Cliente c : clientes) {
                c.mostrar();
            }
        }
    }

    public void modificarDatos(String nuevoNombre, String nuevoDni) {
        if (nuevoNombre != null && !nuevoNombre.isEmpty()) {
            setNombreValidado(nuevoNombre);
        }
        if (nuevoDni != null && !nuevoDni.isEmpty()) {
            setDni(nuevoDni);
        }
        System.out.println("✅ Datos del cliente actualizados");
    }

    public boolean validarDatos() {
        if (getNombre() == null || getNombre().trim().isEmpty()) {
            return false;
        }
        if (getDni() == null || getDni().trim().isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public void mostrar() {
        System.out.println("Cliente: " + getNombre() + " | DNI: " + getDni() + " | Compras: " + compras.size());
    }
}
