package body.screen2.encrypt;

import app.statusbar.MessageTone;
import body.BodyController;
import dto.DTOciphertext;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
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

    private StringProperty dictionaryExcludeCharactersProperty;


    @FXML
    public void initialize() {
        processButton.setText("Process");

        cipheredOutputHeadline.visibleProperty().bind(Bindings.when(outputLabel.textProperty().isEqualTo("")).then(true).otherwise(false));
        lightBulbs.disableProperty().bind(cipherModeTS.selectedProperty().not());
    }

    /**
     * Q5 -> going up the chain to parent controller
     *
     * @param event key press
     */
    @FXML
    void cipherCharacter(KeyEvent event) {
        cipherOneCharacter(event.getCode().getName().toUpperCase());

    }

    private void cipherOneCharacter(String character) {
        inputTextField.getStyleClass().removeAll("invalid-input-text-field");

        if (cipherModeTS.isSelected()) {
            DTOciphertext cipheredCharStatus = parentController.cipher(character);
            if (!cipheredCharStatus.isSucceed()) {
                cipheredLetter = "";
                inputTextField.getStyleClass().add("invalid-input-text-field");
                parentController.setStatusMessage("Could not cipher that character", MessageTone.ERROR);
            } else {
                outputLabel.setText(outputLabel.getText() + cipheredCharStatus.getCipheredText());
                this.cipheredLetter = cipheredCharStatus.getCipheredText();
                activateLightBulb(cipheredCharStatus.getCipheredText());
            }
        }
    }

    /**
     * handles the Process btn action
     *
     * @param ignored mouseEvent ignored
     */
    @FXML
    void processHandler(MouseEvent ignored) {
        if (cipherModeTS.isSelected()) { // done btn
            clearTextFields();
            parentController.doneCurrentCipherProcess();
            parentController.displayStatistics();
        } else {
            DTOciphertext cipheredLineStatus = parentController.cipher(inputTextField.getText().toUpperCase());
            if (!cipheredLineStatus.isSucceed()) {
                inputTextField.getStyleClass().add("invalid-input-text-field");
                parentController.setStatusMessage("Could not cipher that text. " + cipheredLineStatus.getDetails(), MessageTone.ERROR);
            } else {
                outputLabel.setText(cipheredLineStatus.getCipheredText());
                parentController.displayStatistics();
            }
        }
    }

    void processHandlerBruteForce(MouseEvent ignored) {

        StringBuilder textBuilder = new StringBuilder();
        String textToCipher;

        // excludes all the excludedChars
        for (Character regularCharacter : inputTextField.getText().toUpperCase().toCharArray()) {
            if (!dictionaryExcludeCharactersProperty.getValue().contains(regularCharacter.toString())) {
                textBuilder.append(regularCharacter);
            }
        }
        textToCipher = textBuilder.toString();

        // checks if all words are in the dictionary
        if (!parentController.isAllWordsInDictionary(textToCipher)) {
            inputTextField.getStyleClass().add("invalid-input-text-field");

            parentController.setStatusMessage("All words should be from the dictionary", MessageTone.ERROR);
            return;
        }

        // ciphers the text
        DTOciphertext cipheredLineStatus = parentController.cipher(textToCipher);

        if (!cipheredLineStatus.isSucceed()) {
            inputTextField.getStyleClass().add("invalid-input-text-field");
            parentController.setStatusMessage("Could not cipher that text", MessageTone.ERROR);
        } else {
            outputLabel.setText(cipheredLineStatus.getCipheredText());
            parentController.displayStatistics();
        }
    }

    /**
     * @param ignored a mouse click on one of the 2 modes presented
     */
    @FXML
    void setCipherMode(MouseEvent ignored) {
        clearTextFields();
        parentController.setCharByCharCipherMode(cipherModeTS.isSelected());
    }

    /**
     * Q6 -> going up the chain to parent controller
     *
     * @param ignored mouse event ignored
     */
    @FXML
    void ResetConfiguration(MouseEvent ignored) {
        parentController.resetMachineConfiguration();
    }

    /**
     * clear the textBox
     *
     * @param ignored mouse event ignored
     */
    @FXML
    void clearCurrentCipher(MouseEvent ignored) {
        clearTextFields();
    }

    private void clearTextFields() {
        inputTextField.setText("");
        outputLabel.setText("");
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
            lightBulbCircle.setRadius(15);
            lightBulbCircle.getStyleClass().add("lightbulb-circle");

            // adds the letter
            Label lightBulbLetter = new Label("" + letter);
            lightBulbLetter.getStyleClass().add("lightbulb-letter");

            // add circle and letter to stack pane
            nextLightBulb.getChildren().add(lightBulbCircle);
            nextLightBulb.getChildren().add(lightBulbLetter);

            nextLightBulb.setOnMousePressed(event -> {
                StackPane srcLightBulb = (StackPane) event.getSource();
                Label srcLightBulbLetterLabel = (Label) srcLightBulb.getChildren().get(1);
                inputTextField.setText(inputTextField.getText() + srcLightBulbLetterLabel.getText());
                cipherOneCharacter(srcLightBulbLetterLabel.getText());
            });

            nextLightBulb.setOnMouseReleased(event -> {
                deactivateLight();
            });

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
        currentPane.getChildren().get(0).getStyleClass().add("light-on");
        currentPane.getChildren().get(1).getStyleClass().add("light-on");
        //Circle circle = (Circle) currentPane.getChildren().get(0);
        //circle.setFill(Paint.valueOf("YELLOW"));
    }

    /**
     * deactivate the matching bulb
     *
     * @param ignored when user release the key presses
     */
    @FXML
    public void deactivateLightBulb(KeyEvent ignored) {

        if (cipheredLetter.equals("")) {
            return;
        }
        ObservableList<?> elements = lightBulbs.getChildren();
        Button currentPane = (Button) elements.get(alphabet.indexOf(cipheredLetter.charAt(0)));

        currentPane.getStyleClass().removeAll("light-on");
    }

    private void deactivateLight() {
        ObservableList<?> elements = lightBulbs.getChildren();
        Button currentPane = (Button) elements.get(alphabet.indexOf(cipheredLetter.charAt(0)));

        currentPane.getStyleClass().removeAll("light-on");
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

    public void setOnActionProcessToBruteForceMode() {
        processButton.setOnMouseClicked(this::processHandlerBruteForce);
    }

    public void setEncryptExcludeCharsValue(StringProperty dictionaryExcludeCharsProperty) {
        this.dictionaryExcludeCharactersProperty = dictionaryExcludeCharsProperty;
    }
}
