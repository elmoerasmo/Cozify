package Controller;

import DAO.KamarDAO;
import DAO.PembayaranDAO;
import DAO.KosDAO;
import DAO.LaporanDAO;
import Model.Pembayaran;
import Model.Laporan;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class KonfirmasiPembayaranController {
    @FXML private TextField tfIdReservasi, tfnamaKos, tfnamaPenyewa, tftotalHarga, tfmetodePembayaran;
    @FXML private Button btYaKonfirm, btTidakKonfirm;

    private Pembayaran dataPembayaran; 
    private PaymentViewController parentController;

    public void setKonfirmasiData(Pembayaran pembayaran, PaymentViewController parent) {
        this.dataPembayaran = pembayaran;
        this.parentController = parent;

        tfnamaKos.setText(pembayaran.getKos().getNama());
        tfnamaPenyewa.setText(pembayaran.getUser().getNama());
        tftotalHarga.setText("Rp " + String.format("%,.0f", pembayaran.getTotalPembayaran()));
        tfmetodePembayaran.setText(pembayaran.getMetodePembayaran());
        tfIdReservasi.setText("RSV-WAITING");
    }

    @FXML
    public void initialize() {
        btYaKonfirm.setOnAction(e -> handleYa());
        btTidakKonfirm.setOnAction(e -> {
            ((Stage) btTidakKonfirm.getScene().getWindow()).close();
        });
    }

    private void handleYa() {
        if (dataPembayaran != null && dataPembayaran.getKamar() != null) {
            PembayaranDAO pDAO = new PembayaranDAO();
            LaporanDAO lDAO = new LaporanDAO();
            KamarDAO kamDAO = new KamarDAO();
            KosDAO kDAO = new KosDAO();

            if (pDAO.insert(dataPembayaran)) {
                
                Laporan lap = new Laporan();
                lap.setKosId(dataPembayaran.getKos().getIdKos());
                lap.setTanggal(java.time.LocalDate.now());
                lap.setKategori("Pemasukan Sewa");
                lap.setKeterangan("Sewa " + dataPembayaran.getKos().getNama() + " - Kamar: " + dataPembayaran.getKamar().getNomorKamar());
                lap.setPemasukan(dataPembayaran.getTotalPembayaran());
                lap.setPengeluaran(0.0);
                lap.setNamaPenyewa(dataPembayaran.getUser().getNama()); 
                lap.setMetodePembayaran(dataPembayaran.getMetodePembayaran());
                lDAO.insertLaporan(lap);

                kamDAO.updateStatusKamar(dataPembayaran.getKamar().getIdKamar(), "Terisi");

                int sisa = dataPembayaran.getKos().getKamarTersedia();
                if (sisa > 0) {
                    kDAO.updateKamarTersedia(dataPembayaran.getKos().getIdKos(), sisa - 1);
                }

                Stage stage = (Stage) btYaKonfirm.getScene().getWindow();
                stage.close();

                parentController.setPembayaranUntukBerhasil(dataPembayaran); 
                parentController.showPembayaranBerhasil();
            }
        } else {
            System.err.println("Gagal: Pastikan Kamar sudah dipilih di halaman sebelumnya!");
        }
    }
}