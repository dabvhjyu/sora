// PaisDAO.java
package dao;

import control.CoonBD;
import modelo.Pais;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaisDAO {
    
    public List<Pais> obtenerTodos() {
        List<Pais> paises = new ArrayList<>();
        String sql = "SELECT * FROM paises ORDER BY p_nombre";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = CoonBD.conectar();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Pais pais = new Pais();
                pais.setPId(rs.getInt("p_id"));
                pais.setPNombre(rs.getString("p_nombre"));
                paises.add(pais);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener países: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        
        return paises;
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
