package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import gui.listeners.DataChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import model.entities.FabricanteImpressora;
import model.entities.ModeloImpressora;
import model.entities.Setor;
import model.services.CadFabricanteImpressoraService;
import model.services.CadModeloImpressoraService;
import model.services.CadSetorService;

public class CadImpressoraSetorController implements Initializable, DataChangeListener {

	private Setor setorEntity;
	private FabricanteImpressora fabricanteImpressoraEntity;
	private ModeloImpressora modeloImpressoraEntity;
	private CadSetorService setorService;
	private CadFabricanteImpressoraService fabricanteService;
	private CadModeloImpressoraService modeloService;
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

	public void setCadSetorService(CadSetorService setorService) {
		this.setorService = setorService;
	}

	public void setCadFabricanteService(CadFabricanteImpressoraService fabricanteService) {
		this.fabricanteService = fabricanteService;
	}

	public void setCadModeloService(CadModeloImpressoraService modeloService) {
		this.modeloService = modeloService;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}
	
	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		System.out.println("onBtSalvarAction");
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
}