import java.util.Scanner;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class LocationManager {

    private String startingLocation;
    private String endingLocation;
    private final Scanner input = new Scanner(System.in);

    public String getLocations() {
        System.out.print("Enter Starting Location: ");
        startingLocation = input.nextLine();
        System.out.print("Enter Ending Location: ");
        endingLocation = input.nextLine();
        return startingLocation + " " + endingLocation;
    }

    public String getTravelDistanceTime() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            getLocations();

            String apiKey = System.getenv("GOOGLE_MAPS_API_KEY");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://maps.googleapis.com/maps/api/distancematrix/json?" +
                            "origins=" + TicketGenerator.encodeURL(startingLocation) + "&" +
                            "destinations=" + TicketGenerator.encodeURL(endingLocation) + "&" +
                            "key=" + apiKey))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonObject = new JSONObject(response.body());

            JSONArray rowsArray = jsonObject.getJSONArray("rows");
            JSONObject elements0 = rowsArray.getJSONObject(0).getJSONArray("elements").getJSONObject(0);
            String distance = elements0.getJSONObject("distance").getString("text");
            String duration = elements0.getJSONObject("duration").getString("text");

            double numericalDistance = Double.parseDouble(distance.split(" ")[0]);
            double travelCost = (numericalDistance < 3) ? 27.00 + (numericalDistance * 3.093) : 35.00 + (numericalDistance * 3.093);

            System.out.println(startingLocation + " -> " + endingLocation);
            System.out.println("Distance: " + distance);
            System.out.println("Duration: " + duration);
            System.out.printf("Travel Cost: RS.%.2f\n", travelCost);

            return String.join(",", startingLocation, endingLocation, distance, duration, String.valueOf(travelCost));
        } catch (Exception e) {
            System.out.println("Type Locations Correctly!");
        }
        return "";
    }
}
