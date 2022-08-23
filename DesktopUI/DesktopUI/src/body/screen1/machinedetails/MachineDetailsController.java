package body.screen1.machinedetails;

import body.BodyController;
import dto.DTOspecs;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


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


    public void displayMachineDetails(DTOspecs specsStatus) {
        usedRotors.setText("" + specsStatus.getInUseRotorsCount());
        availableRotors.setText("" + specsStatus.getAvailableRotorsCount());
        availableReflectors.setText("" + specsStatus.getAvailableReflectorsCount());
        cipheredTextCount.setText("" + specsStatus.getCipheredTextsCount());
    }

    public void setParentController(BodyController parentController) {
        this.parentController = parentController;
    }
}
