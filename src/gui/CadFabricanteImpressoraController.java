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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import model.exceptions.ValidationException;
import model.services.CadFabricanteImpressoraService;

public class CadFabricanteImpressoraController implements Initializable, DataChangeListener {

	private FabricanteImpressora entity;
	private CadFabricanteImpressoraService service;
	private List<DataChangeListener> dataChangeListener = new ArrayList<>();
	
	@FXML
	private TextField txtNome;
	@FXML
	private Button btSalvar;
	@FXML
	private Label lblErroNome;
	@FXML
	private TableView<FabricanteImpressora> tableViewFabricante;
	@FXML
	private TableColumn<FabricanteImpressora, String> tableColumnFabricante;
	@FXML
	private TableColumn<FabricanteImpressora, FabricanteImpressora> tableColumnEditar;
	@FXML
	private TableColumn<FabricanteImpressora, FabricanteImpressora> tableColumnDeletar;
	
	private ObservableList<FabricanteImpressora> obsList;
	
	private Image imgEditar = new Image("editar.png", 18, 18, true, true);
	private Image imgDeletar = new Image("lixeira.png", 18, 18, true, true);
	
	public void setFabricanteImpressora(FabricanteImpressora entity) {
		this.entity = entity;
	}

	public void setCadFabricanteImpressoraService(CadFabricanteImpressoraService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListener) {
			listener.onDataChanged();
		}
	}
	
	@Override
	public void onDataChanged() {
		updateTableView();
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
			entity = getTextFieldData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
		} catch(ValidationException e) {
			setErrorMessage(e.getErrors());
		} catch(DBException e) {
			Alerts.showAlert("Erro salvando objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void createDialogForm(FabricanteImpressora obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			EditFabricanteImpressoraController controller = loader.getController();
			controller.setFabricanteImpressora(obj);
			controller.setFabricanteImpressoraService(new CadFabricanteImpressoraService());
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
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		tableColumnFabricante.setCellValueFactory(new PropertyValueFactory<>("Fabricante"));
		subscribeDataChangeListener(this);
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewFabricante.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service não iniciado");
		}
		List<FabricanteImpressora> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewFabricante.setItems(obsList);
		initEditButtons();
		initDeleteButtons();
	}
	
	private FabricanteImpressora getTextFieldData() {
		FabricanteImpressora obj = new FabricanteImpressora();
		
		ValidationException exception = new ValidationException("Erro de validação");
		
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addError("vazio", "O campo não pode estar vazio");
		}
		
		obj.setFabricante(txtNome.getText().toUpperCase().trim());
		List<FabricanteImpressora> list = service.findAll();
		if (list.contains(obj)) {
			exception.addError("existente", "Fabricante já existente");
		}
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return obj;
	}
	
	private void setErrorMessage(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("vazio")) {
			lblErroNome.setText(errors.get("vazio"));
		}
		if (fields.contains("existente")) {
			lblErroNome.setText(errors.get("existente"));
		}
	}
	
	private void initEditButtons() {
		tableColumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditar.setCellFactory(param -> new TableCell<FabricanteImpressora, FabricanteImpressora>() {
			private final Button button = new Button("", new ImageView(imgEditar));

			@Override
			protected void updateItem(FabricanteImpressora obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/EditFabricanteImpressora.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	private void initDeleteButtons() {
		tableColumnDeletar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnDeletar.setCellFactory(param -> new TableCell<FabricanteImpressora, FabricanteImpressora>() {
			private final Button button = new Button("", new ImageView(imgDeletar));

			@Override
			protected void updateItem(FabricanteImpressora obj, boolean empty) {
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
	
	private void removeEntity(FabricanteImpressora obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Atenção", "Deseja remover este fabricante?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service não iniciado");
			}
			try {
				service.remove(obj);
				updateTableView();

			} catch (NullPointerException e) {
				Alerts.showAlert("Atenção", null, "Selecione um fabricante", AlertType.INFORMATION);
			}
		}
	}
}
