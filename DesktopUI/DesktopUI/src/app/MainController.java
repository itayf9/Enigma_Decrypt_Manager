package app;

import app.statusbar.MessageTone;
import bindings.CurrWinCharsAndNotchPosBinding;
import body.BodyController;
import body.screen3.candidate.tile.CandidateTileController;
import candidate.Candidate;
import dm.difficultylevel.DifficultyLevel;
import dto.*;
import engine.Engine;
import engine.EnigmaEngine;
import header.HeaderController;
import header.Skin;
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
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import statistics.StatisticRecord;
import ui.adapter.UIAdapter;

import java.io.IOException;
import java.net.URL;

public class MainController {

    /**
     * app private members
     */
    private final Engine engine = new EnigmaEngine();
    @FXML
    private GridPane header;
    @FXML
    private HeaderController headerController;

    @FXML
    private GridPane appGridPane;

    @FXML
    private TabPane body;
    @FXML
    private BodyController bodyController;

    @FXML
    private HBox statusBar;

    @FXML
    private Label statusLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private Rectangle statusBackShape;

    private FadeTransition messageFadeTransition;
    
    private DTOsecretConfig configStatus; // do we still need this!!!!!!?????

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
    private StringProperty bruteForceStatusMessage;
    private BooleanProperty isBruteForceTaskActive;
    private BooleanProperty isBruteForceTaskPaused;
    private long totalPossibleWindowsPositions;
    private DoubleProperty averageTasksProcessTimeProperty;
    private StringProperty dictionaryExcludeCharsProperty;

    private BooleanProperty isAnimationProperty;


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
            this.isBruteForceTaskActive = new SimpleBooleanProperty(false);
            this.isBruteForceTaskPaused = new SimpleBooleanProperty(false);
            this.averageTasksProcessTimeProperty = new SimpleDoubleProperty();
            this.dictionaryExcludeCharsProperty = new SimpleStringProperty();
            this.isAnimationProperty = new SimpleBooleanProperty(true);
            this.bruteForceProgress = new SimpleDoubleProperty();
            this.bruteForceStatusMessage = new SimpleStringProperty("");
            this.bruteForceProgressBarPercentageProperty = new SimpleStringProperty("0%");

            // binding initialize
            bodyController.bindComponents(isMachineConfiguredProperty, inUseRotorsIDsProperty,
                    currentWindowsCharactersProperty, inUseReflectorSymbolProperty, inUsePlugsProperty,
                    currentNotchDistances, isCharByCharModeProperty, cipherCounterProperty, totalDistinctCandidates,
                    totalProcessedConfigurations, totalPossibleConfigurations, bruteForceProgress,
                    bruteForceProgressBarPercentageProperty, bruteForceStatusMessage, isBruteForceTaskActive,
                    isBruteForceTaskPaused, averageTasksProcessTimeProperty);

            // general setting to initialize sub components
            body.visibleProperty().bind(isMachineLoadedProperty);
            messageLabel.textProperty().bind(statusLabel.textProperty());
            messageLabel.opacityProperty().bind(statusBackShape.opacityProperty());
            statusBackShape.heightProperty().bind(Bindings.add(2, statusLabel.heightProperty()));
            statusBackShape.widthProperty().bind(statusLabel.widthProperty());
            statusBackShape.setStrokeWidth(0);
            statusBackShape.setOpacity(0);

            // header bindings & settings
            headerController.setProperties(isAnimationProperty, isMachineLoadedProperty);
            headerController.bindSettings(isAnimationProperty);

