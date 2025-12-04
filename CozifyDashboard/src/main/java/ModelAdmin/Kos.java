package ModelAdmin;

public class Kos {
    private int id;
    private String namaKos;
    private String pemilik;
    private String alamat;
    private double harga;
    private String status;
    private int pemilikId;
    
    public Kos() {}
    
    public Kos(int id, String namaKos, String pemilik, String alamat, double harga, String status) {
        this.id = id;
        this.namaKos = namaKos;
        this.pemilik = pemilik;
        this.alamat = alamat;
        this.harga = harga;
        this.status = status;
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
    
    public String getPemilik() {
        return pemilik;
    }
    
    public void setPemilik(String pemilik) {
        this.pemilik = pemilik;
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public int getPemilikId() {
        return pemilikId;
    }
    
    public void setPemilikId(int pemilikId) {
        this.pemilikId = pemilikId;
    }
    
    public String getFormattedHarga() {
        return String.format("Rp %,.0f", harga);
    }
    
    @Override
    public String toString() {
        return "Kos{" +
                "id=" + id +
                ", namaKos='" + namaKos + '\'' +
                ", pemilik='" + pemilik + '\'' +
                ", alamat='" + alamat + '\'' +
                ", harga=" + harga +
                ", status='" + status + '\'' +
                '}';
    }
}