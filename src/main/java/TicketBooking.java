import java.util.Scanner;

public class TicketBooking {
    private final int TOTAL_SEATS = 50;
    private boolean[] seats = new boolean[TOTAL_SEATS]; // false indicates the seat is available
    private final Scanner input = new Scanner(System.in);
    private final LocationManager locationManager;
    private final StorageManager storageManager;

    // Constructor with dependencies
    public TicketBooking(LocationManager locationManager, StorageManager storageManager) {
        this.locationManager = locationManager;
        this.storageManager = storageManager;
    }

    // Book a ticket with bus assignment
    public void bookTicket(String userName, String busId, BusManager busManager) {
        // Step 1: Fetch travel details
        String[] travelDetails = locationManager.getTravelDistanceTime().split(",");
        if (travelDetails.length < 5) {
            UIManager.showError("Unable to fetch travel details. Please try again.");
            return;
        }

        String startingLocation = travelDetails[0];
        String endingLocation = travelDetails[1];
        double travelCost = Double.parseDouble(travelDetails[4]);
        String distance = travelDetails[2];
        String duration = travelDetails[3];

        // Step 2: Display available seats and get user input
        displayAvailableSeats();
        System.out.print("\nEnter the number of seats you want to book: ");
        int seatsToBook;
        try {
            seatsToBook = input.nextInt();
            if (seatsToBook <= 0 || seatsToBook > availableSeatCount()) {
                UIManager.showError("Invalid number of seats. Must be between 1 and " + availableSeatCount() + ".");
                return;
            }
        } catch (Exception e) {
            UIManager.showError("Invalid input! Please enter a valid number.");
            input.nextLine(); // Clear buffer
            return;
        }

        System.out.print("How many Full Tickets you want to book? (Default: 1): ");
        int fullTickets;
        try {
            fullTickets = input.nextInt();
            if (fullTickets < 0) {
                UIManager.showError("Number of full tickets cannot be negative!");
                return;
            }
        } catch (Exception e) {
            UIManager.showError("Invalid input! Defaulting to 1 full ticket.");
            fullTickets = 1;
            input.nextLine(); // Clear buffer
        }

        System.out.print("How many Half Tickets you want to book? (Default: 0): ");
        int halfTickets;
        try {
            halfTickets = input.nextInt();
            if (halfTickets < 0) {
                UIManager.showError("Number of half tickets cannot be negative!");
                return;
            }
        } catch (Exception e) {
            UIManager.showError("Invalid input! Defaulting to 0 half tickets.");
            halfTickets = 0;
            input.nextLine(); // Clear buffer
        }

        // Step 3: Validate total tickets against seats to book
        if (fullTickets + halfTickets != seatsToBook) {
            UIManager.showError("Total tickets (" + (fullTickets + halfTickets) + ") must match seats to book (" + seatsToBook + ")!");
            return;
        }

        // Step 4: Book specific seats
        System.out.println("Enter the seat numbers you want to book (1-" + TOTAL_SEATS + "):");
        int[] bookedSeats = new int[seatsToBook];
        for (int i = 0; i < seatsToBook; i++) {
            try {
                int seatNumber = input.nextInt();
                if (seatNumber < 1 || seatNumber > TOTAL_SEATS || seats[seatNumber - 1]) {
                    UIManager.showError("Invalid or already booked seat number: " + seatNumber);
                    i--; // Repeat this iteration
                } else {
                    seats[seatNumber - 1] = true;
                    bookedSeats[i] = seatNumber;
                }
            } catch (Exception e) {
                UIManager.showError("Invalid input! Please enter a valid seat number.");
                input.nextLine(); // Clear buffer
                i--; // Repeat this iteration
            }
        }

        // Step 5: Calculate total cost
        double totalTicketCost = (fullTickets * travelCost) + (halfTickets * (travelCost / 2));

        // Step 6: Save to database and assign seats
        String ticketId = storageManager.ticketBooking(userName);
        if (ticketId.isEmpty()) {
            UIManager.showError("Failed to book ticket in database!");
            return;
        }
        storageManager.assignSeatsToBus(ticketId, busId, bookedSeats);

        // Step 7: Generate QR code
        TicketGenerator ticketGen = new TicketGenerator(locationManager, new UserManager(storageManager), storageManager);
        ticketGen.generateQR(ticketId, bookedSeats, totalTicketCost, busId);

        // Step 8: Display booking summary
        System.out.println("\n----- Booking Summary -----");
        System.out.println("Bus ID: " + busId);
        System.out.println("From: " + startingLocation);
        System.out.println("To: " + endingLocation);
        System.out.print("Booked Seats: ");
        for (int seat : bookedSeats) {
            System.out.print(seat + " ");
        }
        System.out.println("\nFull Tickets: " + fullTickets + ", Half Tickets: " + halfTickets);
        System.out.printf("Total Cost: Rs. %.2f\n", totalTicketCost);
        UIManager.showSuccess("Booking completed successfully! Ticket ID: " + ticketId);
    }

    // Cancel a ticket and free seats
    public void cancelTicket(String ticketId) {
        // Fetch booked seats to free them
        String ticketDetails = storageManager.getTicketDetails(ticketId);
        if (ticketDetails.isEmpty()) {
            UIManager.showError("Cannot cancel: Ticket " + ticketId + " not found!");
            return;
        }

        // Free seats (in-memory update; assumes seats are reloaded from DB on startup)
        String sql = "SELECT seat_number FROM Seats WHERE ticket_id = ?";
        try (Connection conn = DriverManager.getConnection(StorageManager.url, StorageManager.username, StorageManager.password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ticketId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int seatNumber = rs.getInt("seat_number");
                if (seatNumber >= 1 && seatNumber <= TOTAL_SEATS) {
                    seats[seatNumber - 1] = false;
                }
            }
        } catch (SQLException e) {
            UIManager.showError("Error fetching seats for cancellation: " + e.getMessage());
        }

        // Cancel ticket in database
        storageManager.cancelTicket(ticketId);
    }

    // Display available seats
    private void displayAvailableSeats() {
        System.out.println("Available Seats:");
        for (int i = 0; i < TOTAL_SEATS; i++) {
            if (!seats[i]) {
                System.out.print((i + 1) + " ");
            }
        }
        System.out.println();
    }

    // Count available seats
    private int availableSeatCount() {
        int count = 0;
        for (boolean seat : seats) {
            if (!seat) count++;
        }
        return count;
    }

    // Getter for booked seats
    public int[] getBookedSeats() {
        int count = TOTAL_SEATS - availableSeatCount();
        int[] booked = new int[count];
        int index = 0;
        for (int i = 0; i < TOTAL_SEATS; i++) {
            if (seats[i]) {
                booked[index++] = i + 1;
            }
        }
        return booked;
    }
}