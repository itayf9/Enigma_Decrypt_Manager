package ui.adapter;

import candidate.Candidate;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.application.Platform;

import java.util.function.Consumer;

public class UIAdapter {

    private Consumer<Candidate> introduceNewCandidate; // need this when finding a candidate
    private Runnable updateDistinct; // need this to update candidates found counter
    private Consumer<Integer> updateTotalProcessedConfigurations; // need this to update scanned configs counter
    private Consumer<String> updateTaskStatus; // need this to update task status
    private Consumer<Double> updateProgressBar; // need this to update progressbar
    private Consumer<Double> updateProgressBarPercentage; // need this to update progressbar percentage label
    private Consumer<Long> updateTotalConfigs;
    private Consumer<Boolean> updateTaskActiveStatus;
    private Consumer<Double> updateAverageTasksProcessTime;


    public UIAdapter(Consumer<Candidate> introduceNewCandidate, Runnable updateDistinct,
                     Consumer<Integer> updateTotalProcessedConfigurations, Consumer<String> updateTaskStatus,
                     Consumer<Double> updateProgressBar, Consumer<Double> updateProgressBarPercentage,
                     Consumer<Long> updateTotalConfigs, Consumer<Boolean> updateTaskOverStatus, Consumer<Double> updateAverageTasksProcessTime) {
        this.introduceNewCandidate = introduceNewCandidate;
        this.updateDistinct = updateDistinct;
        this.updateTotalProcessedConfigurations = updateTotalProcessedConfigurations;
        this.updateTaskStatus = updateTaskStatus;
        this.updateProgressBar = updateProgressBar;
        this.updateProgressBarPercentage = updateProgressBarPercentage;
        this.updateTotalConfigs = updateTotalConfigs;
        this.updateTaskActiveStatus = updateTaskOverStatus;
        this.updateAverageTasksProcessTime = updateAverageTasksProcessTime;
    }

    public void addNewCandidate(Candidate candidate) {
        Platform.runLater(
                () -> {
                    introduceNewCandidate.accept(candidate);
                    updateDistinct.run();
                }
        );
    }

    public void updateTotalProcessedConfigurations(int delta) {
        Platform.runLater(
                () -> updateTotalProcessedConfigurations.accept(delta)
        );
    }

    public void updateTaskStatus(String status) {
        Platform.runLater(
                () -> updateTaskStatus.accept(status)
        );
    }

    public void updateProgressBar(double percentage) {
        Platform.runLater(
                () -> {
                    updateProgressBar.accept(percentage); // get the percentage and update progress
                    updateProgressBarPercentage.accept(percentage); // update label according to percentage
                }
        );
    }

    public void updateTotalConfigsPossible(long totalPossibleConfigurations) {
        Platform.runLater(
                () -> {
                    updateTotalConfigs.accept(totalPossibleConfigurations);
                }
        );
    }

    public void updateTaskActiveStatus(Boolean isActive) {
        Platform.runLater(
                () -> {
                    updateTaskActiveStatus.accept(isActive);
                }
        );
    }

    public void updateAverageTasksProcessTime(double averageTasksProcessTime) {
        Platform.runLater(
                () -> {
                    updateAverageTasksProcessTime.accept(averageTasksProcessTime);
                }
        );
    }
}
