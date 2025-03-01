import java.util.Scanner;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import io.github.cdimascio.dotenv.Dotenv;

public class LocationManager {
    private String startingLocation;
    private String endingLocation;
    private String duration;
    private String distance;
    private double tCost;
    private String busLatitude;  // Bus's current latitude
    private String busLongitude; // Bus's current longitude

    private static final double AVG_COST_PER_KM = 3.093;
    private static final String DISTANCE_MATRIX_API_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?";
    private static final String GPS_API_URL = "http://your-gps-server/api/location"; // Replace with your GPS endpoint

    private final Scanner input = new Scanner(System.in); // Encapsulation
    private final Dotenv dotenv = Dotenv.load();

    // Existing method for user input
    public void gettingLocations() {
        System.out.print("Enter Starting Location: ");
        startingLocation = input.nextLine();
        System.out.print("Enter Ending Location: ");
        endingLocation = input.nextLine();
    }

    // Calculate distance and cost between two locations
    public String getTravelDistanceTime() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String apiKey = dotenv.get("GOOGLE_MAPS_API_KEY");
            gettingLocations();
            String sLoc = TicketGenerator.encodeURL(startingLocation);
            String eLoc = TicketGenerator.encodeURL(endingLocation);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(DISTANCE_MATRIX_API_URL + "origins=" + sLoc + "&destinations=" + eLoc + "&key=" + apiKey))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonObject = new JSONObject(response.body());

            JSONArray rowsArray = jsonObject.getJSONArray("rows");
            JSONObject elements0 = rowsArray.getJSONObject(0).getJSONArray("elements").getJSONObject(0);
            distance = elements0.getJSONObject("distance").getString("text");
            duration = elements0.getJSONObject("duration").getString("text");

            double numericalDistance = Double.parseDouble(distance.split(" ")[0]);
            if (numericalDistance < 3) {
                tCost = 27.00 + (numericalDistance * AVG_COST_PER_KM);
            } else {
                tCost = 35.00 + (numericalDistance * AVG_COST_PER_KM);
            }

            System.out.println("\n" + startingLocation + " -> " + endingLocation);
            System.out.println("Distance: " + distance);
            System.out.println("Duration: " + duration);
            System.out.printf("Travel Cost: Rs. %.2f\n", tCost);

            return startingLocation + "," + endingLocation + "," + distance + "," + duration + "," + tCost;
        } catch (Exception e) {
            UIManager.showError("Failed to fetch travel details. Please ensure locations are typed correctly.");
            return "";
        }
    }

    // Fetch real-time GPS data for a bus via HTTP API
    public String getBusLocation(String busId) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(GPS_API_URL + "?busId=" + busId))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonObject = new JSONObject(response.body());

            busLatitude = jsonObject.getString("latitude");
            busLongitude = jsonObject.getString("longitude");

            UIManager.showBusLocation(busId, busLatitude, busLongitude);
            return busLatitude + "," + busLongitude;
        } catch (Exception e) {
            UIManager.showError("Error fetching bus location for " + busId + ". Check GPS server or bus ID.");
            return "";
        }
    }

    // Calculate ETA from bus's current location to passenger's location
    public String getETA(String busId, String passengerLocation) {
        try {
            String busLocation = getBusLocation(busId);
            if (busLocation.isEmpty()) {
                return "";
            }

            HttpClient client = HttpClient.newHttpClient();
            String apiKey = dotenv.get("GOOGLE_MAPS_API_KEY");
            String busLoc = TicketGenerator.encodeURL(busLatitude + "," + busLongitude);
            String passLoc = TicketGenerator.encodeURL(passengerLocation);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(DISTANCE_MATRIX_API_URL + "origins=" + busLoc + "&destinations=" + passLoc + "&key=" + apiKey))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonObject = new JSONObject(response.body());

            JSONArray rowsArray = jsonObject.getJSONArray("rows");
            JSONObject elements0 = rowsArray.getJSONObject(0).getJSONArray("elements").getJSONObject(0);
            String etaDuration = elements0.getJSONObject("duration").getString("text");

            return etaDuration;
        } catch (Exception e) {
            UIManager.showError("Error calculating ETA for bus " + busId + " to " + passengerLocation + "!");
            return "";
        }
    }

    // Update bus location from IoTCommunicator (e.g., MQTT)
    public void updateBusLocationFromIoT(String busId, String latitude, String longitude) {
        this.busLatitude = latitude;
        this.busLongitude = longitude;
        UIManager.showBusLocation(busId, latitude, longitude);
        // Optionally notify StorageManager to update the database here if not handled by IoTCommunicator
    }

    // Getters
    public String getStartingLocation() {
        return startingLocation;
    }

    public String getEndingLocation() {
        return endingLocation;
    }

    public String getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }

    public double getTotalCost() {
        return tCost;
    }

    public String getBusLatitude() {
        return busLatitude;
    }

    public String getBusLongitude() {
        return busLongitude;
    }
}