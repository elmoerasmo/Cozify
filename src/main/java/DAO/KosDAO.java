package DAO;

import Model.Kos;
import DAO.BaseDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KosDAO {

    private Connection connection;

    public KosDAO() {
        this.connection = BaseDAO.getCon();
    }

    // ===================== CRUD =====================

    public boolean insertKos(Kos kos) {
        String query = "INSERT INTO kos (idPemilik, nama, alamat, deskripsi, harga, tipeKos, " +
                       "fasilitas, totalKamar, kamarTersedia, kamarTerisi, foto, status, rating, tanggal_submit) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURDATE())";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, kos.getIdPemilik());
            stmt.setString(2, kos.getNama());
            stmt.setString(3, kos.getAlamat());
            stmt.setString(4, kos.getDeskripsi());
            stmt.setDouble(5, kos.getHarga());
            stmt.setString(6, kos.getTipeKos());
            stmt.setString(7, kos.getFasilitas());
            stmt.setInt(8, kos.getTotalKamar());
            stmt.setInt(9, kos.getKamarTersedia());
            stmt.setInt(10, kos.getKamarTerisi());
            stmt.setString(11, kos.getFoto());
            stmt.setString(12, kos.getStatus());
            stmt.setDouble(13, kos.getRating());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateKos(Kos kos) {
        String query = "UPDATE kos SET nama = ?, alamat = ?, deskripsi = ?, harga = ?, tipeKos = ?, " +
                       "fasilitas = ?, totalKamar = ?, kamarTersedia = ?, kamarTerisi = ?, foto = ?, " +
                       "status = ?, rating = ?, alasanPenolakan = ? WHERE idKos = ? AND idPemilik = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, kos.getNama());
            stmt.setString(2, kos.getAlamat());
            stmt.setString(3, kos.getDeskripsi());
            stmt.setDouble(4, kos.getHarga());
            stmt.setString(5, kos.getTipeKos());
            stmt.setString(6, kos.getFasilitas());
            stmt.setInt(7, kos.getTotalKamar());
            stmt.setInt(8, kos.getKamarTersedia());
            stmt.setInt(9, kos.getKamarTerisi());
            stmt.setString(10, kos.getFoto());
            stmt.setString(11, kos.getStatus());
            stmt.setDouble(12, kos.getRating());
            stmt.setString(13, kos.getAlasanPenolakan());
            stmt.setInt(14, kos.getIdKos());
            stmt.setInt(15, kos.getIdPemilik());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteKos(int idKos) {
        String query = "DELETE FROM kos WHERE idKos = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idKos);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Kos> getKosByPemilik(int ownerId) {
        List<Kos> kosList = new ArrayList<>();
        String query = "SELECT * FROM kos WHERE idPemilik = ? ORDER BY idKos DESC";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                kosList.add(mapResultSetToKos(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kosList;
    }
    
    // Alias method untuk konsistensi penamaan
    public List<Kos> getKosByOwner(int ownerId) {
        return getKosByPemilik(ownerId);
    }

    public List<Kos> getAllKos() {
        List<Kos> kosList = new ArrayList<>();
        String query = "SELECT * FROM kos ORDER BY idKos DESC";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                kosList.add(mapResultSetToKos(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kosList;
    }

    public List<Kos> getPendingKos() {
        List<Kos> kosList = new ArrayList<>();
        String query = "SELECT * FROM kos WHERE status = 'Menunggu Verifikasi' ORDER BY idKos DESC";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                kosList.add(mapResultSetToKos(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kosList;
    }

    public boolean verifyKos(int kosId) {
        String query = "UPDATE kos SET status = 'Terverifikasi' WHERE idKos = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, kosId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean rejectKos(int kosId, String alasanPenolakan) {
        String query = "UPDATE kos SET status = 'Ditolak', alasanPenolakan = ? WHERE idKos = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, alasanPenolakan);
            stmt.setInt(2, kosId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===================== STATISTICS / UTILITY =====================

    public boolean hasRejectedKos(int ownerId) {
        String query = "SELECT COUNT(*) FROM kos WHERE idPemilik = ? AND status = 'Ditolak'";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
    
    // Alias method
    public boolean hasRejectedKosByOwner(int ownerId) {
        return hasRejectedKos(ownerId);
    }

    public int getRejectedKosCount(int ownerId) {
        String query = "SELECT COUNT(*) FROM kos WHERE idPemilik = ? AND status = 'Ditolak'";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
    
    // Alias method
    public int getRejectedKosCountByOwner(int ownerId) {
        return getRejectedKosCount(ownerId);
    }

    public int getTotalKosByOwner(int ownerId) {
        String query = "SELECT COUNT(*) FROM kos WHERE idPemilik = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public int getTotalKamar(int ownerId) {
        String query = "SELECT SUM(totalKamar) FROM kos WHERE idPemilik = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
    
    // Alias method
    public int getTotalKamarByOwner(int ownerId) {
        return getTotalKamar(ownerId);
    }

    public int getKamarTerisi(int ownerId) {
        String query = "SELECT SUM(totalKamar - kamarTersedia) FROM kos WHERE idPemilik = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
    
    // Alias method
    public int getKamarTerisiByOwner(int ownerId) {
        return getKamarTerisi(ownerId);
    }

    public List<String> getKosNames(int ownerId) {
        List<String> names = new ArrayList<>();
        String query = "SELECT nama FROM kos WHERE idPemilik = ? ORDER BY nama";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) names.add(rs.getString("nama"));
        } catch (SQLException e) { e.printStackTrace(); }
        return names;
    }
    
    // Alias method
    public List<String> getKosNamesByOwner(int ownerId) {
        return getKosNames(ownerId);
    }
    
    // Method untuk mendapatkan statistik count
    public int getVerifiedCount() {
        String query = "SELECT COUNT(*) FROM kos WHERE status = 'Terverifikasi'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
    
    public int getPendingCount() {
        String query = "SELECT COUNT(*) FROM kos WHERE status = 'Menunggu Verifikasi'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
    
    public int getRejectedCount() {
        String query = "SELECT COUNT(*) FROM kos WHERE status = 'Ditolak'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    // ===================== HELPER =====================
    private Kos mapResultSetToKos(ResultSet rs) throws SQLException {
        Kos kos = new Kos();
        kos.setIdKos(rs.getInt("idKos"));
        kos.setIdPemilik(rs.getInt("idPemilik"));
        kos.setNama(rs.getString("nama"));
        kos.setAlamat(rs.getString("alamat"));
        kos.setDeskripsi(rs.getString("deskripsi"));
        kos.setHarga(rs.getDouble("harga"));
        kos.setTipeKos(rs.getString("tipeKos"));
        kos.setFasilitas(rs.getString("fasilitas"));
        kos.setFoto(rs.getString("foto"));
        kos.setTotalKamar(rs.getInt("totalKamar"));
        kos.setKamarTersedia(rs.getInt("kamarTersedia"));
        kos.setKamarTerisi(rs.getInt("kamarTerisi"));
        kos.setStatus(rs.getString("status"));
        kos.setAlasanPenolakan(rs.getString("alasanPenolakan"));
        kos.setRating(rs.getDouble("rating"));
        return kos;
    }
}