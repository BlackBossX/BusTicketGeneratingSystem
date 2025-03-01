import java.util.Scanner;

public class UserManager {
    private String name;
    private String email;
    private String password;
    private String mobileNo;
    private static String returnedName;
    private final Scanner input = new Scanner(System.in);
    private final StorageManager storage;

    // Constructor with StorageManager dependency
    public UserManager(StorageManager storage) {
        this.storage = storage;
    }

    // Parameterized constructor (optional)
    public UserManager(String name, String email, String password, String mobileNo, StorageManager storage) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobileNo = mobileNo;
        this.storage = storage;
    }

    // User registration
    public void userRegister() {
        System.out.println("\n-- Register --");
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
                UIManager.showError("Invalid mobile number. Please enter a valid 10-digit number.");
            }
        }

        storage.userDataInsert(name, email, password, mobileNo);
        UIManager.showSuccess("Registration completed successfully!");
    }

    // Validate mobile number
    private boolean isValidMobileNumber(String mobileNo) {
        return mobileNo.matches("\\d{10}");
    }

    // User login with attempt limit
    public boolean userLogin() {
        System.out.println("\n-- Login --");
        int attempts = 3;
        while (attempts > 0) {
            System.out.print("Enter Email: ");
            email = input.nextLine();
            String userData = storage.getPassFromTable(email);
            if (userData.isEmpty()) {
                UIManager.showError("No user found with this email! Attempts left: " + --attempts);
                continue;
            }

            String[] splitData = userData.split(" ");
            String returnedHashedPass = splitData[0];
            returnedName = splitData[1];

            System.out.print("Enter Password: ");
            password = input.nextLine();

            if (storage.verifyPassword(password, returnedHashedPass)) {
                UIManager.showSuccess("Successfully Logged In! Welcome, " + returnedName);
                return true;
            } else {
                UIManager.showError("Invalid password! Attempts left: " + --attempts);
            }
        }
        UIManager.showError("Login failed. Too many incorrect attempts.");
        return false;
    }

    // Edit user profile with database sync
    public void editUserProfile() {
        System.out.println("\n-- Edit Profile --");
        System.out.println("1. Edit Name");
        System.out.println("2. Edit Email");
        System.out.println("3. Edit Password");
        System.out.println("4. Edit Mobile Number");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choose an option: ");

        try {
            int choice = input.nextInt();
            input.nextLine(); // Clear buffer

            switch (choice) {
                case 1:
                    System.out.print("Enter your new name: ");
                    String newName = input.nextLine();
                    editName(newName);
                    break;
                case 2:
                    System.out.print("Enter your new email: ");
                    String newEmail = input.nextLine();
                    editEmail(newEmail);
                    break;
                case 3:
                    if (verifyCurrentPassword()) {
                        System.out.print("Enter your new password: ");
                        String newPassword = input.nextLine();
                        editPassWord(newPassword);
                    } else {
                        UIManager.showError("Password verification failed. Profile not updated.");
                        return;
                    }
                    break;
                case 4:
                    System.out.print("Enter your new mobile number: ");
                    String newMobileNo = input.nextLine();
                    if (isValidMobileNumber(newMobileNo)) {
                        editMobileNo(newMobileNo);
                    } else {
                        UIManager.showError("Invalid mobile number! Profile not updated.");
                        return;
                    }
                    break;
                case 0:
                    return; // Exit to main menu
                default:
                    UIManager.showError("Invalid choice. Please try again.");
                    return;
            }
            updateUserInDatabase();
            UIManager.showSuccess("Profile updated successfully!");
        } catch (Exception e) {
            UIManager.showError("Invalid input! Please enter a number.");
            input.nextLine(); // Clear buffer
        }
    }

    // Verify current password before editing
    private boolean verifyCurrentPassword() {
        System.out.print("Enter your current password: ");
        String currentPassword = input.nextLine();
        String hashedPass = storage.getPassFromTable(email).split(" ")[0];
        return storage.verifyPassword(currentPassword, hashedPass);
    }

    // Edit methods with feedback
    private void editName(String name) {
        this.name = name;
        System.out.println("Name updated to: " + name);
    }

    private void editEmail(String email) {
        this.email = email;
        System.out.println("Email updated to: " + email);
    }

    private void editPassWord(String password) {
        this.password = storage.hashPassword(password);
        System.out.println("Password updated successfully.");
    }

    private void editMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
        System.out.println("Mobile number updated to: " + mobileNo);
    }

    // Update user data in the database
    private void updateUserInDatabase() {
        String sql = "UPDATE Users SET name = ?, email = ?, password = ?, phone = ? WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(StorageManager.url, StorageManager.username, StorageManager.password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, mobileNo);
            stmt.setString(5, email); // Use email as the key for update
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                UIManager.showError("Profile update failed: User not found in database.");
            }
        } catch (SQLException e) {
            UIManager.showError("Error updating profile in database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Logout user
    public void logout() {
        returnedName = null;
        email = null;
        password = null;
        mobileNo = null;
        UIManager.showSuccess("Logged out successfully!");
    }

    // Getters
    public static String getUserName() {
        return returnedName;
    }

    public String getEmail() {
        return email;
    }
}