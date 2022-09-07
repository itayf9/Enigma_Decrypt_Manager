package body.screen3.candidate.area;

import body.BodyController;
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

    public void setParentController(BodyController parentController) {
        this.parentController = parentController;
    }

    /* public void bindTaskComponentsToUIComponents(CandidatesCollectorTask bruteForceTask, Runnable onFinish) {

        // task message
        BFstatusLabel.textProperty().bind(Bindings.concat("Status: ", bruteForceTask.messageProperty()));

        // task progress bar
        BFprogressBar.progressProperty().bind(bruteForceTask.progressProperty());

        // task percent label
        progressPercentageLabel.textProperty().bind(
                Bindings.concat(Bindings.format("%.0f", Bindings.multiply(bruteForceTask.progressProperty(), 100)), " %"));

        // task cleanup upon finish
        bruteForceTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            onTaskFinished(Optional.ofNullable(onFinish));
        });
    }*/

    /*private void onTaskFinished(Optional<Runnable> onFinish) {
        this.BFstatusLabel.textProperty().unbind();
        this.progressPercentageLabel.textProperty().unbind();
        this.BFprogressBar.progressProperty().unbind();
        onFinish.ifPresent(Runnable::run);
    }*/

    public void bindInitPropertiesToLabels(IntegerProperty totalDistinctCandidates, IntegerProperty totalProcessedConfigurations,
                                           LongProperty totalAmountConfigurations, DoubleProperty bruteForceProgress,
                                           StringProperty bruteForceProgressBarPercentageLabel, StringProperty bruteForceStatus) {

        numberOfTotalPossibleConfigurationsLabel.textProperty().bind(Bindings.concat("Total Configurations: ", totalAmountConfigurations.asString()));
        numberOfDistinctCandidatesLabel.textProperty().bind(Bindings.concat("Candidates found: ", totalDistinctCandidates.asString()));
        numberOfScannedConfigurationsLabel.textProperty().bind(Bindings.concat("Scanned: ", totalProcessedConfigurations.asString()));
        progressPercentageLabel.textProperty().bind(bruteForceProgressBarPercentageLabel);
        BFprogressBar.progressProperty().bind(bruteForceProgress);
        BFstatusLabel.textProperty().bind(Bindings.concat("Status: ", bruteForceStatus));
    }

    public void insertCandidateToFlowPane(Node singleCandidateTile) {
        candidatesFlowPane.getChildren().add(singleCandidateTile);
    }
}
