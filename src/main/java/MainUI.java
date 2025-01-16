import java.util.Scanner;

public class MainUI {
    public static void main(String[] args) {
        LocationManager location = new LocationManager();
        TicketGenerator generateTicket = new TicketGenerator();
        Scanner input = new Scanner(System.in);
        StorageManager storage = new StorageManager();
        UserManager user = new UserManager();
        TicketBooking booking = new TicketBooking();

        UIManager.showSystemArt();
        UIManager.showLoginProcess();
        int inputNumber = input.nextInt();
        switch (inputNumber) {
            case 1:
                user.userRegister();
                user.userLogin();
                break;
            case 2:
                user.userLogin();
                break;
            default:
                System.out.println("Invalid Input. Please enter number 1 or 2.");
                return; // Exit the program if the input is invalid
        }

        boolean exit = false;
        while (!exit) {
            UIManager.showMainMenu();
            int inputMenuNumber = input.nextInt();
            switch (inputMenuNumber) {
                case 1:
                    storage.travelDataInsert();
                    break;
                case 2:
                    generateTicket.generateQR();
                    break;
                case 3:
                    System.out.println("Press 1 to Book a Ticket or 2 to Cancel a Ticket");
                    int subMenuChoice = input.nextInt();
                    if (subMenuChoice == 1) {
                        System.out.println("Booking a ticket...");
                        // Add booking logic here
                    } else if (subMenuChoice == 2) {
                        System.out.println("Canceling a ticket...");
                        // Add cancellation logic here
                    } else {
                        System.out.println("Invalid Input. Returning to main menu.");
                    }
                    break;
                case 4:
                    System.out.println("Searching a ticket...");
                    // Add search logic here
                    break;
                case 5:
                    System.out.println("Canceling a ticket...");
                    // Add cancellation logic here
                    break;
                case 6:
                    System.out.println("Exiting the system. Goodbye!");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid Input");
            }
        }
    }
}
