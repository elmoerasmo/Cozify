package ModelAdmin;

public class PemilikKos {
    private int id;
    private String nama;
    private String email;
    private String phone;
    private int jumlahKos;
    private String status;
    
    public PemilikKos() {}
    
    public PemilikKos(int id, String nama, String email, String phone, int jumlahKos, String status) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.phone = phone;
        this.jumlahKos = jumlahKos;
        this.status = status;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNama() {
        return nama;
    }
    
    public void setNama(String nama) {
        this.nama = nama;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public int getJumlahKos() {
        return jumlahKos;
    }
    
    public void setJumlahKos(int jumlahKos) {
        this.jumlahKos = jumlahKos;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "PemilikKos{" +
                "id=" + id +
                ", nama='" + nama + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", jumlahKos=" + jumlahKos +
                ", status='" + status + '\'' +
                '}';
    }
}