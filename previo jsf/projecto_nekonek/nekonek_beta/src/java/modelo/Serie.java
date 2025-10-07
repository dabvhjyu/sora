/*
 * Modelo Serie - representa la entidad series en la base de datos
 */
package modelo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class Serie implements Serializable {
    private Integer sId;
    private Integer uId;
    private String sTitulo;
    private String sDescripcion;
    private Integer sCantidadEpisodios;
    // NOTA: s_tipo se elimina ya que ahora usamos la relación muchos a muchos
    private Integer sPais;
    private Integer sEstado;
    private String sImagenc; // Imagen cuadrada
    private String sImagenv; // Imagen vertical
    private Timestamp fechaCreacion;
    
    // Objetos relacionados (para mostrar información)
    private Usuario usuario;
    private List<Tipo> tipos; // CAMBIADO: Lista de tipos en lugar de un solo tipo
    private Pais pais;
    private Estado estado;
    private List<Genero> generos; // Lista de géneros de la serie
    private String generosTexto;  // Cadena con géneros concatenados (para mostrar en tabla)
    private String tiposTexto;    // NUEVO: Cadena con tipos concatenados (para mostrar en tabla)
    
    // Constructores
    public Serie() {
        this.fechaCreacion = new Timestamp(System.currentTimeMillis());
        this.sCantidadEpisodios = 0;
    }
    
    public Serie(String sTitulo, Integer uId, Integer sPais, Integer sEstado) {
        this();
        this.sTitulo = sTitulo;
        this.uId = uId;
        this.sPais = sPais;
        this.sEstado = sEstado;
    }
    
    // Getters y Setters
    public Integer getSId() { return sId; }
    public void setSId(Integer sId) { this.sId = sId; }
    
    public Integer getUId() { return uId; }
    public void setUId(Integer uId) { this.uId = uId; }
    
    public String getSTitulo() { return sTitulo; }
    public void setSTitulo(String sTitulo) { this.sTitulo = sTitulo; }
    
    public String getSDescripcion() { return sDescripcion; }
    public void setSDescripcion(String sDescripcion) { this.sDescripcion = sDescripcion; }
    
    public Integer getSCantidadEpisodios() { return sCantidadEpisodios; }
    public void setSCantidadEpisodios(Integer sCantidadEpisodios) { this.sCantidadEpisodios = sCantidadEpisodios; }
    
    // ELIMINADO: getSTipo() y setSTipo() ya que ahora usamos lista de tipos
    
    public Integer getSPais() { return sPais; }
    public void setSPais(Integer sPais) { this.sPais = sPais; }
    
    public Integer getSEstado() { return sEstado; }
    public void setSEstado(Integer sEstado) { this.sEstado = sEstado; }
    
    public String getSImagenc() { return sImagenc; }
    public void setSImagenc(String sImagenc) { this.sImagenc = sImagenc; }
    
    public String getSImagenv() { return sImagenv; }
    public void setSImagenv(String sImagenv) { this.sImagenv = sImagenv; }
    
    public Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    // CAMBIADO: Ahora es lista de tipos
    public List<Tipo> getTipos() { return tipos; }
    public void setTipos(List<Tipo> tipos) { this.tipos = tipos; }
    
    public Pais getPais() { return pais; }
    public void setPais(Pais pais) { this.pais = pais; }
    
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    
    public List<Genero> getGeneros() { return generos; }
    public void setGeneros(List<Genero> generos) { this.generos = generos; }
    
    public String getGenerosTexto() { return generosTexto; }
    public void setGenerosTexto(String generosTexto) { this.generosTexto = generosTexto; }
    
    // NUEVO: Getters y setters para tipos como texto
    public String getTiposTexto() { return tiposTexto; }
    public void setTiposTexto(String tiposTexto) { this.tiposTexto = tiposTexto; }
    
    // Métodos utilitarios
    public String getNombreUsuario() {
        return usuario != null ? usuario.getUNombre() : "Usuario desconocido";
    }
    
    // CAMBIADO: Ahora devuelve los nombres de todos los tipos
    public String getNombreTipo() {
        if (tipos != null && !tipos.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < tipos.size(); i++) {
                sb.append(tipos.get(i).getTNombre());
                if (i < tipos.size() - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }
        return "Tipos desconocidos";
    }
    
    public String getNombrePais() {
        return pais != null ? pais.getPNombre() : "País desconocido";
    }
    
    public String getNombreEstado() {
        return estado != null ? estado.getENombre() : "Estado desconocido";
    }
    
    @Override
    public String toString() {
        return sTitulo + " (" + getNombreTipo() + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Serie serie = (Serie) obj;
        return sId != null ? sId.equals(serie.sId) : serie.sId == null;
    }
    
    @Override
    public int hashCode() {
        return sId != null ? sId.hashCode() : 0;
    }
}