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
import model.services.ImpressorasService;
import model.services.ItensService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemItem;
	@FXML
	private MenuItem menuItemAbout;
	@FXML
	private MenuItem menuItemRecarga;
	@FXML
	private MenuItem meuItemLista;
	@FXML
	private MenuItem menuItemListaImpressora;
	
	@FXML
	public void onMenuItemItemAction() {
		loadView("/gui/ItensList.fxml", (ItensListController controller) -> {
			controller.setItensService(new ItensService());
			controller.updateTableView();
			controller.sortById();
		});
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {});
	}
	
	@FXML
	private void onMenuItemListaImpressoraAction() {
		loadView("/gui/ImpressorasList.fxml", (ImpressorasListController controller) -> {
			controller.setImpressorasService(new ImpressorasService());
			controller.updateTableView();
		});
	}
	
	public void onMenuItemRecargaAction() {
		loadView("/gui/RecargaList.fxml", (RecargaListController controller) -> {
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
