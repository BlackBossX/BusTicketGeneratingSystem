import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import io.github.cdimascio.dotenv.Dotenv;
import org.jetbrains.annotations.NotNull;

public class TicketGenerator {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String QR_API_URL = dotenv.get("QR_API_URL");
    private static final String QR_SIZE = "&size=300x300";
    private final LocationManager locInfo;
    private final UserManager user;
    private final StorageManager storage;

    public TicketGenerator(LocationManager locInfo, UserManager user,StorageManager storage) {
        this.locInfo = locInfo;
        this.user = user;
        this.storage =storage;
    }

    public void generateQR(@NotNull String travelInfo) {

        String[] seperatedTravelInfo = travelInfo.split("-");

        String userName = seperatedTravelInfo[0];
        String startingLocation = seperatedTravelInfo[1];
        String endingLocation = seperatedTravelInfo[2];
        String distance = seperatedTravelInfo[3];
        String duration = seperatedTravelInfo[4];
        double totalFare = Double.parseDouble(seperatedTravelInfo[5]);
        String seatsCount = seperatedTravelInfo[6];

        try {
            String ticketID = storage.getTicketID(userName);
            String encodedURL = encodeURL(userName, ticketID,
                    startingLocation, endingLocation, distance, duration, totalFare, seatsCount);
            String URL = QR_API_URL + encodedURL + QR_SIZE;
            System.out.println("Your Ticket QR code here: " + URL);
        } catch (Exception e) {
            System.out.println("Can't Generate QR!");
            System.out.println(" ");
        }
    }

    public static String encodeURL(String fName, String tID,
                                   String startLocation, String endLocation,
                                   String distance, String duration, double totalFare, String seatsCount) {
        String data = "Ticket ID: " + tID + "\n\n" +
                "Name: " + fName + "\n\n" +
                "Travel Route: " + startLocation + " -> " + endLocation + "\n" +
                "Distance: " + distance + "\n" +
                "Duration: " + duration + "\n" +
                "Seat Count: " + seatsCount + "\n" +
                "Total Fare: " + totalFare;

        return URLEncoder.encode(data, StandardCharsets.UTF_8);
    }

    public static String encodeURL(String location) {
        try {
            return URLEncoder.encode(location, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}