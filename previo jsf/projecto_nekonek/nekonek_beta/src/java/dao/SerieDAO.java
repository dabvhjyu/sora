package dao;

import control.CoonBD;
import modelo.Serie;
import modelo.Usuario;
import modelo.Tipo;
import modelo.Pais;
import modelo.Estado;
import modelo.Genero;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SerieDAO {

    public SerieDAO() {
    }

    // INSERTAR
    public boolean insertarSerie(Serie serie) {
        String sql = "INSERT INTO series (u_id, s_titulo, s_descripcion, "
                + "s_pais, s_estado, s_imagenc, s_imagenv, fecha_creacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = CoonBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, serie.getUId());
            stmt.setString(2, serie.getSTitulo());
            stmt.setString(3, serie.getSDescripcion());
            stmt.setInt(4, serie.getSPais());
            stmt.setInt(5, serie.getSEstado());
            stmt.setString(6, serie.getSImagenc());
            stmt.setString(7, serie.getSImagenv());
            stmt.setTimestamp(8, serie.getFechaCreacion());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    serie.setSId(generatedKeys.getInt(1));
                }
                System.out.println("Serie insertada con ID: " + serie.getSId());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al insertar serie: " + e.getMessage());
        }
        return false;
    }

    // ACTUALIZAR
    public boolean actualizarSerie(Serie serie) {
        String sql = "UPDATE series SET s_titulo = ?, s_descripcion = ?, "
                + "s_pais = ?, s_estado = ?, s_imagenc = ?, s_imagenv = ? WHERE s_id = ?";

        try (Connection conn = CoonBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, serie.getSTitulo());
            stmt.setString(2, serie.getSDescripcion());
            stmt.setInt(3, serie.getSPais());
            stmt.setInt(4, serie.getSEstado());
            stmt.setString(5, serie.getSImagenc());
            stmt.setString(6, serie.getSImagenv());
            stmt.setInt(7, serie.getSId());

            int filasAfectadas = stmt.executeUpdate();
            System.out.println("Serie actualizada, filas afectadas: " + filasAfectadas);
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar serie: " + e.getMessage());
        }
        return false;
    }

    // ELIMINAR
    public boolean eliminarSerie(Integer serieId) {
        String sql = "DELETE FROM series WHERE s_id = ?";

        try (Connection conn = CoonBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, serieId);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar serie: " + e.getMessage());
        }
        return false;
    }

    // LISTAR TODAS (Biblioteca)
    public List<Serie> obtenerTodasLasSeries() {
        List<Serie> series = new ArrayList<>();
        String sql = "SELECT s.*, u.u_nombre, p.p_nombre, e.e_nombre "
                + "FROM series s "
                + "JOIN usuarios u ON s.u_id = u.u_id "
                + "JOIN paises p ON s.s_pais = p.p_id "
                + "JOIN estados e ON s.s_estado = e.e_id "
                + "ORDER BY s.fecha_creacion DESC";

        try (Connection conn = CoonBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Serie serie = crearSerieDesdeResultSet(rs);
                series.add(serie);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener series: " + e.getMessage());
        }

        return series;
    }

    /**
     * ✅ NUEVO: LISTAR SOLO LAS SERIES DEL USUARIO
     */
    public List<Serie> obtenerSeriesPorUsuario(int usuarioId) {
        List<Serie> series = new ArrayList<>();
        String sql = "SELECT s.*, u.u_nombre, p.p_nombre, e.e_nombre "
                + "FROM series s "
                + "JOIN usuarios u ON s.u_id = u.u_id "
                + "JOIN paises p ON s.s_pais = p.p_id "
                + "JOIN estados e ON s.s_estado = e.e_id "
                + "WHERE s.u_id = ? "
                + "ORDER BY s.fecha_creacion DESC";

        try (Connection conn = CoonBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Serie serie = crearSerieDesdeResultSet(rs);
                series.add(serie);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener series del usuario: " + e.getMessage());
        }

        return series;
    }

    // OBTENER POR ID
    public Serie obtenerSeriePorId(Integer serieId) {
        String sql = "SELECT s.*, u.u_nombre, p.p_nombre, e.e_nombre "
                + "FROM series s "
                + "JOIN usuarios u ON s.u_id = u.u_id "
                + "JOIN paises p ON s.s_pais = p.p_id "
                + "JOIN estados e ON s.s_estado = e.e_id "
                + "WHERE s.s_id = ?";

        try (Connection conn = CoonBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, serieId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return crearSerieDesdeResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener serie por ID: " + e.getMessage());
        }

        return null;
    }

    // MÉTODOS PARA GÉNEROS
    public boolean insertarGenerosSerie(Integer serieId, List<Integer> generos) {
        if (generos == null || generos.isEmpty()) {
            return true;
        }

        String sql = "INSERT INTO series_generos (s_id, g_id) VALUES (?, ?)";

        try (Connection conn = CoonBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (Integer generoId : generos) {
                stmt.setInt(1, serieId);
                stmt.setInt(2, generoId);
                stmt.addBatch();
            }

            stmt.executeBatch();
            System.out.println("Géneros insertados para serie ID: " + serieId);
            return true;

        } catch (SQLException e) {
            System.err.println("Error al insertar géneros de serie: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizarGenerosSerie(Integer serieId, List<Integer> generos) {
        try (Connection conn = CoonBD.conectar()) {
            String deleteSql = "DELETE FROM series_generos WHERE s_id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, serieId);
                deleteStmt.executeUpdate();
            }

            if (generos != null && !generos.isEmpty()) {
                String insertSql = "INSERT INTO series_generos (s_id, g_id) VALUES (?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    for (Integer generoId : generos) {
                        insertStmt.setInt(1, serieId);
                        insertStmt.setInt(2, generoId);
                        insertStmt.addBatch();
                    }
                    insertStmt.executeBatch();
                }
            }

            System.out.println("Géneros actualizados para serie ID: " + serieId);
            return true;

        } catch (SQLException e) {
            System.err.println("Error al actualizar géneros de serie: " + e.getMessage());
        }
        return false;
    }

    public List<Genero> obtenerGenerosPorSerie(Integer serieId) {
        List<Genero> generos = new ArrayList<>();
        String sql = "SELECT g.g_id, g.g_nombre FROM generos g "
                + "JOIN series_generos sg ON g.g_id = sg.g_id "
                + "WHERE sg.s_id = ? ORDER BY g.g_nombre";

        try (Connection conn = CoonBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, serieId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Genero genero = new Genero();
                genero.setGId(rs.getInt("g_id"));
                genero.setGNombre(rs.getString("g_nombre"));
                generos.add(genero);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener géneros por serie: " + e.getMessage());
        }

        return generos;
    }

    // MÉTODOS PARA TIPOS
    public boolean insertarTiposSerie(Integer serieId, List<Integer> tipos) {
        if (tipos == null || tipos.isEmpty()) {
            return true;
        }

        String sql = "INSERT INTO series_tipos (s_id, t_id) VALUES (?, ?)";

        try (Connection conn = CoonBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (Integer tipoId : tipos) {
                stmt.setInt(1, serieId);
                stmt.setInt(2, tipoId);
                stmt.addBatch();
            }

            stmt.executeBatch();
            System.out.println("Tipos insertados para serie ID: " + serieId);
            return true;

        } catch (SQLException e) {
            System.err.println("Error al insertar tipos de serie: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizarTiposSerie(Integer serieId, List<Integer> tipos) {
        try (Connection conn = CoonBD.conectar()) {
            String deleteSql = "DELETE FROM series_tipos WHERE s_id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, serieId);
                deleteStmt.executeUpdate();
            }

            if (tipos != null && !tipos.isEmpty()) {
                String insertSql = "INSERT INTO series_tipos (s_id, t_id) VALUES (?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    for (Integer tipoId : tipos) {
                        insertStmt.setInt(1, serieId);
                        insertStmt.setInt(2, tipoId);
                        insertStmt.addBatch();
                    }
                    insertStmt.executeBatch();
                }
            }

            System.out.println("Tipos actualizados para serie ID: " + serieId);
            return true;

        } catch (SQLException e) {
            System.err.println("Error al actualizar tipos de serie: " + e.getMessage());
        }
        return false;
    }

    // ➕ NUEVO: Buscar serie por ID (para cargar en edición)
    public Serie buscar(Serie s) {
        Serie serie = null;
        String sql = "SELECT * FROM series WHERE s_id = ?";
        try (Connection conn = CoonBD.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, s.getSId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    serie = new Serie();
                    serie.setSId(rs.getInt("s_id"));
                    serie.setUId(rs.getInt("u_id"));
                    serie.setSTitulo(rs.getString("s_titulo"));
                    serie.setSDescripcion(rs.getString("s_descripcion"));
                    serie.setSCantidadEpisodios(rs.getInt("s_cantidad_episodios"));
                    serie.setSPais(rs.getInt("s_pais"));
                    serie.setSEstado(rs.getInt("s_estado"));
                    serie.setSImagenc(rs.getString("s_imagenc"));
                    serie.setSImagenv(rs.getString("s_imagenv"));
                    serie.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar serie: " + e.getMessage());
        }
        return serie;
    }

    public List<Tipo> obtenerTiposPorSerie(Integer serieId) {
        List<Tipo> tipos = new ArrayList<>();
        String sql = "SELECT t.t_id, t.t_nombre FROM tipos t "
                + "JOIN series_tipos st ON t.t_id = st.t_id "
                + "WHERE st.s_id = ? ORDER BY t.t_nombre";

        try (Connection conn = CoonBD.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, serieId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Tipo tipo = new Tipo();
                tipo.setTId(rs.getInt("t_id"));
                tipo.setTNombre(rs.getString("t_nombre"));
                tipos.add(tipo);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener tipos por serie: " + e.getMessage());
        }

        return tipos;
    }

    // Helper para mapear ResultSet a Serie
    private Serie crearSerieDesdeResultSet(ResultSet rs) throws SQLException {
        Serie serie = new Serie();
        serie.setSId(rs.getInt("s_id"));
        serie.setUId(rs.getInt("u_id"));
        serie.setSTitulo(rs.getString("s_titulo"));
        serie.setSDescripcion(rs.getString("s_descripcion"));
        serie.setSPais(rs.getInt("s_pais"));
        serie.setSEstado(rs.getInt("s_estado"));
        serie.setSImagenc(rs.getString("s_imagenc"));
        serie.setSImagenv(rs.getString("s_imagenv"));
        serie.setFechaCreacion(rs.getTimestamp("fecha_creacion"));

        // Usuario
        Usuario usuario = new Usuario();
        usuario.setUId(rs.getInt("u_id"));
        usuario.setUNombre(rs.getString("u_nombre"));
        serie.setUsuario(usuario);

        // País
        Pais pais = new Pais();
        pais.setPId(rs.getInt("s_pais"));
        pais.setPNombre(rs.getString("p_nombre"));
        serie.setPais(pais);

        // Estado
        Estado estado = new Estado();
        estado.setEId(rs.getInt("s_estado"));
        estado.setENombre(rs.getString("e_nombre"));
        serie.setEstado(estado);

        return serie;
    }
    
}
