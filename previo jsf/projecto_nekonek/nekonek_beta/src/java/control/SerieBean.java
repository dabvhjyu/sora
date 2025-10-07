package control;

import dao.SerieDAO;
import dao.TipoDAO;
import dao.PaisDAO;
import dao.EstadoDAO;
import dao.GeneroDAO;
import modelo.Serie;
import modelo.Tipo;
import modelo.Pais;
import modelo.Estado;
import modelo.Genero;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@ManagedBean(name = "serieBean")
@ViewScoped
public class SerieBean implements Serializable {

    private static final long serialVersionUID = 1L;

    // DAOs
    private SerieDAO serieDAO;
    private TipoDAO tipoDAO;
    private PaisDAO paisDAO;
    private EstadoDAO estadoDAO;
    private GeneroDAO generoDAO;

    // Entidad principal
    private Serie serie;
    private List<Serie> series;
    private List<Serie> seriesFiltradas;

    // Listas para selectores
    private List<Tipo> tipos;
    private List<Pais> paises;
    private List<Estado> estados;
    private List<Genero> generos;

    // Para manejar múltiples géneros y tipos
    private List<Integer> generosSeleccionados;
    private List<Integer> tiposSeleccionados;

    // Flags
    private boolean modoEdicion = false;
    private Integer usuarioSesionId;

    // Archivos de imagen
    private Part fileCuadrada;
    private Part fileVertical;

