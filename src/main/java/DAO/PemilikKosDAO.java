package DAO;

import Model.PemilikKos;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PemilikKosDAO {
    private Connection connection;

    public PemilikKosDAO() {
        this.connection = BaseDAO.getCon();
    }

    public int getTotalOwners() {
        String query = "SELECT COUNT(*) FROM users WHERE role = 'OWNER'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getVerifiedCount() {
        String query = "SELECT COUNT(*) FROM users WHERE role = 'OWNER' AND status = 'Terverifikasi'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public int getPendingCount() {
        String query = "SELECT COUNT(*) FROM users WHERE role = 'OWNER' AND status = 'Menunggu Verifikasi'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public int getRejectedCount() {
        String query = "SELECT COUNT(*) FROM users WHERE role = 'OWNER' AND status = 'Ditolak'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

   public List<PemilikKos> getAllPemilikKos() {
        List<PemilikKos> list = new ArrayList<>();
        String query = "SELECT u.*, COUNT(k.idKos) as jumlahKos " +
                       "FROM users u " +
                       "LEFT JOIN kos k ON u.idUser = k.idPemilik " +
                       "WHERE u.role = 'OWNER' " +
                       "GROUP BY u.idUser " +
                       "ORDER BY u.idUser DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                PemilikKos p = new PemilikKos();
                p.setId(rs.getInt("idUser"));
                p.setNama(rs.getString("username"));
                p.setEmail(rs.getString("email"));
                p.setNoTelepon(rs.getString("noTelepon"));
                p.setStatus(rs.getString("status"));
                p.setJumlahKos(rs.getInt("jumlahKos")); 
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<PemilikKos> getPendingPemilikKos() {
        List<PemilikKos> list = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = 'OWNER' AND status = 'Menunggu Verifikasi' ORDER BY tanggalDaftar DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                PemilikKos p = new PemilikKos();
                p.setId(rs.getInt("idUser"));
                p.setNama(rs.getString("username"));
                p.setEmail(rs.getString("email"));
                p.setNoTelepon(rs.getString("noTelepon"));
                p.setStatus(rs.getString("status"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean verifyPemilik(int id) {
        String query = "UPDATE users SET status = 'Terverifikasi' WHERE idUser = ? AND role = 'OWNER'";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean rejectPemilik(int id, String alasan) {
        String query = "UPDATE users SET status = 'Ditolak', alasanPenolakan = ? WHERE idUser = ? AND role = 'OWNER'";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, alasan);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deletePemilik(int id) {
        String query = "DELETE FROM users WHERE idUser = ? AND role = 'OWNER'";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertPemilik(PemilikKos pemilik) {
        String query = "INSERT INTO users (username, email, noTelepon, password, role, status, tanggalDaftar) " +
                       "VALUES (?, ?, ?, ?, 'OWNER', 'Menunggu Verifikasi', CURDATE())";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, pemilik.getNama());
            pstmt.setString(2, pemilik.getEmail());
            pstmt.setString(3, pemilik.getNoTelepon());
            pstmt.setString(4, pemilik.getPassword() == null ? "default123" : pemilik.getPassword());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePemilik(PemilikKos pemilik) {
        String query = "UPDATE users SET username = ?, email = ?, noTelepon = ? WHERE idUser = ? AND role = 'OWNER'";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, pemilik.getNama());
            pstmt.setString(2, pemilik.getEmail());
            pstmt.setString(3, pemilik.getNoTelepon());
            pstmt.setInt(4, pemilik.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
