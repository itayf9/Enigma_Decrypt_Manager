package body.screen1.machinedetails;

import body.BodyController;
import body.currentconfig.CurrentConfigController;
import dto.DTOspecs;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import problem.Problem;

import static utill.Utillity.getOriginalConfigFromSpecs;


public class MachineDetailsController {

    BodyController parentController;

    @FXML
    private Label usedRotors;

    @FXML
    private Label availableRotors;

    @FXML
    private Label availableReflectors;

    @FXML
    private Label cipheredTextCount;

    @FXML
    private GridPane originalConfig;

    @FXML
    private CurrentConfigController originalConfigController;

    @FXML
    public void initialize() {
        originalConfigController.setTitle("Original Machine Configuration:");
    }


    public void displayMachineDetails(DTOspecs specsStatus) {
        if (specsStatus.getDetails().equals(Problem.NO_LOADED_MACHINE)) {
            usedRotors.setText("-");
            availableRotors.setText("-");
            availableReflectors.setText("-");
            cipheredTextCount.setText("-");
        } else {
            usedRotors.setText("" + specsStatus.getInUseRotorsCount());
            availableRotors.setText("" + specsStatus.getAvailableRotorsCount());
            availableReflectors.setText("" + specsStatus.getAvailableReflectorsCount());
            cipheredTextCount.setText("" + specsStatus.getCipheredTextsCount());
        }

        originalConfigController.displayCurrentConfig(getOriginalConfigFromSpecs(specsStatus));
    }

    public void setParentController(BodyController parentController) {
        this.parentController = parentController;
    }
}
