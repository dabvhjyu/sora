/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;

public class SerieGenero implements Serializable {
    private Integer sgId;
    private Integer sId;
    private Integer gId;
    
    // Constructores
    public SerieGenero() {}
    
    public SerieGenero(Integer sId, Integer gId) {
        this.sId = sId;
        this.gId = gId;
    }
    
    public SerieGenero(Integer sgId, Integer sId, Integer gId) {
        this.sgId = sgId;
        this.sId = sId;
        this.gId = gId;
    }
    
    // Getters y Setters
    public Integer getSgId() { return sgId; }
    public void setSgId(Integer sgId) { this.sgId = sgId; }
    
    public Integer getSId() { return sId; }
    public void setSId(Integer sId) { this.sId = sId; }
    
    public Integer getGId() { return gId; }
    public void setGId(Integer gId) { this.gId = gId; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SerieGenero sg = (SerieGenero) obj;
        return sgId != null ? sgId.equals(sg.sgId) : sg.sgId == null;
    }
    
    @Override
    public int hashCode() {
        return sgId != null ? sgId.hashCode() : 0;
    }
}