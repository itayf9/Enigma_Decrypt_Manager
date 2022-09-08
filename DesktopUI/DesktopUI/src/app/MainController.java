package app;

import bindings.CurrWinCharsAndNotchPosBinding;
import body.BodyController;
import dm.difficultylevel.DifficultyLevel;
import dto.*;
import engine.Engine;
import engine.EnigmaEngine;
import header.HeaderController;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import statistics.StatisticRecord;
import ui.adapter.UIAdapter;

import java.io.IOException;
import java.util.List;

public class MainController {

    /**
     * app private members
     */
    private Engine engine = new EnigmaEngine();
    @FXML
    private GridPane header;
    @FXML
    private HeaderController headerController;
    @FXML
    private TabPane body;
    @FXML
    private BodyController bodyController;

    @FXML
    private Label statusLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private Rectangle statusBackShape;

    private FadeTransition messageFadeTransition;


    private DTOsecretConfig configStatus;

    /**
     * property stuff
     */
    private BooleanProperty isMachineConfiguredProperty;
    private BooleanProperty isMachineLoadedProperty;

    // these properties would store the data of current config component.
    private ListProperty<Integer> inUseRotorsIDsProperty;
    private StringProperty currentWindowsCharactersProperty;
    private StringProperty inUseReflectorSymbolProperty;
    private StringProperty inUsePlugsProperty;
    private ListProperty<Integer> currentNotchDistances;

    private CurrWinCharsAndNotchPosBinding currWinCharsAndNotchPosBinding;
    private IntegerProperty cipherCounterProperty;
    private BooleanProperty isCharByCharModeProperty;
    private ListProperty<StatisticRecord> statisticsProperty;

    /**
     * bruteforce stuff
     */

    private IntegerProperty totalDistinctCandidates;
    private IntegerProperty totalProcessedConfigurations;
    private LongProperty totalPossibleConfigurations;

    private DoubleProperty bruteForceProgress;
    private StringProperty bruteForceProgressBarPercentageProperty;
    private StringProperty bruteForceStatus;

    private long totalPossibleWindowsPositions;


    @FXML
    public void initialize() {

        // controller initialize
        if (headerController != null && bodyController != null) {
            headerController.setMainController(this);
            bodyController.setMainController(this);

            // property initialize
            this.isMachineConfiguredProperty = new SimpleBooleanProperty(false);
            this.isMachineLoadedProperty = new SimpleBooleanProperty(false);
            this.inUseRotorsIDsProperty = new SimpleListProperty<>();
            this.currentWindowsCharactersProperty = new SimpleStringProperty("");
            this.inUseReflectorSymbolProperty = new SimpleStringProperty("");
            this.inUsePlugsProperty = new SimpleStringProperty("");
            this.currentNotchDistances = new SimpleListProperty<>();
            this.isCharByCharModeProperty = new SimpleBooleanProperty(false);
            this.cipherCounterProperty = new SimpleIntegerProperty(0);
            this.statisticsProperty = new SimpleListProperty<>();
            this.totalDistinctCandidates = new SimpleIntegerProperty();
            this.totalProcessedConfigurations = new SimpleIntegerProperty();
            this.totalPossibleConfigurations = new SimpleLongProperty();
            this.messageFadeTransition = new FadeTransition(Duration.millis(5000), statusBackShape);

            /**
             * bruteForce BINDINGS
             */
            this.bruteForceProgress = new SimpleDoubleProperty();
            this.bruteForceStatus = new SimpleStringProperty("");
            this.bruteForceProgressBarPercentageProperty = new SimpleStringProperty("0%");

            // binding initialize
            bodyController.bindComponents(isMachineConfiguredProperty, inUseRotorsIDsProperty,
                    currentWindowsCharactersProperty, inUseReflectorSymbolProperty, inUsePlugsProperty,
                    currentNotchDistances, cipherCounterProperty, totalDistinctCandidates,
                    totalProcessedConfigurations, totalPossibleConfigurations, bruteForceProgress,
                    bruteForceProgressBarPercentageProperty, bruteForceStatus);

            body.visibleProperty().bind(isMachineLoadedProperty);
            messageLabel.textProperty().bind(statusLabel.textProperty());
            messageLabel.opacityProperty().bind(statusBackShape.opacityProperty());
            statusBackShape.heightProperty().bind(Bindings.add(2, statusLabel.heightProperty()));
            statusBackShape.widthProperty().bind(statusLabel.widthProperty());
            statusBackShape.setStrokeWidth(0);
            statusBackShape.setOpacity(0);
        }
    }

