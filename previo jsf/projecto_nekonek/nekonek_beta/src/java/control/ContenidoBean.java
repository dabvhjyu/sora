package control;

import dao.*;
import modelo.*;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.model.file.UploadedFile;

import java.io.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "contenidoBean")
@SessionScoped
public class ContenidoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    // =====================
    // DAOs
    // =====================
    private transient ContenidoDAO contenidoDAO = new ContenidoDAO();
    private transient AnimeDAO animeDAO = new AnimeDAO();
    private transient MangaDAO mangaDAO = new MangaDAO();
    private transient NovelaDAO novelaDAO = new NovelaDAO();
    private transient EstadoDAO estadoDAO = new EstadoDAO();

    // =====================
    // Variables
    // =====================
    private List<Contenido> contenidos = new ArrayList<>();
    private Contenido contenido = new Contenido();

    private Serie serieSeleccionada;
    private List<Estado> estados = new ArrayList<>();

    // Archivos subidos
    private UploadedFile archivo;
    private List<UploadedFile> imagenes;
    private UploadedFile portada;
    private UploadedFile miniatura;

    private String tipoSeleccionado;

    // =====================
    // CRUD
    // =====================
    public void listar() {
        contenidos = contenidoDAO.listar();
    }

    public void agregar(Serie serie) {
        try {
            // Validar tipo permitido
            boolean permitido = false;
            for (Tipo tipo : serie.getTipos()) {
                if (tipo.getTId().equals(contenido.getTId())) {
                    permitido = true;
                    break;
                }
            }
            if (!permitido) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error", "El tipo de contenido no está habilitado en esta serie"));
                return;
            }

            contenido.setSId(serie.getSId());
            Usuario usuarioLogueado = (Usuario) FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap().get("usuario");
            contenido.setUId(usuarioLogueado != null ? usuarioLogueado.getUId() : 1);

            if (contenido.getCCompleto() == null) {
                contenido.setCCompleto(0);
            }

            // Primero insertamos el contenido para obtener su ID
            contenidoDAO.agregar(contenido);

            // Guardar archivos y datos según el tipo
            switch (contenido.getTId()) {
                case 1:
                    guardarAnime();
                    Anime a = new Anime();
                    a.setCId(contenido.getCId());
                    a.setAVideo(contenido.getArchivo());
                    a.setAMiniatura(contenido.getMiniatura());
                    animeDAO.agregar(a);
                    contenido.setAnime(a);
                    break;
                case 2:
                    guardarManga();
                    Manga m = new Manga();
                    m.setCId(contenido.getCId());
                    m.setMImagenes(contenido.getImagenes());
                    m.setMPortada(contenido.getPortada());
                    mangaDAO.agregar(m);
                    contenido.setManga(m);
                    break;
                case 3:
                    guardarNovela();
                    Novela n = new Novela();
                    n.setCId(contenido.getCId());
                    n.setNPdf(contenido.getArchivo());
                    n.setNPortada(contenido.getPortada());
                    novelaDAO.agregar(n);
                    contenido.setNovela(n);
                    break;
            }

            // Actualizamos contenido con rutas de archivo
            contenidoDAO.actualizar(contenido);

            // 🔑 RECARGAR CONTENIDO DESDE BD PARA QUE TENGA TODO SINCRONIZADO
            contenido = contenidoDAO.buscarPorId(contenido.getCId());
            if (contenido.getTId() == 1) {
                contenido.setAnime(animeDAO.buscarPorContenido(contenido.getCId()));
                if (contenido.getAnime() != null) {
                    contenido.setArchivo(contenido.getAnime().getAVideo());
                    contenido.setMiniatura(contenido.getAnime().getAMiniatura());
                }
            }

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Éxito", "Contenido agregado correctamente"));
            limpiar();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error", "No se pudo agregar el contenido"));
            e.printStackTrace();
        }
    }

    public void editar(Contenido c) {
        this.contenido = c;
        onTipoSeleccionado();
    }
    
    

    public String actualizar() {
        try {
            // 1️⃣ Actualiza campos básicos de Contenido (titulo, descripcion, etc.)
            contenidoDAO.actualizar(contenido);

            switch (contenido.getTId()) {
                case 1: { // ANIME
                    Anime a = animeDAO.buscarPorContenido(contenido.getCId());
                    if (a == null) {
                        a = new Anime();
                        a.setCId(contenido.getCId());
                    }

                    String carpeta = "uploads/anime";
                    String pathFisico = getRealPath(carpeta);

                    // ==== VIDEO ====
                    if (archivo != null && archivo.getSize() > 0) {
                        // 1. Elimina el archivo anterior si existe
                        if (a.getAVideo() != null) {
                            File viejo = new File(getRealPath(a.getAVideo()));
                            if (viejo.exists()) {
                                viejo.delete();
                            }
                        }

                        // 2. Guarda el nuevo
                        String nombre = "video_" + contenido.getCId() + ".mp4";
                        copiarArchivo(archivo, pathFisico + File.separator + nombre);
                        a.setAVideo(carpeta + "/" + nombre);
                    }
                    // Si no se subió, mantener el que ya está (a.getAVideo() no se toca)

                    // ==== MINIATURA ====
                    if (miniatura != null && miniatura.getSize() > 0) {
                        if (a.getAMiniatura() != null) {
                            File viejoMini = new File(getRealPath(a.getAMiniatura()));
                            if (viejoMini.exists()) {
                                viejoMini.delete();
                            }
                        }

                        String nombre = "mini_" + contenido.getCId() + ".png";
                        copiarArchivo(miniatura, pathFisico + File.separator + nombre);
                        a.setAMiniatura(carpeta + "/" + nombre);
                    }

                    // Guardar en BD
                    if (a.getAId() == null) {
                        animeDAO.agregar(a);
                    } else {
                        animeDAO.actualizar(a);
                    }

                    // Sincronizar contenido
                    contenido.setArchivo(a.getAVideo());
                    contenido.setMiniatura(a.getAMiniatura());
                    contenidoDAO.actualizar(contenido);
                    break;
                }

                case 2: { // MANGA
                    Manga m = mangaDAO.buscarPorContenido(contenido.getCId());
                    if (m == null) {
                        m = new Manga();
                        m.setCId(contenido.getCId());
                    }

                    String carpeta = "uploads/manga";
                    String pathFisico = getRealPath(carpeta);

                    if (imagenes != null && !imagenes.isEmpty()) {
                        // Eliminar imágenes anteriores si existen
                        if (m.getMImagenes() != null) {
                            for (String imgPath : m.getMImagenes().split(",")) {
                                File viejo = new File(getRealPath(imgPath));
                                if (viejo.exists()) {
                                    viejo.delete();
                                }
                            }
                        }

                        List<String> rutas = new ArrayList<>();
                        int i = 0;
                        for (UploadedFile img : imagenes) {
                            if (img != null && img.getSize() > 0) {
                                String nombre = "manga_" + contenido.getCId() + "_" + (i++) + ".png";
                                copiarArchivo(img, pathFisico + File.separator + nombre);
                                rutas.add(carpeta + "/" + nombre);
                            }
                        }
                        m.setMImagenes(String.join(",", rutas));
                    }

                    if (portada != null && portada.getSize() > 0) {
                        if (m.getMPortada() != null) {
                            File viejo = new File(getRealPath(m.getMPortada()));
                            if (viejo.exists()) {
                                viejo.delete();
                            }
                        }
                        String nombre = "portada_" + contenido.getCId() + ".png";
                        copiarArchivo(portada, pathFisico + File.separator + nombre);
                        m.setMPortada(carpeta + "/" + nombre);
                    }

                    if (m.getMId() == null) {
                        mangaDAO.agregar(m);
                    } else {
                        mangaDAO.actualizar(m);
                    }

                    contenido.setImagenes(m.getMImagenes());
                    contenido.setPortada(m.getMPortada());
                    contenidoDAO.actualizar(contenido);
                    break;
                }

                case 3: { // NOVELA
                    Novela n = novelaDAO.buscarPorContenido(contenido.getCId());
                    if (n == null) {
                        n = new Novela();
                        n.setCId(contenido.getCId());
                    }

                    String carpeta = "uploads/novelas";
                    String pathFisico = getRealPath(carpeta);

                    if (archivo != null && archivo.getSize() > 0) {
                        if (n.getNPdf() != null) {
                            File viejo = new File(getRealPath(n.getNPdf()));
                            if (viejo.exists()) {
                                viejo.delete();
                            }
                        }
                        String nombre = "novela_" + contenido.getCId() + ".pdf";
                        copiarArchivo(archivo, pathFisico + File.separator + nombre);
                        n.setNPdf(carpeta + "/" + nombre);
                    }

                    if (portada != null && portada.getSize() > 0) {
                        if (n.getNPortada() != null) {
                            File viejo = new File(getRealPath(n.getNPortada()));
                            if (viejo.exists()) {
                                viejo.delete();
                            }
                        }
                        String nombre = "portada_" + contenido.getCId() + ".png";
                        copiarArchivo(portada, pathFisico + File.separator + nombre);
                        n.setNPortada(carpeta + "/" + nombre);
                    }

                    if (n.getNId() == null) {
                        novelaDAO.agregar(n);
                    } else {
                        novelaDAO.actualizar(n);
                    }

                    contenido.setArchivo(n.getNPdf());
                    contenido.setPortada(n.getNPortada());
                    contenidoDAO.actualizar(contenido);
                    break;
                }
            }

            // Recargar datos para que "ver" muestre siempre lo último
            contenido = contenidoDAO.buscarPorId(contenido.getCId());

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualizado", "Contenido actualizado"));

            return "/Usuario/ver.xhtml?faces-redirect=true";

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo actualizar el contenido"));
            e.printStackTrace();
            return null;
        }
    }

    public String ver(Contenido c) {
    this.contenido = c; // mantiene el objeto actual

    // Redirige pasando el id como parámetro
    switch (c.getTId()) {
        case 1: // Anime
            return "/anime.xhtml?faces-redirect=true&amp;id=" + c.getCId();
        case 2: // Manga
            return "/manga.xhtml?faces-redirect=true&amp;id=" + c.getCId();
        case 3: // Novela
            return "/novela.xhtml?faces-redirect=true&amp;id=" + c.getCId();
        default:
            return "/Usuario/ver.xhtml?faces-redirect=true&amp;id=" + c.getCId();
    }
}


    public void eliminar(Contenido c) {
        contenidoDAO.eliminar(c);
        contenidos.remove(c);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Eliminado", "Contenido eliminado"));
    }

    // =====================
    // Guardar / Actualizar por tipo
    // =====================
    private void guardarAnime() throws IOException {
        String carpeta = "uploads/anime";
        String pathFisico = getRealPath(carpeta);

        if (archivo != null) {
            String nombre = "video_" + contenido.getCId() + ".mp4";
            copiarArchivo(archivo, pathFisico + File.separator + nombre);
            contenido.setArchivo(carpeta + "/" + nombre);
        }

        if (miniatura != null) {
            String nombre = "mini_" + contenido.getCId() + ".png";
            copiarArchivo(miniatura, pathFisico + File.separator + nombre);
            contenido.setMiniatura(carpeta + "/" + nombre);
        }
    }

    private void actualizarAnime() throws IOException {
        guardarAnime();
    }

    private void guardarManga() throws IOException {
        String carpeta = "uploads/manga";
        String pathFisico = getRealPath(carpeta);

        List<String> rutas = new ArrayList<>();
        if (imagenes != null) {
            int i = 0;
            for (UploadedFile img : imagenes) {
                String nombre = "manga_" + contenido.getCId() + "_" + (i++) + ".png";
                copiarArchivo(img, pathFisico + File.separator + nombre);
                rutas.add(carpeta + "/" + nombre);
            }
            contenido.setImagenes(String.join(";", rutas));
        }

        if (portada != null) {
            String nombre = "portada_" + contenido.getCId() + ".png";
            copiarArchivo(portada, pathFisico + File.separator + nombre);
            contenido.setPortada(carpeta + "/" + nombre);
        }
    }

    private void actualizarManga() throws IOException {
        guardarManga();
    }

    private void guardarNovela() throws IOException {
        String carpeta = "uploads/novelas";
        String pathFisico = getRealPath(carpeta);

        if (archivo != null) {
            String nombre = "novela_" + contenido.getCId() + ".pdf";
            copiarArchivo(archivo, pathFisico + File.separator + nombre);
            contenido.setArchivo(carpeta + "/" + nombre);
        }

        if (portada != null) {
            String nombre = "portada_" + contenido.getCId() + ".png";
            copiarArchivo(portada, pathFisico + File.separator + nombre);
            contenido.setPortada(carpeta + "/" + nombre);
        }
    }

    private void actualizarNovela() throws IOException {
        guardarNovela();
    }

    // =====================
    // Utilidades
    // =====================
    private String getRealPath(String carpeta) {
        ServletContext sc = (ServletContext) FacesContext.getCurrentInstance()
                .getExternalContext().getContext();
        String path = sc.getRealPath("/" + carpeta);
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return path;
    }

    private void copiarArchivo(UploadedFile archivo, String destino) throws IOException {
        try (InputStream in = archivo.getInputStream(); FileOutputStream out = new FileOutputStream(new File(destino))) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        }
    }

    // =====================
    // Métodos para el XHTML
    // =====================
    public String getMiniaturaAnime(Contenido c) {
        return (c.getAnime() != null && c.getAnime().getAMiniatura() != null) ? "/" + c.getAnime().getAMiniatura() : null;
    }

    public String getPortadaManga(Contenido c) {
        return (c.getManga() != null && c.getManga().getMPortada() != null) ? "/" + c.getManga().getMPortada() : null;
    }

    public String getPortadaNovela(Contenido c) {
        return (c.getNovela() != null && c.getNovela().getNPortada() != null) ? "/" + c.getNovela().getNPortada() : null;
    }

    // =====================
    // Navegación y carga de contenidos por serie
    // =====================
    public String verContenidosPorSerie(Serie serie) {
        this.serieSeleccionada = serie;
        this.contenidos = contenidoDAO.listarPorSerie(serie.getSId());

        for (Contenido c : contenidos) {
            switch (c.getTId()) {
                case 1:
                    c.setAnime(animeDAO.buscarPorContenido(c.getCId()));
                    break;
                case 2:
                    c.setManga(mangaDAO.buscarPorContenido(c.getCId()));
                    break;
                case 3:
                    c.setNovela(novelaDAO.buscarPorContenido(c.getCId()));
                    break;
            }
        }

        return "/Usuario/ver.xhtml?faces-redirect=true";
    }

    public List<Tipo> getTiposPermitidos() {
        return (serieSeleccionada != null) ? serieSeleccionada.getTipos() : new ArrayList<>();
    }

    public void onTipoSeleccionado() {
        if (contenido != null && contenido.getTId() != null) {
            switch (contenido.getTId()) {
                case 1:
                    tipoSeleccionado = "Anime";
                    break;
                case 2:
                    tipoSeleccionado = "Manga";
                    break;
                case 3:
                    tipoSeleccionado = "Novela";
                    break;
                default:
                    tipoSeleccionado = null;
            }
        }
    }

    // =====================
    // Listas filtradas por tipo
    // =====================
    public List<Contenido> getAnimes() {
        List<Contenido> list = new ArrayList<>();
        for (Contenido c : contenidos) {
            if (c.getTId() == 1) {
                list.add(c);
            }
        }
        return list;
    }

    public List<Contenido> getMangas() {
        List<Contenido> list = new ArrayList<>();
        for (Contenido c : contenidos) {
            if (c.getTId() == 2) {
                list.add(c);
            }
        }
        return list;
    }

    public List<Contenido> getNovelas() {
        List<Contenido> list = new ArrayList<>();
        for (Contenido c : contenidos) {
            if (c.getTId() == 3) {
                list.add(c);
            }
        }
        return list;
    }

    // =====================
    // Getters y Setters
    // =====================
    public List<Contenido> getContenidos() {
        return contenidos;
    }

    public Contenido getContenido() {
        return contenido;
    }

    public void setContenido(Contenido contenido) {
        this.contenido = contenido;
    }

    public Serie getSerieSeleccionada() {
        return serieSeleccionada;
    }

    public void setSerieSeleccionada(Serie serieSeleccionada) {
        this.serieSeleccionada = serieSeleccionada;
    }

    public UploadedFile getArchivo() {
        return archivo;
    }

    public void setArchivo(UploadedFile archivo) {
        this.archivo = archivo;
    }

    public List<UploadedFile> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<UploadedFile> imagenes) {
        this.imagenes = imagenes;
    }

    public UploadedFile getPortada() {
        return portada;
    }

    public void setPortada(UploadedFile portada) {
        this.portada = portada;
    }

    public UploadedFile getMiniatura() {
        return miniatura;
    }

    public void setMiniatura(UploadedFile miniatura) {
        this.miniatura = miniatura;
    }

    public String getTipoSeleccionado() {
        return tipoSeleccionado;
    }

    public void setTipoSeleccionado(String tipoSeleccionado) {
        this.tipoSeleccionado = tipoSeleccionado;
    }

    public List<Estado> getEstados() {
        if (estados.isEmpty()) {
            estados = estadoDAO.listar();
        }
        return estados;
    }

    public void setEstados(List<Estado> estados) {
        this.estados = estados;
    }

    public void limpiar() {
        contenido = new Contenido();
        archivo = null;
        imagenes = null;
        portada = null;
        miniatura = null;
        tipoSeleccionado = null;
    }
}
