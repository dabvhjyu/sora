package dao;

import control.CoonBD;
import modelo.Contenido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContenidoDAO {

    // =====================
    // LISTAR
    // =====================
    public List<Contenido> listar() {
        List<Contenido> lista = new ArrayList<>();
        String sql = "SELECT * FROM contenidos ORDER BY fecha_subida DESC";

        try (Connection con = CoonBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapRow(rs));
            }

        } catch (Exception e) {
            System.err.println("Error en listar contenidos: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public List<Contenido> listarPorSerie(int sId) {
        List<Contenido> lista = new ArrayList<>();
        String sql = "SELECT * FROM contenidos WHERE s_id=? ORDER BY fecha_subida DESC";

        try (Connection con = CoonBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }

        } catch (Exception e) {
            System.err.println("Error en listarPorSerie: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // =====================
    // AGREGAR
    // =====================
    public void agregar(Contenido c) {
        String sql = "INSERT INTO contenidos "
                + "(s_id, u_id, t_id, c_titulo, c_descripcion, c_completo) "
                + "VALUES (?,?,?,?,?,?)";

        try (Connection con = CoonBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, c.getSId());
            if (c.getUId() != null) ps.setInt(2, c.getUId());
            else ps.setNull(2, Types.INTEGER);

            ps.setInt(3, c.getTId());
            ps.setString(4, c.getCTitulo());
            ps.setString(5, c.getCDescripcion());

            if (c.getCCompleto() != null) ps.setInt(6, c.getCCompleto());
            else ps.setNull(6, Types.INTEGER);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) c.setCId(rs.getInt(1));
            }

        } catch (Exception e) {
            System.err.println("Error al agregar contenido: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =====================
    // ACTUALIZAR
    // =====================
    public void actualizar(Contenido c) {
        String sql = "UPDATE contenidos SET "
                + "s_id=?, u_id=?, t_id=?, c_titulo=?, c_descripcion=?, c_completo=? "
                + "WHERE c_id=?";

        try (Connection con = CoonBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, c.getSId());
            if (c.getUId() != null) ps.setInt(2, c.getUId());
            else ps.setNull(2, Types.INTEGER);

            ps.setInt(3, c.getTId());
            ps.setString(4, c.getCTitulo());
            ps.setString(5, c.getCDescripcion());

            if (c.getCCompleto() != null) ps.setInt(6, c.getCCompleto());
            else ps.setNull(6, Types.INTEGER);

            ps.setInt(7, c.getCId());

            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("Error al actualizar contenido con ID " + c.getCId() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =====================
    // ELIMINAR
    // =====================
    public void eliminar(Contenido c) {
        String sql = "DELETE FROM contenidos WHERE c_id=?";

        try (Connection con = CoonBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, c.getCId());
            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("Error al eliminar contenido con ID " + c.getCId() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =====================
    // BUSCAR
    // =====================
    public Contenido buscarPorId(int id) {
        Contenido c = null;
        String sql = "SELECT * FROM contenidos WHERE c_id=?";

        try (Connection con = CoonBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) c = mapRow(rs);
            }

        } catch (Exception e) {
            System.err.println("Error en buscarPorId: " + e.getMessage());
            e.printStackTrace();
        }
        return c;
    }

    public boolean existePorId(int id) {
        String sql = "SELECT 1 FROM contenidos WHERE c_id=?";
        try (Connection con = CoonBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            System.err.println("Error en existePorId: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // =====================
    // MAPEO
    // =====================
    private Contenido mapRow(ResultSet rs) throws SQLException {
        Contenido c = new Contenido();
        c.setCId(rs.getInt("c_id"));
        c.setSId(rs.getInt("s_id"));
        c.setUId(rs.getInt("u_id"));
        c.setTId(rs.getInt("t_id"));
        c.setCTitulo(rs.getString("c_titulo"));
        c.setCDescripcion(rs.getString("c_descripcion"));

        int completo = rs.getInt("c_completo");
        c.setCCompleto(rs.wasNull() ? 0 : completo);

        c.setFechaSubida(rs.getTimestamp("fecha_subida"));

        // Eliminado mapeo de columnas inexistentes (archivo, miniatura, portada, imagenes)

        return c;
    }
}
