package dao;

import control.CoonBD;
import modelo.Manga;

import java.sql.*;

public class MangaDAO {

    // =====================
    // AGREGAR
    // =====================
    public void agregar(Manga m) {
        String sql = "INSERT INTO manga (c_id, m_capitulos, m_imagenes, m_volumenes, m_estado, m_portada) "
                   + "VALUES (?,?,?,?,?,?)";

        try (Connection con = CoonBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, m.getCId());

            if (m.getMCapitulos() != null) ps.setInt(2, m.getMCapitulos());
            else ps.setNull(2, Types.INTEGER);

            ps.setString(3, m.getMImagenes());

            if (m.getMVolumenes() != null) ps.setInt(4, m.getMVolumenes());
            else ps.setNull(4, Types.INTEGER);

            if (m.getMEstado() != null) ps.setInt(5, m.getMEstado());
            else ps.setNull(5, Types.INTEGER);

            ps.setString(6, m.getMPortada());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    m.setMId(rs.getInt(1));
                }
            }

        } catch (Exception e) {
            System.err.println("Error al agregar Manga: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =====================
    // BUSCAR POR CONTENIDO
    // =====================
    public Manga buscarPorContenido(int cId) {
        Manga m = null;
        String sql = "SELECT * FROM manga WHERE c_id=?";

        try (Connection con = CoonBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    m = mapRow(rs);
                }
            }

        } catch (Exception e) {
            System.err.println("Error en buscarPorContenido (cId=" + cId + "): " + e.getMessage());
            e.printStackTrace();
        }
        return m;
    }

    // =====================
    // ACTUALIZAR
    // =====================
    public void actualizar(Manga m) {
        String sql = "UPDATE manga SET m_capitulos=?, m_imagenes=?, m_volumenes=?, m_estado=?, m_portada=? "
                   + "WHERE m_id=?";

        try (Connection con = CoonBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (m.getMCapitulos() != null) ps.setInt(1, m.getMCapitulos());
            else ps.setNull(1, Types.INTEGER);

            ps.setString(2, m.getMImagenes());

            if (m.getMVolumenes() != null) ps.setInt(3, m.getMVolumenes());
            else ps.setNull(3, Types.INTEGER);

            if (m.getMEstado() != null) ps.setInt(4, m.getMEstado());
            else ps.setNull(4, Types.INTEGER);

            ps.setString(5, m.getMPortada());
            ps.setInt(6, m.getMId());

            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("Error al actualizar Manga con ID " + m.getMId() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =====================
    // ELIMINAR
    // =====================
    public void eliminar(Manga m) {
        String sql = "DELETE FROM manga WHERE m_id=?";

        try (Connection con = CoonBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, m.getMId());
            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("Error al eliminar Manga con ID " + m.getMId() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =====================
    // EXISTE POR CONTENIDO
    // =====================
    public boolean existePorContenido(int cId) {
        String sql = "SELECT 1 FROM manga WHERE c_id=?";
        try (Connection con = CoonBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            System.err.println("Error en existePorContenido (cId=" + cId + "): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // =====================
    // MAPEADOR
    // =====================
    private Manga mapRow(ResultSet rs) throws SQLException {
        Manga m = new Manga();
        m.setMId(rs.getInt("m_id"));
        m.setCId(rs.getInt("c_id"));

        m.setMCapitulos(rs.getInt("m_capitulos"));
        if (rs.wasNull()) m.setMCapitulos(null);

        m.setMImagenes(rs.getString("m_imagenes"));

        m.setMVolumenes(rs.getInt("m_volumenes"));
        if (rs.wasNull()) m.setMVolumenes(null);

        m.setMEstado(rs.getInt("m_estado"));
        if (rs.wasNull()) m.setMEstado(null);

        m.setMPortada(rs.getString("m_portada"));
        return m;
    }
}

