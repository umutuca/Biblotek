import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    public void createLoan(String userName, int bookId) throws SQLException {
        String sql = "INSERT INTO loans (user_name, book_id, loan_date) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.setInt(2, bookId);
            stmt.setDate(3, Date.valueOf(LocalDate.now()));
            stmt.executeUpdate();
        }
    }

    public void returnLoan(int loanId) throws SQLException {
        String sql = "UPDATE loans SET return_date = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            stmt.setInt(2, loanId);
            stmt.executeUpdate();
        }
    }

    public List<Loan> getLoansByUser(String userName) throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT loans.*, books.title, books.author, GROUP_CONCAT(categories.name SEPARATOR ', ') AS categories " +
                "FROM loans " +
                "JOIN books ON loans.book_id = books.id " +
                "LEFT JOIN book_categories ON books.id = book_categories.book_id " +
                "LEFT JOIN categories ON book_categories.category_id = categories.id " +
                "WHERE loans.user_name = ? AND return_date IS NULL " +
                "GROUP BY loans.id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                loans.add(new Loan(
                        rs.getInt("id"),
                        rs.getString("user_name"),
                        rs.getInt("book_id"),
                        rs.getDate("loan_date").toLocalDate(),
                        null,
                        rs.getString("author"),
                        rs.getString("title"),
                        rs.getString("categories") // Lägg till kategorin
                ));
            }
        }
        return loans;
    }

    public List<Loan> getAllActiveLoans() throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT loans.*, books.title, books.author, GROUP_CONCAT(categories.name SEPARATOR ', ') AS categories, users.username " +
                "FROM loans " +
                "JOIN books ON loans.book_id = books.id " +
                "JOIN users ON loans.user_name = users.username " +
                "LEFT JOIN book_categories ON books.id = book_categories.book_id " +
                "LEFT JOIN categories ON book_categories.category_id = categories.id " +
                "WHERE return_date IS NULL " +
                "GROUP BY loans.id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                loans.add(new Loan(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getInt("book_id"),
                        rs.getDate("loan_date").toLocalDate(),
                        null,
                        rs.getString("author"),
                        rs.getString("title"),
                        rs.getString("categories") // Lägg till kategorin
                ));
            }
        }
        return loans;
    }
}
