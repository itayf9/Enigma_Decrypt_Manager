package body;

import app.MainController;
import body.currentconfig.CurrentConfigController;
import body.screen1.codecalibration.CodeCalibrationController;
import body.screen1.machinedetails.MachineDetailsController;
import body.screen2.encrypt.EncryptDecryptController;
import body.screen2.statistics.StatisticsController;
import body.screen3.candidate.area.CandidatesAreaController;
import body.screen3.dictionary.DictionaryController;
import body.screen3.dm.operational.dmOperationalController;
import dm.difficultylevel.DifficultyLevel;
import dto.*;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Set;

import static utill.Utillity.getCurrentConfigFromSpecs;

public class BodyController {

    private MainController mainController;

    /**
     * screen 1 controllers and components
     */
    @FXML
    private GridPane codeCalibration;

    @FXML
    private CodeCalibrationController codeCalibrationController;

    @FXML
    private GridPane machineDetails;

    @FXML
    private MachineDetailsController machineDetailsController;

    @FXML
    private GridPane currentConfigScreen1;

    @FXML
    private CurrentConfigController currentConfigScreen1Controller;

    /**
     * screen 2 controllers and components
     */

    @FXML
    private GridPane currentConfigScreen2;

    @FXML
    private CurrentConfigController currentConfigScreen2Controller;

    @FXML
    private GridPane encryptDecrypt;

    @FXML
    private EncryptDecryptController encryptDecryptController;

    @FXML
    private GridPane statistics;

    @FXML
    private StatisticsController statisticsController;

    /**
     * screen 3 controllers and components
     */

    @FXML
    private GridPane currentConfigScreen3;

    @FXML
    private CurrentConfigController currentConfigScreen3Controller;

    @FXML
    private GridPane encryptDecrypt2;

    @FXML
    private EncryptDecryptController encryptDecrypt2Controller;

    @FXML
    private GridPane dmOperational;

    @FXML
    private dmOperationalController dmOperationalController;

    @FXML
    private GridPane candidatesArea;

    @FXML
    private CandidatesAreaController candidatesAreaController;

    @FXML
    private GridPane dictionary;

    @FXML
    private DictionaryController dictionaryController;


    /**
     * set up the application, connecting the controllers to their main controller
     */
    @FXML
    public void initialize() {

        //screen 1
        codeCalibrationController.setParentController(this);
        machineDetailsController.setParentController(this);
        currentConfigScreen1Controller.setParentController(this);

        //screen 2
        currentConfigScreen2Controller.setParentController(this);
        encryptDecryptController.setParentController(this);
        statisticsController.setParentController(this);

        //screen 3
        currentConfigScreen3Controller.setParentController(this);
        encryptDecrypt2Controller.setParentController(this);
        dmOperationalController.setParentController(this);
        candidatesAreaController.setParentController(this);
        dictionaryController.setParentController(this);


        dmOperationalController.bindTextToDecipherPropertyToOutputCipher(encryptDecrypt2Controller.getOutputLabelProperty());

        encryptDecrypt2Controller.setAvailabilityOfCharByCharMode(false);
        encryptDecrypt2Controller.showLightBulbs(false);
    }

    /**
     * set Parent Controller
     *
     * @param mainController the Parent Controller
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Q2 display machine specifications
     *
     * @param specsStatus DTO contains machine specs
     */
    public void displayMachineSpecs(DTOspecs specsStatus) {
        machineDetailsController.displayMachineDetails(specsStatus);
    }

    /**
     * Q4 -> going up the chain to the main controller.
     */
    public void setRandomMachineConfig() {
        mainController.setRandomMachineConfig();
        encryptDecryptController.setAllowEncryptDecrypt(true);
    }

    /**
     * display the current configuration
     *
     * @param configStatus the current configuration DTO
     */
    public void displayCurrentConfig(DTOsecretConfig configStatus) {
        currentConfigScreen1Controller.displayCurrentConfig(configStatus);
        currentConfigScreen2Controller.displayCurrentConfig(configStatus);
        codeCalibrationController.displayCurrentConfigInFields(configStatus);
    }

    /**
     * rotors validation
     *
     * @param rotorsIds the rotors ids
     * @return status
     */
    public DTOstatus validateRotorsInput(String rotorsIds) {
        return mainController.validateRotorsInput(rotorsIds);
    }

    /**
     * window character validation
     *
     * @param windows the windows characters
     * @return status
     */
    public DTOstatus validateWindowsCharsInput(String windows) {
        return mainController.validateWindowsCharsInput(windows);
    }

    /**
     * validate chosen reflector
     *
     * @param currentReflector reflector number
     * @return status
     */
    public DTOstatus validateReflectorInput(int currentReflector) {
        return mainController.validateReflectorInput(currentReflector);
    }

    /**
     * validate plugs
     *
     * @param plugs the plugs
     * @return status
     */
    public DTOstatus validatePlugsInput(String plugs) {
        return mainController.validatePlugsInput(plugs);
    }

    /**
     * Q3 -> assuming all components valid and machine will be configured!
     *
     * @param rotors    rotor ids
     * @param windows   window characters
     * @param reflector reflector number
     * @param plugs     plugs
     */
    public void setManualMachineConfig(String rotors, String windows, int reflector, String plugs) {
        mainController.setManualMachineConfig(rotors, windows, reflector, plugs);
        encryptDecryptController.setAllowEncryptDecrypt(true);
    }

    /**
     * Q5 -> going up the chain to the main controller
     *
     * @param character key pressed
     * @return status and ciphered key
     */
    public DTOciphertext cipher(String character) {
        return mainController.cipher(character);
    }

