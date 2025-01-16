import java.util.Scanner;

public class MainUI {
    public static void main(String[] args) {
        LocationManager location = new LocationManager();
        TicketGenerator generateTicket = new TicketGenerator();
        Scanner input = new Scanner(System.in);
        StorageManager storage = new StorageManager();
        UserManager user = new UserManager();
        TicketBooking booking = new TicketBooking(

        ) ;


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
                System.out.println("Invalid Input");
        }

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
                storage.connectionSetup();
                break;
            case 4:
                break;
            case 5:
                location.getTravelDistanceTime();
                break;
            case 6:
                break;
            default:
                System.out.println("Invalid Input");
        }
    }
}