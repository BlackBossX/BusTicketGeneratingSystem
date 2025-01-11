import java.util.Scanner;

public class UserManager {
    private String name;
    private String email;
    private String password;
    private String mobileNo;
    private final Scanner input = new Scanner(System.in);

    public UserManager() {
    }

    public UserManager(String name, String email, String password, String mobileNo) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobileNo = mobileNo;
    }

    public void userRegister() {
        System.out.print("Enter Name: ");
        name = input.nextLine();

        System.out.print("Enter Email: ");
        email = input.nextLine();

        System.out.print("Enter Password: ");
        password = input.nextLine();
        password = StorageManager.hashPassword(password);

        System.out.print("Enter Mobile No: ");
        mobileNo = input.nextLine();

        StorageManager.userDataInsert(name, email, password, mobileNo);
    }

    public void userLogin() {
        System.out.print("Enter Email: ");
        email = input.nextLine();
        String[] userData = StorageManager.getPassFromTable(email).split(" ");
        String returnedHashedPass = userData[0];
        String returnedName = userData[1];

        System.out.print("Enter Password: ");
        password = input.nextLine();

        if (returnedHashedPass.equals("null")) {
            System.out.println("Invalid email or password. Please try again.");
            userLogin();
        } else {
            try {
                if (StorageManager.verifyPassword(password, returnedHashedPass)) {
                    System.out.println("Successfully Logged In!");
                    System.out.println("Hey, " + returnedName);
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid email or password. Please try again.");
                userLogin();
            }
        }
    }

    public String getUserInfo() {
        return String.format("%s %s %s %s", name, email, password, mobileNo);
    }
}
