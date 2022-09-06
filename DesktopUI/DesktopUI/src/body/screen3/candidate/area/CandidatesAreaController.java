package body.screen3.candidate.area;

import body.BodyController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;

public class CandidatesAreaController {

    private BodyController parentController;

    @FXML
    private FlowPane candidatesFlowPane;

    @FXML
    private ProgressBar BFprogressBar;

    @FXML
    private Label progressPercentageLabel;

    @FXML
    private Label numberOfScannedConfigurationsLabel;

    @FXML
    private Label numberOfTotalPossibleConfigurationsLabel;

    @FXML
    private Label numberOfDistinctCandidatesLabel;

    @FXML
    private Label BFstatusLabel;

    public void setParentController(BodyController parentController) {
        this.parentController = parentController;
    }

}
