package modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@Entity
@Table(name = "contenidos")
@ManagedBean(name = "contenido")
@SessionScoped
public class Contenido implements Serializable {
    
    private static final long serialVersionUID = 1L;

    // ================== CAMPOS BÁSICOS ==================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_id")
    private Integer cId;
    
    @NotNull(message = "El ID de serie es requerido")
    @Column(name = "s_id", nullable = false)
    private Integer sId;
    
    @NotNull(message = "El ID de usuario es requerido")
    @Column(name = "u_id", nullable = false)
    private Integer uId;
    
    @NotNull(message = "El ID de tipo es requerido")
    @Column(name = "t_id", nullable = false)
    private Integer tId;   // 1 = Anime, 2 = Manga, 3 = Novela
    
    @NotNull(message = "El título es requerido")
    @Size(max = 255, message = "El título no puede exceder 255 caracteres")
    @Column(name = "c_titulo", nullable = false, length = 255)
    private String cTitulo;
    
    @Column(name = "c_descripcion", columnDefinition = "TEXT")
    private String cDescripcion;
    
    @NotNull(message = "El estado de completado es requerido")
    @Min(value = 0, message = "El valor debe ser 0 o 1")
    @Max(value = 1, message = "El valor debe ser 0 o 1")
    @Column(name = "c_completo", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Integer cCompleto;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_subida", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date fechaSubida;

    // ================== CAMPOS DE ARCHIVOS ==================
    // Usamos rutas relativas para mostrar en la web
    @Column(name = "archivo", length = 255)
    private String archivo;   // Video (anime), PDF (novela), etc.

    @Column(name = "miniatura", length = 255)
    private String miniatura; // Solo anime

    @Column(name = "portada", length = 255)
    private String portada;   // Manga y novela

    @Column(name = "imagenes", columnDefinition = "TEXT")
    private String imagenes;  // Lista de imágenes de manga, separadas por ";"

    // ================== RELACIONES ==================
    @OneToOne(mappedBy = "contenido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Anime anime;

    @OneToOne(mappedBy = "contenido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Manga manga;

    @OneToOne(mappedBy = "contenido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Novela novela;

    // ================== CONSTRUCTORES ==================
    public Contenido() {
        this.fechaSubida = new Date();
        this.cCompleto = 0;
    }
    
    public Contenido(Integer sId, Integer uId, Integer tId, String cTitulo, String cDescripcion, Integer cCompleto) {
        this();
        this.sId = sId;
        this.uId = uId;
        this.tId = tId;
        this.cTitulo = cTitulo;
        this.cDescripcion = cDescripcion;
        this.cCompleto = cCompleto;
    }

    // ================== GETTERS & SETTERS ==================
    public Integer getCId() { return cId; }
    public void setCId(Integer cId) { this.cId = cId; }

    public Integer getSId() { return sId; }
    public void setSId(Integer sId) { this.sId = sId; }

    public Integer getUId() { return uId; }
    public void setUId(Integer uId) { this.uId = uId; }

    public Integer getTId() { return tId; }
    public void setTId(Integer tId) { this.tId = tId; }

    public String getCTitulo() { return cTitulo; }
    public void setCTitulo(String cTitulo) { this.cTitulo = cTitulo; }

    public String getCDescripcion() { return cDescripcion; }
    public void setCDescripcion(String cDescripcion) { this.cDescripcion = cDescripcion; }

    public Integer getCCompleto() { return cCompleto; }
    public void setCCompleto(Integer cCompleto) { this.cCompleto = cCompleto; }

    public Date getFechaSubida() { return fechaSubida; }
    public void setFechaSubida(Date fechaSubida) { this.fechaSubida = fechaSubida; }

    public String getArchivo() { return archivo; }
    public void setArchivo(String archivo) { this.archivo = archivo; }

    public String getMiniatura() { return miniatura; }
    public void setMiniatura(String miniatura) { this.miniatura = miniatura; }

    public String getPortada() { return portada; }
    public void setPortada(String portada) { this.portada = portada; }

    public String getImagenes() { return imagenes; }
    public void setImagenes(String imagenes) { this.imagenes = imagenes; }

    public Anime getAnime() { return anime; }
    public void setAnime(Anime anime) { this.anime = anime; }

    public Manga getManga() { return manga; }
    public void setManga(Manga manga) { this.manga = manga; }

    public Novela getNovela() { return novela; }
    public void setNovela(Novela novela) { this.novela = novela; }

    // ================== MÉTODOS DE UTILIDAD ==================
    public boolean isCompleto() {
        return this.cCompleto != null && this.cCompleto == 1;
    }

    public void setCompleto(boolean completo) {
        this.cCompleto = completo ? 1 : 0;
    }

    public String getEstadoTexto() {
        return isCompleto() ? "Completo" : "En proceso";
    }

    // Para manga: devolver lista de imágenes separadas
    public String[] getListaImagenes() {
        return (imagenes != null && !imagenes.isEmpty()) ? imagenes.split(";") : new String[0];
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Contenido other = (Contenido) obj;
        return cId != null && cId.equals(other.cId);
    }

    @Override
    public int hashCode() {
        return cId != null ? cId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Contenido{" +
                "cId=" + cId +
                ", cTitulo='" + cTitulo + '\'' +
                ", cCompleto=" + cCompleto +
                ", archivo='" + archivo + '\'' +
                ", portada='" + portada + '\'' +
                ", miniatura='" + miniatura + '\'' +
                '}';
    }
}
