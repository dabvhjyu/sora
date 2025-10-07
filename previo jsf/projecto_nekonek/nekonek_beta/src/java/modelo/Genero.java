/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;

public class Genero implements Serializable {
    private Integer gId;
    private String gNombre;
    
    // Constructores
    public Genero() {}
    
    public Genero(Integer gId, String gNombre) {
        this.gId = gId;
        this.gNombre = gNombre;
    }
    
    public Genero(String gNombre) {
        this.gNombre = gNombre;
    }
    
    // Getters y Setters
    public Integer getGId() { return gId; }
    public void setGId(Integer gId) { this.gId = gId; }
    
    public String getGNombre() { return gNombre; }
    public void setGNombre(String gNombre) { this.gNombre = gNombre; }
    
    @Override
    public String toString() {
        return gNombre;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Genero genero = (Genero) obj;
        return gId != null ? gId.equals(genero.gId) : genero.gId == null;
    }
    
    @Override
    public int hashCode() {
        return gId != null ? gId.hashCode() : 0;
    }
}