    /**
     * Q1 + Q2 Load the machine
     *
     * @param selectedMachineFile fileName
     */
    public void loadMachineFromFile(String selectedMachineFile) {
        String fileName = "C:/Users/itayf/Downloads/resource/ex1-sanity-paper-enigma.xml";
        DTOstatus loadStatus = engine.buildMachineFromXmlFile(selectedMachineFile);
        if (!loadStatus.isSucceed()) {
            headerController.displayHeaderProblem(loadStatus.getDetails());
        } else {
            headerController.displaySuccessHeaderLabel();
            DTOspecs specsStatus = engine.displayMachineSpecifications();

            int rotorsCount = specsStatus.getInUseRotorsCount();
            int alphabetLength = engine.getMachineAlphabet().length();

            setStatusMessage("Loaded Successfully");

            bodyController.displayMachineSpecs(specsStatus);
            bodyController.setLightBulb(engine.getMachineAlphabet());
            bodyController.displayStatistics();

            this.totalPossibleWindowsPositions = (int) Math.pow(alphabetLength, rotorsCount);
            bodyController.setDMOperetionalSettings((int) totalPossibleWindowsPositions, specsStatus.getNumOfAvailableAgents());

            headerController.enableLoadButtonTransition(false);

            isMachineConfiguredProperty.setValue(Boolean.FALSE);
            isMachineLoadedProperty.setValue(Boolean.TRUE);
        }
    }

    /**
     * Q3 set manual config
     *
     * @param rotors    rotors ids
     * @param windows   window characters
     * @param reflector reflector number
     * @param plugs     plugs
     */
    public void setManualMachineConfig(String rotors, String windows, int reflector, String plugs) {
        DTOsecretConfig configStatus = engine.selectConfigurationManual(rotors, windows, reflector, plugs);

        ObservableList<Integer> rotorsObservableList = FXCollections.observableArrayList(configStatus.getRotors());
        inUseRotorsIDsProperty.setValue(rotorsObservableList);

        currentWindowsCharactersProperty.setValue(configStatus.getWindows());
        inUseReflectorSymbolProperty.setValue(configStatus.getReflectorSymbol());
        inUsePlugsProperty.setValue(configStatus.getPlugs());

        ObservableList<Integer> notchDistanceObservableList = FXCollections.observableArrayList(configStatus.getNotchDistances());
        currentNotchDistances.setValue(notchDistanceObservableList);

        // display original config in machine specs
        bodyController.displayOriginalConfig(configStatus.getRotors(), configStatus.getWindows(), configStatus.getReflectorSymbol(), configStatus.getPlugs(), configStatus.getNotchDistances());

        setStatusMessage("Configured Successfully");

        isMachineConfiguredProperty.setValue(Boolean.TRUE);
    }

    /**
     * Q4 set configuration auto
     */
    public void setRandomMachineConfig() {
        DTOsecretConfig configStatus = engine.selectConfigurationAuto();

        ObservableList<Integer> rotorsObservableList = FXCollections.observableArrayList(configStatus.getRotors());
        inUseRotorsIDsProperty.setValue(rotorsObservableList);

        currentWindowsCharactersProperty.setValue(configStatus.getWindows());
        inUseReflectorSymbolProperty.setValue(configStatus.getReflectorSymbol());
        inUsePlugsProperty.setValue(configStatus.getPlugs());

        ObservableList<Integer> notchDistanceObservableList = FXCollections.observableArrayList(configStatus.getNotchDistances());
        currentNotchDistances.setValue(notchDistanceObservableList);

        setStatusMessage("Configured Successfully");

        isMachineConfiguredProperty.setValue(Boolean.TRUE);

        bodyController.displayOriginalConfig(inUseRotorsIDsProperty.getValue(), currentWindowsCharactersProperty.getValue(), inUseReflectorSymbolProperty.getValue(), inUsePlugsProperty.getValue(), currentNotchDistances.getValue());
    }

    /**
     * Q5 cipher character
     *
     * @param character String that contains one character
     * @return ciphered Character
     */
    public DTOciphertext cipher(String character) {
        DTOciphertext cipherStatus = engine.cipherInputText(character);

        DTOspecs specsStatus = engine.displayMachineSpecifications();

        // update configuration
        currentWindowsCharactersProperty.setValue(specsStatus.getCurrentWindowsCharacters());
        ObservableList<Integer> notchDistanceObservableList = FXCollections.observableArrayList(specsStatus.getNotchDistancesToWindow());
        currentNotchDistances.setValue(notchDistanceObservableList);
        cipherCounterProperty.setValue(specsStatus.getCipheredTextsCount());

        DTOstatistics statsStatus = engine.getHistoryAndStatistics();
        // update statistics
        ObservableList<StatisticRecord> statisticsObservableList = FXCollections.observableArrayList(statsStatus.getStats());
        statisticsProperty.setValue(statisticsObservableList);

        return cipherStatus;
    }

    /**
     * Q6 reset configuration
     */
    public void resetMachineConfiguration() {
        engine.resetConfiguration();

        DTOspecs specsStatus = engine.displayMachineSpecifications();

        currentWindowsCharactersProperty.setValue(specsStatus.getOriginalWindowsCharacters()); // current should be the same here

        ObservableList<Integer> notchDistanceObservableList = FXCollections.observableArrayList(specsStatus.getOriginalNotchPositions()); // current should be the same here
        currentNotchDistances.setValue(notchDistanceObservableList);

        setStatusMessage("Reset Successfully");
    }

