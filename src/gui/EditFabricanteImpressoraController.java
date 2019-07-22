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
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.FabricanteImpressora;
import model.exceptions.ValidationException;
import model.services.CadFabricanteImpressoraService;

public class EditFabricanteImpressoraController implements Initializable {

	private FabricanteImpressora entity;
	private CadFabricanteImpressoraService service;
	private List<DataChangeListener> dataChangeListener = new ArrayList<>();
	
	@FXML
	private TextField txtNome;
	@FXML
	private Button btSalvar;
	@FXML
	private Button btCancelar;
	@FXML
	private Label lblErroNome;
	
	public void setFabricanteImpressora(FabricanteImpressora entity) {
		this.entity = entity;
	}

	public void setFabricanteImpressoraService(CadFabricanteImpressoraService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
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
		} catch(ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch(DBException e) {
			Alerts.showAlert("Erro salvando objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	@FXML
	public void onBtCancelarAction(ActionEvent e) {
		Utils.currentStage(e).close();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
	}
	
	public FabricanteImpressora getFormData() {
		FabricanteImpressora obj = new FabricanteImpressora();
		
		ValidationException exception = new ValidationException("Erro de validação");
		
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addError("vazio", "O campo não pode estar vazio");
		}
		
		obj.setIdFabricanteImpressora(entity.getIdFabricanteImpressora());
		obj.setFabricante(txtNome.getText().trim().toUpperCase());
		List<FabricanteImpressora> list = service.findAll();
		if (list.contains(obj)) {
			exception.addError("existente", "Fabricante já existente");
		}
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return obj;
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity vazia");
		}
		txtNome.setText(entity.getFabricante());
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("vazio")) {
			lblErroNome.setText(errors.get("vazio"));
		}
		if (fields.contains("existente")) {
			lblErroNome.setText(errors.get("existente"));
		}
	}
}
