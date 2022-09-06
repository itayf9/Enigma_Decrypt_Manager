package ui.adapter;

import candidate.Candidate;
import javafx.application.Platform;

import java.util.function.Consumer;

public class UIAdapter {

    private Consumer<Candidate> introduceNewCandidate;
    private Runnable updateDistinct;
    private Consumer<Integer> updateTotalProcessedConfigurations;

    public UIAdapter(Consumer<Candidate> introduceNewCandidate, Runnable updateDistinct, Consumer<Integer> updateTotalProcessedConfigurations) {
        this.introduceNewCandidate = introduceNewCandidate;
        this.updateDistinct = updateDistinct;
        this.updateTotalProcessedConfigurations = updateTotalProcessedConfigurations;
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
}
