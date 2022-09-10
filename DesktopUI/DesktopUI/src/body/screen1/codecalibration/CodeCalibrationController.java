package body.screen1.codecalibration;

import app.statusbar.MessageTone;
import body.BodyController;
import dto.DTOsecretConfig;
import dto.DTOstatus;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import problem.Problem;

import java.util.List;

import static utill.Utility.romanToDecimal;

public class CodeCalibrationController {

    BodyController parentController;

    @FXML
    private Button randomCalibrationButton;

    @FXML
    private Button setCalibrationButton;

    @FXML
    private TextField rotorsInput;

    @FXML
    private TextField windowsCharsInput;

    @FXML
    private TextField plugsInput;

    @FXML
    private HBox reflectorBox;

    @FXML
    private ToggleGroup reflectorToggles;

    @FXML
    private Label problemLabelPlugs;

    @FXML
    private Label problemLabelReflector;

    @FXML
    private Label problemLabelWindows;

    @FXML
    private Label problemLabelRotors;


    @FXML
    void randomMachineConfig(MouseEvent event) {
        parentController.setRandomMachineConfig();
    }

    DTOstatus validateRotorsInput() {
        String input = rotorsInput.getText();
        DTOstatus rotorStatus = parentController.validateRotorsInput(input);
        if (!rotorStatus.isSucceed()) {
            // generate message
            problemLabelRotors.setText(rotorStatus.getDetails().name());
            if (!rotorsInput.getStyleClass().contains("invalid-input-text-field")) {
                rotorsInput.getStyleClass().add("invalid-input-text-field");
            }
            rotorsInput.requestFocus(); // focus for the user to fix the invalid area
        } else {
            rotorsInput.getStyleClass().remove("invalid-input-text-field");
            problemLabelRotors.setText("");
        }
        return rotorStatus;
    }

    DTOstatus validateWindowsInput() {
        String input = windowsCharsInput.getText().toUpperCase();
        DTOstatus windowsStatus = parentController.validateWindowsCharsInput(input);
        if (!windowsStatus.isSucceed()) {
            // generate message
            problemLabelWindows.setText(windowsStatus.getDetails().name());
            if (!windowsCharsInput.getStyleClass().contains("invalid-input-text-field")) {
                windowsCharsInput.getStyleClass().add("invalid-input-text-field");
            }
            windowsCharsInput.requestFocus(); // focus for the user to fix the invalid area
        } else {
            windowsCharsInput.getStyleClass().remove("invalid-input-text-field");
            problemLabelWindows.setText("");
        }
        return windowsStatus;
    }

    DTOstatus validateReflectorInput() {
        RadioButton currentReflector = (RadioButton) reflectorToggles.getSelectedToggle();
        if (currentReflector == null) {
            problemLabelReflector.setText(Problem.NO_REFLECTOR_BEEN_CHOSEN.name());
            return new DTOstatus(false, Problem.NO_REFLECTOR_BEEN_CHOSEN);
        }

        DTOstatus reflectorStatus = parentController.validateReflectorInput(romanToDecimal(currentReflector.getText()));
        if (!reflectorStatus.isSucceed()) {
            // generate message
            problemLabelReflector.setText(reflectorStatus.getDetails().name());
            if (!currentReflector.getStyleClass().contains("invalid-input-text-field")) {
                currentReflector.getStyleClass().add("invalid-input-text-field");
            }
            //reflector.requestFocus(); // focus for the user to fix the invalid area


        } else {
            currentReflector.getStyleClass().remove("invalid-input-text-field");
            problemLabelReflector.setText("");
        }
        return reflectorStatus;
    }

    DTOstatus validatePlugsInput() {
        String input = plugsInput.getText().toUpperCase();
        DTOstatus plugsStatus = parentController.validatePlugsInput(input);
        if (!plugsStatus.isSucceed()) {
            // generate message
            problemLabelPlugs.setText(plugsStatus.getDetails().name());
            if (!plugsInput.getStyleClass().contains("invalid-input-text-field")) {
                plugsInput.getStyleClass().add("invalid-input-text-field");
            }
            plugsInput.requestFocus(); // focus for the user to fix the invalid area
        } else {
            plugsInput.getStyleClass().remove("invalid-input-text-field");
            problemLabelPlugs.setText("");
        }
        return plugsStatus;
    }

    @FXML
    void setMachineConfig(MouseEvent event) {
        boolean isValid;

        DTOstatus rtrStatus = validateRotorsInput();
        DTOstatus wndStatus = validateWindowsInput();
        DTOstatus rflcStatus = validateReflectorInput();
        DTOstatus plgsStatus = validatePlugsInput();

        // assuming all is valid

        isValid = rtrStatus.isSucceed() &&
                wndStatus.isSucceed() &&
                plgsStatus.isSucceed() &&
                rflcStatus.isSucceed();

        if (isValid) {
            RadioButton currentReflector = (RadioButton) reflectorToggles.getSelectedToggle();
            parentController.setManualMachineConfig(rotorsInput.getText(), windowsCharsInput.getText().toUpperCase(), romanToDecimal(currentReflector.getText()), plugsInput.getText().toUpperCase());
        } else {
            parentController.setStatusMessage("Could not calibrate the machine", MessageTone.ERROR);
        }
    }

    public void setParentController(BodyController parentController) {
        this.parentController = parentController;
    }

    public void selAllowCodeCalibration(boolean isAllow) {

        rotorsInput.setDisable(!isAllow);
        windowsCharsInput.setDisable(!isAllow);
        reflectorToggles.getToggles().forEach((Toggle t) -> {
            RadioButton r = (RadioButton) t;
            r.setDisable(!isAllow);
        });
        plugsInput.setDisable(!isAllow);
        randomCalibrationButton.setDisable(!isAllow);
        setCalibrationButton.setDisable(!isAllow);

    }

    public void displayCurrentConfigInFields(DTOsecretConfig configStatus) {

        // set rotors label
        problemLabelRotors.setText("");

        StringBuilder rotorsStr = new StringBuilder();
        for (int i = configStatus.getRotors().size() - 1; i >= 0; i--) {
            rotorsStr.append(configStatus.getRotors().get(i).toString());

            if (i != 0) {
                rotorsStr.append(",");
            }
        }
        rotorsInput.getStyleClass().removeAll("invalid-input-text-field");
        rotorsInput.setText(rotorsStr.toString());


        // set windows label
        problemLabelWindows.setText("");

        windowsCharsInput.getStyleClass().removeAll("invalid-input-text-field");
        windowsCharsInput.setText(configStatus.getWindows());

        boolean isFoundSelectedRadioButton = false;
        // set Reflector
        problemLabelReflector.setText("");

        ObservableList<Toggle> reflectorButtons = reflectorToggles.getToggles();
        for (Toggle toggle : reflectorButtons) {
            RadioButton radioButton = (RadioButton) toggle;
            if (radioButton.getText().equals(configStatus.getReflectorSymbol())) {
                radioButton.setSelected(true);
                isFoundSelectedRadioButton = true;
            }
            if (!isFoundSelectedRadioButton) {
                radioButton.setSelected(false);
            }
            ((RadioButton) toggle).getStyleClass().removeAll("invalid-input-text-field");
        }

        // set plugs label
        problemLabelPlugs.setText("");
        plugsInput.getStyleClass().removeAll("invalid-input-text-field");
        plugsInput.setText(configStatus.getPlugs());
    }
}
