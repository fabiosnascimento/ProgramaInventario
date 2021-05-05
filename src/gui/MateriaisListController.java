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
import model.entities.Material;
import model.services.MaterialService;

public class MateriaisListController implements Initializable, DataChangeListener {

	private MaterialService service;

	@FXML
	private Button btNovo;
	@FXML
	private TableView<Material> tableViewMateriais;
	@FXML
	private TableColumn<Material, Integer> tableColumnId;
	@FXML
	private TableColumn<Material, String> tableColumnNome;
	@FXML
	private TableColumn<Material, Integer> tableColumnQuantidade;
	@FXML
	private TableColumn<Material, String> tableColumnMarca;
	@FXML
	private TableColumn<Material, Material> tableColumnEditar;
	@FXML
	private TableColumn<Material, Material> tableColumnDeletar;

	private ObservableList<Material> obsList;
	
	private Image imgEditar = new Image("editar.png", 18, 18, true, true);
	private Image imgDeletar = new Image("lixeira.png", 18, 18, true, true);


	@FXML
	public void onBtNewAction(ActionEvent e) {
		Stage parentStage = Utils.currentStage(e);
		Material obj = new Material();
		createDialogForm(obj, "/gui/MateriaisForm.fxml", parentStage);
		sortById();
	}

	public void setItensService(MaterialService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("Nome"));
		tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("Quantidade"));
		tableColumnMarca.setCellValueFactory(new PropertyValueFactory<>("Marca"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewMateriais.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service não inicializado");
		}
		List<Material> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewMateriais.setItems(obsList);
		initEditButtons();
		initDeleteButtons();
	}

	private void createDialogForm(Material obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			MateriaisFormController controller = loader.getController();
			controller.setItens(obj);
			controller.setItensService(new MaterialService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Insira os dados do material");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro carregando tela", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditar.setCellFactory(param -> new TableCell<Material, Material>() {
			private final Button button = new Button("", new ImageView(imgEditar));

			@Override
			protected void updateItem(Material obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/MateriaisForm.fxml", Utils.currentStage(event)));
			}
		});
		sortById();
	}
	
	private void initDeleteButtons() {
		tableColumnDeletar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnDeletar.setCellFactory(param -> new TableCell<Material, Material>() {
			private final Button button = new Button("", new ImageView(imgDeletar));

			@Override
			protected void updateItem(Material obj, boolean empty) {
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
	
	private void removeEntity(Material obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Atenção", "Deseja remover este material?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service não iniciado");
			}
			try {
				service.remove(obj);
				updateTableView();

			} catch (NullPointerException e) {
				Alerts.showAlert("Atenção", null, "Selecione um item", AlertType.INFORMATION);
			}
		}
	}
	
	public void sortById() {
		tableViewMateriais.getSortOrder().add(tableColumnId);
	}
}
