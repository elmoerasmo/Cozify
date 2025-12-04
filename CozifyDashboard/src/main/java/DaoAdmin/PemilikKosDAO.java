package DaoAdmin;

import DAO.DatabaseConnection;
import ModelAdmin.PemilikKos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class PemilikKosDAO {
    
    public ObservableList<PemilikKos> getAllPemilikKos() {
        ObservableList<PemilikKos> list = FXCollections.observableArrayList();
        String query = "SELECT p.*, COUNT(k.id) as jumlah_kos FROM pemilik_kos p " +
                      "LEFT JOIN kos k ON p.id = k.pemilik_id GROUP BY p.id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                PemilikKos pemilik = new PemilikKos(
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getInt("jumlah_kos"),
                    rs.getString("status")
                );
                list.add(pemilik);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public int countByStatus(String status) {
        String query = "SELECT COUNT(*) as total FROM pemilik_kos WHERE status = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public int getTotalPemilikKos() {
        String query = "SELECT COUNT(*) as total FROM pemilik_kos";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public boolean updateStatus(int id, String status) {
        String query = "UPDATE pemilik_kos SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deletePemilik(int id) {
        String query = "DELETE FROM pemilik_kos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}