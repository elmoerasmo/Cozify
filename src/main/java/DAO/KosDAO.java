package DAO;

import Model.Kos;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

    public class KosDAO {
    private Connection connection;

    public KosDAO() {
        this.connection = BaseDAO.getCon();
    }

    public int insertKosAndGetId(Kos kos) {
        String query = "INSERT INTO kos (idPemilik, nama, alamat, deskripsi, harga, tipeKos, " +
                       "fasilitas, totalKamar, kamarTersedia, kamarTerisi, foto, status, rating, tanggal_submit) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURDATE())";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, kos.getIdPemilik());
            stmt.setString(2, kos.getNama());
            stmt.setString(3, kos.getAlamat());
            stmt.setString(4, kos.getDeskripsi());
            stmt.setDouble(5, kos.getHarga());
            stmt.setString(6, kos.getTipeKos());
            stmt.setString(7, kos.getFasilitas());
            stmt.setInt(8, kos.getTotalKamar());
            stmt.setInt(9, kos.getTotalKamar());
            stmt.setInt(10, 0);
            stmt.setString(11, kos.getFoto());
            stmt.setString(12, "Menunggu Verifikasi");
            stmt.setDouble(13, 0.0);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    public boolean updateKos(Kos kos) {
        String query = "UPDATE kos SET nama = ?, alamat = ?, deskripsi = ?, harga = ?, tipeKos = ?, " +
                       "fasilitas = ?, foto = ?, status = 'Menunggu Verifikasi' " + 
                       "WHERE idKos = ? AND idPemilik = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, kos.getNama());
            stmt.setString(2, kos.getAlamat());
            stmt.setString(3, kos.getDeskripsi());
            stmt.setDouble(4, kos.getHarga());
            stmt.setString(5, kos.getTipeKos());
            stmt.setString(6, kos.getFasilitas());
            stmt.setString(7, kos.getFoto()); 
            stmt.setInt(8, kos.getIdKos());
            stmt.setInt(9, kos.getIdPemilik());

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

    public List<Kos> getKosByOwner(int ownerId) {
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
    public List<Kos> getAllKos() {
        List<Kos> list = new ArrayList<>();
        String sql = "SELECT * FROM kos ORDER BY idKos DESC";
        try (Statement stmt = connection.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Kos k = new Kos();
                k.setIdKos(rs.getInt("idKos"));
                k.setNama(rs.getString("nama"));
                k.setHarga(rs.getDouble("harga"));
                k.setTipeKos(rs.getString("tipeKos"));
                k.setFasilitas(rs.getString("fasilitas"));                
                k.setFoto(rs.getString("foto"));
                k.setStatus(rs.getString("status"));
                list.add(mapResultSetToKos(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Kos> getVerifiedKos() {
        List<Kos> list = new ArrayList<>();
        String sql = "SELECT * FROM kos WHERE status = 'Terverifikasi' ORDER BY idKos DESC";
        try (Statement stmt = connection.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToKos(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<String> getKosNamesByOwner(int ownerId) {
        List<String> names = new ArrayList<>();
        String query = "SELECT nama FROM kos WHERE idPemilik = ? ORDER BY nama";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) names.add(rs.getString("nama"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
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

    public int getTotalKamarByOwner(int ownerId) {
        String query = "SELECT SUM(totalKamar) FROM kos WHERE idPemilik = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public int getKamarTerisiByOwner(int ownerId) {
        String query = "SELECT SUM(totalKamar - kamarTersedia) FROM kos WHERE idPemilik = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public boolean hasRejectedKosByOwner(int ownerId) {
        String query = "SELECT COUNT(*) FROM kos WHERE idPemilik = ? AND status = 'Ditolak'";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public int getRejectedKosCountByOwner(int ownerId) {
        String query = "SELECT COUNT(*) FROM kos WHERE idPemilik = ? AND status = 'Ditolak'";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    private Kos mapResultSetToKos(ResultSet rs) throws SQLException {
        Kos kos = new Kos();
        kos.setIdKos(rs.getInt("idKos"));
        kos.setNama(rs.getString("nama"));
        kos.setHarga(rs.getDouble("harga"));
        kos.setTipeKos(rs.getString("tipeKos"));
        kos.setFasilitas(rs.getString("fasilitas"));
        kos.setFoto(rs.getString("foto"));
        kos.setStatus(rs.getString("status"));
        kos.setAlamat(rs.getString("alamat"));
        kos.setDeskripsi(rs.getString("deskripsi"));
        kos.setTotalKamar(rs.getInt("totalKamar"));
        kos.setKamarTersedia(rs.getInt("kamarTersedia"));
        kos.setAlasanPenolakan(rs.getString("alasanPenolakan"));
        return kos;
    }
    public boolean updateKamarTersedia(int idKos, int sisaBaru) {   
        String sql = "UPDATE kos SET kamarTersedia = ?, kamarTerisi = totalKamar - ? WHERE idKos = ?";
        try (Connection conn = BaseDAO.getCon();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sisaBaru);
            ps.setInt(2, sisaBaru);
            ps.setInt(3, idKos);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateStatus(int idKos, String status) {
        String sql = "UPDATE kos SET status = ? WHERE idKos = ?";
        try (Connection conn = BaseDAO.getCon();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, idKos);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}