package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUtility {
    private static final String URL = "jdbc:mysql://localhost:3306/uitestdb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static String[] getTestData() {
        String query = "SELECT username, password FROM TestData WHERE test_name = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "LoginTest");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new String[]{resultSet.getString("username"), resultSet.getString("password")};
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}