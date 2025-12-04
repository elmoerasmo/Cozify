package DAO;

import Model.User;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.UUID;

public class UserDAO {
    private Connection con;

    public UserDAO() {
        this.con = BaseDAO.getCon(); // pastikan BaseDAO.getCon() valid
    }

    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (idUser, username, email, password, kontak) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            String id = UUID.randomUUID().toString();
            ps.setString(1, id);
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setNull(5, java.sql.Types.VARCHAR);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;   
        }
    }
    
    public User loginUserByUsernameOrEmail(String input, String password) { 
        String sql = "SELECT * FROM users WHERE (username=? OR email=?) AND password=?"; 
        try (PreparedStatement stmt = con.prepareStatement(sql)) { 
            stmt.setString(1, input);
            stmt.setString(2, input); 
            stmt.setString(3, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) { 
                return new User(rs.getString("username"), rs.getString("email"), rs.getString("password")); 
            } 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } return null; } // Opsional: login hanya pakai username (legacy) 
    
    public User loginUser(String username, String password) { 
        String sql = "SELECT * FROM users WHERE username=? AND password=?"; 
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, username); 
            stmt.setString(2, password); 
            ResultSet rs = stmt.executeQuery(); 
            if (rs.next()) { 
                return new User(rs.getString("username"), 
                        rs.getString("email"), rs.getString("password")); 
            } 
        } catch (Exception e) {
            e.printStackTrace(); 
        } return null; 
    }
    
}
