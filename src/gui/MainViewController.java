package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.entities.FabricanteImpressora;
import model.entities.ModeloImpressora;
import model.entities.Setor;
import model.services.CadFabricanteImpressoraService;
import model.services.CadModeloImpressoraService;
import model.services.CadSetorService;
import model.services.MaterialService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemMaterial;
	@FXML
	private MenuItem menuItemAbout;
	@FXML
	private MenuItem menuItemRecarga;
	@FXML
	private MenuItem meuItemLista;
	@FXML
	private MenuItem menuItemListaImpressora;
	@FXML
	private MenuItem menuItemFabricanteImpressora;
	@FXML
	private MenuItem menuItemModeloImpressora;
	@FXML
	private MenuItem menuItemSetor;
	
	@FXML
	public void onMenuItemMaterialAction() {
		loadView("/gui/MateriaisList.fxml", (MateriaisListController controller) -> {
			controller.setItensService(new MaterialService());
			controller.updateTableView();
			controller.sortById();
		});
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {});
	}
	
	@FXML
	private void onMenuItemFabricanteImpressoraAction() {
		loadView("/gui/CadFabricanteImpressora.fxml", (CadFabricanteImpressoraController controller) -> {
			controller.setCadFabricanteImpressoraService(new CadFabricanteImpressoraService());
			FabricanteImpressora obj = new FabricanteImpressora();
			controller.setFabricanteImpressora(obj);
			controller.updateTableView();
		});
	}
	
	@FXML
	private void onMenuItemModeloImpressoraAction() {
		loadView("/gui/CadModeloImpressora.fxml", (CadModeloImpressoraController controller) -> {
			controller.setCadFabricanteImpressoraService(new CadFabricanteImpressoraService());
			controller.setCadModeloImpressoraService(new CadModeloImpressoraService());
			ModeloImpressora obj = new ModeloImpressora();
			controller.setModeloImpressora(obj);
			controller.atualizaComboBoxImpressora();
		});
	}
	
	@FXML
	private void onMenuItemSetorAction() {
		loadView("/gui/CadSetor.fxml", (CadSetorController controller) -> {
			controller.setCadSetorService(new CadSetorService());
			Setor obj = new Setor();
			controller.setSetor(obj);
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemRecargaAction() {
		loadView("/gui/RecargaList.fxml", (RecargaListController controller) -> {
			controller.setCadModeloImpressoraService(new CadModeloImpressoraService());
			controller.atualizaImpressoras();
		});
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}
	
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox)((ScrollPane)mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			T controller = loader.getController();
			initializingAction.accept(controller);
			
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro carregando a tela", e.getMessage(), AlertType.ERROR);
		}
	}
}
