package DAO;

import Model.Kamar;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KamarDAO {
    private Connection connection;

    public KamarDAO() {
        this.connection = BaseDAO.getCon();
    }

    public List<Kamar> getKamarTersediaByKos(int idKos) {
        List<Kamar> list = new ArrayList<>();
        String sql = "SELECT * FROM kamar WHERE idKos = ? AND statusKamar = 'Tersedia'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idKos);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Kamar(
                    rs.getInt("idKamar"),
                    rs.getInt("idKos"),
                    rs.getString("nomorKamar"),
                    rs.getString("statusKamar")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean updateStatusKamar(int idKamar, String status) {
        String sql = "UPDATE kamar SET statusKamar = ? WHERE idKamar = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, idKamar);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean batchInsertKamar(int idKos, int jumlah) {
        String sql = "INSERT INTO kamar (idKos, nomorKamar, statusKamar) VALUES (?, ?, 'Tersedia')";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 1; i <= jumlah; i++) {
                ps.setInt(1, idKos);
                ps.setString(2, "Kamar " + i);
                ps.addBatch();
            }
            ps.executeBatch();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}