package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.entities.Impressora;
import model.services.ImpressorasService;

public class CadImpressoraFormController implements Initializable {

	private Impressora entity;
	private ImpressorasService service;
	
	@FXML
	private ComboBox<Impressora> comboBoxImpressora;
	@FXML
	private TextField txtModelo;
	@FXML
	private Button btSalvar;
	@FXML
	private Button btCancelar;
	
	private ObservableList<Impressora> obsList;
	
	public void setImpressora(Impressora entity) {
		this.entity = entity;
	}

	public void setImpressorasService(ImpressorasService service) {
		this.service = service;
	}

	@FXML
	public void onBtSalvarAction(ActionEvent e) {
		System.out.println("onBtSalvarAction");
	}
	
	@FXML
	public void onBtCancelarAction(ActionEvent e) {
		Utils.currentStage(e).close();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity vazia");
		}
		List<Impressora> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxImpressora.setItems(obsList);
		txtModelo.setText(entity.getModelo());
	}
}
