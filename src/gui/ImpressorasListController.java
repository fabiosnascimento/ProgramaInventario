package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Impressora;

public class ImpressorasListController implements Initializable {

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
	
	private ObservableList<Impressora> obsList;
	
	private Image imgEditar = new Image("editar.png", 18, 18, true, true);
	private Image imgDeletar = new Image("lixeira.png", 18, 18, true, true);
	
	@FXML
	public void onBtNewAction(ActionEvent e) {
		Stage parentStage = Utils.currentStage(e);
		Impressora obj = new Impressora();
		createDialogForm(obj, "/gui/ImpressorasForm.fxml", parentStage);
	}
	
	private void createDialogForm(Impressora obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
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

}
