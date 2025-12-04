package DaoAdmin;

import DAO.DatabaseConnection;
import ModelAdmin.Kos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class KosDAO {
    
    public ObservableList<Kos> getAllKos() {
        ObservableList<Kos> list = FXCollections.observableArrayList();
        String query = "SELECT k.*, p.nama as pemilik_nama FROM kos k " +
                      "LEFT JOIN pemilik_kos p ON k.pemilik_id = p.id ORDER BY k.id DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Kos kos = new Kos(
                    rs.getInt("id"),
                    rs.getString("nama_kos"),
                    rs.getString("pemilik_nama"),
                    rs.getString("alamat"),
                    rs.getDouble("harga"),
                    rs.getString("status")
                );
                kos.setPemilikId(rs.getInt("pemilik_id"));
                list.add(kos);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public int countByStatus(String status) {
        String query = "SELECT COUNT(*) as total FROM kos WHERE status = ?";
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
    
    public boolean updateStatus(int id, String status) {
        String query = "UPDATE kos SET status = ? WHERE id = ?";
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
    
    public Kos getKosById(int id) {
        String query = "SELECT k.*, p.nama as pemilik_nama FROM kos k " +
                      "LEFT JOIN pemilik_kos p ON k.pemilik_id = p.id WHERE k.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Kos kos = new Kos(
                    rs.getInt("id"),
                    rs.getString("nama_kos"),
                    rs.getString("pemilik_nama"),
                    rs.getString("alamat"),
                    rs.getDouble("harga"),
                    rs.getString("status")
                );
                kos.setPemilikId(rs.getInt("pemilik_id"));
                return kos;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}