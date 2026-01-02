package Model;

public class Kos {
    private int idKos;
    private int idPemilik;
    private String nama;
    private String alamat;
    private String deskripsi;
    private double harga;
    private String tipeKos;
    private String fasilitas;
    private String foto; 
    private int totalKamar;
    private int kamarTersedia;
    private int kamarTerisi;
    private String status;
    private String alasanPenolakan; 
    private double rating;
    private boolean favorite = false; // ⬅️ favorite



    // Constructor kosong
    public Kos() {}

    // Constructor sederhana
    public Kos(int idKos, String nama, String alamat, int totalKamar, int kamarTerisi, int idPemilik, String status) {
        this.idKos = idKos;
        this.nama = nama;
        this.alamat = alamat;
        this.totalKamar = totalKamar;
        this.kamarTerisi = kamarTerisi;
        this.idPemilik = idPemilik;
        this.status = status;
    }
    
   
    public int getIdKos() { return idKos; }
    public void setIdKos(int idKos) { this.idKos = idKos; }

    public int getIdPemilik() { return idPemilik; }
    public void setIdPemilik(int idPemilik) { this.idPemilik = idPemilik; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public String getTipeKos() { return tipeKos; }
    public void setTipeKos(String tipeKos) { this.tipeKos = tipeKos; }

    public String getFasilitas() { return fasilitas; }
    public void setFasilitas(String fasilitas) { this.fasilitas = fasilitas; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public int getTotalKamar() { return totalKamar; }
    public void setTotalKamar(int totalKamar) { this.totalKamar = totalKamar; }

    public int getKamarTersedia() { return kamarTersedia; }
    public void setKamarTersedia(int kamarTersedia) { this.kamarTersedia = kamarTersedia; }

    public int getKamarTerisi() { return kamarTerisi; }
    public void setKamarTerisi(int kamarTerisi) { this.kamarTerisi = kamarTerisi; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAlasanPenolakan() { return alasanPenolakan; }
    public void setAlasanPenolakan(String alasanPenolakan) { this.alasanPenolakan = alasanPenolakan; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    
    public boolean isFavorite() { return favorite; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }
}
