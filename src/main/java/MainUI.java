import java.util.InputMismatchException;
import java.util.Scanner;

public class MainUI {
    private static int inputNumber = 0;
    private static int inputMenuNumber = 0;
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        LocationManager location = new LocationManager();
        StorageManager storage = new StorageManager();
        UserManager user = new UserManager(storage);
        TicketGenerator generateTicket = new TicketGenerator(location, user, storage);
        TicketBooking booking = new TicketBooking(location, storage, generateTicket, user);

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
                System.out.println("Quitting...");
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
                storage.travelDataInsert(location);
                break;
            case 2:
                booking.bookTicket();
                break;
            case 3:
                booking.cancelTicket();
                break;
            case 0:
                System.out.println("Quitting...");
                System.out.println("Have a Nice Day!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid Input");
                System.exit(0);
        }
    }

}