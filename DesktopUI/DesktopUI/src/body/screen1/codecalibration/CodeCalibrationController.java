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

import static utill.Utility.decimalToRoman;
import static utill.Utility.romanToDecimal;

public class CodeCalibrationController {

    BodyController parentController;

    @FXML
    private Button randomCalibrationButton;

    @FXML
    private Button setCalibrationButton;

    @FXML
    private HBox rotorsHbox;

    @FXML
    private HBox windowsCharHbox;

    @FXML
    private HBox plugsHBox;

    @FXML
    private Button removePlugButton;

    @FXML
    private Button addPlugButton;

    @FXML
    private TextField plugsInput;

    @FXML
    private HBox reflectorBox;

    private ToggleGroup reflectorToggles;
    private String alphabet;

    @FXML
    public void initialize() {
        reflectorToggles = new ToggleGroup();
    }

    @FXML
    void randomMachineConfig(MouseEvent ignored) {
        parentController.setRandomMachineConfig();
    }


  /*  DTOstatus validateRotorsInput() {
        String input = rotorsInput.getText();
        DTOstatus rotorStatus = parentController.validateRotorsInput(input);
        if (!rotorStatus.isSucceed()) {
            // generate message
            // problemLabelRotors.setText(rotorStatus.getDetails().name());
            if (!rotorsInput.getStyleClass().contains("invalid-input-text-field")) {
                rotorsInput.getStyleClass().add("invalid-input-text-field");
            }
            rotorsInput.requestFocus(); // focus for the user to fix the invalid area
        } else {
            rotorsInput.getStyleClass().remove("invalid-input-text-field");
            // problemLabelRotors.setText("");
        }
        return rotorStatus;
    }*/

    /*DTOstatus validateWindowsInput() {
        String input = windowsCharsInput.getText().toUpperCase();
        DTOstatus windowsStatus = parentController.validateWindowsCharsInput(input);
        if (!windowsStatus.isSucceed()) {
            // generate message
            // problemLabelWindows.setText(windowsStatus.getDetails().name());
            if (!windowsCharsInput.getStyleClass().contains("invalid-input-text-field")) {
                windowsCharsInput.getStyleClass().add("invalid-input-text-field");
            }
            windowsCharsInput.requestFocus(); // focus for the user to fix the invalid area
        } else {
            windowsCharsInput.getStyleClass().remove("invalid-input-text-field");
            // problemLabelWindows.setText("");
        }
        return windowsStatus;
    }*/

    DTOstatus validateReflectorInput() {
        RadioButton currentReflector = (RadioButton) reflectorToggles.getSelectedToggle();
        if (currentReflector == null) {
            // problemLabelReflector.setText(Problem.NO_REFLECTOR_BEEN_CHOSEN.name());
            return new DTOstatus(false, Problem.NO_REFLECTOR_BEEN_CHOSEN);
        }

        DTOstatus reflectorStatus = parentController.validateReflectorInput(romanToDecimal(currentReflector.getText()));
        if (!reflectorStatus.isSucceed()) {
            // generate message
            // problemLabelReflector.setText(reflectorStatus.getDetails().name());
            if (!currentReflector.getStyleClass().contains("invalid-input-text-field")) {
                currentReflector.getStyleClass().add("invalid-input-text-field");
            }
            //reflector.requestFocus(); // focus for the user to fix the invalid area


        } else {
            currentReflector.getStyleClass().remove("invalid-input-text-field");
            // problemLabelReflector.setText("");
        }
        return reflectorStatus;
    }

    DTOstatus validatePlugsInput() {
        String input = plugsInput.getText().toUpperCase();
        DTOstatus plugsStatus = parentController.validatePlugsInput(input);
        if (!plugsStatus.isSucceed()) {
            // generate message
            // problemLabelPlugs.setText(plugsStatus.getDetails().name());
            if (!plugsInput.getStyleClass().contains("invalid-input-text-field")) {
                plugsInput.getStyleClass().add("invalid-input-text-field");
            }
            plugsInput.requestFocus(); // focus for the user to fix the invalid area
        } else {
            plugsInput.getStyleClass().remove("invalid-input-text-field");
            // problemLabelPlugs.setText("");
        }
        return plugsStatus;
    }

