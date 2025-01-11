import java.sql.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class StorageManager {
    private static final String URL = "jdbc:mysql://localhost:3306/busticketgeneratingsystem";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "2419624196";
    private static final BCryptPasswordEncoder BCRYPT = new BCryptPasswordEncoder();

    public void connectionSetup() {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            System.out.println("Connected to the database!");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database!");
            e.printStackTrace();
        }
    }

    public void travelDataInsert(String s_location, String e_location, String distance, String duration, double cost) {
        String sql = "INSERT INTO trips (start_location, end_location, distance, duration, fare) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, s_location);
            statement.setString(2, e_location);
            statement.setString(3, distance);
            statement.setString(4, duration);
            statement.setDouble(5, cost);

            statement.executeUpdate();
            System.out.println("Data inserted successfully!");
        } catch (SQLException e) {
            System.out.println("Oops Something Wrong!");
            e.printStackTrace();
        }
    }

    public static void userDataInsert(String name, String email, String pass, String mobileNo) {
        String sql = "INSERT INTO Users (name, email, password, phone) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, pass);
            statement.setString(4, mobileNo);

            statement.executeUpdate();
            System.out.println("Data inserted successfully!");
        } catch (SQLException e) {
            System.out.println("Oops Something Wrong!");
            e.printStackTrace();
        }
    }

    public static String hashPassword(String plainPassword) {
        return BCRYPT.encode(plainPassword);
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCRYPT.matches(plainPassword, hashedPassword);
    }

    public static String getPassFromTable(String email) {
        String sql = "SELECT password, name FROM Users WHERE email = ?";
        String savedPass = null;
        String savedName = null;

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                savedPass = rs.getString("password");
                savedName = rs.getString("name");
            }
        } catch (SQLException e) {
            System.out.println("Please Enter Correct Email!");
            e.printStackTrace();
        }

        return savedPass + " " + savedName;
    }
}
