package header;

import app.MainController;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import problem.Problem;

import java.io.File;

public class HeaderController {

    FileChooser fileChooser = new FileChooser();

    String selectedMachineFile;

    FadeTransition loadButtonFadeTransition;

    private MainController mainController;


    @FXML
    private RadioMenuItem skinDefaultButton;

    @FXML
    private ToggleGroup skinsGroup;

    @FXML
    private RadioMenuItem skinDarkButton;

    @FXML
    private Label filePathLoadMachineLabel;

    @FXML
    private Button loadFileButton;

    @FXML
    private Label headerMessageLabel;

    @FXML
    public void initialize() {

        filePathLoadMachineLabel.setText("");
        headerMessageLabel.setText("");


        loadButtonFadeTransition = new FadeTransition(Duration.millis(1500), loadFileButton);
        loadButtonFadeTransition.setFromValue(1.0);
        loadButtonFadeTransition.setToValue(0.3);
        loadButtonFadeTransition.setCycleCount(Animation.INDEFINITE);
        loadButtonFadeTransition.setAutoReverse(true);

        enableLoadButtonTransition(true);

        // setting the skin selection
        skinsGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            RadioMenuItem radioMenuItem = (RadioMenuItem) newValue;
            Skin skin = Skin.DEFAULT;

            switch (radioMenuItem.getId()) {
                case "skinDefaultButton":
                    skin = Skin.DEFAULT;
                    break;
                case "skinDarkButton":
                    skin = Skin.DARK;
                    break;
            }

            mainController.setAppSkin(skin);
        });


    }


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
        filePathLoadMachineLabel.setText(selectedMachineFile);
    }

    public void enableLoadButtonTransition(boolean isAllow) {
        if (isAllow) {
            loadButtonFadeTransition.play();
        } else {
            loadButtonFadeTransition.stop();
            FadeTransition recover = new FadeTransition(Duration.millis(500), loadFileButton);
            recover.setToValue(1.0);
            recover.play();
        }
    }

}
