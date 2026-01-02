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
        String query = "SELECT * FROM users WHERE role = 'OWNER' ORDER BY idUser DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                PemilikKos p = new PemilikKos();
                p.setId(rs.getInt("idUser"));
                p.setNama(rs.getString("username"));
                p.setEmail(rs.getString("email"));
                p.setNoTelepon(rs.getString("no_telepon"));
                p.setStatus(rs.getString("status"));
                p.setPassword(rs.getString("password"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<PemilikKos> getPendingPemilikKos() {
        List<PemilikKos> list = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = 'OWNER' AND status = 'Menunggu Verifikasi' ORDER BY tanggal_daftar DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                PemilikKos p = new PemilikKos();
                p.setId(rs.getInt("idUser"));
                p.setNama(rs.getString("username"));
                p.setEmail(rs.getString("email"));
                p.setNoTelepon(rs.getString("no_telepon"));
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
        String query = "UPDATE users SET status = 'Ditolak', alasan_penolakan = ? WHERE idUser = ? AND role = 'OWNER'";
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
        String query = "INSERT INTO users (username, email, no_telepon, password, role, status, tanggal_daftar) " +
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
        String query = "UPDATE users SET username = ?, email = ?, no_telepon = ? WHERE idUser = ? AND role = 'OWNER'";
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
