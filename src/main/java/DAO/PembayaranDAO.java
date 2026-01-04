package DAO;

import Model.Pembayaran;
import Model.User;
import Model.Kos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;

public class PembayaranDAO {

    public boolean insert(Pembayaran pembayaran) {
        if (pembayaran == null || pembayaran.getUser() == null || pembayaran.getKos() == null) {
            System.err.println("Gagal Insert: Data pembayaran, user, atau kos null!");
            return false;
        }

        String sql = "INSERT INTO pembayaran (idUser, idKos, durasiBulan, tanggalTransaksi, hargaPerBulan, totalPembayaran, metodePembayaran, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = BaseDAO.getCon();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, pembayaran.getUser().getId());
            ps.setInt(2, pembayaran.getKos().getIdKos());
            ps.setInt(3, pembayaran.getDurasiBulan());
            ps.setDate(4, Date.valueOf(LocalDate.now()));
            ps.setDouble(5, pembayaran.getKos().getHarga());
            ps.setDouble(6, pembayaran.getTotalPembayaran());
            ps.setString(7, pembayaran.getMetodePembayaran() != null ? pembayaran.getMetodePembayaran() : "BELUM DIPILIH");
            ps.setString(8, pembayaran.getStatus() != null ? pembayaran.getStatus() : "BERHASIL");

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        pembayaran.setIdPembayaran(rs.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error insert Pembayaran: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    
    public boolean updateStatus(int idPembayaran, String status) {
        String sql = "UPDATE pembayaran SET status = ? WHERE idPembayaran = ?";
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
            SELECT p.idPembayaran, p.idUser, p.idKos, p.durasiBulan, p.tanggalTransaksi, p.totalPembayaran, p.metodePembayaran, p.status,
                   u.nama AS userNama, u.email AS userEmail, u.noTelepon AS userTelepon,
                   k.nama AS kosNama, k.alamat AS kosAlamat, k.harga AS kosHarga
            FROM pembayaran p
            JOIN user u ON u.id = p.idUser
            JOIN kos k ON k.idKos = p.idKos
            WHERE p.idPembayaran = ?
        """;

        try (Connection conn = BaseDAO.getCon();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPembayaran);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Pembayaran p = new Pembayaran();

                    p.setIdPembayaran(rs.getInt("idPembayaran"));
                    p.setDurasiBulan(rs.getInt("durasiBulan"));
                    p.setTanggalTransaksi(rs.getDate("tanggalTransaksi").toLocalDate());
                    p.setTotalPembayaran(rs.getDouble("totalPembayaran"));
                    p.setMetodePembayaran(rs.getString("metodePembayaran"));
                    p.setStatus(rs.getString("status"));

                    User user = new User();
                    user.setId(rs.getInt("idUser"));
                    user.setNama(rs.getString("userNama"));
                    user.setEmail(rs.getString("userEmail"));
                    user.setNoTelepon(rs.getString("userTelepon"));
                    p.setUser(user);

                    Kos kos = new Kos();
                    kos.setIdKos(rs.getInt("idKos"));
                    kos.setNama(rs.getString("kosNama"));
                    kos.setAlamat(rs.getString("kosAlamat"));
                    kos.setHarga(rs.getDouble("kosHarga"));
                    p.setKos(kos);

                    return p;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getById Pembayaran: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}