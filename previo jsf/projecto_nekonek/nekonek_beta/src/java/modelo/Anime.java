package modelo;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@Entity
@Table(name = "anime")
@ManagedBean(name = "anime")
@SessionScoped
public class Anime implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "a_id")
    private Integer aId;
    
    @NotNull(message = "El ID de contenido es requerido")
    @Column(name = "c_id", nullable = false)
    private Integer cId;
    
    @Min(value = 0, message = "El número de episodios debe ser positivo")
    @Column(name = "a_episodios")
    private Integer aEpisodios;
    
    @Min(value = 0, message = "El número de temporadas debe ser positivo")
    @Column(name = "a_temporadas")
    private Integer aTemporadas;
    
    @Size(max = 255, message = "La URL del video no puede exceder 255 caracteres")
    @Column(name = "a_video", length = 255)
    private String aVideo;

    @Size(max = 255, message = "La URL de la miniatura no puede exceder 255 caracteres")
    @Column(name = "a_miniatura", length = 255)
    private String aMiniatura;
    
    @Column(name = "a_estado")
    private Integer aEstado;
    
    // Relación con Contenido
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "c_id", referencedColumnName = "c_id", insertable = false, updatable = false)
    private Contenido contenido;
    
    // Constructores
    public Anime() {}
    
    public Anime(Integer cId, Integer aEpisodios, Integer aTemporadas, 
                String aVideo, String aMiniatura, Integer aEstado) {
        this.cId = cId;
        this.aEpisodios = aEpisodios;
        this.aTemporadas = aTemporadas;
        this.aVideo = aVideo;
        this.aMiniatura = aMiniatura;
        this.aEstado = aEstado;
    }
    
    // Getters y Setters
    public Integer getAId() {
        return aId;
    }
    
    public void setAId(Integer aId) {
        this.aId = aId;
    }
    
    public Integer getCId() {
        return cId;
    }
    
    public void setCId(Integer cId) {
        this.cId = cId;
    }
    
    public Integer getAEpisodios() {
        return aEpisodios;
    }
    
    public void setAEpisodios(Integer aEpisodios) {
        this.aEpisodios = aEpisodios;
    }
    
    public Integer getATemporadas() {
        return aTemporadas;
    }
    
    public void setATemporadas(Integer aTemporadas) {
        this.aTemporadas = aTemporadas;
    }
    
    public String getAVideo() {
        return aVideo;
    }
    
    public void setAVideo(String aVideo) {
        this.aVideo = aVideo;
    }

    public String getAMiniatura() {
        return aMiniatura;
    }

    public void setAMiniatura(String aMiniatura) {
        this.aMiniatura = aMiniatura;
    }
    
    public Integer getAEstado() {
        return aEstado;
    }
    
    public void setAEstado(Integer aEstado) {
        this.aEstado = aEstado;
    }
    
    public Contenido getContenido() {
        return contenido;
    }
    
    public void setContenido(Contenido contenido) {
        this.contenido = contenido;
    }
    
    // Métodos de utilidad
    public boolean tieneVideo() {
        return aVideo != null && !aVideo.trim().isEmpty();
    }

    public boolean tieneMiniatura() {
        return aMiniatura != null && !aMiniatura.trim().isEmpty();
    }
    
    public String getInformacionCompleta() {
        StringBuilder info = new StringBuilder();
        if (aEpisodios != null && aEpisodios > 0) {
            info.append(aEpisodios).append(" episodios");
        }
        if (aTemporadas != null && aTemporadas > 0) {
            if (info.length() > 0) info.append(", ");
            info.append(aTemporadas).append(" temporadas");
        }
        return info.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Anime anime = (Anime) obj;
        return aId != null ? aId.equals(anime.aId) : anime.aId == null;
    }
    
    @Override
    public int hashCode() {
        return aId != null ? aId.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "Anime{" +
                "aId=" + aId +
                ", cId=" + cId +
                ", aEpisodios=" + aEpisodios +
                ", aTemporadas=" + aTemporadas +
                ", aVideo='" + aVideo + '\'' +
                ", aMiniatura='" + aMiniatura + '\'' +
                ", aEstado=" + aEstado +
                '}';
    }
}
