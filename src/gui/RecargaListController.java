package gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.entities.Impressora;
import model.services.ImpressorasService;

public class RecargaListController implements Initializable {
	
	private Impressora entity;
	private ImpressorasService service;

	@FXML
	private ComboBox<Impressora> comboBoxImpressora;
	@FXML
	private Button btRecarregar;
	@FXML
	private TextArea txtData;

	private ObservableList<Impressora> obsNomeImpressoras;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy;HH:mm:ss");

	public void setImpressorasService(ImpressorasService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	public void atualizaImpressoras() {
		if (service == null) {
			throw new IllegalStateException("Service não inicializado");
		}
		List<Impressora> list = service.findAll();
		obsNomeImpressoras = FXCollections.observableArrayList(list);
		comboBoxImpressora.setItems(obsNomeImpressoras);
		comboBoxImpressora.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				btRecarregar.setVisible(true);
				txtData.setVisible(true);
				exibeDados();
			}
		});
	}
	
	public void updateComboBoxItens() {
		List<Impressora> list = service.findAll();
		obsNomeImpressoras = FXCollections.observableArrayList(list);
		comboBoxImpressora.setItems(obsNomeImpressoras);
	}

	@FXML
	public void onBtRecarregarAction(ActionEvent e) {
		entity = comboBoxImpressora.getSelectionModel().getSelectedItem();
		Optional<ButtonType> result = Alerts.showConfirmation("Recarga", "Deseja recarregar a impressora " + entity.getApelido() + "?");
		if (result.get() == ButtonType.OK) {
			String path = "c:\\temp\\datas.csv";
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
				bw.write(entity.getApelido() + ";" + sdf.format(new Date()));
				bw.newLine();
			} catch (IOException event) {
				Alerts.showAlert("Erro", null, event.getMessage(), AlertType.ERROR);
			}
		}
		exibeDados();
	}

	private void initializeNodes() {
		btRecarregar.setVisible(false);
		txtData.setVisible(false);
		Stage stage = (Stage) Main.getMainScene().getWindow();
		txtData.prefHeightProperty().bind(stage.heightProperty());
	}

	private void exibeDados() {
		entity = comboBoxImpressora.getSelectionModel().getSelectedItem();
		String apelido = entity.getApelido();
		String path = "c:\\temp\\datas.csv";
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			txtData.clear();
			String line = br.readLine();
			while (line != null) {
				String[] vetor = line.split(";");
				if (vetor[0].equals(apelido)) {
					txtData.appendText(vetor[0] + " " + vetor[1] + " " + vetor[2] + "\n");
				}
				line = br.readLine();
			}
		} catch (IOException e) {
			e.getMessage();
		}
	}
}
