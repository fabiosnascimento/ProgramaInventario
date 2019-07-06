package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Impressora;
import model.entities.Itens;
import model.services.ImpressorasService;

public class ImpressorasListController implements Initializable, DataChangeListener {

	private ImpressorasService service;
	
	@FXML
	private Button btNovo;
	@FXML
	private TableView<Impressora> tableViewImpressora;
	@FXML
	private TableColumn<Impressora, Integer> tableColumnId;
	@FXML
	private TableColumn<Impressora, String> tableColumnModelo;
	@FXML
	private TableColumn<Impressora, String> tableColumnSetor;
	@FXML
	private TableColumn<Impressora, String> tableColumnApelido;
	@FXML
	private TableColumn<Impressora, Impressora> tableColumnEditar;
	@FXML
	private TableColumn<Impressora, Impressora> tableColumnDeletar;
	
	private ObservableList<Impressora> obsList;
	
	private Image imgEditar = new Image("editar.png", 18, 18, true, true);
	private Image imgDeletar = new Image("lixeira.png", 18, 18, true, true);
	
	@FXML
	public void onBtNewAction(ActionEvent e) {
		Stage parentStage = Utils.currentStage(e);
		Impressora obj = new Impressora();
		createDialogForm(obj, "/gui/ImpressorasForm.fxml", parentStage);
	}

	public void setImpressorasService(ImpressorasService service) {
		this.service = service;
	}

	private void createDialogForm(Impressora obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			ImpressorasFormController controller = loader.getController();
			controller.setImpressora(obj);
			controller.setImpressoraService(new ImpressorasService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Insira os dados da impressora");
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
	public void onDataChanged() {
		updateTableView();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
		tableColumnModelo.setCellValueFactory(new PropertyValueFactory<>("Modelo"));
		tableColumnSetor.setCellValueFactory(new PropertyValueFactory<>("Setor"));
		tableColumnApelido.setCellValueFactory(new PropertyValueFactory<>("Apelido"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewImpressora.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service não inicializado");
		}
		List<Impressora> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewImpressora.setItems(obsList);
		initEditButtons();
		initDeleteButtons();
	}
	
	private void initEditButtons() {
		tableColumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditar.setCellFactory(param -> new TableCell<Impressora, Impressora>() {
			private final Button button = new Button("", new ImageView(imgEditar));

			@Override
			protected void updateItem(Impressora obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/ImpressorasForm.fxml", Utils.currentStage(event)));
			}
		});
		sortById();
	}
	
	private void initDeleteButtons() {
		tableColumnDeletar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnDeletar.setCellFactory(param -> new TableCell<Impressora, Impressora>() {
			private final Button button = new Button("", new ImageView(imgDeletar));

			@Override
			protected void updateItem(Impressora obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
		sortById();
	}
	
	private void removeEntity(Impressora obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Atenção", "Deseja remover esta impressora?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service não iniciado");
			}
			try {
				service.remove(obj);
				updateTableView();

			} catch (NullPointerException e) {
				Alerts.showAlert("Atenção", null, "Selecione uma impressora", AlertType.INFORMATION);
			}
		}
	}
	
	public void sortById() {
		tableViewImpressora.getSortOrder().add(tableColumnId);
	}
}
