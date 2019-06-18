package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Itens;
import model.services.ItensService;

public class ItensListController implements Initializable {

	private ItensService service;
	
	@FXML
	private Button btNovo;
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
	public void onBtNovoAction() {
		System.out.println("onBtNovoAction");
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

}
