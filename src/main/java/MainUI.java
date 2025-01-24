import java.util.InputMismatchException;
import java.util.Scanner;

public class MainUI {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        LocationManager location = new LocationManager(input);
        StorageManager storage = new StorageManager();
        UserManager user = new UserManager(storage, input);
        TicketGenerator generateTicket = new TicketGenerator(location, user,storage);
        TicketBooking booking = new TicketBooking(location, storage, generateTicket, input);

        UIManager.showSystemArt();
        UIManager.showLoginProcess();
        try {
            int inputNumber = input.nextInt();
            switch (inputNumber) {
                case 1:
                    user.userRegister();
                    user.userLogin();
                    break;
                case 2:
                    user.userLogin();
                    break;
                case 0:
                    System.out.println("Have a Nice Day!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid Input!");
                    System.exit(0);
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid Input!");
            System.exit(0);
        }

        UIManager.showMainMenu();
        int inputMenuNumber = input.nextInt();
        switch (inputMenuNumber) {
            case 1:
                location.getTravelDistanceTime();
                break;
            case 2:
                // Implement the logic for calculating bus ticket
                break;
            case 3:
                booking.bookTicket();
                break;
            case 4:
                // Implement the logic for searching a ticket
                break;
            case 5:
                // Implement the logic for canceling a ticket
                break;
            case 6:
                System.out.println("Quitting...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid Input");
        }
    }
}