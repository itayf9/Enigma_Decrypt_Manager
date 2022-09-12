package body.screen2.encrypt;

import app.statusbar.MessageTone;
import body.BodyController;
import dto.DTOciphertext;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.controlsfx.control.ToggleSwitch;

public class EncryptDecryptController {

    BodyController parentController;

    String cipheredLetter = "";

    String alphabet;

    @FXML
    private ScrollPane lightBulbsScrollPane;

    @FXML
    private Label outputLabel;

    @FXML
    private TextField inputTextField;

    @FXML
    private Label cipherProblemLabel;

    @FXML
    private FlowPane lightBulbs;

    @FXML
    private ToggleSwitch cipherModeTS;

    @FXML
    private Label cipheredOutputHeadline;

    @FXML
    private Button processButton;

    @FXML
    private Button resetButton;

    @FXML
    private Button clearButton;

    @FXML
    public void initialize() {
        processButton.setText("Process");
        cipherProblemLabel.setText("");

        cipheredOutputHeadline.visibleProperty().bind(Bindings.when(outputLabel.textProperty().isEqualTo("")).then(true).otherwise(false));
    }

    /**
     * Q5 -> going up the chain to parent controller
     *
     * @param event key press
     */
    @FXML
    void cipherCharacter(KeyEvent event) {

        if (cipherModeTS.isSelected()) {
            DTOciphertext cipheredCharStatus = parentController.cipher(event.getCharacter().toUpperCase());
            if (!cipheredCharStatus.isSucceed()) {
                cipheredLetter = "";
                inputTextField.getStyleClass().add("invalid-input");
                cipherProblemLabel.getStyleClass().add("problem-details-label");
                cipherProblemLabel.setText(cipheredCharStatus.getDetails().name());
                parentController.setStatusMessage("Could not cipher that character", MessageTone.ERROR);
            } else {
                outputLabel.setText(outputLabel.getText() + cipheredCharStatus.getCipheredText());
                this.cipheredLetter = cipheredCharStatus.getCipheredText();
                activateLightBulb(cipheredCharStatus.getCipheredText());
                cipherProblemLabel.setText("");
            }
        }
    }

    @FXML
    void processHandler(MouseEvent event) {
        if (cipherModeTS.isSelected()) { // done btn
            clearTextFields();
            parentController.doneCurrentCipherProcess();
            parentController.displayStatistics();
        } else {
            DTOciphertext cipheredLineStatus = parentController.cipher(inputTextField.getText().toUpperCase());
            if (!cipheredLineStatus.isSucceed()) {
                inputTextField.getStyleClass().add("invalid-input");
                cipherProblemLabel.getStyleClass().add("problem-details-label");
                cipherProblemLabel.setText(cipheredLineStatus.getDetails().name());
                parentController.setStatusMessage("Could not cipher that text", MessageTone.ERROR);
            } else {
                outputLabel.setText(cipheredLineStatus.getCipheredText());
                parentController.displayStatistics();
                cipherProblemLabel.setText("");
            }
        }
    }

    /**
     * @param event a mouse click on one of the 2 modes presented
     */
    @FXML
    void setCipherMode(MouseEvent event) {
        clearTextFields();
        if (cipherModeTS.isSelected()) {
            parentController.setCharByCharCipherMode(true);
        } else {
            parentController.setCharByCharCipherMode(false);
        }
    }

    /**
     * Q6 -> going up the chain to parent controller
     *
     * @param event button event
     */
    @FXML
    void ResetConfiguration(MouseEvent event) {
        parentController.resetMachineConfiguration();
    }

    /**
     * clear the textBox
     *
     * @param event button event
     */
    @FXML
    void clearCurrentCipher(MouseEvent event) {
        clearTextFields();
    }

    private void clearTextFields() {
        inputTextField.setText("");
        outputLabel.setText("");
        cipherProblemLabel.setText("");
    }

    public void setParentController(BodyController parentController) {
        this.parentController = parentController;
    }

    /**
     * setting up the lightBulbs according to the Alphabet string
     *
     * @param alphabet string of the alphabet
     */
    public void initAlphabetLightBulbs(String alphabet) {
        this.alphabet = alphabet;

        for (Character letter : alphabet.toCharArray()) {
            StackPane nextLightBulb = new StackPane();

            // adds the circle shape
            Circle lightBulbCircle = new Circle();
            lightBulbCircle.setFill(Paint.valueOf("#cc5454"));
            lightBulbCircle.setRadius(15);
            lightBulbCircle.setStroke(Paint.valueOf("BLACK"));
            lightBulbCircle.setStrokeType(StrokeType.valueOf("INSIDE"));

            // adds the letter
            Label lightBulbLetter = new Label("" + letter);
            lightBulbLetter.setTextAlignment(TextAlignment.valueOf("CENTER"));
            Font font = new Font(26.0);
            lightBulbLetter.setFont(font);

            // add circle and letter to stack pane
            nextLightBulb.getChildren().add(lightBulbCircle);
            nextLightBulb.getChildren().add(lightBulbLetter);

            lightBulbs.getChildren().add(nextLightBulb);
        }
    }

    public void setAllowEncryptDecrypt(boolean isAllow) {
        inputTextField.setDisable(!isAllow);
        processButton.setDisable(!isAllow);
        resetButton.setDisable(!isAllow);
        clearButton.setDisable(!isAllow);
        inputTextField.setText("");
        outputLabel.setText("");
    }

    /**
     * activate animation on the matching bulb
     *
     * @param letter the letter to find the bulb to activate
     */
    public void activateLightBulb(String letter) {

        ObservableList<?> elements = lightBulbs.getChildren();
        StackPane currentPane = (StackPane) elements.get(alphabet.indexOf(letter.charAt(0)));
        currentPane.getStyleClass().add("light-on");
        Circle circle = (Circle) currentPane.getChildren().get(0);
        circle.setFill(Paint.valueOf("YELLOW"));
    }

    /**
     * deactivate the matching bulb
     *
     * @param event when user release the key presses
     */
    @FXML
    public void deactivateLightBulb(KeyEvent event) {

        if (cipheredLetter.equals("")) {
            return;
        }
        ObservableList<?> elements = lightBulbs.getChildren();
        StackPane currentPane = (StackPane) elements.get(alphabet.indexOf(cipheredLetter.charAt(0)));
        Circle circle = (Circle) currentPane.getChildren().get(0);
        circle.setFill(Paint.valueOf("#cc5454"));

        currentPane.getStyleClass().remove("light-on");
    }

    public StringProperty getOutputLabelProperty() {
        return outputLabel.textProperty();
    }

    public void setAvailabilityOfCharByCharMode(boolean availabilityOfCharByCharMode) {
        if (!availabilityOfCharByCharMode) {
            cipherModeTS.setSelected(availabilityOfCharByCharMode);
        }

        cipherModeTS.setVisible(availabilityOfCharByCharMode);
    }

    public void showLightBulbs(boolean needToShow) {
        lightBulbsScrollPane.setVisible(needToShow);
        lightBulbsScrollPane.setHmax(0);
    }

    public void bindCipherMode(BooleanProperty isCharByCharModeProperty) {
        processButton.textProperty().bind(Bindings.when(isCharByCharModeProperty.not()).then("Process").otherwise("Done"));
    }

    public void appendNewWordToInputCipherText(String newWord) {
        if (inputTextField.getText().equals("")) {
            inputTextField.setText(newWord);
        } else {
            inputTextField.setText(inputTextField.getText() + " " + newWord);
        }
    }
}
