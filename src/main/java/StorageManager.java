import java.sql.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import io.github.cdimascio.dotenv.Dotenv;

public class StorageManager {
    private static final Dotenv dotenv = Dotenv.load();
    public static final String url = dotenv.get("DB_URL");
    public static final String username = dotnet.get("DB_USERNAME");
    public static final String password = dotenv.get("DB_PASSWORD");
    private static String savedPass;
    private static String savedName;
    private static String savedTicketId;
    private final LocationManager storage;
    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    public StorageManager() {
        storage = new LocationManager(); // Composition
    }

    // Test database connection
    public void connectionSetup() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            UIManager.showSuccess("Connected to the database!");
        } catch (SQLException e) {
            UIManager.showError("Failed to connect to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Insert travel data into Trips table
    public void travelDataInsert() {
        String travelData = storage.getTravelDistanceTime();
        if (travelData.isEmpty()) {
            return;
        }
        String sql = "INSERT INTO trips (start_location, end_location, distance, duration, fare) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            String[] details = travelData.split(",");
            statement.setString(1, details[0]); // start_location
            statement.setString(2, details[1]); // end_location
            statement.setString(3, details[2]); // distance
            statement.setString(4, details[3]); // duration
            statement.setDouble(5, Double.parseDouble(details[4])); // fare

            statement.executeUpdate();
            UIManager.showSuccess("Travel data inserted successfully!");
        } catch (SQLException e) {
            UIManager.showError("Error inserting travel data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Insert user data into Users table
    public void userDataInsert(String name, String email, String pass, String mobileNo) {
        String sql = "INSERT INTO Users (name, email, password, phone) VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE name = VALUES(name), password = VALUES(password), phone = VALUES(phone)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, pass);
            statement.setString(4, mobileNo);

            statement.executeUpdate();
            UIManager.showSuccess("User data saved successfully for " + email);
        } catch (SQLException e) {
            UIManager.showError("Error inserting user data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Insert ticket booking data into Tickets table
    public String ticketBooking(String userName) {
        String travelData = storage.getTravelDistanceTime();
        if (travelData.isEmpty()) {
            return "";
        }
        String[] splitData = travelData.split(",");
        String sql = "INSERT INTO Tickets (user_name, start_location, end_location, distance, duration, total_fare) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, userName);
            statement.setString(2, splitData[0]); // start_location
            statement.setString(3, splitData[1]); // end_location
            statement.setString(4, splitData[2]); // distance
            statement.setString(5, splitData[3]); // duration
            statement.setDouble(6, Double.parseDouble(splitData[4])); // total_fare

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                savedTicketId = "TB" + rs.getInt(1); // Assuming auto-incremented ID
                UIManager.showSuccess("Ticket booked successfully! Ticket ID: " + savedTicketId);
                return savedTicketId;
            }
            return "";
        } catch (SQLException e) {
            UIManager.showError("Error booking ticket: " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    // Store bus GPS location
    public void updateBusLocation(String busId, String latitude, String longitude) {
        String sql = "INSERT INTO BusLocations (bus_id, latitude, longitude, timestamp) " +
                "VALUES (?, ?, ?, CURRENT_TIMESTAMP) " +
                "ON DUPLICATE KEY UPDATE latitude = VALUES(latitude), longitude = VALUES(longitude), " +
                "timestamp = CURRENT_TIMESTAMP";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, busId);
            statement.setString(2, latitude);
            statement.setString(3, longitude);

            statement.executeUpdate();
            UIManager.showSuccess("Bus " + busId + " location updated successfully!");
        } catch (SQLException e) {
            UIManager.showError("Error updating bus location: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Assign seats to a bus and ticket
    public void assignSeatsToBus(String ticketId, String busId, int[] seatNumbers) {
        String sql = "INSERT INTO Seats (ticket_id, bus_id, seat_number) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int seat : seatNumbers) {
                statement.setString(1, ticketId);
                statement.setString(2, busId);
                statement.setInt(3, seat);
                statement.addBatch();
            }
            statement.executeBatch();
            UIManager.showSuccess("Seats assigned to bus " + busId + " for ticket " + ticketId);
        } catch (SQLException e) {
            UIManager.showError("Error assigning seats: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Fetch ticket details
    public String getTicketDetails(String ticketId) {
        String sql = "SELECT user_name, start_location, end_location, distance, duration, total_fare " +
                "FROM Tickets WHERE ticket_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, ticketId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String details = rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3) + "," +
                        rs.getString(4) + "," + rs.getString(5) + "," + rs.getDouble(6);
                return details;
            }
            UIManager.showError("Ticket " + ticketId + " not found!");
            return "";
        } catch (SQLException e) {
            UIManager.showError("Error fetching ticket details: " + e.getMessage());
            return "";
        }
    }

    // Cancel a ticket
    public void cancelTicket(String ticketId) {
        String sqlDeleteSeats = "DELETE FROM Seats WHERE ticket_id = ?";
        String sqlDeleteTicket = "DELETE FROM Tickets WHERE ticket_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement deleteSeats = connection.prepareStatement(sqlDeleteSeats);
             PreparedStatement deleteTicket = connection.prepareStatement(sqlDeleteTicket)) {

            deleteSeats.setString(1, ticketId);
            deleteTicket.setString(1, ticketId);

            deleteSeats.executeUpdate();
            int rowsAffected = deleteTicket.executeUpdate();
            if (rowsAffected > 0) {
                UIManager.showSuccess("Ticket " + ticketId + " canceled successfully!");
            } else {
                UIManager.showError("Ticket " + ticketId + " not found!");
            }
        } catch (SQLException e) {
            UIManager.showError("Error canceling ticket: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Password hashing
    public String hashPassword(String plainPassword) {
        return bcrypt.encode(plainPassword);
    }

    // Verify password
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return bcrypt.matches(plainPassword, hashedPassword);
    }

    // Fetch user credentials
    public String getPassFromTable(String email) {
        String sql = "SELECT password, name FROM Users WHERE email = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                savedPass = rs.getString(1);
                savedName = rs.getString(2);
                return savedPass + " " + savedName;
            }
            UIManager.showError("No user found with email: " + email);
            return "";
        } catch (SQLException e) {
            UIManager.showError("Error fetching user data: " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    // Get ticket ID for a user
    public String getTicketId(String userName) {
        String sql = "SELECT ticket_id FROM Tickets WHERE user_name = ? ORDER BY created_at DESC LIMIT 1";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userName);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                savedTicketId = rs.getString(1);
                return savedTicketId;
            }
            return "";
        } catch (SQLException e) {
            UIManager.showError("Error fetching ticket ID: " + e.getMessage());
            return "";
        }
    }
}