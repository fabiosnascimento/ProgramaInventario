package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Itens;
import model.services.ItensService;

public class ItensFormController implements Initializable {

	private Itens entity;
	private ItensService service;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtNome;
	@FXML
	private TextField txtQuantidade;
	@FXML
	private TextField txtMarca;
	@FXML
	private Button btSalvar;
	@FXML
	private Button btCancelar;
	@FXML
	private Label lblErroNome;
	@FXML
	private Label lblErroQuantidade;
	@FXML
	private Label lblErroMarca;
	
	public void setItens (Itens entity) {
		this.entity = entity;
	}
	
	public void setItensService(ItensService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity não iniciado");
		}
		if (service == null) {
			throw new IllegalStateException("Service não iniciado");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (DBException e) {
			Alerts.showAlert("Erro salvando objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Itens getFormData() {
		Itens obj = new Itens();
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setNome(txtNome.getText());
		obj.setQuantidade(Utils.tryParseToInt(txtQuantidade.getText()));
		obj.setMarca(txtMarca.getText());
		
		return obj;
	}

	@FXML
	public void onBTCancelarAction(ActionEvent e) {
		Utils.currentStage(e).close();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	public void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 30);
		Constraints.setTextFieldInteger(txtQuantidade);
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity vazia");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		txtQuantidade.setText(String.valueOf(entity.getQuantidade()));
		txtMarca.setText(entity.getMarca());
	}
}
