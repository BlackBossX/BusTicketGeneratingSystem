public class UIManager {

    static void showMainMenu() {
        System.out.println("---Select Option---");
        System.out.println("        1) Calculate Distance & Ticket Cost");
        System.out.println("        2) Book a Ticket");
        System.out.println("        3) Cancel a Ticket");
        System.out.println("        0) Quit");
    }

    static void showLoginProcess() {
        System.out.println("Please enter respected number");
        System.out.println("1) Register");
        System.out.println("2) Login");
        System.out.println("0) Quit");
    }

    public static void showSystemArt() {
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
