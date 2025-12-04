package ModelOwner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Laporan {
    private int id;
    private LocalDate tanggal;
    private String kosNama;
    private String kategori;
    private String keterangan;
    private double pemasukan;
    private double pengeluaran;
    private int kosId;
    
    public Laporan() {}
    
    public Laporan(int id, LocalDate tanggal, String kosNama, String kategori, 
                   String keterangan, double pemasukan, double pengeluaran) {
        this.id = id;
        this.tanggal = tanggal;
        this.kosNama = kosNama;
        this.kategori = kategori;
        this.keterangan = keterangan;
        this.pemasukan = pemasukan;
        this.pengeluaran = pengeluaran;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public LocalDate getTanggal() {
        return tanggal;
    }
    
    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }
    
    public String getKosNama() {
        return kosNama;
    }
    
    public void setKosNama(String kosNama) {
        this.kosNama = kosNama;
    }
    
    public String getKategori() {
        return kategori;
    }
    
    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
    
    public String getKeterangan() {
        return keterangan;
    }
    
    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
    
    public double getPemasukan() {
        return pemasukan;
    }
    
    public void setPemasukan(double pemasukan) {
        this.pemasukan = pemasukan;
    }
    
    public double getPengeluaran() {
        return pengeluaran;
    }
    
    public void setPengeluaran(double pengeluaran) {
        this.pengeluaran = pengeluaran;
    }
    
    public int getKosId() {
        return kosId;
    }
    
    public void setKosId(int kosId) {
        this.kosId = kosId;
    }
    
    public String getFormattedTanggal() {
        if (tanggal != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return tanggal.format(formatter);
        }
        return "";
    }
    
    public String getFormattedPemasukan() {
        return pemasukan > 0 ? String.format("Rp %,.0f", pemasukan) : "-";
    }
    
    public String getFormattedPengeluaran() {
        return pengeluaran > 0 ? String.format("Rp %,.0f", pengeluaran) : "-";
    }
    
    @Override
    public String toString() {
        return "Laporan{" +
                "id=" + id +
                ", tanggal=" + tanggal +
                ", kosNama='" + kosNama + '\'' +
                ", kategori='" + kategori + '\'' +
                ", keterangan='" + keterangan + '\'' +
                ", pemasukan=" + pemasukan +
                ", pengeluaran=" + pengeluaran +
                '}';
    }
}