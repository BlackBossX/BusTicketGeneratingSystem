import java.util.Scanner;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;


public class LocationManager {

    String startingLocation;
    String endingLocation;
    double travelDistance;
    double travelCost;

    Scanner input = new Scanner(System.in);

    public String gettingLocations() {

        System.out.print("Enter Starting Location: ");
        startingLocation = input.nextLine();
        System.out.print("Enter Ending Location: ");
        endingLocation = input.nextLine();
        return (startingLocation + " " + endingLocation);

    }


    public String getTravelDistanceTime() {
        try {
            // Create HttpClient
            HttpClient client = HttpClient.newHttpClient();

            String[] n = gettingLocations().split(" ");

            // Build the GET request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://maps.googleapis.com/maps/api/distancematrix/json?" +
                            "origins=" + TicketGenerator.encodeURL(startingLocation) + "&" +
                            "destinations=" + TicketGenerator.encodeURL(endingLocation) + "&" +
                            "key=AIzaSyASImPQNkxyWnMrFZ7hEDgx-szlLkeEPHk"))
                    .GET()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // System.out.println("Response Body: " + response.body());
            JSONObject jsonObject = new JSONObject(response.body());

            // starting with [] mean its JSONArray and starting with {} mean its JSONObject

            JSONArray rowsArray = jsonObject.getJSONArray("rows");
            JSONObject rows0 = rowsArray.getJSONObject(0);
            JSONArray elements = rows0.getJSONArray("elements");
            JSONObject elements0 = elements.getJSONObject(0);
            JSONObject distanceObj = elements0.getJSONObject("distance");
            String distance = distanceObj.getString("text");

            String status = elements0.getString("status");

            JSONObject durationObj = elements0.getJSONObject("duration");
            String duration = durationObj.getString("text");

            double numericalDistance = Double.parseDouble(distance.split(" ")[0]);
            double tCost;
            if (numericalDistance < 3) {
                tCost = 27.00 + (numericalDistance * 3.093);
            } else {
                tCost = 35.00 + (numericalDistance * 3.093);
            }

            System.out.println(startingLocation + " -> " + endingLocation);
            System.out.println("Distance: " + distance);
            System.out.println("Duration: " + duration);
            System.out.printf("Travel Cost: RS.%.2f\n", tCost);

            //   System.out.println("status: " + status);


            return startingLocation + "," + endingLocation + "," + distance + "," + duration + "," + tCost;

            //System.out.println("Response Code: " + response.statusCode());
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println("Type Locations Correctly!");
        }
        return "";
    }
}
