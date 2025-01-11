import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class TicketGenerator {
    private static final String QR_API_URL = "http://api.qrserver.com/v1/create-qr-code/?data=";
    private static final String QR_SIZE = "&size=300x300";

    private LocationManager locationInfo = new LocationManager();
    private UserManager userInfo = new UserManager();

    public void generateQR() {
        try {
            String locationData = locationInfo.getTravelDistanceTime();
            String[] splitLocation = locationData.split(",");

            String encodedURL = encodeURL("Malan", "Alwis", "TB10000",
                    splitLocation[0], splitLocation[1], splitLocation[2], splitLocation[3]);

            String qrCodeURL = QR_API_URL + encodedURL + QR_SIZE;
            System.out.println("Your Ticket QR code here: " + qrCodeURL);
        } catch (Exception e) {
            System.out.println("Can't Generate QR!");
            e.printStackTrace();
        }
    }

    private String encodeURL(String fName, String lName, String tID,
                             String startLocation, String endLocation,
                             String distance, String duration) {
        try {
            String data = String.format("Ticket ID: %s\n\nName: %s %s\n\nTravel Route: %s -> %s\nDistance: %s\nDuration: %s",
                    tID, fName, lName, startLocation, endLocation, distance, duration);

            return URLEncoder.encode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
