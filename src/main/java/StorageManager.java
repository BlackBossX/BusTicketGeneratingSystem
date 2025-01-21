import java.sql.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import io.github.cdimascio.dotenv.Dotenv;

public class StorageManager{
    static Dotenv dotenv = Dotenv.load();
    private final static String url = dotenv.get("DB_URL");
    private final static String username = dotenv.get("DB_USERNAME");
    private final static String password = dotenv.get("DB_PASSWORD");
    private static String savedPass;
    private static String savedName;
    private final LocationManager storage;

    public StorageManager(){
        storage = new LocationManager();
    }   //composition

    public void connectionSetup() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connected to the database!");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database!");
            e.printStackTrace();
        }
    }

    public void travelDataInsert() {
        storage.getTravelDistanceTime();
        String sql = "INSERT INTO trips (start_location, end_location, distance, duration, fare) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, storage.getStartingLocation());
            statement.setString(2, storage.getEndingLocation());
            statement.setString(3, storage.getDistance());
            statement.setString(4, storage.getDuration());
            statement.setDouble(5, storage.getTotalCost());

            statement.executeUpdate();
            System.out.println(storage.getStartingLocation() + " -> " + storage.getEndingLocation());
            System.out.println("Distance: " + storage.getDistance());
            System.out.println("Duration: " + storage.getDuration());
            System.out.printf("Travel Cost: RS.%.2f\n", storage.getTotalCost());
            System.out.println("Data inserted successfully!");
        } catch (SQLException e) {
            System.out.println("Oops Something Wrong!");
            e.printStackTrace();
        }
    }

    public void userDataInsert(String name, String email, String pass, String mobileNo) {
        String sql = "INSERT INTO Users (name, email, password, phone) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, pass);
            statement.setString(4, mobileNo);

            statement.executeUpdate();
            System.out.println("Registration Successful!");
        } catch (Exception e) {
            System.out.println("Oops Something Wrong!");
            e.printStackTrace();
        }
    }

    public void ticketBooking(String user_name,String start_location,String end_location,String distance,String duration){
        String sql = "INSERT INTO Tickets (name, email, password, phone) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {


        }catch (Exception e){
            System.out.println("Oops Something Wrong!");
        }
    }

    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    // Hash the password this make 60 characters hash , so I made password column size for 60 as well
    public String hashPassword(String plainPassword) {
        return bcrypt.encode(plainPassword);
    }

    // verify the password
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return bcrypt.matches(plainPassword, hashedPassword);
    }

    public String getPassFromTable(String email) {
        String sql = "SELECT password,name from Users where email=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                savedPass = rs.getString(1);
                savedName = rs.getString(2);
            }
        } catch (Exception e) {
            System.out.println("Please Enter Correct Email!");
            e.printStackTrace();
        }
        return savedPass + " " + savedName;
    }
}