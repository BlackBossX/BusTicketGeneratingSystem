import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import io.github.cdimascio.dotenv.Dotenv;

public class TicketGenerator {
    static Dotenv dotenv = Dotenv.load();
    private static final String QR_API_URL = dotenv.get("QR_API_URL");
    private static final String QR_SIZE = "&size=300x300";
    private final LocationManager locInfo;
    private final UserManager user;

    TicketGenerator() {
        locInfo = new LocationManager();
        user = new UserManager();
    }


    public void generateQR(String userName,String startingLocation, String endingLocation, String distance, String duration, double totalFare) {
        try {
            String ticketID = StorageManager.getTicketID(userName);
            String encodedURL = encodeURL(userName, ticketID,
                    startingLocation, endingLocation, distance, duration,totalFare);
            String URL = QR_API_URL + encodedURL + QR_SIZE;
            System.out.println("Your Ticket QR code here: " + URL);
        } catch (Exception e) {
            System.out.println("Can't Generate QR!");
            System.out.println(" ");
        }
    }
    // overloading
    public static String encodeURL(String fName, String tID,
                            String startLocation, String endLocation,
                            String distance, String duration,double totalFare) {
        String data = "Ticket ID: " + tID + "\n\n" +
                "Name: " + fName + "\n\n" +
                "Travel Route: " + startLocation + " -> " + endLocation + "\n" +
                "Distance: " + distance + "\n" +
                "Duration: " + duration + "\n" +
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
