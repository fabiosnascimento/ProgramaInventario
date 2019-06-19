package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Itens;
import model.services.ItensService;

public class ItensListController implements Initializable, DataChangeListener {

	private ItensService service;
	
	@FXML
	private Button btNovo;
	@FXML
	private Button btEditar;
	@FXML
	private TableView<Itens> tableViewItens;
	@FXML
	private TableColumn<Itens, Integer> tableColumnId;
	@FXML
	private TableColumn<Itens, String> tableColumnNome;
	@FXML
	private TableColumn<Itens, Integer> tableColumnQuantidade;
	@FXML
	private TableColumn<Itens, String> tableColumnMarca;
	
	private ObservableList<Itens> obsList;
	
	@FXML
	public void onBtNovoAction(ActionEvent e) {
		Stage parentStage = Utils.currentStage(e);
		Itens obj = new Itens();
		createDialogForm(obj, "/gui/ItensForm.fxml", parentStage);;
	}
	
	public void onBtEditarAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Itens obj = tableViewItens.getSelectionModel().getSelectedItem();
		try {
			createDialogForm(obj, "/gui/ItensForm.fxml", parentStage);
		} catch (IllegalStateException e) {
			Alerts.showAlert("Atenção", null, "Selecione um item", AlertType.INFORMATION);
		}
	}
	
	public void setItensService(ItensService service) {
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
		tableViewItens.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service não inicializado");
		}
		List<Itens> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewItens.setItems(obsList);
	}

	private void createDialogForm(Itens obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			ItensFormController controller = loader.getController();
			controller.setItens(obj);
			controller.setItensService(new ItensService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Insira os dados do equipamento");
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
}
