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





    public void userDataInsert(int userID,String name,String email,String pass,String mobile){
        String url = "jdbc:mysql://localhost:3306/busticketgeneratingsystem";
        String username = "root";
        String password = "2419624196";

        // Generate the next user ID
        String prefix = "U";
        String customUserId = prefix + String.format("%04d", userID); // Output: U0001, U0002, ...


        String sql = "INSERT INTO users (user_id, name, email, password, phone) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1,customUserId);
            statement.setString(2, name);
            statement.setString(3, email);
            statement.setString(4, pass);
            statement.setString(5, mobile);

            statement.executeUpdate();

            System.out.println("Data inserted successfully!");
        } catch (Exception e) {
            System.out.println("Oops Something Wrong!");
            e.printStackTrace();
        }
    }



    public void travelDataInsert(int travelID,String s_location,String e_location,String Distance,double cost){
        String url = "jdbc:mysql://localhost:3306/busticketgeneratingsystem";
        String username = "root";
        String password = "2419624196";

        // Generate the next user ID
        String prefix = "R";
        String customTravelId = prefix + String.format("%04d", travelID); // Output: U0001, U0002, ...


        String sql = "INSERT INTO trips (trip_id, start_location, end_location, travel_distance, travel_cost) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1,customTravelId);
            statement.setString(2, s_location);
            statement.setString(3, e_location);
            statement.setString(4, Distance);
            statement.setDouble(5,cost);

            statement.executeUpdate();

            System.out.println("Data inserted successfully!");
        } catch (Exception e) {
            System.out.println("Oops Something Wrong!");
            e.printStackTrace();
        }
    }





}





