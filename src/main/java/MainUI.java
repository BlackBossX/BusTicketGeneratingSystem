import java.util.Scanner;

public class MainUI {
    public static void main(String[] args){
        MainUI UI = new MainUI();
        LocationManager location = new LocationManager();
        TicketGenerator generateTicket = new TicketGenerator();
        Scanner input = new Scanner(System.in);
        StorageManager storage = new StorageManager();

        System.out.println("---Welcome to Bus Tikka---");
        UI.loginProcess();
        int inputNumber = input.nextInt();

        switch (inputNumber) {
            case 1:
                UserManager user = new UserManager();
                user.userRegister();
                break;
            case 2:
                break;
            default:
                System.out.println("Invalid Input");

        }

        UI.showMainMenu();
        int inputMenuNumber = input.nextInt();

        switch (inputMenuNumber) {
            case 1:
                String[] fetchData = location.getTravelDistanceTime().split(",");
                double costIntoDouble = Double.parseDouble(fetchData[4]);
                storage.travelDataInsert(fetchData[0], fetchData[1], fetchData[2], fetchData[3], costIntoDouble);
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
}