    /**
     * enable/disable calibration section
     *
     * @param isAllow bool value to disable or enable
     */
    public void setAllowCodeCalibration(boolean isAllow) {
        codeCalibrationController.selAllowCodeCalibration(isAllow);
    }

    /**
     * Q6 -> going up the chain to mainController
     */
    public void resetMachineConfiguration() {
        mainController.resetMachineConfiguration();
    }

    /**
     * gets the stats from main controller and send it to ths stats controller
     */
    public void displayStatistics() {
        DTOstatistics statistics = mainController.fetchStats();
        statisticsController.displayStats(statistics);
    }

    /**
     * init the lightBulbs
     *
     * @param machineAlphabet String of alphabet
     */
    public void setLightBulb(String machineAlphabet) {
        encryptDecryptController.initAlphabetLightBulbs(machineAlphabet);
    }

    /**
     * update the current config and ciphered text values of machine details
     */
    public void updateMachineInfo() {
        DTOspecs specsStatus = mainController.fetchSpecs();
        machineDetailsController.displayMachineDetails(specsStatus);
        currentConfigScreen1Controller.displayCurrentConfig(getCurrentConfigFromSpecs(specsStatus));
        currentConfigScreen2Controller.displayCurrentConfig(getCurrentConfigFromSpecs(specsStatus));
    }

    public void setAllowEncryptDecrypt(boolean isAllow) {
        encryptDecryptController.setAllowEncryptDecrypt(isAllow);
    }

    public void setCharByCharCipherMode(boolean newCharByCharCipherMode) {
        mainController.setCharByCharCipherMode(newCharByCharCipherMode);
    }

    public void doneCurrentCipherProcess() {
        mainController.doneCurrentCipherProcess();
    }

    public void bindComponents(BooleanProperty isMachineConfiguredProperty, ListProperty<Integer> inUseRotorsIDsProperty,
                               StringProperty currentWindowsCharactersProperty, StringProperty inUseReflectorSymbolProperty,
                               StringProperty inUsePlugs, ListProperty<Integer> currentNotchDistances, BooleanProperty isCharByCharModeProperty,
                               IntegerProperty cipherCounterProperty,
                               IntegerProperty totalDistinctCandidates, IntegerProperty totalProcessedConfigurations,
                               LongProperty totalPossibleConfigurations, DoubleProperty bruteForceProgressBar, StringProperty bruteForceProgressBarPercentageLabel
            , StringProperty bruteForceStatusMessage, BooleanProperty isBruteForceTaskActive) {

        // binds the components that need the isConfigured Boolean property.
        encryptDecrypt.disableProperty().bind(isMachineConfiguredProperty.not());
        encryptDecrypt2.disableProperty().bind(isMachineConfiguredProperty.not());
        statistics.disableProperty().bind(isMachineConfiguredProperty.not());
        dmOperational.disableProperty().bind(isMachineConfiguredProperty.not());
        candidatesArea.disableProperty().bind(isMachineConfiguredProperty.not());

        // config bindings
        currentConfigScreen1Controller.bindConfigComponents(inUseRotorsIDsProperty, currentWindowsCharactersProperty, inUseReflectorSymbolProperty, inUsePlugs, currentNotchDistances, isMachineConfiguredProperty);
        currentConfigScreen2Controller.bindConfigComponents(inUseRotorsIDsProperty, currentWindowsCharactersProperty, inUseReflectorSymbolProperty, inUsePlugs, currentNotchDistances, isMachineConfiguredProperty);
        currentConfigScreen3Controller.bindConfigComponents(inUseRotorsIDsProperty, currentWindowsCharactersProperty, inUseReflectorSymbolProperty, inUsePlugs, currentNotchDistances, isMachineConfiguredProperty);

        // cipher mode (Char By Char / Full line) bindings
        encryptDecryptController.bindCipherMode(isCharByCharModeProperty);

        // cipher counter property bind
        machineDetailsController.bindCipherCounterProperty(cipherCounterProperty);

        // brute force dashboard labels bind
        candidatesAreaController.bindInitPropertiesToLabels(totalDistinctCandidates, totalProcessedConfigurations, totalPossibleConfigurations,
                bruteForceProgressBar, bruteForceProgressBarPercentageLabel
                , bruteForceStatusMessage);

        // dm operational bind
        dmOperationalController.bindComponents(isBruteForceTaskActive);
    }

    public void displayOriginalConfig(List<Integer> rotorsIDs, String currentWindowsCharacters, String inUseReflectorSymbol, String inUsePlugs, List<Integer> currentNotchDistances) {
        machineDetailsController.displayOriginalConfiguration(rotorsIDs, currentWindowsCharacters, inUseReflectorSymbol, inUsePlugs, currentNotchDistances);
    }

    public void setDMOperetionalSettings(int maxTaskSize, int maxNumOfAgents) {
        dmOperationalController.setSettings(maxTaskSize, maxNumOfAgents);
    }

    public void startBruteForce(String textToDecipher, DifficultyLevel difficultyLevel, int taskSize, int numOfAgentSelected) {
        mainController.startBruteForceProcess(textToDecipher, difficultyLevel, taskSize, numOfAgentSelected);
    }

    public void insertCandidateToFlowPane(Node singleCandidateTile) {
        candidatesAreaController.insertCandidateToFlowPane(singleCandidateTile);
    }

    public void setDictionaryWords(Set<String> dictionaryWords) {
        dictionaryController.setDictionaryWords(dictionaryWords);
    }

    public void stopBruteForce() {
        mainController.stopBruteForceProcess();
    }

    public void clearOldResultsOfBruteForce() {
        candidatesAreaController.clearOldResultsOfBruteForce();
    }
}
