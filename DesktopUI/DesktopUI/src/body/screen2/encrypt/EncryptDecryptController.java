package body.screen2.encrypt;

import body.BodyController;
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
    void CipherCharacter(KeyEvent event) {
        String cipheredChar = parentController.cipherCharacter(event.getCharacter());
        outputLabel.setText(cipheredChar);
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
