import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StorageManager {

    public void connectionSetup(){
        String url = "jdbc:mysql://localhost:3306/busticketgeneratingsystem";
        String username = "root";
        String password = "2419624196";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connected to the database!");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database!");
            e.printStackTrace();
        }
    }

    public void dataInsert(){
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
            System.out.println("Oops Something Wrong!");
            e.printStackTrace();
        }
    }

}





