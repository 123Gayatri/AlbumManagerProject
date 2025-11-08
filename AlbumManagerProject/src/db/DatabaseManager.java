package db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    // IMPORTANT: Make sure these three values match your MySQL server configuration!
    private static final String DB_URL = "jdbc:mysql://localhost:3306/album_manager";
    private static final String DB_USER = "root";       // <-- CHECK THIS
    private static final String DB_PASSWORD = "root";   // <-- CHECK THIS

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Ensure the driver is loaded correctly
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found! Make sure the JAR is in your libraries.", e);
            }
            // If the connection fails here, the application will show a database error.
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        }
        return connection;
    }
}
