import java.util.InputMismatchException;
import java.util.Scanner;

public class MainUI {
    private static int inputNumber = 0;
    private static int inputMenuNumber = 0;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        StorageManager storage = new StorageManager();
        LocationManager location = new LocationManager();
        UserManager user = new UserManager(storage);
        TicketGenerator generateTicket = new TicketGenerator(location, user, storage);
        TicketBooking booking = new TicketBooking(location, storage);
        BusManager busManager = new BusManager(location, storage);
        IoTCommunicator iotComm = new IoTCommunicator(location, storage);
        AnalyticsManager analytics = new AnalyticsManager(storage);

        // Initialize IoT communication
        try {
            iotComm.connect();
        } catch (Exception e) {
            UIManager.showError("IoT communication setup failed. Continuing without real-time updates.");
        }

        // Show welcome art and login process
        UIManager.showSystemArt();
        UIManager.showLoginProcess();

        // Handle login/register/exit
        try {
            inputNumber = input.nextInt();
            input.nextLine(); // Clear buffer
        } catch (InputMismatchException e) {
            UIManager.showError("Invalid input! Please enter a number.");
            System.exit(0);
        }

        switch (inputNumber) {
            case 1: // Register and login
                user.userRegister();
                if (!user.userLogin()) {
                    UIManager.showError("Login required to proceed. Exiting...");
                    iotComm.disconnect();
                    System.exit(0);
                }
                break;
            case 2: // Login
                if (!user.userLogin()) {
                    UIManager.showError("Login failed. Exiting...");
                    iotComm.disconnect();
                    System.exit(0);
                }
                break;
            case 0: // Exit
                UIManager.showSuccess("Have a Nice Day!");
                iotComm.disconnect();
                System.exit(0);
                break;
            default:
                UIManager.showError("Invalid option! Exiting...");
                iotComm.disconnect();
                System.exit(0);
        }

        // Main menu loop
        while (true) {
            UIManager.showMainMenu();
            try {
                inputMenuNumber = input.nextInt();
                input.nextLine(); // Clear buffer
            } catch (InputMismatchException e) {
                UIManager.showError("Invalid input! Please enter a number.");
                input.nextLine(); // Clear buffer
                continue;
            }

            switch (inputMenuNumber) {
                case 1: // Calculate Distance & Travel Cost
                    storage.travelDataInsert();
                    break;

                case 2: // Calculate Bus Ticket Cost
                    String travelData = location.getTravelDistanceTime();
                    if (!travelData.isEmpty()) {
                        String[] details = travelData.split(",");
                        double cost = Double.parseDouble(details[4]);
                        System.out.printf("Estimated Ticket Cost: Rs. %.2f\n", cost);
                    }
                    break;

                case 3: // Book a Ticket
                    System.out.print("Enter Bus ID: ");
                    String busId = input.nextLine();
                    if (busManager.getBusStatus(busId).equals("Active")) {
                        booking.bookTicket(user.getUserName(), busId, busManager);
                    } else {
                        UIManager.showError("Bus " + busId + " is not active!");
                    }
                    break;

                case 4: // Search a Ticket
                    System.out.print("Enter Ticket ID to search: ");
                    String ticketId = input.nextLine();
                    String ticketDetails = storage.getTicketDetails(ticketId);
                    if (!ticketDetails.isEmpty()) {
                        String[] details = ticketDetails.split(",");
                        UIManager.showTicketDetails(ticketId, details[0], details[1], details[2], details[3], details[4],
                                Double.parseDouble(details[5]), null); // QR URL could be added
                    }
                    break;

                case 5: // Cancel a Ticket
                    System.out.print("Enter Ticket ID to cancel: ");
                    ticketId = input.nextLine();
                    booking.cancelTicket(ticketId);
                    break;

                case 6: // Track Bus Location
                    System.out.print("Enter Bus ID to track: ");
                    busId = input.nextLine();
                    String gpsData = location.getBusLocation(busId);
                    if (!gpsData.isEmpty()) {
                        String[] coordinates = gpsData.split(",");
                        UIManager.showBusLocation(busId, coordinates[0], coordinates[1]);
                        storage.updateBusLocation(busId, coordinates[0], coordinates[1]);
                    }
                    break;

                case 7: // Check Bus ETA
                    System.out.print("Enter Bus ID: ");
                    busId = input.nextLine();
                    System.out.print("Enter your location: ");
                    String passengerLoc = input.nextLine();
                    String eta = location.getETA(busId, passengerLoc);
                    if (!eta.isEmpty()) {
                        UIManager.showBusETA(busId, eta, passengerLoc);
                    }
                    break;

                case 8: // Edit Profile
                    user.editUserProfile();
                    break;

                case 9: // Manage Buses
                    UIManager.showBusManagementMenu();
                    int busOption;
                    try {
                        busOption = input.nextInt();
                        input.nextLine();
                    } catch (InputMismatchException e) {
                        UIManager.showError("Invalid input! Please enter a number.");
                        input.nextLine();
                        break;
                    }
                    switch (busOption) {
                        case 1:
                            System.out.print("Enter Bus ID to register: ");
                            busId = input.nextLine();
                            busManager.registerBus(busId);
                            break;
                        case 2:
                            System.out.print("Enter Bus ID to activate: ");
                            busId = input.nextLine();
                            busManager.activateBus(busId);
                            break;
                        case 3:
                            System.out.print("Enter Bus ID to deactivate: ");
                            busId = input.nextLine();
                            busManager.deactivateBus(busId);
                            break;
                        case 0:
                            break;
                        default:
                            UIManager.showError("Invalid option!");
                    }
                    break;

                case 10: // View Analytics
                    System.out.print("Enter Bus ID for passenger count (or leave blank for total revenue only): ");
                    busId = input.nextLine();
                    double revenue = analytics.getTotalRevenue();
                    int passengerCount = busId.isEmpty() ? 0 : analytics.getPassengerCount(busId);
                    UIManager.showAnalytics(revenue, passengerCount, busId.isEmpty() ? "All Buses" : busId);
                    break;

                case 11: // Quit
                    user.logout();
                    UIManager.showSuccess("Thank you for using UrbanTrack! Goodbye!");
                    iotComm.disconnect();
                    System.exit(0);
                    break;

                default:
                    UIManager.showError("Invalid option! Please try again.");
            }
        }
    }
}