    public SerieBean() {
        serieDAO = new SerieDAO();
        tipoDAO = new TipoDAO();
        paisDAO = new PaisDAO();
        estadoDAO = new EstadoDAO();
        generoDAO = new GeneroDAO();

        serie = new Serie();
        series = new ArrayList<>();
        generosSeleccionados = new ArrayList<>();
        tiposSeleccionados = new ArrayList<>();
    }
    
    
    public void exportarPDF() {
    try {
        // Ruta del reporte compilado .jasper dentro de tu proyecto (carpeta resources o WebContent)
        String path = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRealPath("/reporte.jasper");

        File jasper = new File(path);

        // Tu DataSource (ajusta según tu implementación)
        ContenidoDataSource cds = new ContenidoDataSource();

        // Llenar el reporte
        JasperPrint jprint = JasperFillManager.fillReport(jasper.getPath(), null, cds);

        // Preparar respuesta HTTP
        HttpServletResponse resp = (HttpServletResponse) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getResponse();

        resp.addHeader("Content-disposition", "attachment; filename=Series.pdf");
        resp.setContentType("application/pdf");

        try (ServletOutputStream stream = resp.getOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jprint, stream);
            stream.flush();
        }

        FacesContext.getCurrentInstance().responseComplete();

    } catch (JRException | IOException e) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error creando reporte: " + e.getMessage()));
        e.printStackTrace();
    }
}


    @PostConstruct
    public void init() {
        obtenerUsuarioSesion();
        cargarListas();

        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null && context.getViewRoot() != null) {
            String viewId = context.getViewRoot().getViewId();
            if ("/Usuario/crearSerie.xhtml".equals(viewId)) {
                inicializarNuevaSerie();
            } else {
                cargarSeries(); // por defecto cargamos TODAS las series (Biblioteca)
            }
        }
    }

    private void obtenerUsuarioSesion() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) context.getExternalContext().getSession(false);

            if (session != null) {
                Object userId = session.getAttribute("usuarioId");
                if (userId != null) {
                    usuarioSesionId = (Integer) userId;
                    System.out.println("👤 Usuario de sesión: " + usuarioSesionId);
                } else {
                    System.err.println("⚠️ No se encontró usuarioId en la sesión");
                }
            } else {
                System.err.println("⚠️ No hay sesión activa");
            }
        } catch (Exception e) {
            System.err.println("❌ Error al obtener usuario de sesión: " + e.getMessage());
        }
    }

    private void cargarListas() {
        try {
            System.out.println("📊 Cargando listas para selectores...");
            tipos = tipoDAO.obtenerTodos();
            paises = paisDAO.obtenerTodos();
            estados = estadoDAO.obtenerTodos();
            generos = generoDAO.obtenerTodos();

            System.out.println("✅ Listas cargadas - Tipos: " + tipos.size()
                    + ", Países: " + paises.size()
                    + ", Estados: " + estados.size()
                    + ", Géneros: " + generos.size());

        } catch (Exception e) {
            System.err.println("❌ Error al cargar listas: " + e.getMessage());
            mostrarMensaje(FacesMessage.SEVERITY_ERROR,
                    "Error", "Error al cargar las listas: " + e.getMessage());
        }
    }

    /**
     * ✅ NUEVO: Cargar TODAS las series (Biblioteca)
     */
    public void cargarSeries() {
        try {
            series = serieDAO.obtenerTodasLasSeries();
            procesarSeries(series);
            System.out.println("📚 Se cargaron " + series.size() + " series en biblioteca.");
        } catch (Exception e) {
            System.err.println("❌ Error al cargar series: " + e.getMessage());
            mostrarMensaje(FacesMessage.SEVERITY_ERROR,
                    "Error", "Error al cargar series: " + e.getMessage());
        }
    }

    /**
     * ✅ NUEVO: Cargar SOLO las series del usuario logueado
     */
    public void cargarSeriesUsuario() {
        try {
            if (usuarioSesionId != null) {
                series = serieDAO.obtenerSeriesPorUsuario(usuarioSesionId);
                procesarSeries(series);
                System.out.println("👤 Se cargaron " + series.size() + " series del usuario " + usuarioSesionId);
            } else {
                series = new ArrayList<>();
                System.err.println("⚠️ No se puede cargar series del usuario: usuarioSesionId es null");
            }
        } catch (Exception e) {
            System.err.println("❌ Error al cargar series del usuario: " + e.getMessage());
            mostrarMensaje(FacesMessage.SEVERITY_ERROR,
                    "Error", "Error al cargar tus series: " + e.getMessage());
        }
    }

    /**
     * Método reutilizado para construir texto de géneros y tipos
     */
    private void procesarSeries(List<Serie> lista) {
        for (Serie s : lista) {
            List<Genero> generos = serieDAO.obtenerGenerosPorSerie(s.getSId());
            s.setGeneros(generos);
            if (generos != null && !generos.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (Genero g : generos) {
                    sb.append(g.getGNombre()).append(", ");
                }
                s.setGenerosTexto(sb.substring(0, sb.length() - 2));
            } else {
                s.setGenerosTexto("Sin géneros");
            }

            List<Tipo> tipos = serieDAO.obtenerTiposPorSerie(s.getSId());
            s.setTipos(tipos);
            if (tipos != null && !tipos.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (Tipo t : tipos) {
                    sb.append(t.getTNombre()).append(", ");
                }
                s.setTiposTexto(sb.substring(0, sb.length() - 2));
            } else {
                s.setTiposTexto("Sin tipos");
            }
        }
    }

    public void inicializarNuevaSerie() {
        serie = new Serie();
        generosSeleccionados = new ArrayList<>();
        tiposSeleccionados = new ArrayList<>();
        modoEdicion = false;

        if (usuarioSesionId != null) {
            serie.setUId(usuarioSesionId);
        }

        serie.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
        System.out.println("🆕 Nueva serie inicializada para usuario: " + serie.getUId());
    }

    public void guardarSerie() {
        try {
            System.out.println("🔧 Guardando serie...");
            if (!validarSerie()) {
                return;
            }

            if (serie.getUId() == null && usuarioSesionId != null) {
                serie.setUId(usuarioSesionId);
            }

            if (serie.getFechaCreacion() == null) {
                serie.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
            }

            procesarImagenes();

            boolean exito;
            if (modoEdicion && serie.getSId() != null) {
                exito = serieDAO.actualizarSerie(serie);
                if (exito) {
                    serieDAO.actualizarGenerosSerie(serie.getSId(), generosSeleccionados);
                    serieDAO.actualizarTiposSerie(serie.getSId(), tiposSeleccionados);
                }
            } else {
                exito = serieDAO.insertarSerie(serie);
                if (exito && serie.getSId() != null) {
                    serieDAO.insertarGenerosSerie(serie.getSId(), generosSeleccionados);
                    serieDAO.insertarTiposSerie(serie.getSId(), tiposSeleccionados);
                }
            }

            if (exito) {
                mostrarMensaje(FacesMessage.SEVERITY_INFO,
                        "✅ Éxito", modoEdicion ? "Serie actualizada" : "Serie creada");
                cargarSeries();
                if (!modoEdicion) {
                    limpiarFormularioSinRecargar();
                }
            } else {
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "❌ Error", "No se pudo guardar la serie");
            }
        } catch (Exception e) {
            System.err.println("❌ Error al guardar serie: " + e.getMessage());
            mostrarMensaje(FacesMessage.SEVERITY_ERROR,
                    "Error", "Error inesperado: " + e.getMessage());
        }
    }

    private void procesarImagenes() {
        try {
            ServletContext sc = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String uploadDir = sc.getRealPath("/") + "uploads";

            File carpeta = new File(uploadDir);
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            if (fileCuadrada != null && fileCuadrada.getSize() > 0) {
                String fileName = System.currentTimeMillis() + "_c_" + getFileName(fileCuadrada);
                File f = new File(carpeta, fileName);
                try (InputStream in = fileCuadrada.getInputStream(); FileOutputStream out = new FileOutputStream(f)) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                }
                serie.setSImagenc("/uploads/" + fileName);
            }

            if (fileVertical != null && fileVertical.getSize() > 0) {
                String fileName = System.currentTimeMillis() + "_v_" + getFileName(fileVertical);
                File f = new File(carpeta, fileName);
                try (InputStream in = fileVertical.getInputStream(); FileOutputStream out = new FileOutputStream(f)) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                }
                serie.setSImagenv("/uploads/" + fileName);
            }

        } catch (Exception e) {
            System.err.println("⚠️ Error al procesar imágenes: " + e.getMessage());
        }
    }

    private String getFileName(Part part) {
        String header = part.getHeader("content-disposition");
        for (String cd : header.split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    // ➕ NUEVO: editar -> carga una serie existente al formulario
    public void editar(Serie s) {
        serie = serieDAO.buscar(s);
        if (serie != null) {
            generosSeleccionados = new ArrayList<>();
            for (Genero g : serieDAO.obtenerGenerosPorSerie(serie.getSId())) {
                generosSeleccionados.add(g.getGId());
            }
            tiposSeleccionados = new ArrayList<>();
            for (Tipo t : serieDAO.obtenerTiposPorSerie(serie.getSId())) {
                tiposSeleccionados.add(t.getTId());
            }
            modoEdicion = true;
        }
    }

    public void cargarParaEdicion(ComponentSystemEvent event) {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();

            // Evitar volver a ejecutar en postbacks (ej: envío del formulario)
            if (fc.isPostback()) {
                return;
            }

            Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
            String idParam = params.get("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                return;
            }

            Integer id = Integer.valueOf(idParam);
            Serie s = serieDAO.obtenerSeriePorId(id);
            if (s == null) {
                mostrarMensaje(FacesMessage.SEVERITY_WARN, "Atención", "Serie no encontrada");
                return;
            }

            // Cargar la serie en el bean (reemplaza el objeto)
            this.serie = s;

            // Cargar géneros seleccionados
            // después de cargar la serie
            List<Genero> generosActuales = serieDAO.obtenerGenerosPorSerie(id);
            generosSeleccionados = new ArrayList<>();
            for (Genero g : generosActuales) {
                generosSeleccionados.add(g.getGId()); // tiene que ser Integer
            }

            List<Tipo> tiposActuales = serieDAO.obtenerTiposPorSerie(id);
            tiposSeleccionados = new ArrayList<>();
            for (Tipo t : tiposActuales) {
                tiposSeleccionados.add(t.getTId()); // tiene que ser Integer
            }

            modoEdicion = true;

            System.out.println("✏️ Cargando serie para edición: ID=" + id);

        } catch (NumberFormatException nf) {
            System.err.println("ID inválido en parámetro id: " + nf.getMessage());
        } catch (Exception e) {
            System.err.println("Error en cargarParaEdicion: " + e.getMessage());
        }
    }

// ➕ NUEVO: actualizar -> guarda cambios de la serie en BD
    public String actualizar() {
        try {
            procesarImagenes();
            boolean exito = serieDAO.actualizarSerie(serie);
            if (exito) {
                serieDAO.actualizarGenerosSerie(serie.getSId(), generosSeleccionados);
                serieDAO.actualizarTiposSerie(serie.getSId(), tiposSeleccionados);

                mostrarMensaje(FacesMessage.SEVERITY_INFO, "✅ Éxito", "Serie actualizada");

                // ✅ navegación por outcome
                return "/Usuario/autenticar.xhtml?faces-redirect=true";
            } else {
                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "❌ Error", "No se pudo actualizar la serie");
                return null;
            }
        } catch (Exception e) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "❌ Error", "Error actualizando la serie: " + e.getMessage());
            return null;
        }
    }

