
package tpintegrado_sanchez_rios;

import java.sql.*;
import java.util.Map;

public class FacturaDAO {

    public int saveFactura(Factura f) throws SQLException, ExcepcionValorEntrada {
        if (f == null || f.getItems().isEmpty()) {
            throw new ExcepcionValorEntrada("❌ La factura no puede estar vacía.");
        }

        String sql = "INSERT INTO facturas(fecha, total, cliente_dni) VALUES(?,?,?)";
        try (Connection conn = ManejoBD.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, f.getFecha());
            ps.setDouble(2, f.getTotal());
            ps.setString(3, f.getCliente() != null ? f.getCliente().getDni() : null);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int fid = rs.getInt(1);
                    f.setId(fid);

                    for (Map.Entry<Producto, Integer> e : f.getItems().entrySet()) {
                        String itemSql = "INSERT INTO factura_items(factura_id, producto_id, cantidad, precio_unitario) VALUES(?,?,?,?)";
                        try (PreparedStatement psi = conn.prepareStatement(itemSql)) {
                            psi.setInt(1, fid);
                            psi.setInt(2, e.getKey().getId());
                            psi.setInt(3, e.getValue());
                            psi.setDouble(4, e.getKey().getPrecio()); // precio histórico al momento de la venta
                            psi.executeUpdate();
                        } catch (SQLException ex) {
                            System.err.println("Error al insertar item de factura: " + ex.getMessage());
                        }
                    }

                    return fid;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar factura: " + e.getMessage());
            throw e;
        }
        return -1;
    }

}
