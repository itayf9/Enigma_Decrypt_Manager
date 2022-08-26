package body.screen2.encrypt;

import body.BodyController;
import dto.DTOciphertext;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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

public class EncryptDecryptController {

    BodyController parentController;

    @FXML
    private Label outputLabel;

    @FXML
    private TextField inputTextField;

    @FXML
    private Label cipherProblemLabel;

    @FXML
    private FlowPane lightbulbs;

    /**
     * Q5 -> going up the chain to parent controller
     *
     * @param event key press
     */
    @FXML
    void CipherCharacter(KeyEvent event) {
        DTOciphertext cipheredCharStatus = parentController.cipherCharacter(event.getCharacter().toUpperCase());
        if (!cipheredCharStatus.isSucceed()) {
            inputTextField.getStyleClass().add("invalid-input");
            cipherProblemLabel.getStyleClass().add("problem-details-label");
            cipherProblemLabel.setText(cipheredCharStatus.getDetails().name());
        } else {
            outputLabel.setText(outputLabel.getText() + cipheredCharStatus.getCipheredText());
            parentController.displayStatistics();
            cipherProblemLabel.setText("");
        }
    }

    /**
     * Q6 -> going up the chain to parent controller
     * @param event button event
     */
    @FXML
    void ResetConfiguration(MouseEvent event) {
        parentController.resetMachineConfiguration();
    }

    /**
     * clear the textBox
     * @param event button event
     */
    @FXML
    void clearCurrentCipher(MouseEvent event) {
        inputTextField.setText("");
        outputLabel.setText("");
    }

    public void setParentController(BodyController parentController) {
        this.parentController = parentController;
    }

    public void initAlphabetLightBulbs(String alphabet) {

        for (Character letter : alphabet.toCharArray()) {
            StackPane nextLightBulb = new StackPane();

            // adds the circle shape
            Circle lightBulbCircle = new Circle();
            lightBulbCircle.setFill(Paint.valueOf("DODGERBLUE"));
            lightBulbCircle.setRadius(23.0);
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

            lightbulbs.getChildren().add(nextLightBulb);
        }

    }
}
