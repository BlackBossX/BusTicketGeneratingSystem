import java.util.InputMismatchException;
import java.util.Scanner;

public class MainUI {
    private int inputNumber = 0;
    private int inputMenuNumber = 0;

    public static void main(String[] args) {
        int inputNumber = 0;
        int inputMenuNumber = 0;
        Scanner input = new Scanner(System.in);
        LocationManager location = new LocationManager();
        StorageManager storage = new StorageManager();
        UserManager user = new UserManager(storage);
        TicketGenerator generateTicket = new TicketGenerator(location, user, storage);
        TicketBooking booking = new TicketBooking(location, storage, generateTicket);


        UIManager.showSystemArt();
        UIManager.showLoginProcess();
        try {
            inputNumber = input.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid Input!");
            System.exit(0);
        }
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

        UIManager.showMainMenu();
        try {
            inputMenuNumber = input.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid Input!");
            System.exit(0);
        }
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
            case 0:
                System.out.println("Quitting...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid Input");
                System.exit(0);
        }
    }

}