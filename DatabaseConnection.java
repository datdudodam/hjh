package giaoDien;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/QLTV?useSSL=false&allowPublicKeyRetrieval=true"; // Thay bằng tên CSDL của bạn
    private static final String USER = "dat"; // Thay bằng tên người dùng
    private static final String PASSWORD = "ntdat1610c"; // Thay bằng mật khẩu

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
