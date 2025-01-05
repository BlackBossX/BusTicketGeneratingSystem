import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class TicketGenerator {
    String ticketID;

    LocationManager locationInfo = new LocationManager();
    UserManager userInfo = new UserManager();


    public void generateQR() {

        try {
            String locationData = locationInfo.getTravelDistanceTime();

            String name1 = "Malan";
            String name2 = "Alwis";
            String ticketID = "TB10000";
            String[] splitLocation = locationData.split(" ");

            String encodedURL = encodeURL(name1, name2, ticketID,
                    splitLocation[0], splitLocation[1], splitLocation[2],
                    splitLocation[3], splitLocation[4], splitLocation[5]);


            String URL = "http://api.qrserver.com/v1/create-qr-code/?data=" + encodedURL + "&size=300x300";
            System.out.println("Your Ticket QR code here: " + URL);
        } catch (Exception e) {
            System.out.println("Can't Generate QR!");
            System.out.println(" ");
            generateQR();
        }


    }

    public String encodeURL(String fName, String lName, String tID,
                            String startLocation, String endLocation,
                            String distance, String unit, String duration, String unit2) {
        try {
            String data = "Ticket ID: " + tID + "\n\n" +
                    "Name: " + fName + lName + "\n\n" +
                    "Travel Route: " + startLocation + " -> " + endLocation + "\n" +
                    "Distance: " + distance + " " + unit + "\n" +
                    "Duration: " + duration + " " + unit2;

            // Encode the string for use in a URL
            String encodedData = URLEncoder.encode(data, "UTF-8");

            return encodedData;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }


}
