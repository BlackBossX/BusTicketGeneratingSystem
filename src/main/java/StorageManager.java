import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

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



    public void travelDataInsert(String s_location,String e_location,String Distance,String Duration,double cost){
        String url = "jdbc:mysql://localhost:3306/busticketgeneratingsystem";
        String username = "root";
        String password = "2419624196";

        String sql = "INSERT INTO trips (start_location, end_location, distance, duration, fare) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1,s_location);
            statement.setString(2, e_location);
            statement.setString(3, Distance);
            statement.setString(4, Duration);
            statement.setDouble(5,cost);

            statement.executeUpdate();

            System.out.println("Data inserted successfully!");
        } catch (Exception e) {
            System.out.println("Oops Something Wrong!");
            e.printStackTrace();
        }
    }

    public void userDataInsert(String name,String email,String pass,String mobileNo){
        String url = "jdbc:mysql://localhost:3306/busticketgeneratingsystem";
        String username = "root";
        String password = "2419624196";

        String sql = "INSERT INTO trips (name, email, password, phone) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1,name);
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



    public static String createMD5Hash(String input)
            throws NoSuchAlgorithmException {

        String hashText = null;
        MessageDigest md = MessageDigest.getInstance("MD5");

        // Compute message digest of the input
        byte[] messageDigest = md.digest(input.getBytes());

        hashText = convertToHex(messageDigest);

        return hashText;
    }

    private static String convertToHex(final byte[] messageDigest) {
        BigInteger bigint = new BigInteger(1, messageDigest);
        String hexText = bigint.toString(16);
        while (hexText.length() < 32) {
            hexText = "0".concat(hexText);
        }
        return hexText;
    }





}





