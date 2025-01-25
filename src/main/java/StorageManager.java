import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.github.cdimascio.dotenv.Dotenv;

public class StorageManager extends Manager {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String url = dotenv.get("DB_URL");
    private static final String username = dotenv.get("DB_USERNAME");
    private static final String password = dotenv.get("DB_PASSWORD");
    private static String savedPass;
    private static String savedName;
    private static String savedTicketID;
    private static String savedUserID;

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }


    public void travelDataInsert(LocationManager locationManager) {
        locationManager.getTravelDistanceTime();
        String sql = "INSERT INTO trips (start_location, end_location, distance, duration, fare) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, locationManager.getStartingLocation());
            statement.setString(2, locationManager.getEndingLocation());
            statement.setString(3, locationManager.getDistance());
            statement.setString(4, locationManager.getDuration());
            statement.setDouble(5, locationManager.getTotalCost());

            statement.executeUpdate();
            System.out.println("Data inserted successfully!");
        } catch (SQLException e) {
            System.out.println("Oops Something Wrong!");
            e.printStackTrace();
        }
    }

    public void userDataInsert(String name, String email, String pass, String mobileNo) {
        String sql = "INSERT INTO Users (name, email, password, phone) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
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

    public void ticketSaving(String travelInfo) {
        String[] seperatedTravelInfo = travelInfo.split("-");
        String userName = seperatedTravelInfo[0];
        String startingLocation = seperatedTravelInfo[1];
        String endingLocation = seperatedTravelInfo[2];
        String distance = seperatedTravelInfo[3];
        String duration = seperatedTravelInfo[4];
        double totalFare = Double.parseDouble(seperatedTravelInfo[5]);

        String sql = "INSERT INTO Tickets (user_name, start_location, end_location, distance, duration, total_fare) VALUES (?,?,?,?,?,?);";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, userName);
            statement.setString(2, startingLocation);
            statement.setString(3, endingLocation);
            statement.setString(4, distance);
            statement.setString(5, duration);
            statement.setDouble(6, totalFare);

            statement.executeUpdate();
            System.out.println("Thank you! Enjoy the journey!");

        } catch (Exception e) {
            System.out.println(e);
        }
    }


    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    public String hashPassword(String plainPassword) {
        return bcrypt.encode(plainPassword);
    }

    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return bcrypt.matches(plainPassword, hashedPassword);
    }


    public String getPassFromTable(String email) {
        String sql = "SELECT password,name,user_id from Users where email=?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                savedPass = rs.getString(1);
                savedName = rs.getString(2);
                savedUserID = rs.getString(3);
            }
        } catch (Exception e) {
            System.out.println("Please Enter Correct Email!");
            e.printStackTrace();
        }
        return savedPass + " " + savedName + " " + savedUserID;
    }

    public String getTicketID(String name) {
        String sql = "SELECT ticket_id from Tickets where user_name=?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                savedTicketID = rs.getString(1);
            }

        } catch (Exception e) {
            System.out.println("Something Wrong!");
            e.printStackTrace();
        }
        return savedTicketID;
    }


    public void updateSeatsTable(String ticketID, String userID, int seatsToBook) {
        String sqlInsert = "INSERT INTO seats (ticket_id, user_id) VALUES (?, ?)";
        String sqlUpdate = "UPDATE seats SET availability = availability - ? WHERE ticket_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(sqlInsert);
             PreparedStatement updateStatement = connection.prepareStatement(sqlUpdate)) {

            insertStatement.setString(1, ticketID);
            insertStatement.setString(2, userID);
            insertStatement.executeUpdate();

            updateStatement.setInt(1, seatsToBook);
            updateStatement.setString(2, ticketID);
            updateStatement.executeUpdate();

            System.out.println("Seats table updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating seats table!");
            e.printStackTrace();
        }
    }


    public void cancelTicket(String ticketID, UserManager userManager) {
        String userID = getPassFromTable(userManager.getEmail()).split(" ")[2];
        String sqlDeleteSeats = "DELETE FROM seats WHERE ticket_id = ? AND user_id = ?";
        String sqlUpdateSeats = "UPDATE seats SET availability = availability + 1 WHERE ticket_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(sqlDeleteSeats);
             PreparedStatement updateStatement = connection.prepareStatement(sqlUpdateSeats)) {

            deleteStatement.setString(1, ticketID);
            deleteStatement.setString(2, userID);
            deleteStatement.executeUpdate();

            updateStatement.setString(1, ticketID);
            updateStatement.executeUpdate();

            System.out.println("Ticket cancelled successfully!");
        } catch (SQLException e) {
            System.out.println("Error cancelling ticket!");
            e.printStackTrace();
        }
    }

}
