package app;

import body.BodyController;
import dto.*;
import engine.Engine;
import engine.EnigmaEngine;
import header.HeaderController;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
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

    private BooleanProperty isCharByCharModeProperty;

    private CurrWinCharsAndNotchPosBinding currWinCharsAndNotchPosBinding;

    @FXML
    public void initialize() {

        // controller initialize
        if (headerController != null && bodyController != null) {
            headerController.setMainController(this);
            bodyController.setMainController(this);
            bodyController.updateMachineInfo();

            // property initialize
            this.isMachineConfiguredProperty = new SimpleBooleanProperty(false);
            this.isMachineLoadedProperty = new SimpleBooleanProperty(false);
            this.inUseRotorsIDsProperty = new SimpleListProperty<>();
            this.currentWindowsCharactersProperty = new SimpleStringProperty("");
            this.inUseReflectorSymbolProperty = new SimpleStringProperty("");
            this.inUsePlugsProperty = new SimpleStringProperty("");
            this.currentNotchDistances = new SimpleListProperty<>();
            this.isCharByCharModeProperty = new SimpleBooleanProperty(false);

            // binding initialize
            bodyController.bindComponents(isMachineConfiguredProperty, inUseRotorsIDsProperty,
                    currentWindowsCharactersProperty, inUseReflectorSymbolProperty, inUsePlugsProperty, currentNotchDistances);
            body.visibleProperty().bind(isMachineLoadedProperty);
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
            bodyController.displayMachineSpecs(specsStatus);
            bodyController.setLightBulb(engine.getMachineAlphabet());
            bodyController.displayStatistics();
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
        return engine.cipherInputText(character);
    }

    /**
     * Q6 reset configuration
     */
    public void resetMachineConfiguration() {
        engine.resetConfiguration();
    }

    public DTOstatistics fetchStats() {
        return engine.getHistoryAndStatistics();
    }

    public DTOspecs fetchSpecs() {
        return engine.displayMachineSpecifications();
    }

    public void setCharByCharCipherMode(boolean newCharByCharCipherMode) {
        engine.setCharByCharState(newCharByCharCipherMode);
    }

    @FXML
    public void startBruteForceProcess() {
        cleanOldResults();
        UIAdapter uiAdapter = createUIAdapter();

        // toggleTaskButtons(true);

        // engine.startBruteForceProcess(uiAdapter, () -> toggleTaskButtons(false));
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
       /* return new UIAdapter(
                (Candidate)->{
                    createCandidateTile(Candidate.getDecipheredText(), Candidate.getRotorsIDs(), Candidate.getWindowChars(),
                            Candidate.getReflectorSymbol(), Candidate.getProcessedByAgentID());
                },

        )*/
        return null;
    }

    private void createCandidateTile(String decipheredText, List<Integer> rotorsIDs, String windowChars, String reflectorSymbol, int processedByAgentID) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/body/screen3/candidate/tile/candidateTile.fxml"));

            Node singleCandidateTile = loader.load();


            // candidateFlowPane.getChildren().add(singleCandidateTile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doneCurrentCipherProcess() {
        engine.doneCurrentCipherProcess();
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
