package Model;

public class Kamar {
    private int idKamar;
    private int idKos;
    private String nomorKamar;
    private String statusKamar;

    public Kamar() {}

    public Kamar(int idKamar, int idKos, String nomorKamar, String statusKamar) {
        this.idKamar = idKamar;
        this.idKos = idKos;
        this.nomorKamar = nomorKamar;
        this.statusKamar = statusKamar;
    }

    public int getIdKamar() { return idKamar; }
    public void setIdKamar(int idKamar) { this.idKamar = idKamar; }
    public int getIdKos() { return idKos; }
    public void setIdKos(int idKos) { this.idKos = idKos; }
    public String getNomorKamar() { return nomorKamar; }
    public void setNomorKamar(String nomorKamar) { this.nomorKamar = nomorKamar; }
    public String getStatusKamar() { return statusKamar; }
    public void setStatusKamar(String statusKamar) { this.statusKamar = statusKamar; }
}