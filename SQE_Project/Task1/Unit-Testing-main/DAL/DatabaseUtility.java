package DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtility {

    private static final String URL = "jdbc:mysql://localhost:3306/scddb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    
    private static Connection customConnection = null;
    
    public static void setConnection(Connection connection) {
        customConnection = connection;
    }
}