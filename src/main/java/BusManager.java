import java.util.HashMap;
import java.util.Map;

public class BusManager {
    private final LocationManager locationManager;
    private final StorageManager storageManager;
    private final Map<String, String> busStatus; // Bus ID -> Status (e.g., "Active", "Inactive")

    public BusManager(LocationManager locationManager, StorageManager storageManager) {
        this.locationManager = locationManager;
        this.storageManager = storageManager;
        this.busStatus = new HashMap<>();
    }

    // Register a new bus
    public void registerBus(String busId) {
        busStatus.put(busId, "Inactive");
        System.out.println("Bus " + busId + " registered.");
    }

    // Activate a bus and start tracking
    public void activateBus(String busId) {
        if (busStatus.containsKey(busId)) {
            busStatus.put(busId, "Active");
            String gpsData = locationManager.getBusLocation(busId);
            if (!gpsData.isEmpty()) {
                String[] coords = gpsData.split(",");
                storageManager.updateBusLocation(busId, coords[0], coords[1]);
                UIManager.showSuccess("Bus " + busId + " activated and tracking started.");
            } else {
                UIManager.showError("Failed to activate bus " + busId + ": GPS unavailable.");
            }
        } else {
            UIManager.showError("Bus " + busId + " not registered!");
        }
    }

    // Deactivate a bus
    public void deactivateBus(String busId) {
        if (busStatus.containsKey(busId)) {
            busStatus.put(busId, "Inactive");
            UIManager.showSuccess("Bus " + busId + " deactivated.");
        } else {
            UIManager.showError("Bus " + busId + " not found!");
        }
    }

    // Get bus status
    public String getBusStatus(String busId) {
        return busStatus.getOrDefault(busId, "Not Registered");
    }
}