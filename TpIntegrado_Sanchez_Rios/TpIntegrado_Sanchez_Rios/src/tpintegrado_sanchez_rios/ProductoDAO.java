package tpintegrado_sanchez_rios;

import java.sql.*;
import java.util.*;

public class ProductoDAO {

    public int saveProducto(Producto p) throws SQLException, ExcepcionValorEntrada {
        if (!p.validarDatos()) throw new ExcepcionValorEntrada("❌ Datos inválidos.");

        String sql = "INSERT INTO productos(tipo, descripcion, precio, stock, subtipo, socket, conexion) VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = ManejoBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getTipo());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getStock());
            ps.setString(5, p.getClass().getSimpleName());

            if (p instanceof Componente) {
                ps.setString(6, ((Componente) p).getSocket());
                ps.setNull(7, Types.VARCHAR);
            } else if (p instanceof Periferico) {
                ps.setNull(6, Types.VARCHAR);
                ps.setString(7, ((Periferico) p).getConexion());
            } else {
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.VARCHAR);
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    p.setId(id);
                    return id;
                }
            }
        }

        return -1;
    }

    public List<Producto> getAll() throws SQLException {
        List<Producto> res = new ArrayList<>();
        String sql = "SELECT * FROM productos";

        try (Connection conn = ManejoBD.getInstance().getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String subtipo = rs.getString("subtipo");
                Producto p;

                if ("Componente".equals(subtipo)) {
                    p = new Componente(rs.getString("tipo"), rs.getString("descripcion"),
                                       rs.getDouble("precio"), rs.getInt("stock"),
                                       rs.getString("socket"));
                } else if ("Periferico".equals(subtipo)) {
                    p = new Periferico(rs.getString("tipo"), rs.getString("descripcion"),
                                       rs.getDouble("precio"), rs.getInt("stock"),
                                       rs.getString("conexion"));
                } else {
                    p = new Periferico(rs.getString("tipo"), rs.getString("descripcion"),
                                       rs.getDouble("precio"), rs.getInt("stock"),
                                       "Desconocido");
                }

                p.setId(rs.getInt("id"));
                res.add(p);
            }
        }

        return res;
    }

    public void update(Producto p) throws SQLException, ExcepcionValorEntrada {
        if (!p.validarDatos()) throw new ExcepcionValorEntrada("❌ Datos inválidos.");

        String sql = "UPDATE productos SET tipo=?, descripcion=?, precio=?, stock=?, socket=?, conexion=? WHERE id=?";

        try (Connection conn = ManejoBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getTipo());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getStock());

            if (p instanceof Componente) {
                ps.setString(5, ((Componente) p).getSocket());
                ps.setNull(6, Types.VARCHAR);
            } else if (p instanceof Periferico) {
                ps.setNull(5, Types.VARCHAR);
                ps.setString(6, ((Periferico) p).getConexion());
            } else {
                ps.setNull(5, Types.VARCHAR);
                ps.setNull(6, Types.VARCHAR);
            }

            ps.setInt(7, p.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM productos WHERE id=?";

        try (Connection conn = ManejoBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Producto findById(int id) throws SQLException, ExcepcionNoEncuentra {
        String sql = "SELECT * FROM productos WHERE id=?";

        try (Connection conn = ManejoBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String subtipo = rs.getString("subtipo");
                    Producto p;

                    if ("Componente".equals(subtipo)) {
                        p = new Componente(rs.getString("tipo"), rs.getString("descripcion"),
                                           rs.getDouble("precio"), rs.getInt("stock"),
                                           rs.getString("socket"));
                    } else if ("Periferico".equals(subtipo)) {
                        p = new Periferico(rs.getString("tipo"), rs.getString("descripcion"),
                                           rs.getDouble("precio"), rs.getInt("stock"),
                                           rs.getString("conexion"));
                    } else {
                        p = new Periferico(rs.getString("tipo"), rs.getString("descripcion"),
                                           rs.getDouble("precio"), rs.getInt("stock"),
                                           "Desconocido");
                    }

                    p.setId(rs.getInt("id"));
                    return p;
                }
            }
        }

        throw new ExcepcionNoEncuentra("❌ Producto no encontrado (id: " + id + ")");
    }
}
