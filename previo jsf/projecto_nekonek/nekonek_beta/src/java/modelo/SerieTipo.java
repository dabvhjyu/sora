/*
 * Modelo SerieTipo - representa la relación muchos a muchos entre Series y Tipos
 */
package modelo;

import java.io.Serializable;

public class SerieTipo implements Serializable {
    private Integer stId;
    private Integer sId;
    private Integer tId;
    
    // Constructores
    public SerieTipo() {}
    
    public SerieTipo(Integer sId, Integer tId) {
        this.sId = sId;
        this.tId = tId;
    }
    
    public SerieTipo(Integer stId, Integer sId, Integer tId) {
        this.stId = stId;
        this.sId = sId;
        this.tId = tId;
    }
    
    // Getters y Setters
    public Integer getStId() { return stId; }
    public void setStId(Integer stId) { this.stId = stId; }
    
    public Integer getSId() { return sId; }
    public void setSId(Integer sId) { this.sId = sId; }
    
    public Integer getTId() { return tId; }
    public void setTId(Integer tId) { this.tId = tId; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SerieTipo st = (SerieTipo) obj;
        return stId != null ? stId.equals(st.stId) : st.stId == null;
    }
    
    @Override
    public int hashCode() {
        return stId != null ? stId.hashCode() : 0;
    }
}