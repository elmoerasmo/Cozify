package ModelOwner;

public class MyKos {
    private int id;
    private String namaKos;
    private String alamat;
    private double harga;
    private int totalKamar;
    private int terisi;
    private int tersedia;
    private String status;
    
    public MyKos() {}
    
    public MyKos(int id, String namaKos, String alamat, double harga, int totalKamar, int terisi, int tersedia) {
        this.id = id;
        this.namaKos = namaKos;
        this.alamat = alamat;
        this.harga = harga;
        this.totalKamar = totalKamar;
        this.terisi = terisi;
        this.tersedia = tersedia;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNamaKos() {
        return namaKos;
    }
    
    public void setNamaKos(String namaKos) {
        this.namaKos = namaKos;
    }
    
    public String getAlamat() {
        return alamat;
    }
    
    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
    
    public double getHarga() {
        return harga;
    }
    
    public void setHarga(double harga) {
        this.harga = harga;
    }
    
    public int getTotalKamar() {
        return totalKamar;
    }
    
    public void setTotalKamar(int totalKamar) {
        this.totalKamar = totalKamar;
    }
    
    public int getTerisi() {
        return terisi;
    }
    
    public void setTerisi(int terisi) {
        this.terisi = terisi;
    }
    
    public int getTersedia() {
        return tersedia;
    }
    
    public void setTersedia(int tersedia) {
        this.tersedia = tersedia;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getFormattedHarga() {
        return String.format("Rp %,.0f", harga);
    }
    
    public double getOccupancyRate() {
        if (totalKamar == 0) return 0;
        return (double) terisi / totalKamar * 100;
    }
    
    @Override
    public String toString() {
        return "MyKos{" +
                "id=" + id +
                ", namaKos='" + namaKos + '\'' +
                ", alamat='" + alamat + '\'' +
                ", harga=" + harga +
                ", totalKamar=" + totalKamar +
                ", terisi=" + terisi +
                ", tersedia=" + tersedia +
                ", status='" + status + '\'' +
                '}';
    }
}