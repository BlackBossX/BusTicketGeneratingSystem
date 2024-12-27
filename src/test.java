/*
import java.util.Scanner;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LocationManager {

    int busRouteID;
    String startingLocation;
    String endingLocation;
    double travelDistance;
    double travelCost;

    Scanner input = new Scanner(System.in);

    // Method to get locations and pass them to calculateTravelDistance_Time
    public void gettingLocationsAndCalculate() {
        System.out.print("Enter Bus Route ID: ");
        int routeID = input.nextInt();
        input.nextLine();  // Consume the newline character left by nextInt()

        System.out.print("Enter Starting Location: ");
        String startingPoint = input.nextLine();

        System.out.print("Enter Ending Location: ");
        String endingPoint = input.nextLine();

        // Call calculateTravelDistance_Time with the starting and ending locations
        calculateTravelDistance_Time(startingPoint, endingPoint);
    }

    // Method to calculate the travel distance and time
    public void calculateTravelDistance_Time(String start, String end) {
        try {
            // Create HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // Build the GET request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://maps.googleapis.com/maps/api/distancematrix/json?" +
                            "origins=" + start + "&" +
                            "destinations=" + end + "&" +
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
*/
