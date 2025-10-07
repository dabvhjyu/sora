package control;

import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import modelo.Usuario;
import modelo.Rol;


@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {
    private Usuario usuario = new Usuario();
    private String email; // Para el formulario de login
    private String password; // Para el formulario de login

    // Getters y Setters para el formulario
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void verificarSiYaEstaLogueado() {
        if (isLoggedIn()) {
            try {
                HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                        .getExternalContext().getSession(false);
                Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
                
                if (usuarioSesion != null) {
                    String contextPath = FacesContext.getCurrentInstance()
                            .getExternalContext().getRequestContextPath();
                    
                    // Verificar rol: 1=Administrador, 2=Usuario Normal
                    if (usuarioSesion.getRolU() == 1) {
                        FacesContext.getCurrentInstance().getExternalContext()
                                .redirect(contextPath + "/Administrador/autenticar.xhtml");
                    } else if (usuarioSesion.getRolU() == 2) {
                        FacesContext.getCurrentInstance().getExternalContext()
                                .redirect(contextPath + "/Usuario/autenticar.xhtml");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String autenticar() {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try {
        // Validación básica de formato de email en el backend
        if (email == null || email.trim().isEmpty() || 
            !email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Por favor ingresa un email válido", null));
            return null;
        }
        
        // Validación de contraseña
        if (password == null || password.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Por favor ingresa tu contraseña", null));
            return null;
        }
        
        con = CoonBD.conectar();
        
        // Consulta adaptada a la nueva estructura de BD
        String sql = "SELECT u.u_id, u.u_email, u.u_password, u.u_nombre, " +
                    "u.u_descripcion, u.u_telefono, u.rol_u, u.fecha_registro, " +
                    "r.r_id, r.r_nombre " +
                    "FROM usuarios u " +
                    "INNER JOIN roles r ON u.rol_u = r.r_id " +
                    "WHERE u.u_email = ? AND u.u_password = ?";
        
        ps = con.prepareStatement(sql);
        ps.setString(1, email.trim().toLowerCase()); // Normalizar email
        ps.setString(2, Utilidades.encriptar(password)); // Usando tu clase Utilidades

        rs = ps.executeQuery();

        if (rs.next()) {
            // Crear objeto usuario para la sesión
            Usuario usuarioAutenticado = new Usuario();
            usuarioAutenticado.setUId(rs.getInt("u_id"));
            usuarioAutenticado.setUEmail(rs.getString("u_email"));
            usuarioAutenticado.setUPassword(rs.getString("u_password"));
            usuarioAutenticado.setUNombre(rs.getString("u_nombre"));
            usuarioAutenticado.setUDescripcion(rs.getString("u_descripcion"));
            usuarioAutenticado.setUTelefono(rs.getString("u_telefono"));
            usuarioAutenticado.setRolU(rs.getInt("rol_u"));
            usuarioAutenticado.setFechaRegistro(rs.getTimestamp("fecha_registro"));
            
            // Crear objeto rol
            Rol rol = new Rol();
            rol.setRId(rs.getInt("r_id"));
            rol.setRNombre(rs.getString("r_nombre"));
            usuarioAutenticado.setRol(rol);

            // Establecer en sesión
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                    .getExternalContext().getSession(true);
            session.setAttribute("usuario", usuarioAutenticado);
            session.setAttribute("user", usuarioAutenticado.getUNombre()); // Para compatibilidad
            session.setAttribute("usuarioId", usuarioAutenticado.getUId()); // 🔥 Necesario para SerieBean
            
            // Actualizar el bean de sesión también
            this.usuario = usuarioAutenticado;
            
            // Limpiar campos del formulario
            this.email = "";
            this.password = "";

            // Redirigir según el rol
            if (usuarioAutenticado.getRolU() == 1) { // Administrador
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "¡Bienvenido Administrador " + usuarioAutenticado.getUNombre() + "!", null));
                return "/Administrador/autenticar.xhtml?faces-redirect=true";
            } else if (usuarioAutenticado.getRolU() == 2) { // Usuario Normal
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "¡Bienvenido " + usuarioAutenticado.getUNombre() + "!", null));
                return "/Usuario/autenticar.xhtml?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Rol de usuario no autorizado", null));
                return null;
            }
        } else {
            // ESTE ES EL MENSAJE QUE NO TE APARECÍA
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Email o contraseña incorrectos.   Por favor verifica tus datos.", null));
            
            // Limpiar solo la contraseña por seguridad
            this.password = "";
            
            
            return null; // Importante: retornar null para que se quede en la misma página
        }

    } catch (SQLException e) {
        e.printStackTrace();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Error del sistema. Intenta nuevamente en unos momentos.", null));
        return null;
    } catch (Exception e) {
        e.printStackTrace();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Error inesperado. Por favor contacta al soporte técnico.", null));
        return null;
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

    public String cerrarSesion() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
            if (session != null) {
                session.invalidate();
            }
            
            // Limpiar el bean
            usuario = new Usuario();
            email = "";
            password = "";
            
            // Agregar mensaje de confirmación
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Sesión cerrada correctamente", null));
            
            return "/login.xhtml?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "/login.xhtml?faces-redirect=true";
        }
    }

    public boolean isLoggedIn() {
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                    .getExternalContext().getSession(false);
            if (session != null) {
                Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
                return usuarioSesion != null && usuarioSesion.getUId() != null;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void verificarLogin() {
        if (!isLoggedIn()) {
            try {
                String contextPath = FacesContext.getCurrentInstance()
                        .getExternalContext().getRequestContextPath();
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect(contextPath + "/login.xhtml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Método para obtener el usuario actual de la sesión
    public Usuario getUsuarioActual() {
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                    .getExternalContext().getSession(false);
            if (session != null) {
                Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
                if (usuarioSesion != null) {
                    this.usuario = usuarioSesion; 
                    return usuarioSesion;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Usuario();
    }

    public String getRolUsuario() {
        Usuario usuarioActual = getUsuarioActual();
        if (usuarioActual != null && usuarioActual.getRol() != null) {
            return usuarioActual.getRol().getRNombre();
        }
        return "";
    }

    public boolean isAdministrador() {
        Usuario usuarioActual = getUsuarioActual();
        return usuarioActual != null && usuarioActual.getRolU() != null 
               && usuarioActual.getRolU() == 1; // 1 = Administrador
    }

    public boolean isUsuarioNormal() {
        Usuario usuarioActual = getUsuarioActual();
        return usuarioActual != null && usuarioActual.getRolU() != null 
               && usuarioActual.getRolU() == 2; // 2 = Usuario Normal
    }

    public void verifSesion() {
        String nom = (String) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("user");
        
        if (nom == null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("noacceso.xhtml");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Método para verificar acceso solo a administradores
    public void verificarAccesoAdministrador() {
        if (!isAdministrador()) {
            try {
                String contextPath = FacesContext.getCurrentInstance()
                        .getExternalContext().getRequestContextPath();
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Acceso denegado. Solo administradores.", null));
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect(contextPath + "/noacceso.xhtml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Método para verificar acceso a usuarios normales
    public void verificarAccesoUsuario() {
        if (!isUsuarioNormal() && !isAdministrador()) {
            try {
                String contextPath = FacesContext.getCurrentInstance()
                        .getExternalContext().getRequestContextPath();
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Debe iniciar sesión para acceder.", null));
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect(contextPath + "/login.xhtml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Método utilitario para obtener el ID del usuario actual
    public Integer getUsuarioActualId() {
        Usuario usuarioActual = getUsuarioActual();
        return usuarioActual != null ? usuarioActual.getUId() : null;
    }

    // Método utilitario para obtener el nombre del usuario actual
    public String getUsuarioActualNombre() {
        Usuario usuarioActual = getUsuarioActual();
        return usuarioActual != null ? usuarioActual.getUNombre() : "";
    }
}