package Model;

import java.time.LocalDate;

public class Laporan {
    private int id;
    private int kosId;
    private String namaKos;
    private LocalDate tanggal;
    private String kategori; 
    private String keterangan;
    private double pemasukan;
    private double pengeluaran;
    
    public Laporan() {
    }
    
    public Laporan(int id, String namaKos, LocalDate tanggal, String kategori, String keterangan, double pemasukan, double pengeluaran) {
        this.id = id;
        this.namaKos = namaKos;
        this.tanggal = tanggal;
        this.kategori = kategori;
        this.keterangan = keterangan;
        this.pemasukan = pemasukan;
        this.pengeluaran = pengeluaran;
    }
    
    public Laporan(int kosId, String kategori, String keterangan, double nominal) {
        this.kosId = kosId;
        this.kategori = kategori;
        this.keterangan = keterangan;
        this.tanggal = LocalDate.now();
        
        if ("Pemasukan".equals(kategori)) {
            this.pemasukan = nominal;
            this.pengeluaran = 0;
        } else {
            this.pemasukan = 0;
            this.pengeluaran = nominal;
        }
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getKosId() {
        return kosId;
    }
    
    public void setKosId(int kosId) {
        this.kosId = kosId;
    }
    
    public String getNamaKos() {
        return namaKos;
    }
    
    public void setNamaKos(String namaKos) {
        this.namaKos = namaKos;
    }
    
    public LocalDate getTanggal() { 
        return tanggal;
    }
    
    public void setTanggal(LocalDate tanggal) { 
        this.tanggal = tanggal;
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
    
    @Override
    public String toString() {
        return "Laporan{" +
                "id=" + id +
                ", namaKos='" + namaKos + '\'' +
                ", tanggal=" + tanggal +
                ", kategori='" + kategori + '\'' +
                ", pemasukan=" + pemasukan +
                ", pengeluaran=" + pengeluaran +
                '}';
    }
}