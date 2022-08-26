package header;

import app.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class HeaderController {

    FileChooser fileChooser = new FileChooser();

    private MainController mainController;

    @FXML
    private Button buttonLoadMachine;

    @FXML
    private TextField textFieldLoadMachine;

    @FXML
    private Label headerMesssageLabel;

    @FXML
    private void loadMachineFile(MouseEvent event) {
        fileChooser.setTitle("Load Machine");
        // String selectedMachineFile = fileChooser.showOpenDialog(new Stage()).getPath();

        //textFieldLoadMachine.setText(selectedMachineFile);
        mainController.loadMachineFromFile("selectedMachineFile");
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

}
