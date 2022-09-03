package body.screen3.dm.operational;

import body.BodyController;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class dmOperationalController {

    private BodyController parentController;

    @FXML
    private Slider numOfAgentsSlider;

    @FXML
    private ComboBox<?> difficultyLevelComboBox;

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

    }

    public void setParentController(BodyController parentController) {
        this.parentController = parentController;
    }
}