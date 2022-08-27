package body;

import app.MainController;
import body.currentconfig.CurrentConfigController;
import body.screen1.codecalibration.CodeCalibrationController;
import body.screen1.machinedetails.MachineDetailsController;
import body.screen2.encrypt.EncryptDecryptController;
import body.screen2.statistics.StatisticsController;
import dto.*;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.awt.*;

import static utill.Utillity.getCurrentConfigFromSpecs;

public class BodyController {

    private MainController mainController;

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

    @FXML
    private GridPane currentConfigScreen2;

    @FXML
    private CurrentConfigController currentConfigScreen2Controller;

    @FXML
    private GridPane currentConfigScreen3;

    @FXML
    private CurrentConfigController currentConfigScreen3Controller;

    @FXML
    private GridPane encryptDecrypt;

    @FXML
    private EncryptDecryptController encryptDecryptController;

    @FXML
    private GridPane statistics;

    @FXML
    private StatisticsController statisticsController;

    /**
     * init controllers
     */
    public void setChildrenControllers() {
        codeCalibrationController.setParentController(this);
        machineDetailsController.setParentController(this);
        currentConfigScreen1Controller.setParentController(this);
        currentConfigScreen2Controller.setParentController(this);
        encryptDecryptController.setParentController(this);
        statisticsController.setParentController(this);
    }

    /**
     * set Parent Controller
     * @param mainController the Parent Controller
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Q2 display machine specifications
     * @param specsStatus DTO contains machine specs
     */
    public void displayMachineSpecs(DTOspecs specsStatus) {
        setChildrenControllers();
        machineDetailsController.displayMachineDetails(specsStatus);
        currentConfigScreen1Controller.displayCurrentConfig();
        currentConfigScreen2Controller.displayCurrentConfig();
    }

    /**
     * Q4 -> going up the chain to the main controller.
     */
    public void setRandomMachineConfig() {
        mainController.setRandomMachineConfig();
    }

    /**
     * display the current configuration
     * @param configStatus the current configuration DTO
     */
    public void displayCurrentConfig(DTOsecretConfig configStatus) {
        currentConfigScreen1Controller.displayCurrentConfig(configStatus);
        currentConfigScreen2Controller.displayCurrentConfig(configStatus);
    }

    /**
     * rotors validation
     * @param rotorsIds the rotors ids
     * @return status
     */
    public DTOstatus validateRotorsInput(String rotorsIds) {
        return mainController.validateRotorsInput(rotorsIds);
    }

    /**
     * window character validation
     * @param windows the windows characters
     * @return status
     */
    public DTOstatus validateWindowsCharsInput(String windows) {
        return mainController.validateWindowsCharsInput(windows);
    }

    /**
     * validate chosen reflector
     * @param currentReflector reflector number
     * @return status
     */
    public DTOstatus validateReflectorInput(int currentReflector) {
        return mainController.validateReflectorInput(currentReflector);
    }

    /**
     * validate plugs
     * @param plugs the plugs
     * @return status
     */
    public DTOstatus validatePlugsInput(String plugs) {
        return mainController.validatePlugsInput(plugs);
    }

    /**
     * Q3 -> going up the chain to the main Controller
     * @param rotors rotor ids
     * @param windows window characters
     * @param reflector reflector number
     * @param plugs plugs
     */
    public void setManualMachineConfig(String rotors, String windows, int reflector, String plugs) {
        mainController.setManualMachineConfig(rotors, windows, reflector, plugs);
    }

    /**
     * Q5 -> going up the chain to the main controller
     * @param character key pressed
     * @return status and ciphered key
     */
    public DTOciphertext cipherCharacter(String character) {
        return mainController.cipherChar(character);
    }

    /**
     * enable/disable calibration section
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

    public void updateMachineInfo() {
        DTOspecs specsStatus = mainController.fetchSpecs();

        machineDetailsController.displayMachineDetails(specsStatus);
        currentConfigScreen1Controller.displayCurrentConfig(getCurrentConfigFromSpecs(specsStatus));
        currentConfigScreen2Controller.displayCurrentConfig(getCurrentConfigFromSpecs(specsStatus));
    }
}
