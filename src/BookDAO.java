import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public void addBook(String title, String author, String category) throws SQLException {
        String sql = "INSERT INTO books (title, author, available) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setBoolean(3, true);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int bookId = rs.getInt(1);
                addBookCategory(bookId, category);
            }
        }
    }

    private void addBookCategory(int bookId, String category) throws SQLException {
        String findCategorySql = "SELECT id FROM categories WHERE name = ?";
        String addCategorySql = "INSERT INTO categories (name) VALUES (?)";
        String linkBookCategorySql = "INSERT INTO book_categories (book_id, category_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement findCategoryStmt = conn.prepareStatement(findCategorySql);
             PreparedStatement addCategoryStmt = conn.prepareStatement(addCategorySql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement linkBookCategoryStmt = conn.prepareStatement(linkBookCategorySql)) {

            findCategoryStmt.setString(1, category);
            ResultSet rs = findCategoryStmt.executeQuery();

            int categoryId;
            if (rs.next()) {
                categoryId = rs.getInt("id");
            } else {
                addCategoryStmt.setString(1, category);
                addCategoryStmt.executeUpdate();
                ResultSet generatedKeys = addCategoryStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    categoryId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve category ID.");
                }
            }

            linkBookCategoryStmt.setInt(1, bookId);
            linkBookCategoryStmt.setInt(2, categoryId);
            linkBookCategoryStmt.executeUpdate();
        }
    }

    public void deleteBook(int bookId) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
        }
    }

    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT books.*, GROUP_CONCAT(categories.name SEPARATOR ', ') AS categories " +
                "FROM books " +
                "LEFT JOIN book_categories ON books.id = book_categories.book_id " +
                "LEFT JOIN categories ON book_categories.category_id = categories.id " +
                "GROUP BY books.id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getBoolean("available"),
                        rs.getString("categories")

                ));
            }
        }
        return books;
    }

    public boolean isBookAvailable(int bookId) throws SQLException {
        String sql = "SELECT available FROM books WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("available");
            }
            return false;
        }
    }

    public void updateAvailability(int bookId, boolean available) throws SQLException {
        String sql = "UPDATE books SET available = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, available);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
        }
    }

    public List<Book> searchBooksByAuthor(String author) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT books.*, GROUP_CONCAT(categories.name SEPARATOR ', ') AS categories " +
                "FROM books " +
                "LEFT JOIN book_categories ON books.id = book_categories.book_id " +
                "LEFT JOIN categories ON book_categories.category_id = categories.id " +
                "WHERE author LIKE ? " +
                "GROUP BY books.id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + author + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getBoolean("available"),
                        rs.getString("categories")

                ));
            }
        }
        return books;
    }
    public List<Book> searchBooksByCategory(String category) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT books.*, GROUP_CONCAT(categories.name SEPARATOR ', ') AS categories " +
                "FROM books " +
                "LEFT JOIN book_categories ON books.id = book_categories.book_id " +
                "LEFT JOIN categories ON book_categories.category_id = categories.id " +
                "WHERE categories.name LIKE ? " +
                "GROUP BY books.id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + category + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getBoolean("available"),
                        rs.getString("categories")

                ));
            }
        }
        return books;
    }

}
