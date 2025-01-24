import java.util.Scanner;

public class UserManager extends Manager {
    private String name;
    private String email;
    private String password;
    private String mobileNo;
    private static String returnedName;
    private final StorageManager storage;

    public UserManager(StorageManager storage, Scanner input) {
        this.storage = storage;
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

        while (true) {
            System.out.print("Enter Mobile No: ");
            mobileNo = input.nextLine();

            if (isValidMobileNumber(mobileNo)) {
                break;
            } else {
                System.out.println("Invalid mobile number. Please enter a valid 10-digit number.");
            }
        }

        storage.userDataInsert(name, email, password, mobileNo);
    }

    private boolean isValidMobileNumber(String mobileNo) {
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

    public static String getUserName() {
        return returnedName;
    }

    public String getEmail() {
        return email;
    }

    public void editName(String name) {
        this.name = name;
        System.out.println("Name is edited.");
    }

    public void editMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
        System.out.println("Mobile Number is edited.");
    }

    public void editEmail(String email) {
        this.email = email;
        System.out.println("Email is edited.");
    }

    public void editPassWord(String password) {
        this.password = password;
        password = storage.hashPassword(password);
        System.out.println("Password is edited.");
    }

    public void editUserProfile() {
        System.out.println("-- Edit Profile --");
        System.out.println("1. Edit Name");
        System.out.println("2. Edit Email");
        System.out.println("3. Edit Password");
        System.out.println("4. Edit Mobile Number");
        System.out.print("Choose an option: ");

        int choice = input.nextInt();
        input.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Enter your new name: ");
                String newName = input.nextLine();
                editName(newName);
                break;
            case 2:
                System.out.print("Enter your new Email: ");
                String newEmail = input.nextLine();
                editEmail(newEmail);
                break;
            case 3:
                System.out.print("Enter your new password: ");
                String currentPassword = input.nextLine();
                editPassWord(currentPassword);
                break;
            case 4:
                System.out.print("Enter your new mobile number: ");
                String newMobileNo = input.nextLine();
                editMobileNo(newMobileNo);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        storage.userDataInsert(name, email, password, mobileNo);
    }
}