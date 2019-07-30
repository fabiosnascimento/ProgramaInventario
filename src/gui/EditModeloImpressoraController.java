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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.ModeloImpressora;
import model.exceptions.ValidationException;
import model.services.CadModeloImpressoraService;

public class EditModeloImpressoraController implements Initializable {
	
	private ModeloImpressora modeloEntity;
	private CadModeloImpressoraService modeloService;
	private List<DataChangeListener> dataChangeListener = new ArrayList<>();
	
	@FXML
	private TextField txtNome;
	@FXML
	private Button btSalvar;
	@FXML
	private Button btCancelar;
	@FXML
	private Label lblFabricante;
	@FXML
	private Label lblErroNome;

	public void setModeloImpressora(ModeloImpressora modeloEntity) {
		this.modeloEntity = modeloEntity;
	}

	public void setCadModeloImpressoraService(CadModeloImpressoraService modeloService) {
		this.modeloService = modeloService;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}

	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		if (modeloService == null) {
			throw new IllegalStateException("Service não iniciado");
		}
		if (modeloEntity == null) {
			throw new IllegalStateException("Entity vazia");
		}
		try {
			modeloEntity = getFormData();
			modeloService.saveOrUpdate(modeloEntity);
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
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("vazio")) {
			lblErroNome.setText(errors.get("vazio"));
		}
		if (fields.contains("existente")) {
			lblErroNome.setText(errors.get("existente"));
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}

	private ModeloImpressora getFormData() {
		ModeloImpressora obj = new ModeloImpressora();
		
		ValidationException exception = new ValidationException("Erro de validação");
		
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addError("vazio", "O campo não pode estar vazio");
		}
		
		obj.setIdModeloImpressora(modeloEntity.getIdModeloImpressora());
		obj.setModelo(txtNome.getText().trim().toUpperCase());
		List<ModeloImpressora> list = modeloService.findAll();
		if (list.contains(obj)) {
			exception.addError("existente", "Modelo já existente");
		}
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return obj;
	}
	
	public void updateFormData() {
		if (modeloEntity == null) {
			throw new IllegalStateException("Entity vazia");
		}
		txtNome.setText(modeloEntity.getModelo());
		lblFabricante.setText(modeloEntity.getFabricante());
	}
}
