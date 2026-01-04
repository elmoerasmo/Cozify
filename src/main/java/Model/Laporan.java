package Model;

import java.time.LocalDate;

public class Laporan {
    private int id;
    private int kosId;
    private String namaKos;
    private String namaPenyewa;    
    private LocalDate tanggal;
    private String kategori;        
    private String keterangan;      
    private String metodePembayaran; 
    private double pemasukan;
    private double pengeluaran;

    public Laporan() {
    }

    public Laporan(int id, int kosId, String namaKos, String namaPenyewa, LocalDate tanggal, String kategori, String keterangan, String metodePembayaran, double pemasukan, double pengeluaran) {
        this.id = id;
        this.kosId = kosId;
        this.namaKos = namaKos;
        this.namaPenyewa = namaPenyewa;
        this.tanggal = tanggal;
        this.kategori = kategori;
        this.keterangan = keterangan;
        this.metodePembayaran = metodePembayaran;
        this.pemasukan = pemasukan;
        this.pengeluaran = pengeluaran;
    }

    public Laporan(int kosId, String namaPenyewa, String keterangan, String metode, double nominal) {
        this.kosId = kosId;
        this.namaPenyewa = namaPenyewa;
        this.kategori = "Pemasukan";
        this.keterangan = keterangan;
        this.metodePembayaran = metode;
        this.tanggal = LocalDate.now();
        this.pemasukan = nominal;
        this.pengeluaran = 0;
    }

    

    public String getNamaPenyewa() { return namaPenyewa; }
    public void setNamaPenyewa(String namaPenyewa) { this.namaPenyewa = namaPenyewa; }

    public String getMetodePembayaran() { return metodePembayaran; }
    public void setMetodePembayaran(String metodePembayaran) { this.metodePembayaran = metodePembayaran; }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getKosId() { return kosId; }
    public void setKosId(int kosId) { this.kosId = kosId; }
    public String getNamaKos() { return namaKos; }
    public void setNamaKos(String namaKos) { this.namaKos = namaKos; }
    public LocalDate getTanggal() { return tanggal; }
    public void setTanggal(LocalDate tanggal) { this.tanggal = tanggal; }
    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
    public double getPemasukan() { return pemasukan; }
    public void setPemasukan(double pemasukan) { this.pemasukan = pemasukan; }
    public double getPengeluaran() { return pengeluaran; }
    public void setPengeluaran(double pengeluaran) { this.pengeluaran = pengeluaran; }
}