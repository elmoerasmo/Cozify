package Model;

import java.time.LocalDate;

public class Pembayaran {

    private int idPembayaran;
    private User user;
    private Kos kos;

    private LocalDate tanggalMasuk;
    private LocalDate tanggalKeluar;

    private double hargaPerBulan;
    private double totalPembayaran;
    private String metodePembayaran;
    private String status; // misal: "Pending", "Berhasil", "Gagal"

    public Pembayaran() {}

    // -------------------- GETTER & SETTER --------------------

    public int getIdPembayaran() {
        return idPembayaran;
    }

    public void setIdPembayaran(int idPembayaran) {
        this.idPembayaran = idPembayaran;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Kos getKos() {
        return kos;
    }

    public void setKos(Kos kos) {
        this.kos = kos;
    }

    public LocalDate getTanggalMasuk() {
        return tanggalMasuk;
    }

    public void setTanggalMasuk(LocalDate tanggalMasuk) {
        this.tanggalMasuk = tanggalMasuk;
    }

    public LocalDate getTanggalKeluar() {
        return tanggalKeluar;
    }

    public void setTanggalKeluar(LocalDate tanggalKeluar) {
        this.tanggalKeluar = tanggalKeluar;
    }

    public double getHargaPerBulan() {
        return hargaPerBulan;
    }

    public void setHargaPerBulan(double hargaPerBulan) {
        this.hargaPerBulan = hargaPerBulan;
    }

    public double getTotalPembayaran() {
        return totalPembayaran;
    }

    public void setTotalPembayaran(double totalPembayaran) {
        this.totalPembayaran = totalPembayaran;
    }

    public String getMetodePembayaran() {
        return metodePembayaran;
    }

    public void setMetodePembayaran(String metodePembayaran) {
        this.metodePembayaran = metodePembayaran;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // -------------------- UTIL --------------------

    // Hitung durasi sewa (bulan) otomatis dari tanggal masuk/keluar
    public long getDurasiBulan() {
        if (tanggalMasuk != null && tanggalKeluar != null) {
            return java.time.temporal.ChronoUnit.MONTHS.between(tanggalMasuk, tanggalKeluar);
        }
        return 0;
    }
}
