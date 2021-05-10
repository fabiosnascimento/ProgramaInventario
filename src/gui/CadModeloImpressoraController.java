package gui;

import java.io.IOException;
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
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.FabricanteImpressora;
import model.entities.ModeloImpressora;
import model.exceptions.ValidationException;
import model.services.CadFabricanteImpressoraService;
import model.services.CadModeloImpressoraService;

public class CadModeloImpressoraController implements Initializable, DataChangeListener {
	
	private FabricanteImpressora fabricanteEntity;
	private ModeloImpressora modeloEntity;
	private CadFabricanteImpressoraService fabricanteService;
	private CadModeloImpressoraService modeloService;
	private List<DataChangeListener> dataChangeListener = new ArrayList<>();

	@FXML
	private ComboBox<FabricanteImpressora> comboBoxFabricante;
	@FXML
	private TextField txtModelo;
	@FXML
	private Button btSalvar;
	@FXML
	private Label lblErroModelo;
	@FXML
	private TableView<ModeloImpressora> tableViewImpressora;
	@FXML
	private TableColumn<FabricanteImpressora, String> tableColumnFabricante;
	@FXML
	private TableColumn<ModeloImpressora, Integer> tableColumnIdModelo;
	@FXML
	private TableColumn<ModeloImpressora, String> tableColumnModelo;
	@FXML
	private TableColumn<ModeloImpressora, ModeloImpressora> tableColumnEditar;
	@FXML
	private TableColumn<ModeloImpressora, ModeloImpressora> tableColumnDeletar;
	
	private ObservableList<FabricanteImpressora> obsFabricante;
	private ObservableList<ModeloImpressora> obsModelo;
	
	private Image imgEditar = new Image("editar.png", 18, 18, true, true);
	private Image imgDeletar = new Image("lixeira.png", 18, 18, true, true);
	
	public void setFabricanteImpressora(FabricanteImpressora fabricanteEntity) {
		this.fabricanteEntity = fabricanteEntity;
	}
	
	public void setModeloImpressora(ModeloImpressora modeloEntity) {
		this.modeloEntity = modeloEntity;
	}

	public void setCadFabricanteImpressoraService(CadFabricanteImpressoraService fabricanteService) {
		this.fabricanteService = fabricanteService;
	}

	public void setCadModeloImpressoraService(CadModeloImpressoraService modeloService) {
		this.modeloService = modeloService;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}

	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		if (modeloEntity == null) {
			throw new IllegalStateException("Entity não iniciado");
		}
		if (modeloService == null) {
			throw new IllegalStateException("Service não iniciado");
		}
		try {
			modeloEntity = getTextFieldData();
			modeloService.saveOrUpdate(modeloEntity);
			notifyDataChangeListeners();
		} catch(ValidationException e) {
			setErrorMessage(e.getErrors());
		} catch(DBException e) {
			Alerts.showAlert("Erro salvando objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void setErrorMessage(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("vazio")) {
			lblErroModelo.setText(errors.get("vazio"));
		}
		if (fields.contains("existente")) {
			lblErroModelo.setText(errors.get("existente"));
		}
		if (fields.contains("idnulo")) {
			lblErroModelo.setText(errors.get("idnulo"));
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
		tableViewImpressora.setVisible(false);
		tableColumnFabricante.setCellValueFactory(new PropertyValueFactory<>("Fabricante"));
		tableColumnIdModelo.setCellValueFactory(new PropertyValueFactory<>("idModeloImpressora"));
		tableColumnModelo.setCellValueFactory(new PropertyValueFactory<>("Modelo"));
		subscribeDataChangeListener(this);
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewImpressora.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void atualizaComboBoxImpressora() {
		if (fabricanteService == null) {
			throw new IllegalStateException("Service não iniciado");
		}
		List<FabricanteImpressora> list = fabricanteService.findAll();
		obsFabricante = FXCollections.observableArrayList(list);
		comboBoxFabricante.setItems(obsFabricante);
		comboBoxFabricante.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				tableViewImpressora.setVisible(true);
				exibeDados();
			}
		});
	}

	private void exibeDados() {
		if (modeloService == null) {
			throw new IllegalStateException("Service não iniciado");
		}
		fabricanteEntity = comboBoxFabricante.getSelectionModel().getSelectedItem();
		Integer id = fabricanteEntity.getIdFabricanteImpressora();
		List<ModeloImpressora> list = modeloService.findById(id);
		obsModelo = FXCollections.observableArrayList(list);
		tableViewImpressora.setItems(obsModelo);
		initEditButtons();
		initDeleteButtons();
	}
	
	private void createDialogForm(ModeloImpressora obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			EditModeloImpressoraController controller = loader.getController();
			controller.setModeloImpressora(obj);
			controller.setCadModeloImpressoraService(new CadModeloImpressoraService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();
			
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Editar");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
			
		} catch(IOException e) {
			Alerts.showAlert("IO Exception", "Erro carregando tela", e.getMessage(), AlertType.ERROR);
		}
	}
	
	private ModeloImpressora getTextFieldData() {
		ModeloImpressora obj = new ModeloImpressora();
		
		ValidationException exception = new ValidationException("Erro de validação");
		
		fabricanteEntity = comboBoxFabricante.getSelectionModel().getSelectedItem();
		if (fabricanteEntity == null) {
			exception.addError("idnulo", "Selecione um fabricante");
			throw exception;
		}

		obj.setIdFabricanteImpressora(fabricanteEntity);
		
		if (txtModelo.getText() == null || txtModelo.getText().trim().equals("")) {
			exception.addError("vazio", "O campo não pode estar vazio");
		}
		obj.setModelo(txtModelo.getText().trim().toUpperCase());
		
		
		List<ModeloImpressora> list = modeloService.findAll();
		if (list.contains(obj)) {
			exception.addError("existente", "Modelo já existente");
		}
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return obj;
	}
	
	private void initEditButtons() {
		tableColumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditar.setCellFactory(param -> new TableCell<ModeloImpressora, ModeloImpressora>() {
			private final Button button = new Button("", new ImageView(imgEditar));

			@Override
			protected void updateItem(ModeloImpressora obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/EditModeloImpressora.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	private void initDeleteButtons() {
		tableColumnDeletar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnDeletar.setCellFactory(param -> new TableCell<ModeloImpressora, ModeloImpressora>() {
			private final Button button = new Button("", new ImageView(imgDeletar));

			@Override
			protected void updateItem(ModeloImpressora obj, boolean empty) {
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
	
	private void removeEntity(ModeloImpressora obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Atenção", "Deseja remover este modelo?");

		if (result.get() == ButtonType.OK) {
			if (modeloService == null) {
				throw new IllegalStateException("Service não iniciado");
			}
			try {
				modeloService.remove(obj);
				setCadModeloImpressoraService(new CadModeloImpressoraService());
				exibeDados();

			} catch (NullPointerException e) {
				Alerts.showAlert("Atenção", null, "Selecione um modelo", AlertType.INFORMATION);
			}
		}
	}
}
