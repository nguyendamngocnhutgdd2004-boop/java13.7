import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings({"SqlNoDataSourceInspection", "SqlDialectInspection", "unused"})
public class BookManager {
    private static final String URL = "jdbc:mysql://localhost:3306/library_db?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "root"; // Đổi thành mật khẩu MySQL của bạn

    public void addBook(Book book) {
        String checkSql = "SELECT COUNT(*) FROM Book WHERE title = ? AND author = ?";
        String insertSql = "INSERT INTO Book (title, author, published_year, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, book.getTitle());
            checkStmt.setString(2, book.getAuthor());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.err.println("Thất bại: Sách này đã tồn tại trong thư viện (Trùng tên và tác giả)!");
                    return;
                }
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, book.getTitle());
                insertStmt.setString(2, book.getAuthor());
                insertStmt.setInt(3, book.getPublishedYear());
                insertStmt.setDouble(4, book.getPrice());
                insertStmt.executeUpdate();
                System.err.println("Thêm sách thành công!");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }

    public void updateBook(int id, Book book) {
        String checkSql = "SELECT COUNT(*) FROM Book WHERE id = ?";
        String updateSql = "UPDATE Book SET title = ?, author = ?, published_year = ?, price = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, id);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    System.err.println("Lỗi: Không tìm thấy sách có mã ID = " + id);
                    return;
                }
            }

            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setString(1, book.getTitle());
                updateStmt.setString(2, book.getAuthor());
                updateStmt.setInt(3, book.getPublishedYear());
                updateStmt.setDouble(4, book.getPrice());
                updateStmt.setInt(5, id);
                updateStmt.executeUpdate();
                System.err.println("Cập nhật thông tin sách thành công!");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }

    public void deleteBook(int id) {
        String checkSql = "SELECT COUNT(*) FROM Book WHERE id = ?";
        String deleteSql = "DELETE FROM Book WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, id);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    System.err.println("Lỗi: Không tìm thấy sách có mã ID = " + id + " để xóa!");
                    return;
                }
            }

            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, id);
                deleteStmt.executeUpdate();
                System.err.println("Xóa sách thành công!");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }

    public void findBooksByAuthor(String author) {
        String sql = "SELECT id, title, author, published_year, price FROM Book WHERE author LIKE ?";
        System.err.println("\n--- KẾT QUẢ TÌM KIẾM ---");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + author + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    Book b = new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getInt("published_year"),
                            rs.getDouble("price")
                    );
                    System.err.println(b);
                }
                if (!found) {
                    System.err.println("Không tìm thấy cuốn sách nào của tác giả: " + author);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }

    public void listAllBooks() {
        String sql = "SELECT id, title, author, published_year, price FROM Book";
        System.err.println("\n--- DANH SÁCH TẤT CẢ SÁCH TRONG THƯ VIỆN ---");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                Book b = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("published_year"),
                        rs.getDouble("price")
                );
                System.err.println(b);
            }
            if (!hasData) {
                System.err.println("Thư viện hiện tại chưa có sách nào!");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }
}
