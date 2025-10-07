/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.sql.Timestamp;

public class Usuario implements Serializable {
    private Integer uId;
    private String uEmail;
    private String uPassword;
    private String uNombre;
    private String uDescripcion;
    private String uTelefono;
    private Integer rolU;
    private Timestamp fechaRegistro;
    
    // Objetos relacionados (para mostrar información)
    private Rol rol; // Para mostrar el nombre del rol
    
    // Constructores
    public Usuario() {
        this.fechaRegistro = new Timestamp(System.currentTimeMillis());
    }
    
    public Usuario(String uEmail, String uPassword, String uNombre, Integer rolU) {
        this();
        this.uEmail = uEmail;
        this.uPassword = uPassword;
        this.uNombre = uNombre;
        this.rolU = rolU;
    }
    
    // Getters y Setters
    public Integer getUId() { return uId; }
    public void setUId(Integer uId) { this.uId = uId; }
    
    public String getUEmail() { return uEmail; }
    public void setUEmail(String uEmail) { this.uEmail = uEmail; }
    
    public String getUPassword() { return uPassword; }
    public void setUPassword(String uPassword) { this.uPassword = uPassword; }
    
    public String getUNombre() { return uNombre; }
    public void setUNombre(String uNombre) { this.uNombre = uNombre; }
    
    public String getUDescripcion() { return uDescripcion; }
    public void setUDescripcion(String uDescripcion) { this.uDescripcion = uDescripcion; }
    
    public String getUTelefono() { return uTelefono; }
    public void setUTelefono(String uTelefono) { this.uTelefono = uTelefono; }
    
    public Integer getRolU() { return rolU; }
    public void setRolU(Integer rolU) { this.rolU = rolU; }
    
    public Timestamp getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Timestamp fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
    
    // Métodos utilitarios
    public boolean esAdministrador() {
        return rol != null && "Administrador".equals(rol.getRNombre());
    }
    
    public String getNombreRol() {
        return rol != null ? rol.getRNombre() : "Sin rol";
    }
    
    @Override
    public String toString() {
        return uNombre + " (" + uEmail + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario usuario = (Usuario) obj;
        return uId != null ? uId.equals(usuario.uId) : usuario.uId == null;
    }
    
    @Override
    public int hashCode() {
        return uId != null ? uId.hashCode() : 0;
    }
}