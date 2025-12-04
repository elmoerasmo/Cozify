package ModelAdmin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PendingItem {
    private int id;
    private String type;
    private String nama;
    private String details;
    private LocalDateTime tanggalPengajuan;
    private String status;
    
    public PendingItem() {}
    
    public PendingItem(int id, String type, String nama, String details, LocalDateTime tanggalPengajuan, String status) {
        this.id = id;
        this.type = type;
        this.nama = nama;
        this.details = details;
        this.tanggalPengajuan = tanggalPengajuan;
        this.status = status;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getNama() {
        return nama;
    }
    
    public void setNama(String nama) {
        this.nama = nama;
    }
    
    public String getDetails() {
        return details;
    }
    
    public void setDetails(String details) {
        this.details = details;
    }
    
    public LocalDateTime getTanggalPengajuan() {
        return tanggalPengajuan;
    }
    
    public void setTanggalPengajuan(LocalDateTime tanggalPengajuan) {
        this.tanggalPengajuan = tanggalPengajuan;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getFormattedTanggal() {
        if (tanggalPengajuan != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            return tanggalPengajuan.format(formatter);
        }
        return "";
    }
    
    @Override
    public String toString() {
        return "PendingItem{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", nama='" + nama + '\'' +
                ", details='" + details + '\'' +
                ", tanggalPengajuan=" + tanggalPengajuan +
                ", status='" + status + '\'' +
                '}';
    }
}