/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;

public class Pais implements Serializable {
    private Integer pId;
    private String pNombre;
    
    // Constructores
    public Pais() {}
    
    public Pais(Integer pId, String pNombre) {
        this.pId = pId;
        this.pNombre = pNombre;
    }
    
    public Pais(String pNombre) {
        this.pNombre = pNombre;
    }
    
    // Getters y Setters
    public Integer getPId() { return pId; }
    public void setPId(Integer pId) { this.pId = pId; }
    
    public String getPNombre() { return pNombre; }
    public void setPNombre(String pNombre) { this.pNombre = pNombre; }
    
    @Override
    public String toString() {
        return pNombre;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pais pais = (Pais) obj;
        return pId != null ? pId.equals(pais.pId) : pais.pId == null;
    }
    
    @Override
    public int hashCode() {
        return pId != null ? pId.hashCode() : 0;
    }
}