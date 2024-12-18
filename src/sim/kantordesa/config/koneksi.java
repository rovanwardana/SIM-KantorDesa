package sim.kantordesa.config;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class koneksi {

    static Connection conn;

    public static Connection getConnection() {
        if (conn == null) {
            try {
//                String url = "jdbc:mysql://server.razik.net/db_pbo";
                String url = "jdbc:mysql://server.razik.net/test_validasi";
                String user = "razik";
                String password = "razik";
                DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
                conn = (Connection) DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                Logger.getLogger(koneksi.class.getName()).log(Level.SEVERE, "Connection failed!", e);

            }
        }
        return conn;
    }

    public static boolean insert(String query, Object... params) {
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            Logger.getLogger(koneksi.class.getName()).log(Level.SEVERE, "Gagal melakukan insert", e);
            return false;
        }
    }

    public static boolean update(String query, Object... params) {
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            Logger.getLogger(koneksi.class.getName()).log(Level.SEVERE, "Gagal melakukan update", e);
            return false;
        }
    }

    public static boolean delete(String query, Object... params) {
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            Logger.getLogger(koneksi.class.getName()).log(Level.SEVERE, "Gagal melakukan delete", e);
            return false;
        }
    }

}
