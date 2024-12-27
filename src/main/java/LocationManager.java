import java.util.Scanner;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;



public class LocationManager {

    int busRouteID;
    String startingLocation;
    String endingLocation;
    double travelDistance;
    double travelCost;

    Scanner input = new Scanner(System.in);
    // LocationManager location = new LocationManager();


    public String gettingLocations() {
        System.out.print("Enter Bus Route ID: ");
        int routeID = input.nextInt();
        input.nextLine();

        System.out.print("Enter Starting Location: ");
        String startingPoint = input.nextLine();
        System.out.print("Enter Ending Location: ");
        String endingPoint = input.nextLine();
        return (startingPoint + " " + endingPoint);

        // calculateTravelDistance_Time(startingPoint,endingPoint);

    }


    public void getTravelDistanceTime() {
        try {
            // Create HttpClient
            HttpClient client = HttpClient.newHttpClient();

            String[] n = gettingLocations().split(" ");

            // Build the GET request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://maps.googleapis.com/maps/api/distancematrix/json?" +
                            "origins=" + n[0] + "&" +
                            "destinations=" + n[1] + "&" +
                            "key=AIzaSyBCQYvM4XOxbImK4pxXZqK8tSawEemlh1Q"))
                    .GET()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Print the response
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
