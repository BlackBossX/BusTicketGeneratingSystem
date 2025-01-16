import java.sql.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StorageManager {
    static Dotenv dotenv = Dotenv.load();
    private final static String url = dotenv.get("DB_URL");
    private final static String username = dotenv.get("DB_USERNAME");
    private final static String password = dotenv.get("DB_PASSWORD");
    private static String savedPass;
    private static String savedName;
    private final LocationManager storage;

    private static final Logger logger = Logger.getLogger(StorageManager.class.getName());

    public StorageManager() {
        storage = new LocationManager();
    } // composition

    public void connectionSetup() {
        if (url == null || username == null || password == null) {
            logger.log(Level.SEVERE, "Database connection details are missing in the .env file.");
            return;
        }
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            if (connection != null) {
                logger.log(Level.INFO, "Connected to the database successfully!");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to connect to the database.", e);
        }
    }

    public void travelDataInsert() {
        if (url == null || username == null || password == null) {
            logger.log(Level.SEVERE, "Database connection details are missing in the .env file.");
            return;
        }
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
            logger.log(Level.INFO, "Data inserted successfully!");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to insert travel data.", e);
        }
    }

    public void userDataInsert(String name, String email, String pass, String mobileNo) {
        if (url == null || username == null || password == null) {
            logger.log(Level.SEVERE, "Database connection details are missing in the .env file.");
            return;
        }
        String sql = "INSERT INTO Users (name, email, password, phone) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, pass);
            statement.setString(4, mobileNo);

            statement.executeUpdate();
            logger.log(Level.INFO, "Registration Successful!");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to insert user data.", e);
        }
    }

    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    // Hash the password this makes a 60-character hash, so the password column size is set for 60 as well
    public String hashPassword(String plainPassword) {
        return bcrypt.encode(plainPassword);
    }

    // Verify the password
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return bcrypt.matches(plainPassword, hashedPassword);
    }

    public String getPassFromTable(String email) {
        if (url == null || username == null || password == null) {
            logger.log(Level.SEVERE, "Database connection details are missing in the .env file.");
            return null;
        }
        String sql = "SELECT password, name FROM Users WHERE email = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                savedPass = rs.getString(1);
                savedName = rs.getString(2);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to retrieve user data for email: " + email, e);
        }
        return savedPass + " " + savedName;
    }
}
