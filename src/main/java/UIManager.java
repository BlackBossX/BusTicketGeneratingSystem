public class UIManager {
    // Main menu with all options
    static void showMainMenu() {
        System.out.println("\n================ UrbanTrack Menu ================");
        System.out.println("  1) Calculate Distance & Travel Cost");
        System.out.println("  2) Calculate Bus Ticket Cost");
        System.out.println("  3) Book a Ticket");
        System.out.println("  4) Search a Ticket");
        System.out.println("  5) Cancel a Ticket");
        System.out.println("  6) Track Bus Location");
        System.out.println("  7) Check Bus ETA");
        System.out.println("  8) Edit Profile");
        System.out.println("  9) Manage Buses");
        System.out.println(" 10) View Analytics");
        System.out.println(" 11) Quit");
        System.out.println("=================================================");
        System.out.print("Select an option: ");
    }

    // Login process menu
    static void showLoginProcess() {
        System.out.println("\n=========== Welcome to UrbanTrack ===========");
        System.out.println("Please select an option:");
        System.out.println("  1) Register");
        System.out.println("  2) Login");
        System.out.println("  0) Exit");
        System.out.println("=============================================");
        System.out.print("Enter your choice: ");
    }

    // System art
    public static void showSystemArt() {
        System.out.println(
                "  ____              _______ _ _    _         \n" +
                        " |  _ \\            |__   __(_) |  | |        \n" +
                        " | |_) |_   _ ___     | |   _| | _| | ____ _ \n" +
                        " |  _ <| | | / __|    | |  | | |/ / |/ / _` |\n" +
                        " | |_) | |_| \\__ \\    | |  | |   <|   < (_| |\n" +
                        " |____/ \\__,_|___/    |_|  |_|_|\\_\\_|\\_\\__,_|\n" +
                        "                                             "
        );
    }

    // Display bus location
    static void showBusLocation(String busId, String latitude, String longitude) {
        System.out.println("\n=========== Bus Location ===========");
        System.out.println("Bus ID: " + busId);
        System.out.println("Latitude: " + latitude);
        System.out.println("Longitude: " + longitude);
        System.out.println("====================================");
    }

    // Display bus ETA
    static void showBusETA(String busId, String eta, String destination) {
        System.out.println("\n=========== Bus ETA ===========");
        System.out.println("Bus ID: " + busId);
        System.out.println("Destination: " + destination);
        System.out.println("Estimated Arrival Time: " + eta);
        System.out.println("===============================");
    }

    // Display ticket details
    static void showTicketDetails(String ticketId, String userName, String start, String end, String distance, String duration, double totalFare, String qrUrl) {
        System.out.println("\n=========== Ticket Details ===========");
        System.out.println("Ticket ID: " + ticketId);
        System.out.println("User: " + userName);
        System.out.println("Route: " + start + " -> " + end);
        System.out.println("Distance: " + distance);
        System.out.println("Duration: " + duration);
        System.out.printf("Total Fare: Rs. %.2f\n", totalFare);
        System.out.println("QR Code: " + (qrUrl != null ? qrUrl : "Not generated"));
        System.out.println("======================================");
    }

    // Bus management sub-menu
    static void showBusManagementMenu() {
        System.out.println("\n=========== Bus Management ===========");
        System.out.println("  1) Register Bus");
        System.out.println("  2) Activate Bus");
        System.out.println("  3) Deactivate Bus");
        System.out.println("  0) Back to Main Menu");
        System.out.println("======================================");
        System.out.print("Select an option: ");
    }

    // Display analytics
    static void showAnalytics(double totalRevenue, int passengerCount, String busId) {
        System.out.println("\n=========== System Analytics ===========");
        System.out.printf("Total Revenue: Rs. %.2f\n", totalRevenue);
        System.out.println("Passenger Count for Bus " + busId + ": " + passengerCount);
        System.out.println("========================================");
    }

    // Consistent error message display
    static void showError(String message) {
        System.out.println("\n[ERROR] " + message);
    }

    // Consistent success message display
    static void showSuccess(String message) {
        System.out.println("\n[SUCCESS] " + message);
    }
}