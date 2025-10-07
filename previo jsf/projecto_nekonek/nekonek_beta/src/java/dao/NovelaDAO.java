package dao;

import control.CoonBD;
import modelo.Novela;

import java.sql.*;

public class NovelaDAO {

    // =====================
    // AGREGAR
    // =====================
    public void agregar(Novela n) {
        String sql = "INSERT INTO novela (c_id, n_volumenes, n_pdf, n_portada, n_estado) "
                   + "VALUES (?,?,?,?,?)";

        try (Connection con = CoonBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, n.getCId());

            if (n.getNVolumenes() != null) ps.setInt(2, n.getNVolumenes());
            else ps.setNull(2, Types.INTEGER);

            ps.setString(3, n.getNPdf());
            ps.setString(4, n.getNPortada());

            if (n.getNEstado() != null) ps.setInt(5, n.getNEstado());
            else ps.setNull(5, Types.INTEGER);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    n.setNId(rs.getInt(1));
                }
            }

        } catch (Exception e) {
            System.err.println("Error al agregar Novela: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =====================
    // BUSCAR POR CONTENIDO
    // =====================
    public Novela buscarPorContenido(int cId) {
        Novela n = null;
        String sql = "SELECT * FROM novela WHERE c_id=?";

        try (Connection con = CoonBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    n = mapRow(rs);
                }
            }

        } catch (Exception e) {
            System.err.println("Error en buscarPorContenido (cId=" + cId + "): " + e.getMessage());
            e.printStackTrace();
        }
        return n;
    }

    // =====================
    // ACTUALIZAR
    // =====================
    public void actualizar(Novela n) {
        String sql = "UPDATE novela SET n_volumenes=?, n_pdf=?, n_portada=?, n_estado=? "
                   + "WHERE n_id=?";

        try (Connection con = CoonBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (n.getNVolumenes() != null) ps.setInt(1, n.getNVolumenes());
            else ps.setNull(1, Types.INTEGER);

            ps.setString(2, n.getNPdf());
            ps.setString(3, n.getNPortada());

            if (n.getNEstado() != null) ps.setInt(4, n.getNEstado());
            else ps.setNull(4, Types.INTEGER);

            ps.setInt(5, n.getNId());

            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("Error al actualizar Novela con ID " + n.getNId() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =====================
    // ELIMINAR
    // =====================
    public void eliminar(Novela n) {
        String sql = "DELETE FROM novela WHERE n_id=?";

        try (Connection con = CoonBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, n.getNId());
            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("Error al eliminar Novela con ID " + n.getNId() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =====================
    // EXISTE POR CONTENIDO
    // =====================
    public boolean existePorContenido(int cId) {
        String sql = "SELECT 1 FROM novela WHERE c_id=?";
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
    private Novela mapRow(ResultSet rs) throws SQLException {
        Novela n = new Novela();
        n.setNId(rs.getInt("n_id"));
        n.setCId(rs.getInt("c_id"));

        n.setNVolumenes(rs.getInt("n_volumenes"));
        if (rs.wasNull()) n.setNVolumenes(null);

        n.setNPdf(rs.getString("n_pdf"));
        n.setNPortada(rs.getString("n_portada"));

        n.setNEstado(rs.getInt("n_estado"));
        if (rs.wasNull()) n.setNEstado(null);

        return n;
    }
}

