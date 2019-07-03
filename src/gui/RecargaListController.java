package gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.entities.Impressora;

public class RecargaListController implements Initializable {

	@FXML
	private ComboBox<Impressora> comboBoxImpressora = new ComboBox<Impressora>();
	@FXML
	private Button btRecarregar;
	@FXML
	private TextArea txtData;

	private ObservableList<Impressora> obsNomeImpressoras;

	private List<Impressora> listImpressoras = null;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	public void atualizaImpressoras() {
		if (listImpressoras == null) {
			Impressora carteirinha = new Impressora("Carteirinha");
			Impressora raiox = new Impressora("Raio-X");
			listImpressoras = new ArrayList<>();
			listImpressoras.add(carteirinha);
			listImpressoras.add(raiox);
			obsNomeImpressoras = FXCollections.observableArrayList(listImpressoras);
			comboBoxImpressora.setItems(obsNomeImpressoras);

			comboBoxImpressora.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Impressora>() {
				@Override
				public void changed(ObservableValue<? extends Impressora> obs, Impressora oldValue,	Impressora newValue) {
					btRecarregar.setVisible(true);
					txtData.setVisible(true);
					exibeDados();
				}
			});
		}
	}

	@FXML
	public void onBtRecarregarAction(ActionEvent e) {
		Impressora impressora = comboBoxImpressora.getSelectionModel().getSelectedItem();
		Optional<ButtonType> result = Alerts.showConfirmation("Recarga", "Deseja recarregar a impressora " + impressora.getNome() + "?");
		if (result.get() == ButtonType.OK) {
			String path = "c:\\temp\\datas.txt";
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
				bw.write(impressora.getNome() + " " + sdf.format(new Date()));
				bw.newLine();
			} catch (IOException event) {
				Alerts.showAlert("Erro", null, event.getMessage(), AlertType.ERROR);
			}
		}
		exibeDados();
	}

	@FXML
	public void onBtExibirAction(ActionEvent e) {
		exibeDados();
	}

	private void initializeNodes() {
		btRecarregar.setVisible(false);
		txtData.setVisible(false);
		Stage stage = (Stage) Main.getMainScene().getWindow();
		txtData.prefHeightProperty().bind(stage.heightProperty());
	}

	private void exibeDados() {
		Impressora impressora = comboBoxImpressora.getSelectionModel().getSelectedItem();
		String nome = impressora.getNome();
		String path = "c:\\temp\\datas.txt";
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			txtData.clear();
			String line = br.readLine();
			while (line != null) {
				String[] vetor = line.split(" ");
				if (vetor[0].equals(nome)) {
					txtData.appendText(line + "\n");
				}
				line = br.readLine();
			}
		} catch (IOException e) {
			e.getMessage();
		}
	}
}
