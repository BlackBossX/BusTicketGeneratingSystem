import java.util.Scanner;

public class MainUI {
    public static void main(String[] args) {
        MainUI UI = new MainUI();
        LocationManager location = new LocationManager();
        TicketGenerator generateTicket = new TicketGenerator();
        Scanner input = new Scanner(System.in);
        StorageManager storage = new StorageManager();
        UserManager user = new UserManager();

        MainUI.BusTikkaArt();
        UI.loginProcess();
        int inputNumber = input.nextInt();

        switch (inputNumber) {
            case 1:
                user.userRegister();
                user.userLogin();
                break;
            case 2:
                //user.userLogin();
                break;
            default:
                System.out.println("Invalid Input");

        }

        UI.showMainMenu();
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

    private void showMainMenu() {
        System.out.println("---Select Option---");
        System.out.println("        1) Calculate Distance");
        System.out.println("        2) Calculate Bus Ticket");
        System.out.println("        3) Book a Ticket");
        System.out.println("        4) Search a Ticket");
        System.out.println("        5) Cancel a Ticket");
        System.out.println("        6) Quit");
    }

    private void loginProcess() {
        System.out.println("1) Register");
        System.out.println("2) Login");
    }

    public static void BusTikkaArt() {
        System.out.print(
                "  ____              _______ _ _    _         \n" +
                        " |  _ \\            |__   __(_) |  | |        \n" +
                        " | |_) |_   _ ___     | |   _| | _| | ____ _ \n" +
                        " |  _ <| | | / __|    | |  | | |/ / |/ / _` |\n" +
                        " | |_) | |_| \\__ \\    | |  | |   <|   < (_| |\n" +
                        " |____/ \\__,_|___/    |_|  |_|_|\\_\\_|\\_\\__,_|\n" +
                        "                                             \n"
        );
    }
}