package sim.kantordesa.config;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserData {
    
    private static final Logger LOGGER = Logger.getLogger(UserData.class.getName());
    
    public static boolean registerUser(String username, String password, String email,
            String birthDate, String fullName, int idRole) {
        String query = "INSERT INTO users (username, password, email, birth_date, full_name, created_at, id_role) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return koneksi.insert(query, username, password, email, birthDate, fullName,
                Timestamp.valueOf(LocalDateTime.now()), idRole);
    }

    public static List<User> getUsers() {
        String query = "SELECT user_id, username, email, password, birth_date, full_name, created_at, id_role FROM users";
        List<User> users = new ArrayList<>();

        try (Connection connection = koneksi.getConnection(); PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapToUser(resultSet));
            }
        } catch (SQLException e) {
             LOGGER.log(Level.SEVERE, "Gagal mengambil data user: " + e.getMessage(), e); 
        }

        return users;
    }

    public static boolean verifyLogin(String username, String password) {
        String query = "SELECT COUNT(*) AS count FROM users WHERE username = ? AND password = ?";
        try (Connection connection = koneksi.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count") > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Gagal memverifikasi login: " + e.getMessage(), e);
        }
        return false;
    }

    private static User mapToUser(ResultSet resultSet) throws SQLException {
        User user = new User();

        
        user.setUserId(resultSet.getInt("user_id")); // Ambil kolom "user_id" dari ResultSet
        user.setUsername(resultSet.getString("username")); // Kolom "username" → user.setUsername()
        user.setPassword(resultSet.getString("password")); // Kolom "password" → user.setPassword()
        user.setEmail(resultSet.getString("email")); // Kolom "email" → user.setEmail()
        user.setBirthDate(resultSet.getString("birth_date")); // Kolom "birth_date" → user.setBirthDate()
        user.setFullName(resultSet.getString("full_name")); // Kolom "full_name" → user.setFullName()
        user.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime()); // Konversi Timestamp → LocalDateTime
        user.setIdRole(resultSet.getInt("id_role")); // Kolom "id_role" → user.setIdRole()

        return user;
    }

}
