package dao;

import control.CoonBD;
import modelo.Rol;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolDAO {
    
    private final CoonBD conexion;
    
    public RolDAO() {
        this.conexion = new CoonBD();
    }
    
    // Obtener todos los roles
    public List<Rol> obtenerTodosLosRoles() {
        List<Rol> roles = new ArrayList<>();
        String sql = "SELECT r_id, r_nombre FROM roles ORDER BY r_nombre";
        
        try (Connection conn = conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Rol rol = new Rol();
                rol.setRId(rs.getInt("r_id"));
                rol.setRNombre(rs.getString("r_nombre"));
                roles.add(rol);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener roles: " + e.getMessage());
        }
        
        return roles;
    }
    
    // Obtener rol por ID
    public Rol obtenerRolPorId(Integer rolId) {
        String sql = "SELECT r_id, r_nombre FROM roles WHERE r_id = ?";
        
        try (Connection conn = conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, rolId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Rol rol = new Rol();
                rol.setRId(rs.getInt("r_id"));
                rol.setRNombre(rs.getString("r_nombre"));
                return rol;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener rol por ID: " + e.getMessage());
        }
        
        return null;
    }
    
    // Obtener rol por nombre
    public Rol obtenerRolPorNombre(String nombre) {
        String sql = "SELECT r_id, r_nombre FROM roles WHERE r_nombre = ?";
        
        try (Connection conn = conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombre);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Rol rol = new Rol();
                rol.setRId(rs.getInt("r_id"));
                rol.setRNombre(rs.getString("r_nombre"));
                return rol;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener rol por nombre: " + e.getMessage());
        }
        
        return null;
    }
    
    // Crear un nuevo rol
    public boolean crearRol(Rol rol) {
        String sql = "INSERT INTO roles (r_nombre) VALUES (?)";
        
        try (Connection conn = conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, rol.getRNombre());
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                // Obtener el ID generado
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    rol.setRId(generatedKeys.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al crear rol: " + e.getMessage());
        }
        
        return false;
    }
    
    // Actualizar rol existente
    public boolean actualizarRol(Rol rol) {
        String sql = "UPDATE roles SET r_nombre = ? WHERE r_id = ?";
        
        try (Connection conn = conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, rol.getRNombre());
            pstmt.setInt(2, rol.getRId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar rol: " + e.getMessage());
        }
        
        return false;
    }
    
    // Eliminar rol por ID
    public boolean eliminarRol(Integer rolId) {
        // Primero verificar si hay usuarios con este rol
        if (tieneUsuariosAsignados(rolId)) {
            System.err.println("No se puede eliminar el rol porque tiene usuarios asignados");
            return false;
        }
        
        String sql = "DELETE FROM roles WHERE r_id = ?";
        
        try (Connection conn = conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, rolId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar rol: " + e.getMessage());
        }
        
        return false;
    }
    
    // Verificar si el rol existe por nombre (para evitar duplicados)
    public boolean existeRolPorNombre(String nombre) {
        String sql = "SELECT COUNT(*) FROM roles WHERE r_nombre = ?";
        
        try (Connection conn = conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombre);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de rol: " + e.getMessage());
        }
        
        return false;
    }
    
    // Verificar si un rol tiene usuarios asignados
    public boolean tieneUsuariosAsignados(Integer rolId) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE rol_u = ?";
        
        try (Connection conn = conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, rolId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar usuarios asignados: " + e.getMessage());
        }
        
        return false;
    }
    
    // Contar total de roles
    public int contarRoles() {
        String sql = "SELECT COUNT(*) FROM roles";
        
        try (Connection conn = conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar roles: " + e.getMessage());
        }
        
        return 0;
    }
    
    // Obtener roles activos (que tienen usuarios asignados)
    public List<Rol> obtenerRolesActivos() {
        List<Rol> roles = new ArrayList<>();
        String sql = "SELECT DISTINCT r.r_id, r.r_nombre " +
                    "FROM roles r " +
                    "INNER JOIN usuarios u ON r.r_id = u.rol_u " +
                    "ORDER BY r.r_nombre";
        
        try (Connection conn = conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Rol rol = new Rol();
                rol.setRId(rs.getInt("r_id"));
                rol.setRNombre(rs.getString("r_nombre"));
                roles.add(rol);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener roles activos: " + e.getMessage());
        }
        
        return roles;
    }
}