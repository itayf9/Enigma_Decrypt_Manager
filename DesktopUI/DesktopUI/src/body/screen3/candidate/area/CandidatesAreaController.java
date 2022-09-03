package body.screen3.candidate.area;

import body.BodyController;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;

public class CandidatesAreaController {

    private BodyController parentController;

    @FXML
    private FlowPane candidatesFlowPane;

    public void setParentController(BodyController parentController) {
        this.parentController = parentController;
    }

}
