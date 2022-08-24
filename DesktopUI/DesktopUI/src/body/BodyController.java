package body;

import app.MainController;
import body.currentconfig.CurrentConfigController;
import body.screen1.codecalibration.CodeCalibrationController;
import body.screen1.machinedetails.MachineDetailsController;
import dto.DTOsecretConfig;
import dto.DTOspecs;
import dto.DTOstatus;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.GridPane;

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
    private GridPane currentConfig;

    @FXML
    private CurrentConfigController currentConfigController;

    public void setChildrenControllers() {
        codeCalibrationController.setParentController(this);
        machineDetailsController.setParentController(this);
        currentConfigController.setParentController(this);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }


    public void displayMachineSpecs(DTOspecs specsStatus) {
        setChildrenControllers();
        machineDetailsController.displayMachineDetails(specsStatus);
        currentConfigController.displayCurrentConfig();
    }

    public void setRandomMachineConfig() {
        mainController.setRandomMachineConfig();
    }

    public void displayCurrentConfig(DTOsecretConfig configStatus) {
        currentConfigController.displayCurrentConfig(configStatus);
    }

    public DTOstatus validateRotorsInput(String text) {
        return mainController.validateRotorsInput(text);
    }

    public DTOstatus validateWindowsCharsInput(String input) {
        return mainController.validateWindowsCharsInput(input);
    }

    public DTOstatus validateReflectorInput(int currentReflector) {
        return mainController.validateReflectorInput(currentReflector);
    }

    public DTOstatus validatePlugsInput(String input) {
        return mainController.validatePlugsInput(input);
    }

    public void setManualMachineConfig(String rotors, String windwos, int reflector, String plugs) {
        mainController.setManualMachineConfig(rotors, windwos, reflector, plugs);
    }

    public String cipherCharacter(String character) {
        return mainController.cipherChar(character);
    }
}