    @FXML
    void setMachineConfig(MouseEvent ignored) {
        boolean isValid;

        /*
        DTOstatus rtrStatus = validateRotorsInput();
        DTOstatus wndStatus = validateWindowsInput();

         */
        DTOstatus rflcStatus = validateReflectorInput();
        DTOstatus plgsStatus = validatePlugsInput();

        // assuming all is valid

        /*isValid = rtrStatus.isSucceed() &&
                wndStatus.isSucceed() &&
                plgsStatus.isSucceed() &&
                rflcStatus.isSucceed();*/

       /* if (isValid) {
            RadioButton currentReflector = (RadioButton) reflectorToggles.getSelectedToggle();
            parentController.setManualMachineConfig(rotorsInput.getText(), windowsCharsInput.getText().toUpperCase(), romanToDecimal(currentReflector.getText()), plugsInput.getText().toUpperCase());
        } else {
            parentController.setStatusMessage("Could not calibrate the machine", MessageTone.ERROR);
        }*/
    }

    public void setParentController(BodyController parentController) {
        this.parentController = parentController;
    }

    public void setCodeCalibration(int inUseRotorsCount, int availableRotorsCount, String machineAlphabet, int availableReflectorsCount) {

        this.alphabet = machineAlphabet;
        // remove old machine stuff
        rotorsHbox.getChildren().clear();
        windowsCharHbox.getChildren().clear();
        reflectorBox.getChildren().clear();
        plugsHBox.getChildren().clear();

        // create new components

//         <ComboBox minWidth="53.0" prefHeight="25.0" prefWidth="53.0" promptText="1" />

        for (int i = 0; i < inUseRotorsCount; i++) {
            ComboBox<Integer> nextRotorComboBox = new ComboBox<>();
            ComboBox<Character> nextWindowComboBox = new ComboBox<>();

            nextRotorComboBox.getStyleClass().add("code-calibration-combo-box");
            nextWindowComboBox.getStyleClass().add("code-calibration-combo-box");

            nextRotorComboBox.setMinWidth(53);
            nextWindowComboBox.setMinWidth(53);

            nextRotorComboBox.setPrefWidth(53);
            nextWindowComboBox.setPrefWidth(53);

            nextRotorComboBox.setMinHeight(25);
            nextWindowComboBox.setMinHeight(25);

            nextRotorComboBox.setPrefHeight(25);
            nextWindowComboBox.setPrefHeight(25);


            for (int j = 1; j <= availableRotorsCount; j++) {
                nextRotorComboBox.setPromptText("-");
                nextRotorComboBox.getItems().add(j);
            }
            rotorsHbox.getChildren().add(nextRotorComboBox);

            for (Character window : machineAlphabet.toCharArray()) {
                nextWindowComboBox.setPromptText("-");
                nextWindowComboBox.getItems().add(window);
            }

            windowsCharHbox.getChildren().add(nextWindowComboBox);
        }

        for (int i = 1; i <= availableReflectorsCount; i++) {
            RadioButton nextReflectorRadioButton = new RadioButton();
            nextReflectorRadioButton.setText(decimalToRoman(i));
            nextReflectorRadioButton.setToggleGroup(reflectorToggles);

            reflectorBox.getChildren().add(nextReflectorRadioButton);
        }
    }

    /*public void displayCurrentConfigInFields(DTOsecretConfig configStatus) {

        // set rotors label
        // problemLabelRotors.setText("");

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
        // problemLabelWindows.setText("");

        windowsCharsInput.getStyleClass().removeAll("invalid-input-text-field");
        windowsCharsInput.setText(configStatus.getWindows());

        boolean isFoundSelectedRadioButton = false;
        // set Reflector
        // problemLabelReflector.setText("");

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
        // problemLabelPlugs.setText("");
        plugsInput.getStyleClass().removeAll("invalid-input-text-field");
        plugsInput.setText(configStatus.getPlugs());
    }*/

    @FXML
    void addPlugAction(MouseEvent event) {
        HBox plugPair = new HBox();
        ComboBox<Character> firstInPair = new ComboBox<>();
        ComboBox<Character> secondInPair = new ComboBox<>();

        firstInPair.getStyleClass().add("code-calibration-combo-box");
        secondInPair.getStyleClass().add("code-calibration-combo-box");

        firstInPair.setPromptText("-");
        secondInPair.setPromptText("-");

        firstInPair.setMinWidth(53);
        secondInPair.setMinWidth(53);

        firstInPair.setPrefWidth(53);
        secondInPair.setPrefWidth(53);

        firstInPair.setMinHeight(25);
        secondInPair.setMinHeight(25);

        firstInPair.setPrefHeight(25);
        secondInPair.setPrefHeight(25);

        for (Character letter : alphabet.toCharArray()) {

            firstInPair.getItems().add(letter);
            secondInPair.getItems().add(letter);
        }

        plugPair.getChildren().addAll(firstInPair, secondInPair);
        plugsHBox.getChildren().add(plugPair);
    }

    @FXML
    void removePlugAction(MouseEvent event) {
        if (plugsHBox.getChildren().size() != 0) {
            plugsHBox.getChildren().remove(plugsHBox.getChildren().size() - 1);
        }
    }

}
