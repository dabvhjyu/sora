package dao;

import control.CoonBD;
import modelo.Usuario;
import modelo.Rol;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;

public class UsuarioDAO {
    
    private CoonBD conexion;
    
    public UsuarioDAO() {
        this.conexion = new CoonBD();
    }
    
   public boolean registrarUsuario(Usuario usuario) {
    Connection con = null;
    PreparedStatement ps = null;
    
    try {
        con = CoonBD.conectar();
        
        String sql = "INSERT INTO usuarios (u_email, u_password, u_nombre, u_descripcion, u_telefono, rol_u) " +
                    "VALUES (?, MD5(?), ?, ?, ?, ?)";
        
        ps = con.prepareStatement(sql);
        ps.setString(1, usuario.getUEmail());
        ps.setString(2, usuario.getUPassword()); // La contraseña se encripta con MD5 en la query
        ps.setString(3, usuario.getUNombre());
        ps.setString(4, usuario.getUDescripcion());
        ps.setString(5, usuario.getUTelefono());
        ps.setInt(6, usuario.getRolU()); // Siempre será 2 para usuarios normales
        
        int resultado = ps.executeUpdate();
        return resultado > 0;
        
    } catch (SQLException e) {
        System.err.println("Error al registrar usuario: " + e.getMessage());
        e.printStackTrace();
        return false;
    } finally {
        try {
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

/**
 * Verificar si un email ya existe en la base de datos
 * @param email Email a verificar
 * @return true si existe, false si no existe
 */
public boolean existeEmail(String email) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try {
        con = CoonBD.conectar();
        
        String sql = "SELECT COUNT(*) FROM usuarios WHERE u_email = ?";
        ps = con.prepareStatement(sql);
        ps.setString(1, email);
        
        rs = ps.executeQuery();
        
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        
        return false;
        
    } catch (SQLException e) {
        System.err.println("Error al verificar email: " + e.getMessage());
        e.printStackTrace();
        return true; // Por seguridad, asumir que existe si hay error
    } finally {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
    
    // Obtener usuario por email y password (para login)
    public Usuario obtenerUsuarioPorCredenciales(String email, String password) {
        String sql = "SELECT u.*, r.r_nombre, r.r_descripcion " +
                    "FROM usuarios u " +
                    "LEFT JOIN roles r ON u.rol_u = r.r_id " +
                    "WHERE u.u_email = ? AND u.u_password = ?";
        
        try (Connection conn = conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String passwordEncriptada = encriptarPassword(password);
            pstmt.setString(1, email);
            pstmt.setString(2, passwordEncriptada);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setUId(rs.getInt("u_id"));
                usuario.setUEmail(rs.getString("u_email"));
                usuario.setUPassword(rs.getString("u_password"));
                usuario.setUNombre(rs.getString("u_nombre"));
                usuario.setUDescripcion(rs.getString("u_descripcion"));
                usuario.setUTelefono(rs.getString("u_telefono"));
                usuario.setRolU(rs.getInt("rol_u"));
                usuario.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                
                // Crear objeto Rol si existe
                if (rs.getString("r_nombre") != null) {
                    Rol rol = new Rol();
                    rol.setRId(rs.getInt("rol_u"));
                    rol.setRNombre(rs.getString("r_nombre"));
                    usuario.setRol(rol);
                }
                
                return usuario;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario: " + e.getMessage());
        }
        
        return null;
    }
    
    // Obtener todos los usuarios
    public List<Usuario> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT u.*, r.r_nombre " +
                    "FROM usuarios u " +
                    "LEFT JOIN roles r ON u.rol_u = r.r_id " +
                    "ORDER BY u.fecha_registro DESC";
        
        try (Connection conn = conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setUId(rs.getInt("u_id"));
                usuario.setUEmail(rs.getString("u_email"));
                usuario.setUNombre(rs.getString("u_nombre"));
                usuario.setUDescripcion(rs.getString("u_descripcion"));
                usuario.setUTelefono(rs.getString("u_telefono"));
                usuario.setRolU(rs.getInt("rol_u"));
                usuario.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                
                // Crear objeto Rol si existe
                if (rs.getString("r_nombre") != null) {
                    Rol rol = new Rol();
                    rol.setRId(rs.getInt("rol_u"));
                    rol.setRNombre(rs.getString("r_nombre"));
                    usuario.setRol(rol);
                }
                
                usuarios.add(usuario);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios: " + e.getMessage());
        }
        
        return usuarios;
    }
    
    // Actualizar usuario
    public boolean actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuarios SET u_nombre = ?, u_descripcion = ?, u_telefono = ?, rol_u = ? " +
                    "WHERE u_id = ?";
        
        try (Connection conn = conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, usuario.getUNombre());
            pstmt.setString(2, usuario.getUDescripcion());
            pstmt.setString(3, usuario.getUTelefono());
            pstmt.setInt(4, usuario.getRolU());
            pstmt.setInt(5, usuario.getUId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
        }
        
        return false;
    }
    
    // Eliminar usuario
    public boolean eliminarUsuario(Integer userId) {
        String sql = "DELETE FROM usuarios WHERE u_id = ?";
        
        try (Connection conn = conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
        }
        
        return false;
    }
    
    // Método privado para encriptar contraseñas usando SHA-256
    private String encriptarPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
            
        } catch (Exception e) {
            System.err.println("Error al encriptar contraseña: " + e.getMessage());
            return password; // En caso de error, devolver la contraseña original
        }
    }
}