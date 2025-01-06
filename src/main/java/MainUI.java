import java.util.Scanner;

public class MainUI {
    public static void main(String[] args) {
        MainUI UI = new MainUI();
        LocationManager location = new LocationManager();
        TicketGenerator generateTicket = new TicketGenerator();
        Scanner input = new Scanner(System.in);
        StorageManager storage = new StorageManager();
        UserManager user1 = new UserManager(1,"Malan",
                "malandealwis@gamil.com","24196","0712832025");

        UserManager user2 = new UserManager(2,"Dilhara",
                "dilharabandara@gamil.com","1234","0718697687");




        System.out.println("---Welcome to Bus Tikka---");
        UI.loginProcess();
        int inputNumber = input.nextInt();

        switch (inputNumber) {
            case 1:
                System.out.println("User Details: "+user1.getUserInfo().split(" ")[1]);
                break;
            case 2:
                // break;
            default:
                System.out.println("Invalid Input");

        }

        //    location.callAPI();

        UI.showMainMenu();
        int inputMenuNumber = input.nextInt();

        switch (inputMenuNumber) {
            case 1:
                location.getTravelDistanceTime();
                break;
            case 2:
                generateTicket.generateQR();
                break;
            case 3:
                storage.connectionSetup();
                break;
            case 4:
                storage.dataInsert(user1.userID,user1.name, user1.email, user1.password, user1.mobileNo);
                storage.dataInsert(user2.userID,user2.name, user2.email, user2.password, user2.mobileNo);
                break;
            case 5:
                // break;
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