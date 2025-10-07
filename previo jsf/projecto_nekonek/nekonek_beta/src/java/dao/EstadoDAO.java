package dao;

import control.CoonBD;
import modelo.Estado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstadoDAO {

    // ===========================
    // Listar todos los estados
    // ===========================
    public List<Estado> listar() {
        return obtenerTodos(); // 🔹 Reutiliza obtenerTodos()
    }

    // ===========================
    // Obtener todos (para Series)
    // ===========================
    public List<Estado> obtenerTodos() {
        List<Estado> estados = new ArrayList<>();
        String sql = "SELECT * FROM estados ORDER BY e_nombre";

        try (Connection conn = CoonBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Estado estado = new Estado();
                estado.setEId(rs.getInt("e_id"));
                estado.setENombre(rs.getString("e_nombre"));
                estados.add(estado);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener estados: " + e.getMessage());
            e.printStackTrace();
        }

        return estados;
    }

    // ===========================
    // Buscar un estado por ID
    // ===========================
    public Estado obtenerPorId(int id) {
        Estado estado = null;
        String sql = "SELECT * FROM estados WHERE e_id=?";

        try (Connection conn = CoonBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    estado = new Estado();
                    estado.setEId(rs.getInt("e_id"));
                    estado.setENombre(rs.getString("e_nombre"));
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener estado por ID: " + e.getMessage());
            e.printStackTrace();
        }

        return estado;
    }
}
