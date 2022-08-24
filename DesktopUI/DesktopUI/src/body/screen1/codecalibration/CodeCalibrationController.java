package body.screen1.codecalibration;

import body.BodyController;
import dto.DTOstatus;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import problem.Problem;

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
    private ToggleGroup reflector;

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
            if (!rotorsInput.getStyleClass().contains("invalid-input")) {
                rotorsInput.getStyleClass().add("invalid-input");
            }
            rotorsInput.requestFocus(); // focus for the user to fix the invalid area
        } else {
            rotorsInput.getStyleClass().remove("invalid-input");
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
            if (!windowsCharsInput.getStyleClass().contains("invalid-input")) {
                windowsCharsInput.getStyleClass().add("invalid-input");
            }
            windowsCharsInput.requestFocus(); // focus for the user to fix the invalid area
        } else {
            windowsCharsInput.getStyleClass().remove("invalid-input");
            problemLabelWindows.setText("");
        }
        return windowsStatus;
    }

    DTOstatus validateReflectorInput() {
        RadioButton currentReflector = (RadioButton) reflector.getSelectedToggle();
        if (currentReflector == null) {
            problemLabelReflector.setText(Problem.NO_REFLECTOR_BEEN_CHOSEN.name());
            return new DTOstatus(false, Problem.NO_REFLECTOR_BEEN_CHOSEN);
        }

        DTOstatus reflectorStatus = parentController.validateReflectorInput(romanToDecimal(currentReflector.getText()));
        if (!reflectorStatus.isSucceed()) {
            // generate message
            problemLabelReflector.setText(reflectorStatus.getDetails().name());
            if (!currentReflector.getStyleClass().contains("invalid-input")) {
                currentReflector.getStyleClass().add("invalid-input");
            }
            //reflector.requestFocus(); // focus for the user to fix the invalid area


        } else {
            currentReflector.getStyleClass().remove("invalid-input");
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
            if (!plugsInput.getStyleClass().contains("invalid-input")) {
                plugsInput.getStyleClass().add("invalid-input");
            }
            plugsInput.requestFocus(); // focus for the user to fix the invalid area
        } else {
            plugsInput.getStyleClass().remove("invalid-input");
            problemLabelPlugs.setText("");
        }
        return plugsStatus;
    }

    @FXML
    void setMachineConfig(MouseEvent event) {
        boolean isValid = true;

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
            RadioButton currentReflector = (RadioButton) reflector.getSelectedToggle();
            parentController.setManualMachineConfig(rotorsInput.getText(), windowsCharsInput.getText().toUpperCase(), romanToDecimal(currentReflector.getText()), plugsInput.getText().toUpperCase());
        }
    }

    public void setParentController(BodyController parentController) {
        this.parentController = parentController;
    }

    public void selAllowCodeCalibration(boolean isAllow) {

        rotorsInput.setDisable(!isAllow);
        windowsCharsInput.setDisable(!isAllow);
        reflector.getToggles().forEach((Toggle t) -> {
            RadioButton r = (RadioButton) t;
            r.setDisable(!isAllow);
        });
        plugsInput.setDisable(!isAllow);
        randomCalibrationButton.setDisable(!isAllow);
        setCalibrationButton.setDisable(!isAllow);

    }
}
