import java.sql.*;

public class DatabaseConnection {

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");

        String dbUrl = "jdbc:postgresql://localhost:5432/ecommerce_db";
        String username = "postgres";
        String password = "ENTER_YOUR_PASSWORD_HERE";

        return DriverManager.getConnection(dbUrl, username, password);
    }
}