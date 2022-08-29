package header;

import app.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import problem.Problem;

import java.io.File;

public class HeaderController {

    FileChooser fileChooser = new FileChooser();

    String selectedMachineFile;

    private MainController mainController;

    @FXML
    private Button buttonLoadMachine;

    @FXML
    private TextField textFieldLoadMachine;

    @FXML
    private Label headerMessageLabel;

    @FXML
    private void loadMachineFile(MouseEvent event) {
        fileChooser.setTitle("Load Machine");
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml")));
        File chosenFile = fileChooser.showOpenDialog(new Stage());

        if (chosenFile != null) {
            this.selectedMachineFile = chosenFile.getAbsolutePath();
            mainController.loadMachineFromFile(selectedMachineFile);
        }

    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void displayHeaderProblem(Problem details) {
        headerMessageLabel.getStyleClass().removeAll("success-label");
        headerMessageLabel.getStyleClass().add("error-label");
        headerMessageLabel.setText(details.name());
    }

    public void displaySuccessHeaderLabel() {
        headerMessageLabel.getStyleClass().removeAll("error-label");
        headerMessageLabel.getStyleClass().add("success-label");
        headerMessageLabel.setText("Machine Loaded Successfully!");
        textFieldLoadMachine.setText(selectedMachineFile);
    }
}
