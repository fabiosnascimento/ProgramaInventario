package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import application.Main;
import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
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
	private ComboBox<Setor> comboBoxFiltro;
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
	@FXML
	private TableView<ImpressoraSetor> tableViewFiltro;
	@FXML
	private TableColumn<ImpressoraSetor, Integer> tableColumnIdImpressoraSetor;
	@FXML
	private TableColumn<Setor, String> tableColumnSetor;
	@FXML
	private TableColumn<FabricanteImpressora, String> tableColumnFabricante;
	@FXML
	private TableColumn<ModeloImpressora, String> tableColumnModelo;
	@FXML
	private TableColumn<ImpressoraSetor, ImpressoraSetor> tableColumnDeletar;
	
	private ObservableList<Setor> obsSetor;
	private ObservableList<FabricanteImpressora> obsFabricante;
	private ObservableList<ModeloImpressora> obsModelo;
	private ObservableList<ImpressoraSetor> obsFiltro;
	
	private Image imgDeletar = new Image("lixeira.png", 18, 18, true, true);
	
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
			notifyDataChangeListeners();
		} catch(ValidationException e) {
			setErrorMessage(e.getErrors());
		} catch(DBException e) {
			Alerts.showAlert("Erro salvando objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}

	@Override
	public void onDataChanged() {
		exibeDados();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		tableViewFiltro.setVisible(false);
		tableColumnIdImpressoraSetor.setCellValueFactory(new PropertyValueFactory<>("idImpressoraSetor"));
		tableColumnSetor.setCellValueFactory(new PropertyValueFactory<>("idSetor"));
		tableColumnFabricante.setCellValueFactory(new PropertyValueFactory<>("idFabricanteImpressora"));
		tableColumnModelo.setCellValueFactory(new PropertyValueFactory<>("idModeloImpressora"));
		subscribeDataChangeListener(this);
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewFiltro.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void atualizaComboBoxSetor() {
		if (setorService == null) {
			throw new IllegalStateException("Service não iniciado");
		}
		List<Setor> list = setorService.findAll();
		obsSetor = FXCollections.observableArrayList(list);
		comboBoxSetor.setItems(obsSetor);
		comboBoxSetor.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				lblErroSetor.setText("");				
			}
		});
		comboBoxFiltro.setItems(obsSetor);
		comboBoxFiltro.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				tableViewFiltro.setVisible(true);
				exibeDados();
			}
		});
	}
	
	private void exibeDados() {
		if (impressoraService == null) {
			throw new IllegalStateException("Service não iniciado");
		}
		setorEntity = comboBoxFiltro.getSelectionModel().getSelectedItem();
		Integer id = setorEntity.getIdSetor();
		List<ImpressoraSetor> list = impressoraService.findById(id);
		obsFiltro = FXCollections.observableArrayList(list);
		tableViewFiltro.setItems(obsFiltro);
		initDeleteButtons();
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
				atualizaComboBoxModelo();
				lblErroFabricante.setText("");
			}
		});
	}
	
	private void atualizaComboBoxModelo() {
		if (modeloService == null) {
			throw new IllegalStateException("Service não iniciado");
		}
		fabricanteImpressoraEntity = comboBoxFabricante.getSelectionModel().getSelectedItem();
		Integer id = fabricanteImpressoraEntity.getIdFabricanteImpressora();
		List<ModeloImpressora> list = modeloService.findById(id);
		obsModelo = FXCollections.observableArrayList(list);
		comboBoxModelo.setItems(obsModelo);
		comboBoxModelo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				lblErroModelo.setText("");
			}
		});
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
		if (fields.contains("existente")) {
			lblConfirmacao.setText(errors.get("existente"));
		}
	}
	
	private void initDeleteButtons() {
		tableColumnDeletar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnDeletar.setCellFactory(param -> new TableCell<ImpressoraSetor, ImpressoraSetor>() {
			private final Button button = new Button("", new ImageView(imgDeletar));

			@Override
			protected void updateItem(ImpressoraSetor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}
	
	private void removeEntity(ImpressoraSetor obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Atenção", "Deseja remover este registro?");

		if (result.get() == ButtonType.OK) {
			if (impressoraService == null) {
				throw new IllegalStateException("Service não iniciado");
			}
			try {
				impressoraService.remove(obj);
				setCadImpressoraService(new CadImpressoraSetorService());
				exibeDados();

			} catch (NullPointerException e) {
				Alerts.showAlert("Atenção", null, "Selecione um registro", AlertType.INFORMATION);
			}
		}
	}
}