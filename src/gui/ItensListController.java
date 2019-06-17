package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Itens;

public class ItensListController implements Initializable {

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
	
	@FXML
	public void onBtNovoAction() {
		System.out.println("onBtNovoAction");
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

}
