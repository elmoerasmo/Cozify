package Model;

public class PemilikKos extends User {
    private int jumlahKos;

    public PemilikKos() {
        this.role = "OWNER";
    }

    public PemilikKos(int id, String nama, String email, String noTelepon, String password, String status, int jumlahKos) {
        super(id, nama, email, "OWNER", status, noTelepon);
        this.noTelepon = noTelepon;
        this.password = password;
        this.jumlahKos = jumlahKos;
    }

    public int getJumlahKos() { return jumlahKos; }
    public void setJumlahKos(int jumlahKos) { this.jumlahKos = jumlahKos; }
}
