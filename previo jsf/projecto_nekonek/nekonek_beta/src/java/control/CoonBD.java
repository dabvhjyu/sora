package control;

import com.mysql.cj.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CoonBD {

    // Configuración de conexión
    private static final String URL = "jdbc:mysql://localhost:3306/adso_nekonek";
    private static final String USER = "root";
    private static final String PASSWORD = "QWERTY";

    // Método estático (llamado sin crear objeto)
    public static Connection conectar() {
        Connection conn = null;
        try {
            Driver drv = new Driver();
            DriverManager.registerDriver(drv);

            String cad = URL + "?user=" + USER + "&password=" + PASSWORD + "&useSSL=false&serverTimezone=UTC";
            conn = DriverManager.getConnection(cad);

        } catch (SQLException e) {
            System.err.println("❌ Error en Conexión a BD: " + e.getMessage());
        }
        return conn;
    }

    // Método de instancia (si trabajas con objetos de la clase)
    public Connection getConnection() {
        Connection conn = null;
        try {
            Driver drv = new Driver();
            DriverManager.registerDriver(drv);

            String cad = URL + "?user=" + USER + "&password=" + PASSWORD + "&useSSL=false&serverTimezone=UTC";
            conn = DriverManager.getConnection(cad);

        } catch (SQLException e) {
            System.err.println("❌ Error en Conexión a BD: " + e.getMessage());
        }
        return conn;
    }

    // Cerrar conexión de forma segura
    public static void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("✅ Conexión cerrada correctamente.");
            } catch (SQLException e) {
                System.err.println("❌ Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}
