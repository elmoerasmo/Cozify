package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

public class PilihanFasilitasController implements Initializable {

    @FXML private CheckBox cbKMandiDalam, cbKlosetDuduk, cbKlosetJongkok, cbKMandiLuar, cbAirPanas, cbKasur, cbMejaBelajar, cbTV, cbLemari, cbAC, cbLaundry, cbWifi;

    
    private List<String> selectedFasilitas = new ArrayList<>();

    public List<String> getSelectedFasilitas() {
        return selectedFasilitas;
    }

    @FXML
    private void handleTerapkan(ActionEvent event) {
        selectedFasilitas.clear();
        
        if (cbAC != null && cbAC.isSelected()) selectedFasilitas.add("AC");
        if (cbWifi != null && cbWifi.isSelected()) selectedFasilitas.add("WiFi");
        if (cbKMandiDalam != null && cbKMandiDalam.isSelected()) selectedFasilitas.add("Kamar Mandi Dalam");
        if (cbKasur != null && cbKasur.isSelected()) selectedFasilitas.add("Kasur");
        if (cbTV != null && cbTV.isSelected()) selectedFasilitas.add("TV");
        if (cbKMandiLuar != null && cbKMandiLuar.isSelected()) selectedFasilitas.add("Kamar Mandi Luar");
        if (cbKlosetJongkok != null && cbKlosetJongkok.isSelected()) selectedFasilitas.add("Kloset Jongkok");
        if (cbKlosetDuduk != null && cbKlosetDuduk.isSelected()) selectedFasilitas.add("Kloset Duduk");
        if (cbLemari != null && cbLemari.isSelected()) selectedFasilitas.add("Lemari");
        if (cbAirPanas != null && cbAirPanas.isSelected()) selectedFasilitas.add("Air Panas");
        if (cbMejaBelajar != null && cbMejaBelajar.isSelected()) selectedFasilitas.add("Meja Belajar");
        if (cbLaundry != null && cbLaundry.isSelected()) selectedFasilitas.add("Laundry");



        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {}
}