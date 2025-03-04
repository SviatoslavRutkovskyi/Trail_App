import java.sql.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        String url = "jdbc:mysql://localhost:3306/project10_db";
        String username = "root";
        String password = "1231";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Database connected!");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from trail");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("trail_id") + "\t"
                        + resultSet.getString("trail_name") + "\t"
                        + resultSet.getString("trail_length") + "\t"
                        + resultSet.getString("trail_difficulty") + "\t"
                        + resultSet.getString("trail_rating") + "\t"
                        + resultSet.getString("trail_estimated_time") + "\t");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
}