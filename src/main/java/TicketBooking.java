import java.util.Scanner;

public class TicketBooking extends StorageManager {
    private final int TOTAL_SEATS = 50;
    private boolean[] seats = new boolean[TOTAL_SEATS]; // false indicates the seat is available
    private Scanner input = new Scanner(System.in);







}

































/*
    public void bookTicket() {
        LocationManager locationManager = new LocationManager();
        StorageManager storageManager = new StorageManager();

        // Step 1: Get travel details
        String[] travelDetails = locationManager.getTravelDistanceTime().split(",");
        if (travelDetails.length < 5) {
            System.out.println("Unable to fetch travel details. Please try again.");
            return;
        }

        String startingLocation = travelDetails[0];
        String endingLocation = travelDetails[1];
        double travelCost = Double.parseDouble(travelDetails[4]);

        // Step 2: Display available seats
        displayAvailableSeats();

        // Step 3: Ask the user to book seats
        System.out.print("Enter the number of seats you want to book: ");
        int seatsToBook = input.nextInt();

        if (seatsToBook <= 0 || seatsToBook > availableSeatCount()) {
            System.out.println("Invalid number of seats. Please try again.");
            return;
        }

        System.out.println("Enter the seat numbers you want to book (1-50):");
        for (int i = 0; i < seatsToBook; i++) {
            int seatNumber = input.nextInt();
            if (seatNumber < 1 || seatNumber > TOTAL_SEATS || seats[seatNumber - 1]) {
                System.out.println("Invalid or already booked seat number: " + seatNumber);
                i--; // Repeat this iteration
            } else {
                seats[seatNumber - 1] = true;
            }
        }

        // Step 4: Display booking details
        System.out.println("Booking Summary:");
        System.out.println("From: " + startingLocation);
        System.out.println("To: " + endingLocation);
        System.out.print("Booked Seats: ");
        for (int i = 0; i < TOTAL_SEATS; i++) {
            if (seats[i]) {
                System.out.print((i + 1) + " ");
            }
        }
        System.out.println("Total Cost: Rs. " + (travelCost * seatsToBook));

        // Step 5: Save travel details (optional)
      //  storageManager.travelDataInsert(startingLocation, endingLocation, travelDetails[2], travelDetails[3], travelCost * seatsToBook);
    }

    private void displayAvailableSeats() {
        System.out.println("Available Seats:");
        for (int i = 0; i < TOTAL_SEATS; i++) {
            if (!seats[i]) {
                System.out.print((i + 1) + " ");
            }
        }
        System.out.println();
    }

    private int availableSeatCount() {
        int count = 0;
        for (boolean seat : seats) {
            if (!seat) count++;
        }
        return count;
    }
}*/
