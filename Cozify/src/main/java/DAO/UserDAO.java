/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import static DAO.BaseDAO.closeCon;
import static DAO.BaseDAO.getCon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import Model.User;



/**
 *
 * @author LENOVO
 */
public class UserDAO {
    
     private static PreparedStatement st;
    private static Connection con;

    public static User validate(String name, String passwd) {
        User u = null;
        try {
            con = getCon();
            String query = "select idUser from users where username = ? and password = ?";
            st = con.prepareStatement(query);
            
            st.setString(1, name);
            st.setString(2, passwd);
            
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                u = new User(UUID.fromString(rs.getString("idUser")), name, passwd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeCon(con);
        }
        return u;
    }

    public static User searchByUid(String uid) {
        User u = null;
        try {
            con = getCon();
            String query = "select * from users where idUser = ?";

            st = con.prepareStatement(query);
            st.setString(1, uid);
            
            ResultSet rsUser = st.executeQuery();
            u = new User(UUID.fromString(rsUser.getString("idUser")),
                    rsUser.getString("username"), rsUser.getString("password"));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeCon(con);
        }
        return u;
    }

    public static void registerUser(User u) {
        try {
            con = getCon();
            String query = "insert into users"
                    + " values (?,?,?) ";
            st = con.prepareStatement(query);
            
            st.setString(1, u.getUid().toString());
            st.setString(2, u.getUname());
            st.setString(3, u.getPass());
            
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeCon(con);
        }

    }
    
}
