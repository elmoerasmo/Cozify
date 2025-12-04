package DaoAdmin;

import DAO.DatabaseConnection;
import ModelAdmin.PendingItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class PendingItemDAO {
    
    public ObservableList<PendingItem> getAllPendingItems() {
        ObservableList<PendingItem> list = FXCollections.observableArrayList();
        
        String queryPemilik = "SELECT id, 'PEMILIK' as type, nama, email as details, " +
                             "created_at, status FROM pemilik_kos WHERE status = 'PENDING'";
        
        String queryKos = "SELECT k.id, 'KOS' as type, k.nama_kos as nama, " +
                         "k.alamat as details, k.created_at, k.status FROM kos k WHERE k.status = 'PENDING'";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(queryPemilik)) {
                while (rs.next()) {
                    PendingItem item = new PendingItem(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("nama"),
                        rs.getString("details"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getString("status")
                    );
                    list.add(item);
                }
            }
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(queryKos)) {
                while (rs.next()) {
                    PendingItem item = new PendingItem(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("nama"),
                        rs.getString("details"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getString("status")
                    );
                    list.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public int getPendingCount() {
        int count = 0;
        try (Connection conn = DatabaseConnection.getConnection()) {
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as total FROM pemilik_kos WHERE status = 'PENDING'")) {
                if (rs.next()) count += rs.getInt("total");
            }
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as total FROM kos WHERE status = 'PENDING'")) {
                if (rs.next()) count += rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}