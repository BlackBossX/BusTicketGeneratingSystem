public class UIManager {
    static void showMainMenu() {
        System.out.println("---Select Option---");
        System.out.println("        1) Calculate Distance");
        System.out.println("        2) Calculate Bus Ticket");
        System.out.println("        3) Book a Ticket");
        System.out.println("        4) Search a Ticket");
        System.out.println("        5) Cancel a Ticket");
        System.out.println("        6) Quit");
    }

    static void showLoginProcess() {
        System.out.println("1) Register");
        System.out.println("2) Login");
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
