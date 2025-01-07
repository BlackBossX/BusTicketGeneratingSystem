import java.util.Scanner;
public class UserManager {
    String name;
    String email;
    String password;
    String mobileNo;

    UserManager(){

    }

    UserManager(String name,String email,String password,String mobileNo){
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobileNo = mobileNo;
    }

    Scanner input = new Scanner(System.in);

    public void userRegister(){
        System.out.print("Enter Name: ");
        name = input.nextLine();

        System.out.print("Enter Email: ");
        email = input.nextLine();

        System.out.print("Enter Password: ");
        password = input.nextLine();
       // StorageManager.createMD5Hash(password)

        System.out.print("Enter Mobile No: ");
        mobileNo = input.nextLine();

    }

    public void userLogin(String email,String password){
        this.email = email;
        this.password = password;
    }

    public String getUserInfo(){
        return name+" "+email+" "+password+" "+mobileNo;
    }



}
