package control;

import dao.UsuarioDAO;
import dao.RolDAO;
import modelo.Usuario;
import modelo.Rol;
import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 * Bean para manejar el registro y gestión de usuarios
 */
@ManagedBean(name = "registroBean")
@SessionScoped
public class RegistroBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Atributos para el formulario de registro
    private String email;
    private String password;
    private String confirmarPassword;
    private String nombre;
    private String descripcion;
    private String telefono;
    // Removido: private Integer rolId; - Ya no se necesita porque siempre será 2
    
    // Atributos para el formulario de login
    private String loginEmail;
    private String loginPassword;
    private boolean recordarSesion;
    
    // Objetos y listas
    private Usuario usuario;
    private List<Rol> roles;
    private List<Usuario> usuarios;
    
    // DAOs
    private UsuarioDAO usuarioDAO;
    private RolDAO rolDAO;
    
    // Mensajes
    private String mensaje;
    private String tipoMensaje; // success, error, warning, info
    
    // Constructor
    public RegistroBean() {
        this.usuarioDAO = new UsuarioDAO();
        this.rolDAO = new RolDAO();
        this.usuario = new Usuario();
        inicializar();
    }
    
    /**
     * Inicializar el bean
     */
    private void inicializar() {
        limpiarFormulario();
        cargarRoles();
    }
    
    /**
     * Cargar lista de roles disponibles
     */
    public void cargarRoles() {
        try {
            this.roles = rolDAO.obtenerTodosLosRoles();
        } catch (Exception e) {
            mostrarMensaje("Error al cargar los roles: " + e.getMessage(), "error");
        }
    }
    
    /**
     * Registrar nuevo usuario con rol fijo de Usuario Normal (rol_u = 2)
     */
    public String registrarUsuario() {
        try {
            // Validar datos del formulario
            if (!validarDatosRegistro()) {
                return null; // Permanecer en la misma página
            }
            
            // Verificar si el email ya existe
            if (usuarioDAO.existeEmail(email)) {
                mostrarMensaje("El email ya está registrado en el sistema", "error");
                return null;
            }
            
            // Crear objeto usuario con rol fijo de Usuario Normal
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setUEmail(email.trim().toLowerCase());
            nuevoUsuario.setUPassword(password); // La encriptación se hace en el DAO
            nuevoUsuario.setUNombre(nombre.trim());
            nuevoUsuario.setUDescripcion(descripcion != null ? descripcion.trim() : "");
            nuevoUsuario.setUTelefono(telefono != null ? telefono.trim() : "");
            nuevoUsuario.setRolU(2); // SIEMPRE asignar rol 2 (Usuario Normal)
            
            // Intentar registrar el usuario
            boolean registroExitoso = usuarioDAO.registrarUsuario(nuevoUsuario);
            
            if (registroExitoso) {
                mostrarMensaje("Usuario registrado exitosamente. Ahora puede iniciar sesión.", "success");
                limpiarFormulario();
                return "login?faces-redirect=true"; // Navegar a la página de login
            } else {
                mostrarMensaje("Error al registrar el usuario. Intente nuevamente.", "error");
                return null;
            }
            
        } catch (Exception e) {
            mostrarMensaje("Error inesperado: " + e.getMessage(), "error");
            return null;
        }
    }
    
    /**
     * Iniciar sesión
     */
    public String iniciarSesion() {
        try {
            // Validar campos de login
            if (!validarDatosLogin()) {
                return null;
            }
            
            // Verificar credenciales
            Usuario usuarioEncontrado = usuarioDAO.obtenerUsuarioPorCredenciales(
                loginEmail.trim().toLowerCase(), loginPassword);
            
            if (usuarioEncontrado != null) {
                // Crear sesión
                FacesContext context = FacesContext.getCurrentInstance();
                HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
                
                session.setAttribute("usuario", usuarioEncontrado);
                session.setAttribute("usuarioId", usuarioEncontrado.getUId());
                session.setAttribute("usuarioNombre", usuarioEncontrado.getUNombre());
                session.setAttribute("usuarioEmail", usuarioEncontrado.getUEmail());
                session.setAttribute("usuarioRol", usuarioEncontrado.getNombreRol());
                
                // Configurar tiempo de sesión si se seleccionó recordar
                if (recordarSesion) {
                    session.setMaxInactiveInterval(60 * 60 * 24 * 7); // 7 días
                } else {
                    session.setMaxInactiveInterval(60 * 30); // 30 minutos
                }
                
                mostrarMensaje("Bienvenido, " + usuarioEncontrado.getUNombre(), "success");
                limpiarFormularioLogin();
                
                return "dashboard?faces-redirect=true"; // Navegar al dashboard
                
            } else {
                mostrarMensaje("Email o contraseña incorrectos", "error");
                return null;
            }
            
        } catch (Exception e) {
            mostrarMensaje("Error al iniciar sesión: " + e.getMessage(), "error");
            return null;
        }
    }
    
    /**
     * Cerrar sesión
     */
    public String cerrarSesion() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
            
            if (session != null) {
                session.invalidate();
            }
            
            mostrarMensaje("Sesión cerrada correctamente", "info");
            return "login?faces-redirect=true"; // Navegar al login
            
        } catch (Exception e) {
            mostrarMensaje("Error al cerrar sesión: " + e.getMessage(), "error");
            return null;
        }
    }
    
    /**
     * Obtener lista de usuarios
     */
    public void cargarUsuarios() {
        try {
            this.usuarios = usuarioDAO.obtenerTodosLosUsuarios();
        } catch (Exception e) {
            mostrarMensaje("Error al cargar usuarios: " + e.getMessage(), "error");
        }
    }
    
    /**
     * Eliminar usuario
     */
    public void eliminarUsuario(Integer userId) {
        try {
            boolean eliminado = usuarioDAO.eliminarUsuario(userId);
            
            if (eliminado) {
                mostrarMensaje("Usuario eliminado correctamente", "success");
                cargarUsuarios(); // Recargar lista
            } else {
                mostrarMensaje("Error al eliminar el usuario", "error");
            }
            
        } catch (Exception e) {
            mostrarMensaje("Error al eliminar usuario: " + e.getMessage(), "error");
        }
    }
    
    /**
     * Validar datos del formulario de registro (actualizado sin rolId)
     */
    private boolean validarDatosRegistro() {
        // Verificar campos obligatorios
        if (email == null || email.trim().isEmpty()) {
            mostrarMensaje("El email es obligatorio", "error");
            return false;
        }
        
        if (password == null || password.trim().isEmpty()) {
            mostrarMensaje("La contraseña es obligatoria", "error");
            return false;
        }
        
        if (confirmarPassword == null || confirmarPassword.trim().isEmpty()) {
            mostrarMensaje("Debe confirmar la contraseña", "error");
            return false;
        }
        
        if (nombre == null || nombre.trim().isEmpty()) {
            mostrarMensaje("El nombre es obligatorio", "error");
            return false;
        }
        
        // Removido: Validación de rolId - Ya no se necesita
        
        // Validar formato de email
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!Pattern.matches(emailRegex, email.trim())) {
            mostrarMensaje("El formato del email no es válido", "error");
            return false;
        }
        
        // Validar longitud de contraseña
        if (password.length() < 6) {
            mostrarMensaje("La contraseña debe tener al menos 6 caracteres", "error");
            return false;
        }
        
        // Verificar que las contraseñas coincidan
        if (!password.equals(confirmarPassword)) {
            mostrarMensaje("Las contraseñas no coinciden", "error");
            return false;
        }
        
        // Validar longitud del nombre
        if (nombre.trim().length() > 100) {
            mostrarMensaje("El nombre no puede exceder los 100 caracteres", "error");
            return false;
        }
        
        // Validar teléfono si se proporciona
        if (telefono != null && !telefono.trim().isEmpty()) {
            if (telefono.length() > 20) {
                mostrarMensaje("El teléfono no puede exceder los 20 caracteres", "error");
                return false;
            }
            
            // Validar formato básico de teléfono
            String phoneRegex = "^[\\d\\s\\-\\(\\)\\+]+$";
            if (!Pattern.matches(phoneRegex, telefono.trim())) {
                mostrarMensaje("El formato del teléfono no es válido", "error");
                return false;
            }
        }
        
        // Validar descripción si se proporciona
        if (descripcion != null && descripcion.length() > 500) {
            mostrarMensaje("La descripción no puede exceder los 500 caracteres", "error");
            return false;
        }
        
        return true;
    }
    
    /**
     * Validar datos del formulario de login
     */
    private boolean validarDatosLogin() {
        if (loginEmail == null || loginEmail.trim().isEmpty()) {
            mostrarMensaje("El email es obligatorio", "error");
            return false;
        }
        
        if (loginPassword == null || loginPassword.trim().isEmpty()) {
            mostrarMensaje("La contraseña es obligatoria", "error");
            return false;
        }
        
        // Validar formato de email
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!Pattern.matches(emailRegex, loginEmail.trim())) {
            mostrarMensaje("El formato del email no es válido", "error");
            return false;
        }
        
        return true;
    }
    
    /**
     * Limpiar formulario de registro (actualizado sin rolId)
     */
    public void limpiarFormulario() {
        this.email = "";
        this.password = "";
        this.confirmarPassword = "";
        this.nombre = "";
        this.descripcion = "";
        this.telefono = "";
        // Removido: this.rolId = null; - Ya no se necesita
        this.mensaje = "";
        this.tipoMensaje = "";
    }
    
    /**
     * Limpiar formulario de login
     */
    public void limpiarFormularioLogin() {
        this.loginEmail = "";
        this.loginPassword = "";
        this.recordarSesion = false;
    }
    
    /**
     * Mostrar mensaje usando FacesMessage
     */
    private void mostrarMensaje(String mensaje, String tipo) {
        this.mensaje = mensaje;
        this.tipoMensaje = tipo;
        
        FacesMessage.Severity severity = FacesMessage.SEVERITY_INFO;
        
        switch (tipo) {
            case "success":
                severity = FacesMessage.SEVERITY_INFO;
                break;
            case "error":
                severity = FacesMessage.SEVERITY_ERROR;
                break;
            case "warning":
                severity = FacesMessage.SEVERITY_WARN;
                break;
            default:
                severity = FacesMessage.SEVERITY_INFO;
                break;
        }
        
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(severity, mensaje, null));
    }
    
    /**
     * Verificar si el usuario está logueado
     */
    public boolean isUsuarioLogueado() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        return session != null && session.getAttribute("usuario") != null;
    }
    
    /**
     * Obtener usuario de la sesión
     */
    public Usuario getUsuarioSesion() {
        if (isUsuarioLogueado()) {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
            return (Usuario) session.getAttribute("usuario");
        }
        return null;
    }
    
    // Getters y Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getConfirmarPassword() { return confirmarPassword; }
    public void setConfirmarPassword(String confirmarPassword) { this.confirmarPassword = confirmarPassword; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    // Removido: getRolId() y setRolId() - Ya no se necesitan
    
    public String getLoginEmail() { return loginEmail; }
    public void setLoginEmail(String loginEmail) { this.loginEmail = loginEmail; }
    
    public String getLoginPassword() { return loginPassword; }
    public void setLoginPassword(String loginPassword) { this.loginPassword = loginPassword; }
    
    public boolean isRecordarSesion() { return recordarSesion; }
    public void setRecordarSesion(boolean recordarSesion) { this.recordarSesion = recordarSesion; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public List<Rol> getRoles() { 
        if (roles == null) {
            cargarRoles();
        }
        return roles; 
    }
    public void setRoles(List<Rol> roles) { this.roles = roles; }
    
    public List<Usuario> getUsuarios() { 
        if (usuarios == null) {
            cargarUsuarios();
        }
        return usuarios; 
    }
    public void setUsuarios(List<Usuario> usuarios) { this.usuarios = usuarios; }
    
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    
    public String getTipoMensaje() { return tipoMensaje; }
    public void setTipoMensaje(String tipoMensaje) { this.tipoMensaje = tipoMensaje; }
}