    public DTOstatistics fetchStats() {
        return engine.getHistoryAndStatistics();
    }

    public DTOspecs fetchSpecs() {
        return engine.displayMachineSpecifications();
    }

    public void setCharByCharCipherMode(boolean newCharByCharCipherMode) {
        engine.setCharByCharState(newCharByCharCipherMode);
        if (newCharByCharCipherMode) {
            setStatusMessage("Switched to \"Char-By-Char\" Mode");
        } else {
            setStatusMessage("Switched to \"Full Line\" Mode");
        }
    }

    @FXML
    public void startBruteForceProcess(String textToDecipher, DifficultyLevel difficultyLevel, int taskSize, int numOfAgentSelected) {

        cleanOldResults();
        UIAdapter uiAdapter = createUIAdapter();
        toggleTaskButtons(true);

        //fire up the DM
        engine.startBruteForceProcess(uiAdapter, () -> toggleTaskButtons(false),
                textToDecipher, difficultyLevel, taskSize, numOfAgentSelected);
    }

    private void toggleTaskButtons(boolean isActive) {
        // stopTaskButton.setDisable(!isActive);
        // clearTaskButton.setDisable(isActive);
    }

    private void cleanOldResults() {
        // candidateFlowPane.getChildren().clear();
        // taskProgressBar.setProgress(0);
        // totalDistinctWords.set(0); delete if no use
        // totalProcessedCandidates.set(0);
    }

    private UIAdapter createUIAdapter() {
        return new UIAdapter(
                (Candidate) -> {
                    createCandidateTile(Candidate.getDecipheredText(), Candidate.getRotorsIDs(), Candidate.getWindowChars(),
                            Candidate.getReflectorSymbol(), Candidate.getProcessedByAgentID());
                },
                () -> {
                    totalDistinctCandidates.set(totalDistinctCandidates.get() + 1);
                },
                (delta) -> {
                    totalProcessedConfigurations.set(totalProcessedConfigurations.get() + delta);
                }, (status) -> {
            bruteForceStatus.set(status);
        }, (percentage) -> {
            bruteForceProgress.set(percentage.doubleValue());
        },
                (percentage) -> {
                    double percent = percentage.doubleValue();
                    int tmpPercentValue = (int) percent * 100;
                    String percentValue = tmpPercentValue + "%";
                    bruteForceProgressBarPercentageProperty.set(percentValue);
                }, (totalConfigs) -> {
            totalPossibleConfigurations.setValue(totalConfigs);
        }
        );
    }

    /**
     * creates a Candidate that shows in the flow-pane at the ui
     *
     * @param decipheredText     the decipheredText
     * @param rotorsIDs          rotors part of config
     * @param windowChars        windows part of config
     * @param reflectorSymbol    reflector part of config
     * @param processedByAgentID agent ID
     */
    private void createCandidateTile(String decipheredText, List<Integer> rotorsIDs, String windowChars, String reflectorSymbol, int processedByAgentID) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/body/screen3/candidate/tile/candidateTile.fxml"));

            Node singleCandidateTile = loader.load();

            bodyController.insertCandidateToFlowPane(singleCandidateTile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * tells the engine to save ciphered characters to statistics and to stop holding characters in the buffer.
     */
    public void doneCurrentCipherProcess() {
        engine.doneCurrentCipherProcess();
    }

    public void setStatusMessage(String newStatus) {
        statusBackShape.setOpacity(1);
        messageFadeTransition.stop();

        messageFadeTransition = new FadeTransition(Duration.millis(5000), statusBackShape);
        statusLabel.setText(newStatus);
        messageFadeTransition.setFromValue(1.0);
        messageFadeTransition.setToValue(0.0);
        messageFadeTransition.setDelay(Duration.millis(3000));
        messageFadeTransition.play();
    }

    /**
     * validate rotors
     *
     * @param rotors the rotors ids
     * @return status
     */
    public DTOstatus validateRotorsInput(String rotors) {
        return engine.validateRotors(rotors);
    }

    /**
     * validate window characters
     *
     * @param windowChars the window characters
     * @return status
     */
    public DTOstatus validateWindowsCharsInput(String windowChars) {
        return engine.validateWindowCharacters(windowChars);
    }

    /**
     * validate reflector
     *
     * @param currentReflector reflector number
     * @return status
     */
    public DTOstatus validateReflectorInput(int currentReflector) {
        return engine.validateReflector(currentReflector);
    }

    /**
     * validate plugs
     *
     * @param plugs the plugs
     * @return status
     */
    public DTOstatus validatePlugsInput(String plugs) {
        return engine.validatePlugs(plugs);
    }
}
