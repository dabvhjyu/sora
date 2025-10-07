// GeneroDAO.java
package dao;

import control.CoonBD;
import modelo.Genero;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GeneroDAO {
    
    public List<Genero> obtenerTodos() {
        List<Genero> generos = new ArrayList<>();
        String sql = "SELECT * FROM generos ORDER BY g_nombre";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = CoonBD.conectar();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Genero genero = new Genero();
                genero.setGId(rs.getInt("g_id"));
                genero.setGNombre(rs.getString("g_nombre"));
                generos.add(genero);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener géneros: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        
        return generos;
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