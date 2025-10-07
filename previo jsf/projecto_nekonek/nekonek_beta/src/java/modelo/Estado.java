/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;

public class Estado implements Serializable {
    private Integer eId;
    private String eNombre;
    
    // Constructores
    public Estado() {}
    
    public Estado(Integer eId, String eNombre) {
        this.eId = eId;
        this.eNombre = eNombre;
    }
    
    public Estado(String eNombre) {
        this.eNombre = eNombre;
    }
    
    // Getters y Setters
    public Integer getEId() { return eId; }
    public void setEId(Integer eId) { this.eId = eId; }
    
    public String getENombre() { return eNombre; }
    public void setENombre(String eNombre) { this.eNombre = eNombre; }
    
    @Override
    public String toString() {
        return eNombre;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Estado estado = (Estado) obj;
        return eId != null ? eId.equals(estado.eId) : estado.eId == null;
    }
    
    @Override
    public int hashCode() {
        return eId != null ? eId.hashCode() : 0;
    }
}