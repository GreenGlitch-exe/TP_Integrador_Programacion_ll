package tpintegrado_sanchez_rios;

import java.sql.*;
import java.util.*;

public class ClienteDAO {

    public boolean saveCliente(Cliente c) throws SQLException, ExcepcionValorEntrada {
        if (c.getNombre() == null || c.getNombre().isEmpty() || c.getDni() == null || c.getDni().isEmpty()) {
            throw new ExcepcionValorEntrada("❌ Nombre o DNI inválido.");
        }

        String sql = "INSERT INTO clientes(dni, nombre) VALUES(?, ?)";
        try (Connection conn = ManejoBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getDni());
            ps.setString(2, c.getNombre());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE") || e.getMessage().contains("PRIMARY KEY")) {
                System.out.println("⚠️ Ya existe un cliente con ese DNI.");
            } else {
                throw e;
            }
        }
        return false;
    }

    public void update(Cliente c) throws SQLException, ExcepcionValorEntrada {
        if (c.getNombre() == null || c.getNombre().isEmpty()) {
            throw new ExcepcionValorEntrada("❌ El nombre del cliente no puede estar vacío.");
        }
        String sql = "UPDATE clientes SET nombre=?, dni=? WHERE dni=?";
        try (Connection conn = ManejoBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDni());
            ps.setString(3, c.getDni());
            ps.executeUpdate();
        }
    }

    public List<Cliente> getAll() throws SQLException {
        List<Cliente> res = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Connection conn = ManejoBD.getInstance().getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Cliente c = new Cliente(rs.getString("nombre"), rs.getString("dni"));
                res.add(c);
            }
        }
        return res;
    }

    public Cliente findByDni(String dni) throws SQLException, ExcepcionNoEncuentra {
        String sql = "SELECT * FROM clientes WHERE dni = ?";
        try (Connection conn = ManejoBD.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(rs.getString("nombre"), rs.getString("dni"));
                }
            }
        }
        throw new ExcepcionNoEncuentra("❌ Cliente no encontrado (DNI: " + dni + ")");
    }
}
