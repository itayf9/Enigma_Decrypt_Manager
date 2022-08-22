package header;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class HeaderController {

    FileChooser fileChooser = new FileChooser();

    @FXML
    private Button buttonLoadMachine;

    @FXML
    private TextField textFieldLoadMachine;

    @FXML
    void loadMachineFile(MouseEvent event) {
        fileChooser.setTitle("Load Machine");
        File selectedMachineFile = fileChooser.showOpenDialog(new Stage());
        
        textFieldLoadMachine.setText(selectedMachineFile.getPath());
    }

}
