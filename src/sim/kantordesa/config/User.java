package sim.kantordesa.config;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class User {

    private int userId;
    private String username;
    private String password;
    private String email;
    private String birthDate;
    private String fullName;
    private LocalDateTime createdAt;
    private int idRole;
    private String role;

    public User() {
        conn = koneksi.getConnection();
    }

    // Getter dan Setter
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public String getRole() {
        return role;
    }
    
     public void setRole(String role) {
        this.role = role;
    }

    private final Connection conn;

    public static User getUserFromDatabase(String username) {
        User user = null;
        try {
            Connection conn = koneksi.getConnection();
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setBirthDate(rs.getString("birth_date"));
                user.setFullName(rs.getString("full_name"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                user.setIdRole(rs.getInt("id_role"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

}
