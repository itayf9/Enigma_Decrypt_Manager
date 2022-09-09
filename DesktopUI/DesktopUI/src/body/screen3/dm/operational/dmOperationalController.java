package body.screen3.dm.operational;

import body.BodyController;
import dm.difficultylevel.DifficultyLevel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class dmOperationalController {

    private BodyController parentController;

    @FXML
    private Slider numOfAgentsSlider;

    @FXML
    private ComboBox<DifficultyLevel> difficultyLevelComboBox;

    @FXML
    private Spinner<Integer> taskSizeSpinner;

    @FXML
    private Button startButton;

    @FXML
    private Button pauseButton;

    @FXML
    private Label numOfAgentLabel;
    private IntegerProperty totalPossibleWindowsPositions;
    private StringProperty textToDecipherProperty;

    private BooleanProperty isBruteForceTaskActive;


    @FXML
    public void initialize() {
        difficultyLevelComboBox.getItems().add(DifficultyLevel.EASY);
        difficultyLevelComboBox.getItems().add(DifficultyLevel.MEDIUM);
        difficultyLevelComboBox.getItems().add(DifficultyLevel.HARD);
        difficultyLevelComboBox.getItems().add(DifficultyLevel.IMPOSSIBLE);
        difficultyLevelComboBox.setPromptText("Please Select");
        this.textToDecipherProperty = new SimpleStringProperty();
        numOfAgentLabel.textProperty().bind(Bindings.concat("Number Of Agents: ", Bindings.format("%.0f", numOfAgentsSlider.valueProperty())));
    }

    @FXML
    void handleBruteForceStartStopOperationAction(MouseEvent event) {

        if (isBruteForceTaskActive.getValue()) {
            stopBruteForceAction();
        } else {
            startBruteForceAction();
        }
    }

    @FXML
    void pauseBruteForceAction(MouseEvent event) {

    }

    @FXML
    void resumeBruteForceAction(MouseEvent event) {

    }

    void stopBruteForceAction() {
    }

    void startBruteForceAction() {
        DifficultyLevel difficultyLevel = difficultyLevelComboBox.getValue();
        int taskSize = taskSizeSpinner.getValue(); // need to fix text value not updating if no button pressed
        int numOfAgentSelected = (int) numOfAgentsSlider.getValue();
        String textToDecipher = textToDecipherProperty.getValue();

        if (difficultyLevel == null) {
            System.out.println("Error not entered dificulty level");
            return;
        }

        parentController.startBruteForce(textToDecipher, difficultyLevel, taskSize, numOfAgentSelected);
    }

    public void setSettings(int maxTaskSize, int maxNumOfAgents) {
        taskSizeSpinner.valueFactoryProperty().set(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxTaskSize));
        numOfAgentsSlider.setMax(maxNumOfAgents);
    }

    public void bindTextToDecipherPropertyToOutputCipher(StringProperty cipheredText) {
        textToDecipherProperty.bind(cipheredText);
    }

    public void setParentController(BodyController parentController) {
        this.parentController = parentController;
    }

    public void bindComponents(BooleanProperty isBruteForceTaskActive) {
        this.isBruteForceTaskActive = isBruteForceTaskActive;
        startButton.textProperty().bind(Bindings.when(isBruteForceTaskActive.not()).then("Start").otherwise("Stop"));
        pauseButton.disableProperty().bind(Bindings.when(isBruteForceTaskActive.not()).then(true).otherwise(false));
    }
}
