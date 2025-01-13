import java.util.Scanner;

public class UserManager {
    String name;
    String email;
    String password;
    String mobileNo;

    UserManager() {

    }

    UserManager(String name, String email, String password, String mobileNo) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobileNo = mobileNo;
    }

    Scanner input = new Scanner(System.in);

    public void userRegister() {

        System.out.println("--Register--\n");
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
        System.out.println("--Login--\n");
        System.out.print("Enter Email: ");
        email = input.nextLine();
        String returnedHashedPass = StorageManager.getPassFromTable(email).split(" ")[0];
        String returnedName = StorageManager.getPassFromTable(email).split(" ")[1];

        System.out.print("Enter Password: ");
        password = input.nextLine();
        if (returnedHashedPass.equals("null")) {
            System.out.println("Invalid email or password. Please try again.");
            userLogin();
        } else {
                if (StorageManager.verifyPassword(password, returnedHashedPass)) {
                    System.out.println("Successfully Logged In!");
                    System.out.println("Hey, " + returnedName);
                }else{
                    System.out.println("Invalid email or password. Please try again.");
                    userLogin();
                }
        }
    }


    public String getUserInfo() {
        return name + " " + email + " " + password + " " + mobileNo;


    }
}





