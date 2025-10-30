package tpintegrado_sanchez_rios;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Tienda {

    private String nombre;
    private List<Cliente> clientes;
    private List<Producto> productos;
    private Administrador admin;
    private FacturaDAO facturaDAO;
    private AdministradorDAO adminDAO;
    private ProductoDAO productoDAO;
    private ClienteDAO clienteDAO;

    public Tienda(String nombre) {
        this.nombre = nombre;
        this.clientes = new ArrayList<>();
        this.productos = new ArrayList<>();
        this.facturaDAO = new FacturaDAO();
        this.adminDAO = new AdministradorDAO();
        this.productoDAO = new ProductoDAO();
        this.clienteDAO = new ClienteDAO();
        cargarAdministrador();
        cargarClientes();
        cargarProductos();
    }

    public Administrador getAdministrador() {
        return admin;
    }

    public void cargarAdministrador() {
        try {
            this.admin = adminDAO.getAdministrador();
        } catch (SQLException e) {
            System.out.println("❌ Error al cargar administrador: " + e.getMessage());
            this.admin = null;
        }
    }

    public void registrarAdministrador(Administrador a) {
        this.admin = a;
        try {
            adminDAO.saveAdministrador(a);
        } catch (SQLException e) {
            System.out.println("❌ Error al guardar administrador: " + e.getMessage());
        }
    }

    public boolean validarPasswordAdmin(String pass) {
        return admin != null && admin.getPassword().equals(pass);
    }

    public boolean registrarCliente(Cliente c) {
        if (c == null || !c.validarDatos()) {
            System.out.println("❌ Datos del cliente inválidos.");
            return false;
        }
        try {
            ClienteDAO dao = new ClienteDAO();
            boolean guardado = dao.saveCliente(c);
            if (guardado) {
                clientes.add(c);
                return true;
            }
        } catch (SQLException | ExcepcionValorEntrada e) {
            System.out.println("❌ Error al guardar cliente: " + e.getMessage());
        }
        return false;
    }

    public Cliente buscarClientePorDni(String dni) {
        for (Cliente c : clientes) {
            if (c.getDni().equals(dni)) {
                return c;
            }
        }
        return null;
    }

    public void modificarCliente(Cliente c, String nuevoNombre, String nuevoDni) {
        if (c != null) {
            c.setNombre(nuevoNombre);
            c.setDni(nuevoDni);
            try {
                clienteDAO.update(c);
            } catch (SQLException | ExcepcionValorEntrada e) {
                System.out.println("❌ Error al modificar cliente: " + e.getMessage());
            }
        }
    }

    public void mostrarClientes() {
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        for (Cliente c : clientes) {
            System.out.println("Nombre: " + c.getNombre() + " | DNI: " + c.getDni());
        }
    }

    private void cargarClientes() {
        try {
            clientes = clienteDAO.getAll();
        } catch (SQLException e) {
            System.out.println("❌ Error al cargar clientes desde BD: " + e.getMessage());
            clientes = new ArrayList<>();
        }
    }

    public void agregarProducto(Producto p) {
        if (p != null) {
            try {
                int id = productoDAO.saveProducto(p);
                p.setId(id);
                productos.add(p);
            } catch (SQLException | ExcepcionValorEntrada e) {
                System.out.println("❌ Error al agregar producto: " + e.getMessage());
            }
        }
    }

    public void modificarProducto(Producto p, double nuevoPrecio, int nuevoStock) {
        if (p != null) {
            p.setPrecio(nuevoPrecio);
            p.setStock(nuevoStock);
            try {
                productoDAO.update(p);
            } catch (SQLException | ExcepcionValorEntrada e) {
                System.out.println("❌ Error al actualizar producto: " + e.getMessage());
            }
        }
    }

    public Producto buscarProductoPorId(int id) {
        for (Producto p : productos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public void mostrarProductos() {
        if (productos.isEmpty()) {
            System.out.println("No hay productos cargados.");
            return;
        }

        for (Producto p : productos) {
            String tipoProducto = (p instanceof Componente) ? "Componente" : "Periférico";
            String info = "ID: " + p.getId() + " | Tipo: " + tipoProducto
                    + " | Descripción: " + p.getDescripcion()
                    + " | Precio: $" + p.getPrecio()
                    + " | Stock: " + p.getStock();

            if (p instanceof Componente) {
                info += " | Socket: " + ((Componente) p).getSocket();
            } else if (p instanceof Periferico) {
                info += " | Conexión: " + ((Periferico) p).getConexion();
            }

            System.out.println(info);
        }
    }

    private void cargarProductos() {
        try {
            productos = productoDAO.getAll();
        } catch (SQLException e) {
            System.out.println("❌ Error al cargar productos desde BD: " + e.getMessage());
            productos = new ArrayList<>();
        }
    }

    public void registrarVenta(Factura f) throws ExcepcionStock, SQLException, ExcepcionValorEntrada {
        if (f == null) {
            return;
        }

        // Finaliza la factura (calcula totales, etc.)
        f.finalizar();

        // Actualizar stock de cada producto vendido
        for (Map.Entry<Producto, Integer> item : f.getItems().entrySet()) {
            Producto p = item.getKey();
            int cantidadVendida = item.getValue();
            p.actualizarStock(-cantidadVendida); // resta stock
            productoDAO.update(p); // actualiza en BD
        }

        // Guardar la factura
        facturaDAO.saveFactura(f);

        // Registrar la compra en el cliente
        if (f.getCliente() != null) {
            f.getCliente().agregarCompra(f);
        }
    }

    public void mostrarFacturasCliente(String dni) {
        Cliente c = buscarClientePorDni(dni);
        if (c == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        if (c.getCompras().isEmpty()) {
            System.out.println("El cliente no tiene facturas.");
            return;
        }
        for (Factura f : c.getCompras()) {
            f.mostrarFactura();
        }
    }

    public void reiniciarBase() {
        System.out.print("Ingrese la contraseña del administrador para reiniciar la base: ");
        String pass = new java.util.Scanner(System.in).nextLine();
        if (admin != null && admin.getPassword().equals(pass)) {
            admin.reiniciarBaseDeDatos(pass);
            clientes.clear();
            productos.clear();
        } else {
            System.out.println("❌ Contraseña incorrecta. No se reinició la base de datos.");
        }
    }
}
