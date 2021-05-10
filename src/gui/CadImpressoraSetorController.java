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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import model.entities.FabricanteImpressora;
import model.entities.ImpressoraSetor;
import model.entities.ModeloImpressora;
import model.entities.Setor;
import model.exceptions.ValidationException;
import model.services.CadFabricanteImpressoraService;
import model.services.CadImpressoraSetorService;
import model.services.CadModeloImpressoraService;
import model.services.CadSetorService;

public class CadImpressoraSetorController implements Initializable, DataChangeListener {

	private Setor setorEntity;
	private FabricanteImpressora fabricanteImpressoraEntity;
	private ModeloImpressora modeloImpressoraEntity;
	private ImpressoraSetor impressoraSetorEntity;
	private CadSetorService setorService;
	private CadFabricanteImpressoraService fabricanteService;
	private CadModeloImpressoraService modeloService;
	private CadImpressoraSetorService impressoraService;
	private List<DataChangeListener> dataChangeListener = new ArrayList<>();
	
	@FXML
	private ComboBox<Setor> comboBoxSetor;
	@FXML
	private ComboBox<FabricanteImpressora> comboBoxFabricante;
	@FXML
	private ComboBox<ModeloImpressora> comboBoxModelo;
	@FXML
	private Button btSalvar;
	@FXML
	private Label lblConfirmacao;
	@FXML
	private Label lblErroSetor;
	@FXML
	private Label lblErroFabricante;
	@FXML
	private Label lblErroModelo;
	
	private ObservableList<Setor> obsSetor;
	private ObservableList<FabricanteImpressora> obsFabricante;
	private ObservableList<ModeloImpressora> obsModelo;
	

	public void setSetor(Setor setorEntity) {
		this.setorEntity = setorEntity;
	}

	public void setFabricanteImpressora(FabricanteImpressora fabricanteImpressoraEntity) {
		this.fabricanteImpressoraEntity = fabricanteImpressoraEntity;
	}

	public void setModeloImpressora(ModeloImpressora modeloImpressoraEntity) {
		this.modeloImpressoraEntity = modeloImpressoraEntity;
	}

	public void setImpressoraSetor(ImpressoraSetor impressoraSetorEntity) {
		this.impressoraSetorEntity = impressoraSetorEntity;
	}

	public void setCadSetorService(CadSetorService setorService) {
		this.setorService = setorService;
	}

	public void setCadFabricanteService(CadFabricanteImpressoraService fabricanteService) {
		this.fabricanteService = fabricanteService;
	}

	public void setCadModeloService(CadModeloImpressoraService modeloService) {
		this.modeloService = modeloService;
	}
	
	public void setCadImpressoraService(CadImpressoraSetorService impressoraService) {
		this.impressoraService = impressoraService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}
	
	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		if (impressoraSetorEntity == null) {
			throw new IllegalStateException("Entity não iniciado");
		}
		if (impressoraService == null) {
			throw new IllegalStateException("Service não iniciado");
		}
		try {
			impressoraSetorEntity = getComboBoxData();
			impressoraService.save(impressoraSetorEntity);
		} catch(ValidationException e) {
			setErrorMessage(e.getErrors());
		} catch(DBException e) {
			Alerts.showAlert("Erro salvando objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}
	
	public void atualizaComboBoxSetor() {
		if (setorService == null) {
			throw new IllegalStateException("Service não iniciado");
		}
		List<Setor> list = setorService.findAll();
		obsSetor = FXCollections.observableArrayList(list);
		comboBoxSetor.setItems(obsSetor);
	}
	
	public void atualizaComboBoxFabricante() {
		if (fabricanteService == null) {
			throw new IllegalStateException("Service não iniciado");
		}
		List<FabricanteImpressora> list = fabricanteService.findAll();
		obsFabricante = FXCollections.observableArrayList(list);
		comboBoxFabricante.setItems(obsFabricante);
		comboBoxFabricante.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				comboBoxModelo.setVisible(true);
				exibeDados();
			}
		});
	}
	
	private void exibeDados() {
		if (modeloService == null) {
			throw new IllegalStateException("Service não iniciado");
		}
		fabricanteImpressoraEntity = comboBoxFabricante.getSelectionModel().getSelectedItem();
		Integer id = fabricanteImpressoraEntity.getIdFabricanteImpressora();
		List<ModeloImpressora> list = modeloService.findById(id);
		obsModelo = FXCollections.observableArrayList(list);
		comboBoxModelo.setItems(obsModelo);
	}
	
	private ImpressoraSetor getComboBoxData() {
		ImpressoraSetor obj = new ImpressoraSetor();
		
		ValidationException exception = new ValidationException("Erro de validação");
		
		setorEntity = comboBoxSetor.getSelectionModel().getSelectedItem();
		if (setorEntity == null) {
			exception.addError("idsetornulo", "Selecione um setor");
			throw exception;
		}
		obj.setIdSetor(setorEntity);
		
		fabricanteImpressoraEntity = comboBoxFabricante.getSelectionModel().getSelectedItem();
		if (fabricanteImpressoraEntity == null) {
			exception.addError("idfabricantenulo", "Selecione um fabricante");
			throw exception;
		}
		obj.setIdFabricanteImpressora(fabricanteImpressoraEntity);
		
		modeloImpressoraEntity = comboBoxModelo.getSelectionModel().getSelectedItem();
		if (modeloImpressoraEntity == null) {
			exception.addError("idmodelonulo", "Selecione um modelo");
			throw exception;
		}
		obj.setIdModeloImpressora(modeloImpressoraEntity);
		
		List<ImpressoraSetor> list = impressoraService.findAll();
		if (list.contains(obj)) {
			exception.addError("existente", "Impressora já cadastrada");
		}
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return obj;
	}
	
	private void setErrorMessage(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("idsetornulo")) {
			lblErroSetor.setText(errors.get("idsetornulo"));
		}
		if (fields.contains("idfabricantenulo")) {
			lblErroFabricante.setText(errors.get("idfabricantenulo"));
		}
		if (fields.contains("idmodelonulo")) {
			lblErroModelo.setText(errors.get("idmodelonulo"));
		}
	}
}