import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public void registerUser(String userName, String email, String password, boolean isAdmin) throws SQLException {
        String sql = "INSERT INTO users (username, email, password_hash, is_admin) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.setString(2, email);
            stmt.setString(3, password); // Helst ska lösenordet hashas
            stmt.setBoolean(4, isAdmin);
            stmt.executeUpdate();
        }
    }

    public User loginUser(String userName, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.setString(2, password); // Helst ska lösenordet hashas och jämföras
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getBoolean("is_admin")
                );
            }
            return null;
        }
    }

    // Lägg till metod för att lista alla användare
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getBoolean("is_admin")
                ));
            }
        }
        return users;
    }

    // Lägg till metod för att ta bort en användare
    public void deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }
}
