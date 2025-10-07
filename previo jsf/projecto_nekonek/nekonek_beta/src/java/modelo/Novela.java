package modelo;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@Entity
@Table(name = "novela")
@ManagedBean(name = "novela")
@SessionScoped
public class Novela implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_id")
    private Integer nId;
    
    @NotNull(message = "El ID de contenido es requerido")
    @Column(name = "c_id", nullable = false)
    private Integer cId;
    
    @Min(value = 0, message = "El número de volúmenes debe ser positivo")
    @Column(name = "n_volumenes")
    private Integer nVolumenes;
    
    @Size(max = 255, message = "La URL del PDF no puede exceder 255 caracteres")
    @Column(name = "n_pdf", length = 255)
    private String nPdf;
    
    @Size(max = 255, message = "La URL de la portada no puede exceder 255 caracteres")
    @Column(name = "n_portada", length = 255)
    private String nPortada;
    
    @Column(name = "n_estado")
    private Integer nEstado;
    
    // Relación con Contenido
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "c_id", referencedColumnName = "c_id", insertable = false, updatable = false)
    private Contenido contenido;
    
    // Constructores
    public Novela() {}
    
    public Novela(Integer cId, Integer nVolumenes, String nPdf, String nPortada, Integer nEstado) {
        this.cId = cId;
        this.nVolumenes = nVolumenes;
        this.nPdf = nPdf;
        this.nPortada = nPortada;
        this.nEstado = nEstado;
    }
    
    // Getters y Setters
    public Integer getNId() {
        return nId;
    }
    
    public void setNId(Integer nId) {
        this.nId = nId;
    }
    
    public Integer getCId() {
        return cId;
    }
    
    public void setCId(Integer cId) {
        this.cId = cId;
    }
    
    public Integer getNVolumenes() {
        return nVolumenes;
    }
    
    public void setNVolumenes(Integer nVolumenes) {
        this.nVolumenes = nVolumenes;
    }
    
    public String getNPdf() {
        return nPdf;
    }
    
    public void setNPdf(String nPdf) {
        this.nPdf = nPdf;
    }
    
    public String getNPortada() {
        return nPortada;
    }
    
    public void setNPortada(String nPortada) {
        this.nPortada = nPortada;
    }
    
    public Integer getNEstado() {
        return nEstado;
    }
    
    public void setNEstado(Integer nEstado) {
        this.nEstado = nEstado;
    }
    
    public Contenido getContenido() {
        return contenido;
    }
    
    public void setContenido(Contenido contenido) {
        this.contenido = contenido;
    }
    
    // Métodos de utilidad
    public boolean tienePdf() {
        return nPdf != null && !nPdf.trim().isEmpty();
    }
    
    public boolean tienePortada() {
        return nPortada != null && !nPortada.trim().isEmpty();
    }
    
    public String getNombreArchivoPdf() {
        if (tienePdf()) {
            String[] partes = nPdf.split("/");
            return partes[partes.length - 1];
        }
        return "Sin archivo";
    }
    
    public String getExtensionArchivo() {
        if (tienePdf()) {
            int ultimoPunto = nPdf.lastIndexOf('.');
            if (ultimoPunto != -1 && ultimoPunto < nPdf.length() - 1) {
                return nPdf.substring(ultimoPunto + 1).toLowerCase();
            }
        }
        return "";
    }
    
    public boolean esPdf() {
        return "pdf".equals(getExtensionArchivo());
    }
    
    public String getInformacionCompleta() {
        StringBuilder info = new StringBuilder();
        if (nVolumenes != null && nVolumenes > 0) {
            info.append(nVolumenes).append(" volúmenes");
        }
        if (tienePdf()) {
            if (info.length() > 0) info.append(", ");
            info.append("PDF disponible");
        }
        if (tienePortada()) {
            if (info.length() > 0) info.append(", ");
            info.append("Portada disponible");
        }
        return info.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Novela novela = (Novela) obj;
        return nId != null ? nId.equals(novela.nId) : novela.nId == null;
    }
    
    @Override
    public int hashCode() {
        return nId != null ? nId.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "Novela{" +
                "nId=" + nId +
                ", cId=" + cId +
                ", nVolumenes=" + nVolumenes +
                ", nPdf='" + nPdf + '\'' +
                ", nPortada='" + nPortada + '\'' +
                ", nEstado=" + nEstado +
                '}';
    }
}

