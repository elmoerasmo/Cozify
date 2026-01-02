package DAO;

import Model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    private Connection con;

    public AdminDAO() {
        this.con = BaseDAO.getCon();
    }

    // Ambil semua pemilik kos
    public List<User> getAllOwners() {
        List<User> owners = new ArrayList<>();
        String sql = "SELECT id, nama, email, role, status FROM users WHERE role='OWNER'";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getString("status"),
                    rs.getString("noTelepon")
                );
                owners.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return owners;
    }

    // Statistik
    public int getTotalOwners() {
        String sql = "SELECT COUNT(*) FROM users WHERE role='OWNER'";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getVerifiedCount() {
        String sql = "SELECT COUNT(*) FROM users WHERE role='OWNER' AND status='VERIFIED'";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getPendingCount() {
        String sql = "SELECT COUNT(*) FROM users WHERE role='OWNER' AND status='PENDING'";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getRejectedCount() {
        String sql = "SELECT COUNT(*) FROM users WHERE role='OWNER' AND status='REJECTED'";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
