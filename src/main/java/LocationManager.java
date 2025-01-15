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

    private static final double AVG_COST_PER_KM = 3.093;
    private static final String API_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?";

    private final Scanner input = new Scanner(System.in);   //encapsulation
    Dotenv dotenv = Dotenv.load();

    public void gettingLocations() {
        System.out.print("Enter Starting Location: ");
        startingLocation = input.nextLine();
        System.out.print("Enter Ending Location: ");
        endingLocation = input.nextLine();
    }

    public String getTravelDistanceTime() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String apiKey = dotenv.get("GOOGLE_MAPS_API_KEY");
            gettingLocations();
            String sLoc = TicketGenerator.encodeURL(startingLocation);
            String eLoc = TicketGenerator.encodeURL(endingLocation);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(API_URL + "origins=" + sLoc + "&" +
                            "destinations=" + eLoc + "&" + "key=" + apiKey))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonObject = new JSONObject(response.body());
            //System.out.println("Response Body: " + response.body());

            // starting with [] mean its JSONArray and starting with {} mean its JSONObject
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

            System.out.println(startingLocation + " -> " + endingLocation);
            System.out.println("Distance: " + distance);
            System.out.println("Duration: " + duration);
            System.out.printf("Travel Cost: RS.%.2f\n", tCost);

            return startingLocation + "," + endingLocation + "," + distance + "," + duration + "," + tCost;
        } catch (Exception e) {
            System.out.println("Type Locations Correctly!");
        }
        return "";
    }

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
}
