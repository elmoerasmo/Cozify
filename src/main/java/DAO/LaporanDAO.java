package DAO;

import Model.Laporan;
import DAO.BaseDAO;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LaporanDAO {
    private Connection connection;
    
    public LaporanDAO() {
        this.connection = BaseDAO.getCon();
    }
    
    public double getPendapatanBulanIni(int idPemilik) {
        String query = "SELECT SUM(l.pemasukan) " +
                      "FROM laporan l " +
                      "INNER JOIN kos k ON l.idKos = k.idKos " +
                      "WHERE k.idPemilik = ? " +
                      "AND MONTH(l.tanggal) = MONTH(CURDATE()) " +
                      "AND YEAR(l.tanggal) = YEAR(CURDATE())";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idPemilik);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    
    public List<Laporan> getLaporanByPeriode(int ownerId, String periode, String filterKos) {
    List<Laporan> list = new ArrayList<>();
    StringBuilder query = new StringBuilder();
    
    System.out.println("\n=== LaporanDAO.getLaporanByPeriode ===");
    System.out.println("Owner ID: " + ownerId);
    System.out.println("Periode: " + periode);
    System.out.println("Filter Kos: " + filterKos);
    
    query.append("SELECT l.*, k.nama as nama ")
         .append("FROM laporan l ")
         .append("INNER JOIN kos k ON l.idKos = k.idKos ")
         .append("WHERE k.idPemilik = ? ");
    
    switch (periode) {
        case "Hari Ini":
            query.append("AND DATE(l.tanggal) = CURDATE() ");
            break;
        case "Minggu Ini":
            query.append("AND YEARWEEK(l.tanggal) = YEARWEEK(CURDATE()) ");
            break;
        case "Bulan Ini":
            query.append("AND MONTH(l.tanggal) = MONTH(CURDATE()) ")
                 .append("AND YEAR(l.tanggal) = YEAR(CURDATE()) ");
            break;
        case "Tahun Ini":
            query.append("AND YEAR(l.tanggal) = YEAR(CURDATE()) ");
            break;
    }
    
    if (!"Semua Kos".equals(filterKos)) {
        query.append("AND k.nama = ? ");
    }
    
    query.append("ORDER BY l.tanggal DESC");
    
    System.out.println("Query: " + query.toString());
    
    try (PreparedStatement pstmt = connection.prepareStatement(query.toString())) {
        pstmt.setInt(1, ownerId);
        
        if (!"Semua Kos".equals(filterKos)) {
            pstmt.setString(2, filterKos);
        }
        
        System.out.println("Executing query...");
        ResultSet rs = pstmt.executeQuery();
        
        int count = 0;
        while (rs.next()) {
            Laporan laporan = extractLaporanFromResultSet(rs);
            list.add(laporan);
            count++;
        }
        
        System.out.println("✅ Records found: " + count);
        
    } catch (SQLException e) {
        System.err.println("❌ SQL Error: " + e.getMessage());
        e.printStackTrace();
    }
    
    return list;
    }

    public List<Laporan> getAllLaporanByOwner(int ownerId) {
        List<Laporan> list = new ArrayList<>();
        String query = "SELECT l.*, k.nama as nama " +
                      "FROM laporan l " +
                      "INNER JOIN kos k ON l.idKos = k.idKos " +
                      "WHERE k.idPemilik = ? " +
                      "ORDER BY l.tanggal DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, ownerId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Laporan laporan = extractLaporanFromResultSet(rs);
                list.add(laporan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

   public boolean insertLaporan(Laporan laporan) {
        String query = "INSERT INTO laporan (idKos, tanggal, kategori, keterangan, pemasukan, pengeluaran, namaPenyewa, metodePembayaran) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, laporan.getKosId());
            pstmt.setDate(2, Date.valueOf(laporan.getTanggal()));
            pstmt.setString(3, laporan.getKategori());
            pstmt.setString(4, laporan.getKeterangan());
            pstmt.setDouble(5, laporan.getPemasukan());
            pstmt.setDouble(6, laporan.getPengeluaran());
            pstmt.setString(7, laporan.getNamaPenyewa()); 
            pstmt.setString(8, laporan.getMetodePembayaran());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateLaporan(Laporan laporan) {
        String query = "UPDATE laporan SET idKos = ?, tanggal = ?, kategori = ?, " +
                      "keterangan = ?, pemasukan = ?, pengeluaran = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, laporan.getKosId());
            pstmt.setDate(2, Date.valueOf(laporan.getTanggal()));
            pstmt.setString(3, laporan.getKategori());
            pstmt.setString(4, laporan.getKeterangan());
            pstmt.setDouble(5, laporan.getPemasukan());
            pstmt.setDouble(6, laporan.getPengeluaran());
            pstmt.setInt(7, laporan.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteLaporan(int id) {
        String query = "DELETE FROM laporan WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Laporan extractLaporanFromResultSet(ResultSet rs) throws SQLException {
        Laporan laporan = new Laporan();
        laporan.setId(rs.getInt("id"));
        laporan.setKosId(rs.getInt("idKos"));
        laporan.setNamaKos(rs.getString("nama"));
        laporan.setTanggal(rs.getDate("tanggal").toLocalDate());
        laporan.setKategori(rs.getString("kategori"));
        laporan.setKeterangan(rs.getString("keterangan"));
        laporan.setPemasukan(rs.getDouble("pemasukan"));
        laporan.setPengeluaran(rs.getDouble("pengeluaran"));
        laporan.setNamaPenyewa(rs.getString("namaPenyewa"));
        laporan.setMetodePembayaran(rs.getString("metodePembayaran"));
        return laporan;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}