import java.util.Scanner;

public class TicketBooking {
    private final int TOTAL_SEATS = 50;
    private boolean[] seats = new boolean[TOTAL_SEATS]; // false indicates the seat is available
    private Scanner input = new Scanner(System.in);

    public void bookTicket() {
        LocationManager locationManager = new LocationManager();
        StorageManager storageManager = new StorageManager();

        String[] travelDetails = locationManager.getTravelDistanceTime().split(",");
        if (travelDetails.length < 5) {
            System.out.println("Unable to fetch travel details. Please try again.");
            return;
        }

        String startingLocation = travelDetails[0];
        String endingLocation = travelDetails[1];
        double travelCost = Double.parseDouble(travelDetails[4]);
        String distance = travelDetails[2];
        String duration = travelDetails[3];


        System.out.println("\n"+startingLocation + " -> " + endingLocation);
        System.out.println("Distance: " + distance);
        System.out.println("Duration: " + duration);
        System.out.printf("Travel Cost: RS.%.2f\n", travelCost);

      //  displayAvailableSeats();
        System.out.print("\nEnter the number of seats you want to book: ");
        int seatsToBook = input.nextInt();

/*        if (seatsToBook <= 0 || seatsToBook > availableSeatCount()) {
            System.out.println("Invalid number of seats. Please try again.");
            return;
        }*/

        System.out.print("How many Full Tickets you want to book? (Default: 1)  : ");
        int fullTickets = input.nextInt();

        System.out.print("How many Half Tickets you want to book? (Default: 0)  : ");
        int halfTickets = input.nextInt();

        double totalTicketCost = (fullTickets * travelCost) + halfTickets * (travelCost / 2);






/*        System.out.println("Enter the seat numbers you want to book (1-50):");
        for (int i = 0; i < seatsToBook; i++) {
            int seatNumber = input.nextInt();
            if (seatNumber < 1 || seatNumber > TOTAL_SEATS || seats[seatNumber - 1]) {
                System.out.println("Invalid or already booked seat number: " + seatNumber);
                i--; // Repeat this iteration
            } else {
                seats[seatNumber - 1] = true;
            }
        }*/

        // Step 4: Display booking details
        System.out.println("\n-----Booking Summary-----");
        System.out.println("From: " + startingLocation);
        System.out.println("To: " + endingLocation);
        System.out.println("Booked Seats: "+seatsToBook);
        System.out.println("Total Cost: Rs. " + totalTicketCost);

        // Step 5: Save travel details (optional)
       // storageManager.travelDataInsert(startingLocation, endingLocation, travelDetails[2], travelDetails[3], travelCost * seatsToBook);
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
}