            bodyController.setIsAnimationPropertyEncryptDecrypt(isAnimationProperty);
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
            setStatusMessage(loadStatus.getDetails().name(), MessageTone.ERROR);
        } else {
            headerController.displayFilePath();
            DTOspecs specsStatus = engine.displayMachineSpecifications();
            int rotorsCount = specsStatus.getInUseRotorsCount();
            int alphabetLength = engine.getMachineAlphabet().length();
            setStatusMessage("Machine Loaded Successfully!", MessageTone.SUCCESS);

            inUseRotorsIDsProperty.clear();
            currentWindowsCharactersProperty.set("");
            inUseReflectorSymbolProperty.set("");
            currentNotchDistances.clear();
            inUsePlugsProperty.set("");
            
            bodyController.setDictionaryWords(engine.getDictionaryWords().getDictionary());
            bodyController.displayMachineSpecs(specsStatus);
            cipherCounterProperty.set(0);
            bodyController.setLightBulb(engine.getMachineAlphabet());
            bodyController.displayStatistics();
            this.totalPossibleWindowsPositions = (int) Math.pow(alphabetLength, rotorsCount);
            bodyController.setEncryptExcludeCharsValue(dictionaryExcludeCharsProperty);
            bodyController.setDMOperetionalSettings((int) totalPossibleWindowsPositions, specsStatus.getNumOfAvailableAgents(), specsStatus);
            headerController.enableLoadButtonTransition(false);
            isMachineConfiguredProperty.setValue(Boolean.FALSE);
            isMachineLoadedProperty.setValue(Boolean.TRUE);
            dictionaryExcludeCharsProperty.setValue(specsStatus.getDictionaryExcludeCharacters());
            bodyController.setCodeCalibration(specsStatus.getInUseRotorsCount(), specsStatus.getAvailableRotorsCount(), engine.getMachineAlphabet(), specsStatus.getAvailableReflectorsCount());
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

        setStatusMessage("Configured Successfully", MessageTone.SUCCESS);

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

        setStatusMessage("Configured Successfully", MessageTone.SUCCESS);

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

        setStatusMessage("Reset Successfully", MessageTone.SUCCESS);
    }

    /**
     * gets all the statistics data from the engine
     *
     * @return all the stats gathered at the engine statistics
     */
    public DTOstatistics fetchStats() {
        return engine.getHistoryAndStatistics();
    }

    /**
     * gets all the specification data from the engine
     *
     * @return all the specification .of the current machine .from the engine
     */
    public DTOspecs fetchSpecs() {
        return engine.displayMachineSpecifications();
    }

    /**
     * configure the engine to which cipher state it needs to answer.
     *
     * @param newCharByCharCipherMode true for char-by-char mode, false otherwise
     */
    public void setCharByCharCipherMode(boolean newCharByCharCipherMode) {
        engine.setCharByCharState(newCharByCharCipherMode);
        isCharByCharModeProperty.set(newCharByCharCipherMode);
        if (newCharByCharCipherMode) {
            setStatusMessage("Switched to \"Char-By-Char\" Mode", MessageTone.INFO);
        } else {
            setStatusMessage("Switched to \"Full Line\" Mode", MessageTone.INFO);
        }
    }

    /**
     * this method initiates the brute force task at the engine
     *
     * @param textToDecipher     the text to decipher
     * @param difficultyLevel    Easy, Medium, Hard or Impossible.
     * @param taskSize           the size of single task every agent going to scan through
     * @param numOfAgentSelected how many agents going wo work on this process
     */
    public void startBruteForceProcess(String textToDecipher, DifficultyLevel difficultyLevel, int taskSize, int numOfAgentSelected) {
        isBruteForceTaskActive.set(true);
        cleanOldResults();
        UIAdapter uiAdapter = createUIAdapter();

        //fire up the DM
        engine.startBruteForceProcess(uiAdapter, textToDecipher, difficultyLevel, taskSize, numOfAgentSelected);
    }

    /**
     * clear all findings of last process and labels progress
     */
    private void cleanOldResults() {
        bodyController.clearOldResultsOfBruteForce();
        bruteForceProgress.set(0);
        totalDistinctCandidates.set(0);
        totalProcessedConfigurations.set(0);
        averageTasksProcessTimeProperty.set(0);
    }

    /**
     * create & configure how the ui adapter will change each property of Main Controller.
     *
     * @return ui adapter object
     */
    private UIAdapter createUIAdapter() {
        return new UIAdapter(
                this::createCandidateTile,
                () -> totalDistinctCandidates.set(totalDistinctCandidates.get() + 1),
                (delta) -> totalProcessedConfigurations.set(totalProcessedConfigurations.get() + delta),
                (status) -> bruteForceStatusMessage.set(status),
                (percentage) -> bruteForceProgress.set(percentage),
                (percentage) -> {
                    double percent = percentage;
                    int tmpPercentValue = (int) (percent * 100);
                    String percentValue = tmpPercentValue + "%";
                    bruteForceProgressBarPercentageProperty.set(percentValue);
                }, (totalConfigs) -> totalPossibleConfigurations.setValue(totalConfigs),
                (isActive) -> isBruteForceTaskActive.set(isActive),
                (averageTasksProcessTime) -> averageTasksProcessTimeProperty.set(averageTasksProcessTime)
        );
    }

    /**
     * creates a Candidate that shows in the flow-pane at the ui
     *
     * @param candidate the candidate to create a tile from
     */
    private void createCandidateTile(Candidate candidate) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/body/screen3/candidate/tile/candidateTile.fxml"));
            Node singleCandidateTile = loader.load();
            CandidateTileController candidateTileController = loader.getController();

            candidateTileController.setDecipheredText(candidate.getDecipheredText());
            candidateTileController.setRotorsIDs(candidate.getRotorsIDs());
            candidateTileController.setWindowsCharsAndNotches(candidate.getWindowChars(), candidate.getNotchPositions());
            candidateTileController.setReflectorSymbol(candidate.getReflectorSymbol());
            candidateTileController.setProcessedByAgentName(candidate.getProcessedByAgentName());
            
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
        DTOspecs specsStatus = engine.displayMachineSpecifications();
        cipherCounterProperty.set(specsStatus.getCipheredTextsCount());
    }

    /**
     * set the Status m
     *
     * @param newStatus   the status text message to show
     * @param messageTone Red for Errors, Blue for normal Status updates.
     */
    public void setStatusMessage(String newStatus, MessageTone messageTone) {
        statusBackShape.setOpacity(1);
        statusBackShape.getStyleClass().add(messageTone.colorClassOfMessage());
        messageTone.removeAllStyleClassExcept(statusBackShape.getStyleClass());
        messageFadeTransition.stop();

        messageFadeTransition = new FadeTransition(Duration.millis(5000), statusBackShape);
        statusLabel.setText(newStatus);
        messageFadeTransition.setFromValue(1.0);
        messageFadeTransition.setToValue(0.0);
        messageFadeTransition.setDelay(Duration.millis(3000));

        if (isAnimationProperty.getValue()) {
            messageFadeTransition.play();
        }
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

    /**
     * stops & cancel the engine Brute-Force process
     */
    public void stopBruteForceProcess() {
        isBruteForceTaskActive.set(false);
        isBruteForceTaskPaused.set(false);
        engine.stopBruteForceProcess();
    }

    /**
     * changes the skin theme across the entire app.
     *
     * @param skin the skin to change to: Dark, Normal, Spacial
     */
    public void setAppSkin(Skin skin) {

        System.out.println("before remove");
        System.out.println(header.getStylesheets());


        // removes all stylesheets
        header.getStylesheets().removeAll(header.getStylesheets());
        appGridPane.getStylesheets().removeAll(appGridPane.getStylesheets());
        body.getStylesheets().removeAll(body.getStylesheets());
        statusBar.getStylesheets().removeAll(statusBar.getStylesheets());

        System.out.println("after remove");
        System.out.println(header.getStylesheets());

        // adds "app", "header", "body", "statusbar" stylesheets
        URL appUrl = getClass().getResource("/app/app-" + skin.skinName() + ".css");
        header.getStylesheets().add(appUrl.toString());
        appGridPane.getStylesheets().add(appUrl.toString());
        body.getStylesheets().add(appUrl.toString());
        statusBar.getStylesheets().add(appUrl.toString());

        URL headerUrl = getClass().getResource("/header/header-" + skin.skinName() + ".css");
        header.getStylesheets().add(headerUrl.toString());

        URL bodyUrl = getClass().getResource("/body/body-" + skin.skinName() + ".css");
        body.getStylesheets().add(bodyUrl.toString());

        URL statusbarUrl = getClass().getResource("/app/statusbar/statusbar-" + skin.skinName() + ".css");
        statusBar.getStylesheets().add(statusbarUrl.toString());

        System.out.println("after add");
        System.out.println(header.getStylesheets());

        // adds stylesheets to the body components
        bodyController.setComponentsSkin(appUrl.toString(), skin);

        // sets images
        headerController.setImages(skin);

    }

    /**
     * pause the engine brute force process
     */
    public void pauseBruteForceProcess() {
        isBruteForceTaskPaused.set(true);
        engine.pauseBruteForceProcess();
    }

    /**
     * resume the engine brute force process
     */
    public void resumeBruteForceProcess() {
        isBruteForceTaskPaused.set(false);
        engine.resumeBruteForceProcess();
    }

    /**
     * check if text words are from dictionary
     *
     * @param textToCipher textToCipher
     * @return .True - if all words are in dictionary, False otherwise
     */
    public boolean isAllWordsInDictionary(String textToCipher) {
        return engine.isAllWordsInDictionary(textToCipher);
    }
}
