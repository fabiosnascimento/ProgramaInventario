package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

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
import model.entities.Impressora;
import model.exceptions.ValidationException;
import model.services.ImpressorasService;

public class ImpressorasFormController implements Initializable {
	
	private Impressora entity;
	private ImpressorasService service;
	private List<DataChangeListener> dataChangeListener = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtModelo;
	@FXML
	private TextField txtSetor;
	@FXML
	private TextField txtApelido;
	@FXML
	private Button btSalvar;
	@FXML
	private Button btCancelar;
	@FXML
	private Label lblErroApelido;

	public void setImpressora(Impressora entity) {
		this.entity = entity;
	}

	public void setImpressoraService(ImpressorasService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldInteger(txtId);
	}
	
	@FXML
	public void onBtSalvarAction (ActionEvent event) {
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
		} catch(ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch(DBException e) {
			Alerts.showAlert("Erro salvando objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}

	@FXML
	public void onBtCancelarAction (ActionEvent e) {
		Utils.currentStage(e).close();
	}

	private Impressora getFormData() {
		Impressora obj = new Impressora();
		
		ValidationException exception = new ValidationException("Erro de validação");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if (txtApelido.getText() == null || txtApelido.getText().trim().equals("")) {
			exception.addError("name", "O campo não pode estar vazio");
		}
		
		obj.setModelo(txtModelo.getText());
		obj.setSetor(txtSetor.getText());
		obj.setApelido(txtApelido.getText());
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return obj;
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("name")) {
			lblErroApelido.setText(errors.get("name"));
		}
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity vazia");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtModelo.setText(entity.getModelo());
		txtSetor.setText(entity.getSetor());
		txtApelido.setText(entity.getApelido());
	}
}
