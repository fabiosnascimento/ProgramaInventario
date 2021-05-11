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
import model.entities.Setor;
import model.exceptions.ValidationException;
import model.services.CadSetorService;

public class CadSetorController implements Initializable, DataChangeListener {
	
	private Setor entity;
	private CadSetorService service;
	private List<DataChangeListener> dataChangeListener = new ArrayList<>();
	
	@FXML
	private TextField txtNomeSetor;
	@FXML
	private Button btSalvar;
	@FXML
	private Label lblErroNome;
	@FXML
	private TableView<Setor> tableViewSetor;
	@FXML
	private TableColumn<Setor, String> tableColumnSetor;
	@FXML
	private TableColumn<Setor, Setor> tableColumnEditar;
	@FXML
	private TableColumn<Setor, Setor> tableColumnDeletar;
	
	private ObservableList<Setor> obsList;
	
	private Image imgEditar = new Image("editar.png", 18, 18, true, true);
	private Image imgDeletar = new Image("lixeira.png", 18, 18, true, true);
	
	public void setSetor(Setor entity) {
		this.entity = entity;
	}
	
	public void setCadSetorService(CadSetorService service) {
		this.service = service;
	}
	
	private void subscribeDataChangeListener(DataChangeListener listener) {
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
			lblErroNome.setText("");
		} catch(ValidationException e) {
			setErrorMessage(e.getErrors());
		} catch(DBException e) {
			Alerts.showAlert("Erro salvando objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void createDialogForm(Setor obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			EditSetorController controller = loader.getController();
			controller.setSetor(obj);
			controller.setCadSetorService(new CadSetorService());
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
		tableColumnSetor.setCellValueFactory(new PropertyValueFactory<>("nomeSetor"));
		subscribeDataChangeListener(this);
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewSetor.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service não iniciado");
		}
		List<Setor> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSetor.setItems(obsList);
		initEditButtons();
		initDeleteButtons();
	}
	
	public Setor getTextFieldData() {
		Setor obj = new Setor();
		
		ValidationException exception = new ValidationException("Erro de validação");
		
		if (txtNomeSetor.getText() == null || txtNomeSetor.getText().trim().equals("")) {
			exception.addError("vazio", "O campo não pode estar vazio");
		}
		
		obj.setNomeSetor(txtNomeSetor.getText().trim().toUpperCase());
		List<Setor> list = service.findAll();
		if (list.contains(obj)) {
			exception.addError("existente", "Setor já existente");
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
		tableColumnEditar.setCellFactory(param -> new TableCell<Setor, Setor>() {
			private final Button button = new Button("", new ImageView(imgEditar));

			@Override
			protected void updateItem(Setor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/EditSetor.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	private void initDeleteButtons() {
		tableColumnDeletar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnDeletar.setCellFactory(param -> new TableCell<Setor, Setor>() {
			private final Button button = new Button("", new ImageView(imgDeletar));

			@Override
			protected void updateItem(Setor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
				lblErroNome.setText("");
			}
		});
	}
	
	private void removeEntity(Setor obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Atenção", "Deseja remover este setor?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service não iniciado");
			}
			try {
				service.remove(obj);
				updateTableView();

			} catch (NullPointerException e) {
				Alerts.showAlert("Atenção", null, "Selecione um setor", AlertType.INFORMATION);
			}
		}
	}
}
