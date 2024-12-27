import java.util.Scanner;

public class LocationManager {
    int busRouteID;
    String startingLocation;
    String endingLocation;
    double travelDistance;
    double travelCost;

    Scanner input = new Scanner(System.in);


    public void gettingLocations(){
        System.out.print("Enter Bus Route ID: ");
        int routeID = input.nextInt();

        System.out.print("Enter Starting Location: ");
        String startingPoint = input.nextLine();
        System.out.print("Enter Ending Location: ");
        String endingPoint = input.nextLine();
    }
}
