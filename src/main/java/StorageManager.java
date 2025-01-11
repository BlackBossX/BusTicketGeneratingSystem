import java.sql.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class StorageManager {
    final static String url = "jdbc:mysql://localhost:3306/busticketgeneratingsystem";
    final static String username = "root";
    final static String password = "2419624196";

    public void connectionSetup() {
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


    public void travelDataInsert(String s_location, String e_location,
                                 String Distance, String Duration, double cost) {

        String sql = "INSERT INTO trips (start_location, end_location, distance, duration, fare) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, s_location);
            statement.setString(2, e_location);
            statement.setString(3, Distance);
            statement.setString(4, Duration);
            statement.setDouble(5, cost);

            statement.executeUpdate();

            System.out.println("Data inserted successfully!");
        } catch (Exception e) {
            System.out.println("Oops Something Wrong!");
            e.printStackTrace();
        }
    }

    public static void userDataInsert(String name, String email, String pass, String mobileNo) {

        String sql = "INSERT INTO Users (name, email, password, phone) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, pass);
            statement.setString(4, mobileNo);

            statement.executeUpdate();

            System.out.println("Data inserted successfully!");
        } catch (Exception e) {
            System.out.println("Oops Something Wrong!");
            e.printStackTrace();
        }
    }


    private static final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    // Hash the password this make 60 characters hash , so I made password column size for 60 as well
    public static String hashPassword(String plainPassword) {
        return bcrypt.encode(plainPassword);
    }

    // verify the password
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return bcrypt.matches(plainPassword, hashedPassword);
    }



    static String savedPass;
    static String savedName;
    public static String getPassFromTable(String email){
        String sql = "SELECT password,name from Users where email=?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            ResultSet rs= statement.executeQuery();

            while (rs.next()){
                savedPass = rs.getString(1);
                savedName = rs.getString(2);
            }


        } catch (Exception e) {
            System.out.println("Please Enter Correct Email!");
            e.printStackTrace();
        }

        return savedPass+" "+savedName;

    }


}





