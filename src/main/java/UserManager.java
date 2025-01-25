public class UserManager extends Manager implements UserAction {
    private String name;
    private static String email;
    private String password;
    private String mobileNo;
    private static String returnedName;
    private final StorageManager storage;

    public UserManager(StorageManager storage) {
        this.storage = storage;
    }

    @Override
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

    @Override
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

}