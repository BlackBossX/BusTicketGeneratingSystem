import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class StorageManager {
    public static void main(String []args) {
        String url = "jdbc:mysql://localhost:3306/busticketgeneratingsystem";
        String username = "root";
        String password = "2419624196";


        String sql = "INSERT INTO users (name, email, password, phone) VALUES (?, ?, ?, ?)";;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "Malan De Alwis");
            statement.setString(2, "malandealwis@gmail.com");
            statement.setString(3, "12345678");
            statement.setString(4, "0757292396");

            statement.executeUpdate();

            System.out.println("Data inserted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

