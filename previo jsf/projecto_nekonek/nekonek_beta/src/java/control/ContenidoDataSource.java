package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import modelo.Estado;
import modelo.Pais;
import modelo.Serie;
import modelo.Usuario;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class ContenidoDataSource implements JRDataSource {
    private List<Serie> lstCont;
    private int indice = -1;

    // Constructor que recibe lista ya cargada
    public ContenidoDataSource(List<Serie> lstCont) {
        this.lstCont = lstCont;
    }

    // Constructor que se conecta y ejecuta la consulta
    public ContenidoDataSource() {
        lstCont = new ArrayList<>();

        String sql = "SELECT "
                + " u.u_nombre    AS NombreUsuario, "
                + " u.u_email     AS CorreoUsuario, "
                + " s.s_titulo    AS TituloSerie, "
                + " t.t_nombre    AS Tipo, "
                + " GROUP_CONCAT(DISTINCT g.g_nombre ORDER BY g.g_nombre SEPARATOR ', ') AS Generos, "
                + " p.p_nombre    AS Pais, "
                + " e.e_nombre    AS Estado, "
                + " s.s_cantidadepisodios AS Episodios, "
                + " s.fecha_creacion "
                + "FROM usuarios u "
                + "INNER JOIN series s ON u.u_id = s.u_id "
                + "INNER JOIN series_tipos st ON s.s_id = st.s_id "
                + "INNER JOIN tipos t ON st.t_id = t.t_id "
                + "INNER JOIN series_generos sg ON s.s_id = sg.s_id "
                + "INNER JOIN generos g ON sg.g_id = g.g_id "
                + "INNER JOIN paises p ON s.s_pais = p.p_id "
                + "INNER JOIN estados e ON s.s_estado = e.e_id "
                + "GROUP BY u.u_id, u.u_nombre, u.u_email, "
                + " s.s_id, s.s_titulo, t.t_nombre, "
                + " p.p_nombre, e.e_nombre, "
                + " s.s_cantidadepisodios, s.fecha_creacion "
                + "ORDER BY u.u_nombre, s.s_titulo";

        try (Connection conn = CoonBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Serie serie = new Serie();

                // Usuario
                Usuario u = new Usuario();
                u.setUNombre(rs.getString("NombreUsuario"));
                u.setUEmail(rs.getString("CorreoUsuario"));
                serie.setUsuario(u);

                // Datos de serie
                serie.setSTitulo(rs.getString("TituloSerie"));
                serie.setTiposTexto(rs.getString("Tipo"));       // tipo concatenado
                serie.setGenerosTexto(rs.getString("Generos")); // géneros concatenados

                // País
                Pais pais = new Pais();
                pais.setPNombre(rs.getString("Pais"));
                serie.setPais(pais);

                // Estado
                Estado estado = new Estado();
                estado.setENombre(rs.getString("Estado"));
                serie.setEstado(estado);

                serie.setSCantidadEpisodios(rs.getInt("Episodios"));
                serie.setFechaCreacion(rs.getTimestamp("fecha_creacion"));

                lstCont.add(serie);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean next() throws JRException {
        indice++;
        return (lstCont != null && indice < lstCont.size());
    }

    @Override
    public Object getFieldValue(JRField jrf) throws JRException {
        Serie serie = lstCont.get(indice);
        String fieldName = jrf.getName();
        Object valor = null;

        switch (fieldName) {
            case "NombreUsuario":
                valor = serie.getUsuario() != null ? serie.getUsuario().getUNombre() : "";
                break;
            case "CorreoUsuario":
                valor = serie.getUsuario() != null ? serie.getUsuario().getUEmail() : "";
                break;
            case "TituloSerie":
                valor = serie.getSTitulo();
                break;
            case "Tipo":
                valor = serie.getTiposTexto();
                break;
            case "Generos":
                valor = serie.getGenerosTexto();
                break;
            case "Pais":
                valor = serie.getPais() != null ? serie.getPais().getPNombre() : "";
                break;
            case "Estado":
                valor = serie.getEstado() != null ? serie.getEstado().getENombre() : "";
                break;
            case "Episodios":
                valor = serie.getSCantidadEpisodios();
                break;
            case "fecha_creacion":
                // 🔹 Ahora devolvemos directamente el Timestamp
                valor = serie.getFechaCreacion();
                break;
        }
        return valor;
    }
}
