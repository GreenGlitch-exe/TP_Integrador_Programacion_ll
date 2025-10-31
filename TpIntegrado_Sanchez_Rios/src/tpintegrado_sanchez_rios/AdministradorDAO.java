package tpintegrado_sanchez_rios;

import java.sql.*;

public class AdministradorDAO {

    public void saveAdministrador(Administrador admin) throws SQLException {
        String sql = "INSERT OR REPLACE INTO administradores(dni, nombre, sueldo, password) VALUES(?,?,?,?)";
        try (Connection conn = ManejoBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, admin.getDni());
            ps.setString(2, admin.getNombre());
            ps.setDouble(3, admin.getSueldo());
            ps.setString(4, admin.getPassword());
            ps.executeUpdate();
        }
    }

    public Administrador getAdministrador() throws SQLException {
        String sql = "SELECT * FROM administradores LIMIT 1";
        try (Connection conn = ManejoBD.getInstance().getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return new Administrador(
                        rs.getString("nombre"),
                        rs.getString("dni"),
                        rs.getDouble("sueldo"),
                        rs.getString("password")
                );
            }
        }
        return null;
    }

    public boolean validarPassword(Administrador admin, String pass) {
        return admin != null && admin.getPassword().equals(pass);
    }
}
