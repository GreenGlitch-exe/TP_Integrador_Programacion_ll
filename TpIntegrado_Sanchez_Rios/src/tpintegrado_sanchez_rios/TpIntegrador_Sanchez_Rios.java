package tpintegrado_sanchez_rios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TpIntegrador_Sanchez_Rios {

    public static void main(String[] args) {

        try {
            ManejoBD db = ManejoBD.getInstance();
            db.initDatabase();

            System.out.println("✅ Conexión a la base de datos exitosa.\n");

            Tienda tienda = new Tienda("Mi Tienda");
            tienda.cargarAdministrador();

            boolean salirPrograma = false;

            while (!salirPrograma) {
                String rol = leerStringSeguro("¿Desea ingresar como Cliente (C) o Administrador (A) o Salir (S)? ").toUpperCase();

                switch (rol) {

                    //CLIENTE
                    case "C":
                        int opcionCliente = 0;
                        while (opcionCliente != 3) {
                            System.out.println("=== MODO CLIENTE ===");
                            System.out.println("1 - Registrarse");
                            System.out.println("2 - Iniciar sesión");
                            System.out.println("3 - Volver al menú principal");
                            opcionCliente = leerIntSeguro("Seleccione opción (1-3): ", 1, 3);

                            switch (opcionCliente) {
                                case 1:
                                    String nombreC = leerStringSeguro("Nombre: ");
                                    String dniC = "";
                                    while (true) {
                                        dniC = leerStringSeguro("DNI (8 dígitos): ");
                                        if (dniC.matches("\\d{8}")) {
                                            break;
                                        }
                                        System.out.println("❌ DNI inválido.");
                                    }
                                    Cliente c = new Cliente(nombreC, dniC);
                                    if (tienda.registrarCliente(c)) {
                                        System.out.println("✅ Cliente registrado.\n");
                                    } else {
                                        System.out.println("⚠️ No se pudo registrar el cliente.\n");
                                    }
                                    break;

                                case 2:
                                    String dniLogin = leerStringSeguro("Ingrese su DNI: ");
                                    Cliente clienteLogin = tienda.buscarClientePorDni(dniLogin);
                                    if (clienteLogin != null) {
                                        System.out.println("✅ Bienvenido " + clienteLogin.getNombre() + "!");

                                        boolean salirCliente = false;
                                        while (!salirCliente) {
                                            System.out.println("=== MENÚ CLIENTE LOGUEADO ===");
                                            System.out.println("1 - Ver productos disponibles");
                                            System.out.println("2 - Comprar productos");
                                            System.out.println("3 - Ver mis facturas");
                                            System.out.println("4 - Cerrar sesión");
                                            int opcionClienteLog = leerIntSeguro("Seleccione opción (1-4): ", 1, 4);

                                            switch (opcionClienteLog) {
                                                case 1:
                                                    tienda.mostrarProductos();
                                                    break;

                                                case 2:
                                                    Factura f = new Factura(clienteLogin);
                                                    boolean agregarMas = true;
                                                    while (agregarMas) {
                                                        int idProd = leerIntSeguro("Ingrese ID producto: ", 1, 100000);
                                                        Producto prod = tienda.buscarProductoPorId(idProd);
                                                        if (prod == null) {
                                                            System.out.println("❌ Producto no encontrado.");
                                                            continue;
                                                        }
                                                        int cantidad = leerIntSeguro("Cantidad: ", 1, 100000);
                                                        try {
                                                            f.addItem(prod, cantidad);
                                                        } catch (ExcepcionStock e) {
                                                            System.out.println("❌ Error: " + e.getMessage());
                                                            continue;
                                                        }
                                                        String cont = leerStringSeguro("Agregar otro producto? (S/N): ");
                                                        agregarMas = cont.equalsIgnoreCase("S");
                                                    }
                                                    f.calcularTotal();
                                                    try {
                                                        tienda.registrarVenta(f);
                                                        System.out.printf("✅ Venta registrada. Total: $%.2f\n\n", f.getTotal());
                                                    } catch (SQLException | ExcepcionValorEntrada e) {
                                                        System.out.println("❌ Error al registrar la venta: " + e.getMessage());
                                                    }
                                                    break;

                                                case 3:
                                                    tienda.mostrarFacturasCliente(clienteLogin.getDni());
                                                    break;

                                                case 4:
                                                    salirCliente = true;
                                                    System.out.println("👋 Sesión cerrada.\n");
                                                    break;
                                            }
                                        }

                                    } else {
                                        System.out.println("❌ Cliente no encontrado. Debe registrarse primero.\n");
                                    }
                                    break;
                            }
                        }
                        break;

                    //ADMINISTRADOR
                    case "A":
                        Administrador admin = tienda.getAdministrador();

                        if (admin == null) {
                            System.out.println("No hay administrador cargado. Debe crear uno.");
                            String nombreAdmin = leerStringSeguro("Nombre: ");
                            String dniAdmin = "";
                            while (true) {
                                dniAdmin = leerStringSeguro("DNI (8 dígitos): ");
                                if (dniAdmin.matches("\\d{8}")) {
                                    break;
                                }
                                System.out.println("❌ DNI inválido.");
                            }
                            double sueldo = leerDoubleSeguro("Sueldo: $", 0.01, 1_000_000);
                            String password = leerStringSeguro("Contraseña para administrador: ");
                            admin = new Administrador(nombreAdmin, dniAdmin, sueldo, password);
                            tienda.registrarAdministrador(admin);
                            System.out.println("✅ Administrador creado y guardado.\n");
                        }

                        String pass = leerStringSeguro("Ingrese contraseña: ");
                        if (!tienda.validarPasswordAdmin(pass)) {
                            System.out.println("❌ Contraseña incorrecta.\n");
                            break;
                        }

                        int opcionAdmin = 0;
                        while (opcionAdmin != 9) {
                            System.out.println("=== MODO ADMINISTRADOR ===");
                            System.out.println("1 - Agregar producto");
                            System.out.println("2 - Modificar producto");
                            System.out.println("3 - Ver clientes");
                            System.out.println("4 - Modificar cliente");
                            System.out.println("5 - Ver facturas de un cliente");
                            System.out.println("6 - Ver productos disponibles");
                            System.out.println("7 - Mostrar datos de administrador");
                            System.out.println("8 - Reiniciar base de datos");
                            System.out.println("9 - Salir");
                            opcionAdmin = leerIntSeguro("Seleccione opción (1-9): ", 1, 9);

                            switch (opcionAdmin) {
                                case 1:
                                    String tipo = "";
                                    // Validar que solo sea "Componente" o "Periférico"
                                    while (true) {
                                        tipo = leerStringSeguro("Tipo (Componente / Periférico): ");
                                        if (tipo.equalsIgnoreCase("Componente") || tipo.equalsIgnoreCase("Periférico")) {
                                            break;
                                        }
                                        System.out.println("❌ Tipo inválido. Debe ser 'Componente' o 'Periférico'.");
                                    }

                                    String descripcion = leerStringSeguro("Descripción: ");
                                    double precio = leerDoubleSeguro("Precio $: ", 0.01, 1_000_000);
                                    int stock = leerIntSeguro("Stock inicial: ", 0, 1_000_000);

                                    if (tipo.equalsIgnoreCase("Componente")) {
                                        String socket = leerStringSeguro("Tipo de socket: ");
                                        Componente comp = new Componente("Componente", descripcion, precio, stock, socket);

                                        if (comp.validarDatos(socket)) {
                                            tienda.agregarProducto(comp);
                                            System.out.println("✅ Componente validado y agregado.\n");
                                        } else {
                                            System.out.println("❌ Datos inválidos para el componente.\n");
                                        }
                                    } else {
                                        String conexion = leerStringSeguro("Tipo de conexión: ");
                                        Periferico per = new Periferico("Periférico", descripcion, precio, stock, conexion);

                                        if (per.validarDatos(true)) {
                                            tienda.agregarProducto(per);
                                            System.out.println("✅ Periférico validado y agregado.\n");
                                        } else {
                                            System.out.println("❌ Datos inválidos para el periférico.\n");
                                        }
                                    }

                                    System.out.println("✅ Producto agregado.\n");
                                    break;

                                case 2:
                                    int idProdMod = leerIntSeguro("Ingrese ID producto: ", 1, 100000);
                                    Producto prodMod = tienda.buscarProductoPorId(idProdMod);
                                    if (prodMod == null) {
                                        System.out.println("❌ Producto no encontrado.");
                                        break;
                                    }
                                    double nuevoPrecio = leerDoubleSeguro("Nuevo precio $: ", 0.01, 1_000_000);
                                    int nuevoStock = leerIntSeguro("Nuevo stock: ", 0, 1_000_000);
                                    tienda.modificarProducto(prodMod, nuevoPrecio, nuevoStock);
                                    System.out.println("✅ Producto modificado.\n");
                                    break;

                                case 3:
                                    tienda.mostrarClientes();
                                    break;

                                case 4:
                                    String dniCMod = leerStringSeguro("Ingrese DNI cliente: ");
                                    Cliente clienteMod = tienda.buscarClientePorDni(dniCMod);
                                    if (clienteMod == null) {
                                        System.out.println("❌ Cliente no encontrado.");
                                        break;
                                    }
                                    String nuevoNombreC = leerStringSeguro("Nuevo nombre: ");
                                    tienda.modificarCliente(clienteMod, nuevoNombreC, clienteMod.getDni());
                                    System.out.println("✅ Cliente modificado.\n");
                                    break;

                                case 5:
                                    String dniCFact = leerStringSeguro("Ingrese DNI cliente: ");
                                    tienda.mostrarFacturasCliente(dniCFact);
                                    break;

                                case 6:
                                    tienda.mostrarProductos();
                                    break;

                                case 7:

                                    admin.mostrar();
                                    break;

                                case 8:
                                    tienda.reiniciarBase();
                                    break;

                            }
                        }
                        break;

                    case "S":
                        salirPrograma = true;
                        System.out.println("👋 Saliendo del programa.");
                        break;

                    default:
                        System.out.println("❌ Opción inválida.");
                        break;
                }
            }

        } catch (Exception ex) {
            System.err.println("❌ Error inesperado: " + ex.getMessage());
        }
    }

    public static int leerIntSeguro(String msg, int min, int max) {
        while (true) {
            try {
                return AyudaScanner.readInt(msg, min, max);
            } catch (ExcepcionValorEntrada e) {
                System.out.println("❌ Entrada inválida: " + e.getMessage());
            }
        }
    }

    public static double leerDoubleSeguro(String msg, double min, double max) {
        while (true) {
            try {
                return AyudaScanner.readDouble(msg, min, max);
            } catch (ExcepcionValorEntrada e) {
                System.out.println("❌ Entrada inválida: " + e.getMessage());
            }
        }
    }

    public static String leerStringSeguro(String msg) {
        while (true) {
            try {
                return AyudaScanner.readString(msg);
            } catch (ExcepcionValorEntrada e) {
                System.out.println("❌ Entrada inválida: " + e.getMessage());
            }
        }
    }
}
