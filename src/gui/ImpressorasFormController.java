package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ImpressorasFormController implements Initializable {
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtModelo;
	@FXML
	private TextField txtSetor;
	@FXML
	private TextField txtApelido;
	@FXML
	private Button btSalvar;
	@FXML
	private Button btCancelar;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
	}

}
