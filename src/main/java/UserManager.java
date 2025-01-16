import java.util.Scanner;

public class UserManager {
    private String name;
    private String email;
    private String password;
    private String mobileNo;
    private static String returnedName;
    private final Scanner input = new Scanner(System.in);

    StorageManager storage = new StorageManager();

    public UserManager() {
    }

    public UserManager(String name, String email, String password, String mobileNo) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobileNo = mobileNo;
    }

    public void userRegister() {
        System.out.println("--Register--\n");
        System.out.print("Enter Name: ");
        name = input.nextLine();

        System.out.print("Enter Email: ");
        email = input.nextLine();

        System.out.print("Enter Password: ");
        password = input.nextLine();
        password = storage.hashPassword(password);

        String mobileNo;
        while (true) {
            System.out.print("Enter Mobile No: ");
            mobileNo = input.nextLine();

            if (isValidMobileNumber(mobileNo)) {
                break; // Exit the loop if the mobile number is valid
            } else {
                System.out.println("Invalid mobile number. Please enter a valid 10-digit number.");
            }
        }

        storage.userDataInsert(name, email, password, mobileNo);
    }

    private boolean isValidMobileNumber(String mobileNo) {
        // Check if the mobile number is exactly 10 digits and contains only numbers
        return mobileNo.matches("\\d{10}");
    }


    public void userLogin() {
        System.out.println("--Login--\n");
        System.out.print("Enter Email: ");
        email = input.nextLine();
        String[] userData = storage.getPassFromTable(email).split(" ");
        String returnedHashedPass = userData[0];
        returnedName = userData[1];

        System.out.print("Enter Password: ");
        password = input.nextLine();

        if (returnedHashedPass.equals("null")) {
            System.out.println("Invalid email or password. Please try again.");
            userLogin();
        } else {
            if (storage.verifyPassword(password, returnedHashedPass)) {
                System.out.println("Successfully Logged In!");
                System.out.println("Hey, " + returnedName);
            } else {
                System.out.println("Invalid email or password. Please try again.");
                userLogin();
            }
        }
    }

    public String getUserName() {
        return returnedName;
    }

    public String getEmail() {
        return email;
    }
}
