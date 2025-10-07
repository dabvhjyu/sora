package modelo;

import java.io.Serializable;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import modelo.Contenido;

@Entity
@Table(name = "manga")
@ManagedBean(name = "manga")
@SessionScoped
public class Manga implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "m_id")
    private Integer mId;
    
    @NotNull(message = "El ID de contenido es requerido")
    @Column(name = "c_id", nullable = false)
    private Integer cId;
    
    @Min(value = 0, message = "El número de capítulos debe ser positivo")
    @Column(name = "m_capitulos")
    private Integer mCapitulos;
    
    @Column(name = "m_imagenes", columnDefinition = "TEXT")
    private String mImagenes;
    
    @Min(value = 0, message = "El número de volúmenes debe ser positivo")
    @Column(name = "m_volumenes")
    private Integer mVolumenes;
    
    @Column(name = "m_estado")
    private Integer mEstado;

    @Size(max = 255, message = "La URL de la portada no puede exceder 255 caracteres")
    @Column(name = "m_portada", length = 255)
    private String mPortada;
    
    // Relación con Contenido
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "c_id", referencedColumnName = "c_id", insertable = false, updatable = false)
    private Contenido contenido;
    
    // Constructores
    public Manga() {}
    
    public Manga(Integer cId, Integer mCapitulos, String mImagenes, 
                 Integer mVolumenes, Integer mEstado, String mPortada) {
        this.cId = cId;
        this.mCapitulos = mCapitulos;
        this.mImagenes = mImagenes;
        this.mVolumenes = mVolumenes;
        this.mEstado = mEstado;
        this.mPortada = mPortada;
    }
    
    // Getters y Setters
    public Integer getMId() {
        return mId;
    }
    
    public void setMId(Integer mId) {
        this.mId = mId;
    }
    
    public Integer getCId() {
        return cId;
    }
    
    public void setCId(Integer cId) {
        this.cId = cId;
    }
    
    public Integer getMCapitulos() {
        return mCapitulos;
    }
    
    public void setMCapitulos(Integer mCapitulos) {
        this.mCapitulos = mCapitulos;
    }
    
    public String getMImagenes() {
        return mImagenes;
    }
    
    public void setMImagenes(String mImagenes) {
        this.mImagenes = mImagenes;
    }
    
    public Integer getMVolumenes() {
        return mVolumenes;
    }
    
    public void setMVolumenes(Integer mVolumenes) {
        this.mVolumenes = mVolumenes;
    }
    
    public Integer getMEstado() {
        return mEstado;
    }
    
    public void setMEstado(Integer mEstado) {
        this.mEstado = mEstado;
    }

    public String getMPortada() {
        return mPortada;
    }

    public void setMPortada(String mPortada) {
        this.mPortada = mPortada;
    }
    
    public Contenido getContenido() {
        return contenido;
    }
    
    public void setContenido(Contenido contenido) {
        this.contenido = contenido;
    }
    
    // Métodos de utilidad para manejar imágenes
    public List<String> getListaImagenes() {
        if (mImagenes != null && !mImagenes.trim().isEmpty()) {
            return Arrays.asList(mImagenes.split(","));
        }
        return new ArrayList<>();
    }
    
    public void setListaImagenes(List<String> imagenes) {
        if (imagenes != null && !imagenes.isEmpty()) {
            this.mImagenes = String.join(",", imagenes);
        } else {
            this.mImagenes = null;
        }
    }
    
    public void agregarImagen(String urlImagen) {
        if (urlImagen != null && !urlImagen.trim().isEmpty()) {
            List<String> imagenes = getListaImagenes();
            if (!imagenes.contains(urlImagen)) {
                imagenes.add(urlImagen);
                setListaImagenes(imagenes);
            }
        }
    }
    
    public void eliminarImagen(String urlImagen) {
        List<String> imagenes = getListaImagenes();
        imagenes.remove(urlImagen);
        setListaImagenes(imagenes);
    }
    
    public boolean tieneImagenes() {
        return mImagenes != null && !mImagenes.trim().isEmpty();
    }
    
    public int getCantidadImagenes() {
        return getListaImagenes().size();
    }

    public boolean tienePortada() {
        return mPortada != null && !mPortada.trim().isEmpty();
    }
    
    public String getInformacionCompleta() {
        StringBuilder info = new StringBuilder();
        if (mCapitulos != null && mCapitulos > 0) {
            info.append(mCapitulos).append(" capítulos");
        }
        if (mVolumenes != null && mVolumenes > 0) {
            if (info.length() > 0) info.append(", ");
            info.append(mVolumenes).append(" volúmenes");
        }
        return info.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Manga manga = (Manga) obj;
        return mId != null ? mId.equals(manga.mId) : manga.mId == null;
    }
    
    @Override
    public int hashCode() {
        return mId != null ? mId.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "Manga{" +
                "mId=" + mId +
                ", cId=" + cId +
                ", mCapitulos=" + mCapitulos +
                ", mVolumenes=" + mVolumenes +
                ", mPortada='" + mPortada + '\'' +
                ", mEstado=" + mEstado +
                '}';
    }
}
