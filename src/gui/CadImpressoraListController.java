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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Impressora;
import model.services.ImpressorasService;

public class CadImpressoraListController implements Initializable {

	@FXML
	private Button btNovo;
	@FXML
	private TableView<Impressora> tableViewImpressoras;
	@FXML
	private TableColumn<Impressora, String> tableColumnFabricante;
	@FXML
	private TableColumn<Impressora, String> tableColumnModelo;
	
	private ObservableList<Impressora> obsList;
	
	@FXML
	public void onBtNewAction(ActionEvent e) {
		Stage parentStage = Utils.currentStage(e);
		Impressora obj = new Impressora();
		createDialogForm(obj, "/gui/CadImpressoraForm.fxml", parentStage);
	}
	
	private void createDialogForm(Impressora obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			CadImpressoraFormController controller = loader.getController();
			controller.setImpressora(obj);
			controller.setImpressorasService(new ImpressorasService());
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Insira os dados da impressora");
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
	public void initialize(URL url, ResourceBundle rb) {
		tableColumnFabricante.setCellValueFactory(new PropertyValueFactory<>("Fabricante"));
		tableColumnModelo.setCellValueFactory(new PropertyValueFactory<>("Modelo"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewImpressoras.prefHeightProperty().bind(stage.heightProperty());
	}
}