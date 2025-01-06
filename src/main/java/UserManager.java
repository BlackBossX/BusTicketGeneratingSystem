public class UserManager {
    int userID;
    String name;
    String email;
    String password;
    String mobileNo;

    UserManager(){

    }

    UserManager(int userID,String name,String email,String password,String mobileNo){
        this.userID= userID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobileNo = mobileNo;
    }

    public String getUserInfo(){
        return userID+" "+name+" "+email+" "+password+" "+mobileNo;
    }

}
