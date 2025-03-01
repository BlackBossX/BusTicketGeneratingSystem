import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class IoTCommunicator {
    private final LocationManager locationManager;
    private final StorageManager storageManager;
    private MqttClient mqttClient;
    private static final String BROKER = "tcp://broker.hivemq.com:1883"; // Public MQTT broker for testing
    private static final String CLIENT_ID = "UrbanTrackIoT_" + System.currentTimeMillis(); // Unique client ID

    // Constructor with dependencies
    public IoTCommunicator(LocationManager locationManager, StorageManager storageManager) {
        this.locationManager = locationManager;
        this.storageManager = storageManager;
    }

    // Connect to MQTT broker
    public void connect() {
        try {
            // Use MemoryPersistence to avoid file-based persistence issues
            mqttClient = new MqttClient(BROKER, CLIENT_ID, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true); // Start with a clean session
            options.setConnectionTimeout(10); // Timeout in seconds
            options.setKeepAliveInterval(60); // Keep alive interval in seconds

            mqttClient.connect(options);
            UIManager.showSuccess("Connected to IoT broker at " + BROKER);
            subscribeToGPS();
        } catch (MqttException e) {
            UIManager.showError("Failed to connect to IoT broker: " + e.getReasonCode() + " - " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Subscribe to GPS updates
    private void subscribeToGPS() {
        try {
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    UIManager.showError("Connection to IoT broker lost: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    try {
                        String payload = new String(message.getPayload());
                        String[] gpsData = payload.split(",");
                        if (gpsData.length < 2) {
                            UIManager.showError("Invalid GPS data received from topic " + topic + ": " + payload);
                            return;
                        }
                        String busId = topic.split("/")[2]; // e.g., "urbantrack/gps/BUS001"
                        String latitude = gpsData[0].trim();
                        String longitude = gpsData[1].trim();

                        // Validate latitude and longitude (simple check)
                        if (!isValidCoordinate(latitude) || !isValidCoordinate(longitude)) {
                            UIManager.showError("Invalid GPS coordinates for bus " + busId + ": " + payload);
                            return;
                        }

                        locationManager.updateBusLocationFromIoT(busId, latitude, longitude);
                        storageManager.updateBusLocation(busId, latitude, longitude);
                        UIManager.showSuccess("Received GPS update for " + busId + ": Lat " + latitude + ", Long " + longitude);
                    } catch (Exception e) {
                        UIManager.showError("Error processing GPS message: " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Not used in this context, but required by the interface
                }
            });

            mqttClient.subscribe("urbantrack/gps/#", 1); // QoS 1 for at-least-once delivery
            UIManager.showSuccess("Subscribed to GPS topic: urbantrack/gps/#");
        } catch (MqttException e) {
            UIManager.showError("Failed to subscribe to GPS topic: " + e.getReasonCode() + " - " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Disconnect from broker
    public void disconnect() {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                mqttClient.disconnect();
                UIManager.showSuccess("Disconnected from IoT broker.");
            }
        } catch (MqttException e) {
            UIManager.showError("Error disconnecting from IoT broker: " + e.getReasonCode() + " - " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Check connection status
    public boolean isConnected() {
        return mqttClient != null && mqttClient.isConnected();
    }

    // Validate coordinate format (simple check for latitude/longitude)
    private boolean isValidCoordinate(String coord) {
        try {
            double value = Double.parseDouble(coord);
            return value >= -90 && value <= 90; // For latitude; adjust for longitude (-180 to 180)
        } catch (NumberFormatException e) {
            return false;
        }
    }
}