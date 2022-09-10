package body.screen3.candidate.area;

import body.BodyController;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

    @FXML
    private Label averageProcessTimeLabel;

    public void setParentController(BodyController parentController) {
        this.parentController = parentController;
    }

    /*private void onTaskFinished(Optional<Runnable> onFinish) {
        this.BFstatusLabel.textProperty().unbind();
        this.progressPercentageLabel.textProperty().unbind();
        this.BFprogressBar.progressProperty().unbind();
        onFinish.ifPresent(Runnable::run);
    }*/

    public void bindInitPropertiesToLabels(IntegerProperty totalDistinctCandidates, IntegerProperty totalProcessedConfigurations,
                                           LongProperty totalAmountConfigurations, DoubleProperty bruteForceProgress,
                                           StringProperty bruteForceProgressBarPercentageLabel, StringProperty bruteForceStatus,
                                           DoubleProperty averageTasksProcessTimeProperty) {

        numberOfTotalPossibleConfigurationsLabel.textProperty().bind(Bindings.concat("Total Configurations: ", totalAmountConfigurations.asString()));
        numberOfDistinctCandidatesLabel.textProperty().bind(Bindings.concat("Candidates found: ", totalDistinctCandidates.asString()));
        numberOfScannedConfigurationsLabel.textProperty().bind(Bindings.concat("Scanned: ", totalProcessedConfigurations.asString()));
        progressPercentageLabel.textProperty().bind(bruteForceProgressBarPercentageLabel);
        BFprogressBar.progressProperty().bind(bruteForceProgress);
        BFstatusLabel.textProperty().bind(Bindings.concat("Status: ", bruteForceStatus));
        averageProcessTimeLabel.textProperty().bind(Bindings.concat("averageProcessTime: ", Bindings.format("%.3f", averageTasksProcessTimeProperty), " ns"));
    }

    public void insertCandidateToFlowPane(Node singleCandidateTile) {
        candidatesFlowPane.getChildren().add(singleCandidateTile);
    }

    public void clearOldResultsOfBruteForce() {
        candidatesFlowPane.getChildren().clear();
    }
}
