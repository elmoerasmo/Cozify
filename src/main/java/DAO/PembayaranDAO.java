package DAO;

import Model.Pembayaran;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PembayaranDAO {

    public boolean insert(Pembayaran pembayaran) {
        if (pembayaran == null || pembayaran.getUser() == null || pembayaran.getKos() == null) {
            System.err.println("Data pembayaran, user atau kos tidak lengkap!");
            return false;
        }

        String sql = """
            INSERT INTO pembayaran
            (idUser, idKos, totalPembayaran, metodePembayaran, status, tanggalMasuk, tanggalKeluar)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = BaseDAO.getCon();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, pembayaran.getUser().getId());
            ps.setInt(2, pembayaran.getKos().getIdKos());
            ps.setDouble(3, pembayaran.getTotalPembayaran());
            ps.setString(4, pembayaran.getMetodePembayaran() != null ? pembayaran.getMetodePembayaran() : "");
            ps.setString(5, pembayaran.getStatus() != null ? pembayaran.getStatus() : "PENDING");
            ps.setObject(6, pembayaran.getTanggalMasuk());
            ps.setObject(7, pembayaran.getTanggalKeluar());

            int rows = ps.executeUpdate();

            // Ambil auto-generated ID dan set ke object
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        pembayaran.setIdPembayaran(rs.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

   
    public boolean updateStatus(int idPembayaran, String status) {
        String sql = "UPDATE pembayaran SET status = ? WHERE id = ?";
        try (Connection conn = BaseDAO.getCon();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, idPembayaran);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

   
    public Pembayaran getById(int idPembayaran) {
        String sql = """
            SELECT p.id, p.idUser, p.idKos, p.totalPembayaran, p.metodePembayaran,
                   p.status, p.tanggalMasuk, p.tanggalKeluar,
                   u.id AS userId, u.nama AS userNama, u.email AS userEmail, u.noTelepon AS userTelepon,
                   k.idKos, k.nama AS kosNama, k.alamat AS kosAlamat, k.harga AS kosHarga
            FROM pembayaran p
            JOIN user u ON u.id = p.idUser
            JOIN kos k ON k.idKos = p.idKos
            WHERE p.id = ?
        """;

        try (Connection conn = BaseDAO.getCon();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPembayaran);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Pembayaran p = new Pembayaran();

                    p.setIdPembayaran(rs.getInt("id"));
                    p.setTotalPembayaran(rs.getDouble("totalPembayaran"));
                    p.setMetodePembayaran(rs.getString("metodePembayaran"));
                    p.setStatus(rs.getString("status"));
                    p.setTanggalMasuk(rs.getObject("tanggalMasuk", java.time.LocalDate.class));
                    p.setTanggalKeluar(rs.getObject("tanggalKeluar", java.time.LocalDate.class));

                    // Set user
                    Model.User user = new Model.User();
                    user.setId(rs.getInt("userId"));
                    user.setNama(rs.getString("userNama"));
                    user.setEmail(rs.getString("userEmail"));
                    user.setNoTelepon(rs.getString("userTelepon"));
                    p.setUser(user);

                    // Set kos
                    Model.Kos kos = new Model.Kos();
                    kos.setIdKos(rs.getInt("idKos"));
                    kos.setNama(rs.getString("kosNama"));
                    kos.setAlamat(rs.getString("kosAlamat"));
                    kos.setHarga(rs.getDouble("kosHarga"));
                    p.setKos(kos);

                    return p;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
