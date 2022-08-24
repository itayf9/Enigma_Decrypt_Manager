package body.screen2.encrypt;

import body.BodyController;
import dto.DTOciphertext;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class EncryptDecryptController {

    BodyController parentController;

    @FXML
    private Label outputLabel;

    @FXML
    private TextField inputTextField;

    @FXML
    private Label cipherProblemLabel;

    @FXML
    void CipherCharacter(KeyEvent event) {
        DTOciphertext cipheredCharStatus = parentController.cipherCharacter(event.getCharacter().toUpperCase());
        if (!cipheredCharStatus.isSucceed()) {
            inputTextField.getStyleClass().add("invalid-input");
            cipherProblemLabel.getStyleClass().add("problem-details-label");
            cipherProblemLabel.setText(cipheredCharStatus.getDetails().name());
        } else {
            outputLabel.setText(outputLabel.getText() + cipheredCharStatus.getCipheredText());
            cipherProblemLabel.setText("");
        }

    }

    @FXML
    void ResetConfiguration(MouseEvent event) {

    }

    @FXML
    void clearCurrentCipher(MouseEvent event) {
        inputTextField.setText("");
        outputLabel.setText("");
    }

    public void setParentController(BodyController parentController) {
        this.parentController = parentController;
    }


}
