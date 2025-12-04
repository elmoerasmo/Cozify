package DaoOwner;

import DAO.DatabaseConnection;
import ModelOwner.MyKos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class MyKosDAO {
    private int currentOwnerId;
    
    public MyKosDAO(int ownerId) {
        this.currentOwnerId = ownerId;
    }
    
    public ObservableList<MyKos> getMyKosList() {
        ObservableList<MyKos> list = FXCollections.observableArrayList();
        String query = "SELECT k.*, " +
                      "(SELECT COUNT(*) FROM kamar WHERE kos_id = k.id) as total_kamar, " +
                      "(SELECT COUNT(*) FROM kamar WHERE kos_id = k.id AND status = 'TERISI') as terisi, " +
                      "(SELECT COUNT(*) FROM kamar WHERE kos_id = k.id AND status = 'TERSEDIA') as tersedia " +
                      "FROM kos k WHERE k.pemilik_id = ? ORDER BY k.id DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, currentOwnerId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                MyKos kos = new MyKos(
                    rs.getInt("id"),
                    rs.getString("nama_kos"),
                    rs.getString("alamat"),
                    rs.getDouble("harga"),
                    rs.getInt("total_kamar"),
                    rs.getInt("terisi"),
                    rs.getInt("tersedia")
                );
                kos.setStatus(rs.getString("status"));
                list.add(kos);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public int getTotalKos() {
        String query = "SELECT COUNT(*) as total FROM kos WHERE pemilik_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, currentOwnerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public int getTotalKamar() {
        String query = "SELECT COUNT(*) as total FROM kamar k " +
                      "INNER JOIN kos ko ON k.kos_id = ko.id WHERE ko.pemilik_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, currentOwnerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public int getKamarTerisi() {
        String query = "SELECT COUNT(*) as total FROM kamar k " +
                      "INNER JOIN kos ko ON k.kos_id = ko.id " +
                      "WHERE ko.pemilik_id = ? AND k.status = 'TERISI'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, currentOwnerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public double getPendapatanBulanIni() {
        String query = "SELECT SUM(jumlah) as total FROM transaksi t " +
                      "INNER JOIN kamar k ON t.kamar_id = k.id " +
                      "INNER JOIN kos ko ON k.kos_id = ko.id " +
                      "WHERE ko.pemilik_id = ? " +
                      "AND MONTH(t.tanggal) = MONTH(CURRENT_DATE()) " +
                      "AND YEAR(t.tanggal) = YEAR(CURRENT_DATE()) " +
                      "AND t.kategori = 'PEMASUKAN'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, currentOwnerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public boolean addKos(MyKos kos) {
        String query = "INSERT INTO kos (nama_kos, alamat, harga, pemilik_id, status) " +
                      "VALUES (?, ?, ?, ?, 'PENDING')";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, kos.getNamaKos());
            pstmt.setString(2, kos.getAlamat());
            pstmt.setDouble(3, kos.getHarga());
            pstmt.setInt(4, currentOwnerId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateKos(MyKos kos) {
        String query = "UPDATE kos SET nama_kos = ?, alamat = ?, harga = ? WHERE id = ? AND pemilik_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, kos.getNamaKos());
            pstmt.setString(2, kos.getAlamat());
            pstmt.setDouble(3, kos.getHarga());
            pstmt.setInt(4, kos.getId());
            pstmt.setInt(5, currentOwnerId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteKos(int id) {
        String query = "DELETE FROM kos WHERE id = ? AND pemilik_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            pstmt.setInt(2, currentOwnerId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}