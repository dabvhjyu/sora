package modelo;
import java.io.Serializable;

public class Rol implements Serializable {
    private Integer rId;
    private String rNombre;
    
    // Constructores
    public Rol() {}
    
    public Rol(Integer rId, String rNombre) {
        this.rId = rId;
        this.rNombre = rNombre;
    }
    
    public Rol(String rNombre) {
        this.rNombre = rNombre;
    }
    
    // Getters y Setters
    public Integer getRId() { 
        return rId; 
    }
    
    public void setRId(Integer rId) { 
        this.rId = rId; 
    }
    
    public String getRNombre() { 
        return rNombre; 
    }
    
    public void setRNombre(String rNombre) { 
        this.rNombre = rNombre; 
    }
    
    @Override
    public String toString() {
        return rNombre;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Rol rol = (Rol) obj;
        return rId != null ? rId.equals(rol.rId) : rol.rId == null;
    }
    
    @Override
    public int hashCode() {
        return rId != null ? rId.hashCode() : 0;
    }
}