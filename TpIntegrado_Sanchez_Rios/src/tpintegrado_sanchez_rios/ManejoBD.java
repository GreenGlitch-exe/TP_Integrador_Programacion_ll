package tpintegrado_sanchez_rios;

import java.sql.*;

public class ManejoBD {

    private boolean yaMostroMensaje = false;
    private static ManejoBD instance;
    private Connection connection;

    // 🔧 Cambiá esta línea según tu sistema operativo
    // Para Linux:
    private final String url = "jdbc:sqlite:/home/nekuruixd/NetBeansProjects/TpIntegrado_Sanchez_Rios/Megatecnologia_2.sqlite";

    // Para Windows (descomentá esta y comentá la de arriba si usás Windows):
    // private final String url = "jdbc:sqlite:C:/Users/Usuario/Desktop/Megatecnologia_2.sqlite";
    private ManejoBD() {
        connect();
    }

    public static ManejoBD getInstance() {
        if (instance == null) {
            instance = new ManejoBD();
        }
        return instance;
    }

    private void connect() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url);
                if (!yaMostroMensaje) {
                    System.out.println("✅ Conectado a la base de datos SQLite.");
                    System.out.println("📂 Ruta: "
                            + new java.io.File(url.replace("jdbc:sqlite:", "")).getAbsolutePath());
                    yaMostroMensaje = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al verificar la conexión: " + e.getMessage());
        }
        return connection;
    }

    // 🧱 Crear tablas solo si no existen
    public void initDatabase() {
        try (Statement st = getConnection().createStatement()) {

            st.execute("CREATE TABLE IF NOT EXISTS productos ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "tipo TEXT NOT NULL,"
                    + "descripcion TEXT NOT NULL,"
                    + "precio REAL NOT NULL,"
                    + "stock INTEGER NOT NULL,"
                    + "subtipo TEXT,"
                    + "socket TEXT,"
                    + "conexion TEXT"
                    + ");");

            st.execute("CREATE TABLE IF NOT EXISTS clientes ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "nombre TEXT NOT NULL,"
                    + "dni TEXT NOT NULL UNIQUE"
                    + ");");

            st.execute("CREATE TABLE IF NOT EXISTS administradores ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "nombre TEXT NOT NULL,"
                    + "dni TEXT NOT NULL UNIQUE,"
                    + "password TEXT NOT NULL,"
                    + "sueldo REAL NOT NULL"
                    + ");");

            st.execute("CREATE TABLE IF NOT EXISTS facturas ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "cliente_dni TEXT,"
                    + "fecha TEXT,"
                    + "total REAL,"
                    + "FOREIGN KEY(cliente_dni) REFERENCES clientes(dni)"
                    + ");");

            st.execute("CREATE TABLE IF NOT EXISTS factura_items ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "factura_id INTEGER NOT NULL,"
                    + "producto_id INTEGER NOT NULL,"
                    + "cantidad INTEGER NOT NULL,"
                    + "precio_unitario REAL NOT NULL,"
                    + "FOREIGN KEY(factura_id) REFERENCES facturas(id),"
                    + "FOREIGN KEY(producto_id) REFERENCES productos(id)"
                    + ");");

            System.out.println("✅ Base de datos inicializada.");

        } catch (SQLException e) {
            System.err.println("❌ Error al inicializar la base de datos: " + e.getMessage());
        }
    }

    public void resetDatabase() {
        try (Statement st = getConnection().createStatement()) {
            // 🔹 Eliminar todas las tablas
            st.execute("DROP TABLE IF EXISTS factura_items;");
            st.execute("DROP TABLE IF EXISTS facturas;");
            st.execute("DROP TABLE IF EXISTS productos;");
            st.execute("DROP TABLE IF EXISTS clientes;");
            st.execute("DROP TABLE IF EXISTS administradores;");

            // 🔹 Crear las tablas de nuevo
            st.execute("CREATE TABLE productos ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "tipo TEXT NOT NULL,"
                    + "descripcion TEXT NOT NULL,"
                    + "precio REAL NOT NULL,"
                    + "stock INTEGER NOT NULL,"
                    + "subtipo TEXT,"
                    + "socket TEXT,"
                    + "conexion TEXT"
                    + ");");

            st.execute("CREATE TABLE clientes ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "nombre TEXT NOT NULL,"
                    + "dni TEXT NOT NULL UNIQUE"
                    + ");");

            st.execute("CREATE TABLE administradores ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "nombre TEXT NOT NULL,"
                    + "dni TEXT NOT NULL UNIQUE,"
                    + "password TEXT NOT NULL,"
                    + "sueldo REAL NOT NULL"
                    + ");");

            st.execute("CREATE TABLE facturas ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "cliente_dni TEXT,"
                    + "fecha TEXT,"
                    + "total REAL,"
                    + "FOREIGN KEY(cliente_dni) REFERENCES clientes(dni)"
                    + ");");

            st.execute("CREATE TABLE factura_items ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "factura_id INTEGER NOT NULL,"
                    + "producto_id INTEGER NOT NULL,"
                    + "cantidad INTEGER NOT NULL,"
                    + "precio_unitario REAL NOT NULL,"
                    + "FOREIGN KEY(factura_id) REFERENCES facturas(id),"
                    + "FOREIGN KEY(producto_id) REFERENCES productos(id)"
                    + ");");

            System.out.println("✅ Base de datos reiniciada correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al reiniciar la base de datos: " + e.getMessage());
        }
    }

    public void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔒 Conexión a la base de datos cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
