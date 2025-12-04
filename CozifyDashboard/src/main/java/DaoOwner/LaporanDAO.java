package DaoOwner;

import DAO.DatabaseConnection;
import ModelOwner.Laporan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class LaporanDAO {
    private int currentOwnerId;
    
    public LaporanDAO(int ownerId) {
        this.currentOwnerId = ownerId;
    }
    
    public ObservableList<Laporan> getLaporanByPeriode(String periode, Integer kosId) {
        ObservableList<Laporan> list = FXCollections.observableArrayList();
        
        StringBuilder query = new StringBuilder(
            "SELECT t.*, ko.nama_kos " +
            "FROM transaksi t " +
            "INNER JOIN kamar k ON t.kamar_id = k.id " +
            "INNER JOIN kos ko ON k.kos_id = ko.id " +
            "WHERE ko.pemilik_id = ? "
        );
        
        //filter tanggal
        switch (periode) {
            case "Bulan Ini":
                query.append("AND MONTH(t.tanggal) = MONTH(CURRENT_DATE()) " +
                           "AND YEAR(t.tanggal) = YEAR(CURRENT_DATE()) ");
                break;
            case "Bulan Lalu":
                query.append("AND MONTH(t.tanggal) = MONTH(DATE_SUB(CURRENT_DATE(), INTERVAL 1 MONTH)) " +
                           "AND YEAR(t.tanggal) = YEAR(DATE_SUB(CURRENT_DATE(), INTERVAL 1 MONTH)) ");
                break;
            case "3 Bulan Terakhir":
                query.append("AND t.tanggal >= DATE_SUB(CURRENT_DATE(), INTERVAL 3 MONTH) ");
                break;
            case "6 Bulan Terakhir":
                query.append("AND t.tanggal >= DATE_SUB(CURRENT_DATE(), INTERVAL 6 MONTH) ");
                break;
            case "Tahun Ini":
                query.append("AND YEAR(t.tanggal) = YEAR(CURRENT_DATE()) ");
                break;
        }
        
        //filter kos
        if (kosId != null && kosId > 0) {
            query.append("AND ko.id = ? ");
        }
        
        query.append("ORDER BY t.tanggal DESC");
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query.toString())) {
            
            pstmt.setInt(1, currentOwnerId);
            if (kosId != null && kosId > 0) {
                pstmt.setInt(2, kosId);
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Laporan laporan = new Laporan();
                laporan.setId(rs.getInt("id"));
                laporan.setTanggal(rs.getDate("tanggal").toLocalDate());
                laporan.setKosNama(rs.getString("nama_kos"));
                laporan.setKategori(rs.getString("kategori"));
                laporan.setKeterangan(rs.getString("keterangan"));
                
                if ("PEMASUKAN".equals(rs.getString("kategori"))) {
                    laporan.setPemasukan(rs.getDouble("jumlah"));
                    laporan.setPengeluaran(0);
                } else {
                    laporan.setPemasukan(0);
                    laporan.setPengeluaran(rs.getDouble("jumlah"));
                }
                
                list.add(laporan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public double getTotalPemasukan(String periode, Integer kosId) {
        StringBuilder query = new StringBuilder(
            "SELECT SUM(t.jumlah) as total FROM transaksi t " +
            "INNER JOIN kamar k ON t.kamar_id = k.id " +
            "INNER JOIN kos ko ON k.kos_id = ko.id " +
            "WHERE ko.pemilik_id = ? AND t.kategori = 'PEMASUKAN' "
        );
        
        addPeriodeFilter(query, periode);
        
        if (kosId != null && kosId > 0) {
            query.append("AND ko.id = ? ");
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query.toString())) {
            
            pstmt.setInt(1, currentOwnerId);
            if (kosId != null && kosId > 0) {
                pstmt.setInt(2, kosId);
            }
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public double getTotalPengeluaran(String periode, Integer kosId) {
        StringBuilder query = new StringBuilder(
            "SELECT SUM(t.jumlah) as total FROM transaksi t " +
            "INNER JOIN kamar k ON t.kamar_id = k.id " +
            "INNER JOIN kos ko ON k.kos_id = ko.id " +
            "WHERE ko.pemilik_id = ? AND t.kategori = 'PENGELUARAN' "
        );
        
        addPeriodeFilter(query, periode);
        
        if (kosId != null && kosId > 0) {
            query.append("AND ko.id = ? ");
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query.toString())) {
            
            pstmt.setInt(1, currentOwnerId);
            if (kosId != null && kosId > 0) {
                pstmt.setInt(2, kosId);
            }
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    private void addPeriodeFilter(StringBuilder query, String periode) {
        switch (periode) {
            case "Bulan Ini":
                query.append("AND MONTH(t.tanggal) = MONTH(CURRENT_DATE()) " +
                           "AND YEAR(t.tanggal) = YEAR(CURRENT_DATE()) ");
                break;
            case "Bulan Lalu":
                query.append("AND MONTH(t.tanggal) = MONTH(DATE_SUB(CURRENT_DATE(), INTERVAL 1 MONTH)) " +
                           "AND YEAR(t.tanggal) = YEAR(DATE_SUB(CURRENT_DATE(), INTERVAL 1 MONTH)) ");
                break;
            case "3 Bulan Terakhir":
                query.append("AND t.tanggal >= DATE_SUB(CURRENT_DATE(), INTERVAL 3 MONTH) ");
                break;
            case "6 Bulan Terakhir":
                query.append("AND t.tanggal >= DATE_SUB(CURRENT_DATE(), INTERVAL 6 MONTH) ");
                break;
            case "Tahun Ini":
                query.append("AND YEAR(t.tanggal) = YEAR(CURRENT_DATE()) ");
                break;
        }
    }
}