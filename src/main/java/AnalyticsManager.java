import java.sql.*;

public class AnalyticsManager {
    private final StorageManager storageManager;

    public AnalyticsManager(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    // Calculate total revenue from tickets
    public double getTotalRevenue() {
        String sql = "SELECT SUM(total_fare) FROM Tickets";
        try (Connection conn = DriverManager.getConnection(StorageManager.url, StorageManager.username, StorageManager.password);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                double revenue = rs.getDouble(1);
                UIManager.showSuccess("Total Revenue: Rs. " + String.format("%.2f", revenue));
                return revenue;
            }
        } catch (SQLException e) {
            UIManager.showError("Error calculating revenue: " + e.getMessage());
        }
        return 0.0;
    }

    // Get passenger count for a specific bus
    public int getPassengerCount(String busId) {
        String sql = "SELECT COUNT(*) FROM Tickets WHERE ticket_id IN (SELECT ticket_id FROM Seats WHERE bus_id = ?)";
        try (Connection conn = DriverManager.getConnection(StorageManager.url, StorageManager.username, StorageManager.password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, busId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                UIManager.showSuccess("Passenger Count for Bus " + busId + ": " + count);
                return count;
            }
        } catch (SQLException e) {
            UIManager.showError("Error fetching passenger count: " + e.getMessage());
        }
        return 0;
    }
}