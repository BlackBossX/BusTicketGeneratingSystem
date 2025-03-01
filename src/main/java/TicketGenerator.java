import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import io.github.cdimascio.dotenv.Dotenv;

public class TicketGenerator {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String QR_API_URL = dotenv.get("QR_API_URL");
    private static final String QR_SIZE = "&size=300x300";
    private final LocationManager locInfo;
    private final UserManager user;
    private final StorageManager storage;

    // Constructor with dependencies
    public TicketGenerator(LocationManager locInfo, UserManager user, StorageManager storage) {
        this.locInfo = locInfo;
        this.user = user;
        this.storage = storage;
    }

    // Generate QR code with ticket details
    public void generateQR(String ticketId, int[] bookedSeats, double totalCost, String busId) {
        try {
            String locationData = locInfo.getTravelDistanceTime();
            if (locationData.isEmpty()) {
                UIManager.showError("Failed to fetch travel details for QR generation!");
                return;
            }

            String[] splitLocation = locationData.split(",");
            if (splitLocation.length < 5) {
                UIManager.showError("Invalid travel data format!");
                return;
            }

            String userName = user.getUserName();
            if (userName == null || userName.isEmpty()) {
                UIManager.showError("User not logged in! Cannot generate QR code.");
                return;
            }

            // Fetch current bus location
            String busLocation = locInfo.getBusLocation(busId);
            String busLocText = busLocation.isEmpty() ? "Not available" : "Lat " + locInfo.getBusLatitude() + ", Long " + locInfo.getBusLongitude();

            // Build seat numbers string
            StringBuilder seatsText = new StringBuilder();
            for (int seat : bookedSeats) {
                seatsText.append(seat).append(" ");
            }

            String encodedURL = encodeURL(userName, ticketId, splitLocation[0], splitLocation[1],
                    splitLocation[2], splitLocation[3], seatsText.toString().trim(),
                    totalCost, busLocText, busId);
            String qrUrl = QR_API_URL + encodedURL + QR_SIZE;
            UIManager.showSuccess("Your Ticket QR Code: " + qrUrl);

            // Optionally save QR URL to database
            saveQRUrl(ticketId, qrUrl);
        } catch (Exception e) {
            UIManager.showError("Error generating QR code: " + e.getMessage());
        }
    }

    // Overloaded method to encode ticket details
    public static String encodeURL(String fName, String tID, String startLocation, String endLocation,
                                   String distance, String duration, String seats, double totalCost,
                                   String busLocation, String busId) {
        String data = "Ticket ID: " + tID + "\n" +
                "Bus ID: " + busId + "\n" +
                "Name: " + fName + "\n" +
                "Travel Route: " + startLocation + " -> " + endLocation + "\n" +
                "Distance: " + distance + "\n" +
                "Duration: " + duration + "\n" +
                "Seats: " + seats + "\n" +
                "Total Cost: Rs. " + String.format("%.2f", totalCost) + "\n" +
                "Bus Location: " + busLocation;
        try {
            return URLEncoder.encode(data, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            UIManager.showError("Error encoding QR data: " + e.getMessage());
            return "";
        }
    }

    // Overloaded method for encoding a single location
    public static String encodeURL(String location) {
        try {
            return URLEncoder.encode(location, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            UIManager.showError("Error encoding location: " + e.getMessage());
            return "";
        }
    }

    // Save QR URL to database (optional)
    private void saveQRUrl(String ticketId, String qrUrl) {
        String sql = "UPDATE Tickets SET qr_url = ? WHERE ticket_id = ?";
        try (java.sql.Connection conn = DriverManager.getConnection(StorageManager.url, StorageManager.username, StorageManager.password);
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, qrUrl);
            stmt.setString(2, ticketId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("QR URL saved to ticket " + ticketId);
            }
        } catch (java.sql.SQLException e) {
            UIManager.showError("Error saving QR URL to database: " + e.getMessage());
        }
    }
}