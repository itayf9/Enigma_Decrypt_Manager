package app;

import body.BodyController;
import dto.DTOciphertext;
import dto.DTOsecretConfig;
import dto.DTOspecs;
import dto.DTOstatus;
import engine.Engine;
import engine.EnigmaEngine;
import header.HeaderController;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;

import static utill.Utility.decimalToRoman;

public class MainController {

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
    public void initialize() {
        if (headerController != null && bodyController != null) {
            headerController.setMainController(this);
            bodyController.setMainController(this);
        }
    }

    /**
     * Q1 Load the machine
     *
     * @param selectedMachineFile fileName
     */
    public void loadMachineFromFile(String selectedMachineFile) {
        String fileName = "C:/Users/itayf/Downloads/resource/ex1-sanity-small.xml";
        DTOstatus loadStatus = engine.buildMachineFromXmlFile(fileName);
        if (!loadStatus.isSucceed()) {
            // create error msg
        } else {
            // create success msg
            DTOspecs specsStatus = engine.displayMachineSpecifications();
            bodyController.setAllowCodeCalibration(true);
            if (!specsStatus.isSucceed()) {
                // mashu
            } else {
                bodyController.displayMachineSpecs(specsStatus);
            }
        }
    }

    /**
     * Q4 set configuration auto
     */
    public void setRandomMachineConfig() {
        DTOsecretConfig configStatus = engine.selectConfigurationAuto();

        if (!configStatus.isSucceed()) {
            // err msg
        } else {
            bodyController.displayCurrentConfig(configStatus);
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
     * Q3 set manual config
     *
     * @param rotors    rotors ids
     * @param windows   window characters
     * @param reflector reflector number
     * @param plugs     plugs
     */
    public void setManualMachineConfig(String rotors, String windows, int reflector, String plugs) {
        DTOsecretConfig configStatus = engine.selectConfigurationManual(rotors, windows, reflector, plugs);
        if (!configStatus.isSucceed()) {
            // err msg
        } else {
            bodyController.displayCurrentConfig(configStatus);
        }
    }

    public String cipherChar(String character) {
        DTOciphertext cipheredTextStatus = engine.cipherInputText(character);
        return cipheredTextStatus.getCipheredText();
    }
}
