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


    public void generateQR() {
        try {
            String locationData = locInfo.getTravelDistanceTime();
            String ticketID = "TB10000";
            String[] splitLocation = locationData.split(",");
            String encodedURL = encodeURL(user.getUserName(), ticketID,
                    splitLocation[0], splitLocation[1], splitLocation[2], splitLocation[3]);
            String URL = QR_API_URL + encodedURL + QR_SIZE;
            System.out.println("Your Ticket QR code here: " + URL);
        } catch (Exception e) {
            System.out.println("Can't Generate QR!");
            System.out.println(" ");
            generateQR();
        }
    }
    // overloading
    public static String encodeURL(String fName, String tID,
                            String startLocation, String endLocation,
                            String distance, String duration) {
        String data = "Ticket ID: " + tID + "\n\n" +
                "Name: " + fName + "\n\n" +
                "Travel Route: " + startLocation + " -> " + endLocation + "\n" +
                "Distance: " + distance + "\n" +
                "Duration: " + duration;
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
