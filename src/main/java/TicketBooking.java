public class TicketBooking extends Manager {
    private final LocationManager locationManager;
    private final StorageManager storageManager;
    private final TicketGenerator generateTicket;

    public TicketBooking(LocationManager locationManager, StorageManager storageManager, TicketGenerator generateTicket) {
        this.locationManager = locationManager;
        this.storageManager = storageManager;
        this.generateTicket = generateTicket;
    }

    public void bookTicket() {
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

        System.out.print("\nEnter the number of seats you want to book: ");
        int seatsToBook = input.nextInt();

        System.out.print("How many Full Tickets you want to book? (Default: 1)  : ");
        int fullTickets = input.nextInt();

        System.out.print("How many Half Tickets you want to book? (Default: 0)  : ");
        int halfTickets = input.nextInt();

        double totalTicketCost = (fullTickets * travelCost) + halfTickets * (travelCost / 2);

        String checkout;
        System.out.println("\n-----Booking Summary-----");
        System.out.println("From: " + startingLocation);
        System.out.println("To: " + endingLocation);
        System.out.println("Booked Seats: " + seatsToBook);
        System.out.println("Total Cost: Rs. " + totalTicketCost);

        System.out.println("\nDo you need to Checkout? (Y/N): ");
        try {
            checkout = input.next();
        } catch (Exception e) {
            System.out.println("Enter Y or N");
            checkout = input.next();
        }
        String userName = UserManager.getUserName();
        String TravelInformation = userName + "-" + startingLocation + "-" +
                endingLocation + "-" + distance + "-" + duration + "-" + totalTicketCost + "-" + seatsToBook;

        switch (checkout) {
            case "Y":
                storageManager.ticketSaving(TravelInformation);
                generateTicket.generateQR(TravelInformation);
                System.out.print("Thank You for using our Ticket Booking System!\n\n");
                break;
            case "N":
                System.out.print("Thank You have a nice day!\n\n");
                break;
        }
    }
}