// ➕ NUEVO: eliminar -> elimina serie seleccionada
    public void eliminar(Serie s) {
        boolean exito = serieDAO.eliminarSerie(s.getSId());
        if (exito) {
            mostrarMensaje(FacesMessage.SEVERITY_INFO, "✅ Éxito", "Serie eliminada");
            cargarSeries();
        } else {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "❌ Error", "No se pudo eliminar la serie");
        }
    }

    private boolean validarSerie() {
        if (serie.getSTitulo() == null || serie.getSTitulo().trim().isEmpty()) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error", "El título es obligatorio");
            return false;
        }
        if (tiposSeleccionados == null || tiposSeleccionados.isEmpty()) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error", "Debe seleccionar al menos un tipo");
            return false;
        }
        if (serie.getSPais() == null) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error", "Debe seleccionar un país");
            return false;
        }
        if (serie.getSEstado() == null) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error", "Debe seleccionar un estado");
            return false;
        }
        return true;
    }

    private void limpiarFormularioSinRecargar() {
        serie = new Serie();
        if (usuarioSesionId != null) {
            serie.setUId(usuarioSesionId);
        }
        serie.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
        generosSeleccionados = new ArrayList<>();
        tiposSeleccionados = new ArrayList<>();
        modoEdicion = false;
        fileCuadrada = null;
        fileVertical = null;
    }

    private void mostrarMensaje(FacesMessage.Severity severity, String titulo, String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage facesMessage = new FacesMessage(severity, titulo, mensaje);
        context.addMessage(null, facesMessage);
    }

    public String cancelar() {
        return "listaSeries?faces-redirect=true";
    }

    // Getters y Setters
    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public List<Serie> getSeries() {
        return series;
    }

    public void setSeries(List<Serie> series) {
        this.series = series;
    }

    public List<Serie> getSeriesFiltradas() {
        return seriesFiltradas;
    }

    public void setSeriesFiltradas(List<Serie> seriesFiltradas) {
        this.seriesFiltradas = seriesFiltradas;
    }

    public List<Tipo> getTipos() {
        return tipos;
    }

    public List<Pais> getPaises() {
        return paises;
    }

    public List<Estado> getEstados() {
        return estados;
    }

    public List<Genero> getGeneros() {
        return generos;
    }

    public List<Integer> getGenerosSeleccionados() {
        return generosSeleccionados;
    }

    public void setGenerosSeleccionados(List<Integer> generosSeleccionados) {
        this.generosSeleccionados = generosSeleccionados;
    }

    public List<Integer> getTiposSeleccionados() {
        return tiposSeleccionados;
    }

    public void setTiposSeleccionados(List<Integer> tiposSeleccionados) {
        this.tiposSeleccionados = tiposSeleccionados;
    }

    public boolean isModoEdicion() {
        return modoEdicion;
    }

    public void setModoEdicion(boolean modoEdicion) {
        this.modoEdicion = modoEdicion;
    }

    public Integer getUsuarioSesionId() {
        return usuarioSesionId;
    }

    public void setUsuarioSesionId(Integer usuarioSesionId) {
        this.usuarioSesionId = usuarioSesionId;
    }

    public Part getFileCuadrada() {
        return fileCuadrada;
    }

    public void setFileCuadrada(Part fileCuadrada) {
        this.fileCuadrada = fileCuadrada;
    }

    public Part getFileVertical() {
        return fileVertical;
    }

    public void setFileVertical(Part fileVertical) {
        this.fileVertical = fileVertical;
    }
}
