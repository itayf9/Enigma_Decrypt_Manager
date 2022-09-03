package body.screen3.dm.operational;

import body.BodyController;
import dm.difficultylevel.DifficultyLevel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class dmOperationalController {

    private BodyController parentController;

    @FXML
    private Slider numOfAgentsSlider;

    @FXML
    private ComboBox<String> difficultyLevelComboBox;

    @FXML
    private Spinner<?> taskSizeSpinner;

    @FXML
    private Button playButton;

    @FXML
    private Button pauseButton;

    @FXML
    private Label numOfAgentLabel;

    @FXML
    public void initialize() {
        difficultyLevelComboBox.getItems().add("Easy");
        difficultyLevelComboBox.getItems().add("Medium");
        difficultyLevelComboBox.getItems().add("Hard");
        difficultyLevelComboBox.getItems().add("Impossible");

        difficultyLevelComboBox.setPromptText("Please Select");
        
    }

    public void setParentController(BodyController parentController) {
        this.parentController = parentController;
    }
}
