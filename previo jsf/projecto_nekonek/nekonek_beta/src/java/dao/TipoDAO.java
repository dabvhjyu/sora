// TipoDAO.java
package dao;

import control.CoonBD;
import modelo.Tipo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoDAO {
    
    public List<Tipo> obtenerTodos() {
        List<Tipo> tipos = new ArrayList<>();
        String sql = "SELECT * FROM tipos ORDER BY t_nombre";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = CoonBD.conectar();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Tipo tipo = new Tipo();
                tipo.setTId(rs.getInt("t_id"));
                tipo.setTNombre(rs.getString("t_nombre"));
                tipos.add(tipo);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener tipos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        
        return tipos;
    }
    
    private void cerrarRecursos(Connection conn, PreparedStatement stmt, ResultSet rs) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) { 
                System.err.println("❌ Error al cerrar ResultSet: " + e.getMessage()); 
            }
        }
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException e) { 
                System.err.println("❌ Error al cerrar PreparedStatement: " + e.getMessage()); 
            }
        }
        if (conn != null) {
            CoonBD.cerrarConexion(conn);
        }
    }
}