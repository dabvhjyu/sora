package dao;

import control.CoonBD;
import modelo.Anime;

import java.sql.*;

public class AnimeDAO {

    // =====================
    // AGREGAR
    // =====================
    public void agregar(Anime a) {
        String sql = "INSERT INTO anime (c_id, a_episodios, a_temporadas, a_video, a_miniatura, a_estado) "
                   + "VALUES (?,?,?,?,?,?)";

        try (Connection con = CoonBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, a.getCId());

            if (a.getAEpisodios() != null) ps.setInt(2, a.getAEpisodios());
            else ps.setNull(2, Types.INTEGER);

            if (a.getATemporadas() != null) ps.setInt(3, a.getATemporadas());
            else ps.setNull(3, Types.INTEGER);

            ps.setString(4, a.getAVideo());
            ps.setString(5, a.getAMiniatura());

            if (a.getAEstado() != null) ps.setInt(6, a.getAEstado());
            else ps.setNull(6, Types.INTEGER);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    a.setAId(rs.getInt(1));
                }
            }

        } catch (Exception e) {
            System.err.println("Error al agregar Anime: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =====================
    // BUSCAR POR CONTENIDO
    // =====================
    public Anime buscarPorContenido(int cId) {
        Anime a = null;
        String sql = "SELECT * FROM anime WHERE c_id=?";

        try (Connection con = CoonBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    a = mapRow(rs);
                }
            }

        } catch (Exception e) {
            System.err.println("Error en buscarPorContenido (cId=" + cId + "): " + e.getMessage());
            e.printStackTrace();
        }
        return a;
    }

    // =====================
    // ACTUALIZAR
    // =====================
    public void actualizar(Anime a) {
        String sql = "UPDATE anime SET a_episodios=?, a_temporadas=?, a_video=?, a_miniatura=?, a_estado=? "
                   + "WHERE a_id=?";

        try (Connection con = CoonBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (a.getAEpisodios() != null) ps.setInt(1, a.getAEpisodios());
            else ps.setNull(1, Types.INTEGER);

            if (a.getATemporadas() != null) ps.setInt(2, a.getATemporadas());
            else ps.setNull(2, Types.INTEGER);

            ps.setString(3, a.getAVideo());
            ps.setString(4, a.getAMiniatura());

            if (a.getAEstado() != null) ps.setInt(5, a.getAEstado());
            else ps.setNull(5, Types.INTEGER);

            ps.setInt(6, a.getAId());

            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("Error al actualizar Anime con ID " + a.getAId() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =====================
    // ELIMINAR
    // =====================
    public void eliminar(Anime a) {
        String sql = "DELETE FROM anime WHERE a_id=?";

        try (Connection con = CoonBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, a.getAId());
            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("Error al eliminar Anime con ID " + a.getAId() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =====================
    // EXISTE POR CONTENIDO
    // =====================
    public boolean existePorContenido(int cId) {
        String sql = "SELECT 1 FROM anime WHERE c_id=?";
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
    private Anime mapRow(ResultSet rs) throws SQLException {
        Anime a = new Anime();
        a.setAId(rs.getInt("a_id"));
        a.setCId(rs.getInt("c_id"));
        a.setAEpisodios(rs.getInt("a_episodios"));
        if (rs.wasNull()) a.setAEpisodios(null);

        a.setATemporadas(rs.getInt("a_temporadas"));
        if (rs.wasNull()) a.setATemporadas(null);

        a.setAVideo(rs.getString("a_video"));
        a.setAMiniatura(rs.getString("a_miniatura"));

        a.setAEstado(rs.getInt("a_estado"));
        if (rs.wasNull()) a.setAEstado(null);

        return a;
    }
}
