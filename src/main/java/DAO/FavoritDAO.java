/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Kos;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;  

/**
 *
 * @author LENOVO
 */
public class FavoritDAO {

    private Connection connection;

    public FavoritDAO() {
        this.connection = BaseDAO.getCon();
    }

    public boolean addFavorite(int idUser, int idKos) {
        String query = "INSERT INTO favorit (idUser, idKos) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idUser);
            stmt.setInt(2, idKos);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeFavorite(int idUser, int idKos) {
        String query = "DELETE FROM favorit WHERE idUser = ? AND idKos = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idUser);
            stmt.setInt(2, idKos);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Integer> getFavoritesByUser(int idUser) {
        List<Integer> favoriteIds = new ArrayList<>();
        String query = "SELECT idKos FROM favorit WHERE idUser = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idUser);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                favoriteIds.add(rs.getInt("idKos"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favoriteIds;
    }

    public boolean isFavorite(int idUser, int idKos) {
        String query = "SELECT COUNT(*) FROM favorit WHERE idUser = ? AND idKos = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idUser);
            stmt.setInt(2, idKos);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}