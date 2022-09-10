package body.screen3.dm.operational;

import body.BodyController;
import dm.difficultylevel.DifficultyLevel;
import dto.DTOspecs;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import static utill.Utility.factorial;

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

    @FXML
    private Label tasksAmountLabel;

    private long totalPossibleWindowsPositions;
    private StringProperty textToDecipherProperty;

    private BooleanProperty isBruteForceTaskActive;

    private DTOspecs specStatus;


    @FXML
    public void initialize() {
        difficultyLevelComboBox.getItems().add(DifficultyLevel.EASY);
        difficultyLevelComboBox.getItems().add(DifficultyLevel.MEDIUM);
        difficultyLevelComboBox.getItems().add(DifficultyLevel.HARD);
        difficultyLevelComboBox.getItems().add(DifficultyLevel.IMPOSSIBLE);
        difficultyLevelComboBox.setPromptText("Please Select");
        this.textToDecipherProperty = new SimpleStringProperty();
        numOfAgentLabel.textProperty().bind(Bindings.concat("Number Of Agents: ", Bindings.format("%.0f", numOfAgentsSlider.valueProperty())));

        taskSizeSpinner.valueProperty().addListener((observable, oldValue, newValue) -> displayTasksAmount(newValue, difficultyLevelComboBox.getValue()));
        difficultyLevelComboBox.valueProperty().addListener((observable, oldValue, newValue) -> displayTasksAmount(taskSizeSpinner.getValue(), newValue));
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
        parentController.stopBruteForce();
    }

    void startBruteForceAction() {
        DifficultyLevel difficultyLevel = difficultyLevelComboBox.getValue();
        int taskSize = taskSizeSpinner.getValue(); // need to fix text value not updating if no button pressed
        int numOfAgentSelected = (int) numOfAgentsSlider.getValue();
        String textToDecipher = textToDecipherProperty.getValue();
        System.out.println("take size: " + taskSize);

        if (difficultyLevel == null) {
            System.out.println("Error not entered difficulty level");
            return;
        }

        if (textToDecipher.equals("")) {
            System.out.println("Error not entered text to decipher");
            return;
        }

        parentController.startBruteForce(textToDecipher, difficultyLevel, taskSize, numOfAgentSelected);
    }

    public void setSettings(long totalPossibleWindowsPositions, int maxNumOfAgents, DTOspecs specStatus) {
        this.specStatus = specStatus;
        taskSizeSpinner.valueFactoryProperty().set(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, (int) totalPossibleWindowsPositions));
        numOfAgentsSlider.setMax(maxNumOfAgents);
        this.totalPossibleWindowsPositions = totalPossibleWindowsPositions;
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

    public void displayTasksAmount(int taskSize, DifficultyLevel difficultyLevel) {

        long totalPossibleConfigurations = 0;

        if (difficultyLevel == null) {
            tasksAmountLabel.setText("");
            return;
        }

        switch (difficultyLevel) {
            case EASY:
                totalPossibleConfigurations = (totalPossibleWindowsPositions);
                break;
            case MEDIUM:
                totalPossibleConfigurations = (totalPossibleWindowsPositions * specStatus.getAvailableReflectorsCount());
                break;
            case HARD:
                totalPossibleConfigurations = (totalPossibleWindowsPositions *
                        specStatus.getAvailableReflectorsCount() *
                        factorial(specStatus.getInUseRotorsCount()));
                break;
            case IMPOSSIBLE:
                // need to calculate then implement Too HARD
                break;
        }

        long numOfTasks;
        if (totalPossibleConfigurations % taskSize != 0) {
            numOfTasks = (totalPossibleConfigurations / taskSize) + 1;
        } else {
            numOfTasks = totalPossibleConfigurations / taskSize;
        }

        tasksAmountLabel.setText(String.valueOf(numOfTasks));

    }
}
