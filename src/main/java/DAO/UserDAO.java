package DAO;

import Model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    protected Connection con;

    public UserDAO() {
        this.con = BaseDAO.getCon();
    }

    // Cek apakah email sudah terdaftar
    public boolean isEmailExist(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // Cek apakah username sudah digunakan
    public boolean isUsernameExist(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // Register user baru
    public boolean registerUser(User user) {
        if (isUsernameExist(user.getNama())) {
            System.out.println("Username sudah digunakan!");
            return false;
        }
        if (isEmailExist(user.getEmail())) {
            System.out.println("Email sudah digunakan!");
            return false;
        }

        String sql = "INSERT INTO users (idUser, username, email, password, role, tanggal_daftar) " +
                    "VALUES (?, ?, ?, ?, ?, CURDATE())"; 
       try (PreparedStatement ps = con.prepareStatement(sql)) {
           ps.setInt(1, user.getId());
           ps.setString(2, user.getNama());
           ps.setString(3, user.getEmail());
           ps.setString(4, user.getPassword());
           ps.setString(5, user.getRole());
           return ps.executeUpdate() > 0;
       } catch (SQLException e) {
           e.printStackTrace();
       }
        return false;
    }

    // Login user
    public User loginUser(String input, String password) {
        String sql = "SELECT idUser, username, email, role, password FROM users WHERE (username = ? OR email = ?) AND password = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, input);
            ps.setString(2, input);
            ps.setString(3, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("idUser"));
                u.setNama(rs.getString("username"));
                u.setEmail(rs.getString("email"));
                u.setRole(rs.getString("role"));
                u.setPassword(rs.getString("password"));
                return u;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // Update data user
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, password = ?, role = ? WHERE idUser = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getNama());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());
            ps.setInt(5, user.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // Hapus user
    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE idUser = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // Ambil user berdasarkan id
    public User getUserById(int id) {
        String sql = "SELECT idUser, username, email, role, password FROM users WHERE idUser = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("idUser"));
                u.setNama(rs.getString("username"));
                u.setEmail(rs.getString("email"));
                u.setRole(rs.getString("role"));
                u.setPassword(rs.getString("password"));
                return u;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // Ambil semua user
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT idUser, username, email, role, password FROM users ORDER BY idUser DESC";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("idUser"));
                u.setNama(rs.getString("username"));
                u.setEmail(rs.getString("email"));
                u.setRole(rs.getString("role"));
                u.setPassword(rs.getString("password"));
                list.add(u);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    
    public boolean updateProfile(User user) {
        String sql = """
            UPDATE users 
            SET username = ?, email = ?, no_telepon = ?
            WHERE idUser = ?
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getNama());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getNoTelepon());
            ps.setInt(4, user.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean upgradeToOwner(int userId) {
    String sql = "UPDATE users SET role='OWNER', status='Menunggu Verifikasi' WHERE idUser=?";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, userId);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
}
