/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;

public class Tipo implements Serializable {
    private Integer tId;
    private String tNombre;
    
    // Constructores
    public Tipo() {}
    
    public Tipo(Integer tId, String tNombre) {
        this.tId = tId;
        this.tNombre = tNombre;
    }
    
    public Tipo(String tNombre) {
        this.tNombre = tNombre;
    }
    
    // Getters y Setters
    public Integer getTId() { return tId; }
    public void setTId(Integer tId) { this.tId = tId; }
    
    public String getTNombre() { return tNombre; }
    public void setTNombre(String tNombre) { this.tNombre = tNombre; }
    
    @Override
    public String toString() {
        return tNombre;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tipo tipo = (Tipo) obj;
        return tId != null ? tId.equals(tipo.tId) : tipo.tId == null;
    }
    
    @Override
    public int hashCode() {
        return tId != null ? tId.hashCode() : 0;
    }
}