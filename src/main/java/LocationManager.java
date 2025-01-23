import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONArray;
import org.json.JSONObject;

public class LocationManager {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String API_KEY = dotenv.get("GOOGLE_MAPS_API_KEY");
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Fetch travel details using Google Maps Distance Matrix API.
     * @return String containing starting location, ending location, distance, duration, and cost.
     */
    public String getTravelDistanceTime() {
        try {
            System.out.print("Enter Starting Location: ");
            String startingLocation = scanner.nextLine();

            System.out.print("Enter Destination Location: ");
            String destinationLocation = scanner.nextLine();

            // Validate inputs
            if (startingLocation.isEmpty() || destinationLocation.isEmpty()) {
                System.out.println("Invalid input. Both locations must be provided.");
                return getTravelDistanceTime();
            }

            String encodedStartingLocation = encodeValue(startingLocation);
            String encodedDestinationLocation = encodeValue(destinationLocation);

            // Build the URL for the API call
            String apiUrl = String.format(
                    "https://maps.googleapis.com/maps/api/distancematrix/json?origins=%s&destinations=%s&key=%s",
                    encodedStartingLocation, encodedDestinationLocation, API_KEY
            );

            // Send API request
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                System.out.println("Failed to fetch data from Google Maps API. Response Code: " + responseCode);
                return "null";
            }

            // Read response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray rows = jsonResponse.getJSONArray("rows");
            if (rows.isEmpty()) {
                System.out.println("No data found for the given locations. Please try again.");
                return getTravelDistanceTime();
            }

            JSONObject elements = rows.getJSONObject(0).getJSONArray("elements").getJSONObject(0);

            // Extract distance and duration
            String distance = elements.getJSONObject("distance").getString("text");
            String duration = elements.getJSONObject("duration").getString("text");

            // Calculate cost (example: Rs. 10 per km)
            double distanceInKm = Double.parseDouble(distance.split(" ")[0]);
            double travelCost = distanceInKm * 10;

            // Return formatted travel details
            return String.format("%s,%s,%s,%s,%.2f", startingLocation, destinationLocation, distance, duration, travelCost);

        } catch (Exception e) {
            System.out.println("Error fetching travel details: " + e.getMessage());
            return "null";
        }
    }

    /**
     * Encode a value for use in a URL query parameter.
     * @param value The string to encode.
     * @return The encoded string.
     */
    private String encodeValue(String value) {
        try {
            return java.net.URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            return value;
        }
    }
}
