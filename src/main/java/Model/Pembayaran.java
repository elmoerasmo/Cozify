package Model;
import java.time.LocalDate;

public class Pembayaran {
    private int idPembayaran;
    private User user;
    private Kos kos;
    private int durasiBulan;
    private double hargaPerBulan;
    private LocalDate tanggalTransaksi;
    private double totalPembayaran;
    private String metodePembayaran;
    private String status;
    private Kamar kamar;

    public Pembayaran() {}

    public Kamar getKamar() { return kamar; }
    public void setKamar(Kamar kamar) { this.kamar = kamar; }
    public int getIdPembayaran() { return idPembayaran; }
    public void setIdPembayaran(int idPembayaran) { this.idPembayaran = idPembayaran; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Kos getKos() { return kos; }
    public void setKos(Kos kos) { this.kos = kos; }
    public int getDurasiBulan() { return durasiBulan; }
    public void setDurasiBulan(int durasiBulan) { this.durasiBulan = durasiBulan; }
    public double getHargaPerBulan() { return hargaPerBulan; }
    public void setHargaPerBulan(double hargaPerBulan) { this.hargaPerBulan = hargaPerBulan; }
    public LocalDate getTanggalTransaksi() { return tanggalTransaksi; }
    public void setTanggalTransaksi(LocalDate tanggalTransaksi) { this.tanggalTransaksi = tanggalTransaksi; }
    public double getTotalPembayaran() { return totalPembayaran; }
    public void setTotalPembayaran(double totalPembayaran) { this.totalPembayaran = totalPembayaran; }
    public String getMetodePembayaran() { return metodePembayaran; }
    public void setMetodePembayaran(String metodePembayaran) { this.metodePembayaran = metodePembayaran; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}