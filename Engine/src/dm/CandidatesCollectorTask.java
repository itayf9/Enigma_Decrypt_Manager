package dm;

import candidate.Candidate;
import dm.agent.AgentConclusion;
import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import ui.adapter.UIAdapter;

import java.util.concurrent.BlockingQueue;

public class CandidatesCollectorTask extends Task<Boolean> {

    private final BlockingQueue<AgentConclusion> candidateQueue;
    private final long totalPossibleConfigurations;
    private UIAdapter uiAdapter;
    private BooleanProperty isBruteForceActionCancelled;

    public CandidatesCollectorTask(BlockingQueue<AgentConclusion> candidateQueue, long totalPossibleConfigurations,
                                   UIAdapter uiAdapter, BooleanProperty isBruteForceActionCancelled) {
        this.candidateQueue = candidateQueue;
        this.totalPossibleConfigurations = totalPossibleConfigurations;
        this.uiAdapter = uiAdapter;
        this.isBruteForceActionCancelled = isBruteForceActionCancelled;
    }

    @Override
    protected Boolean call() throws Exception {

        final long[] scannedConfigsCount = {0};
        uiAdapter.updateTotalConfigsPossible(totalPossibleConfigurations);

        updateMessage("Searching for Candidates...");
        uiAdapter.updateTaskStatus("Searching for Candidates...");

        while (scannedConfigsCount[0] < totalPossibleConfigurations && !isBruteForceActionCancelled.getValue()) {
            AgentConclusion queueTakenCandidates = null;
            try {
                queueTakenCandidates = candidateQueue.take();

                scannedConfigsCount[0] += queueTakenCandidates.getNumOfScannedConfigurations();

                updateProgress(scannedConfigsCount[0], totalPossibleConfigurations);
                uiAdapter.updateProgressBar((double) scannedConfigsCount[0] / (double) totalPossibleConfigurations);

                uiAdapter.updateTotalProcessedConfigurations(queueTakenCandidates.getNumOfScannedConfigurations());
            } catch (InterruptedException e) {
                if (scannedConfigsCount[0] >= totalPossibleConfigurations) {
                    return Boolean.TRUE;
                } else {
                    uiAdapter.updateTaskStatus("Stopped...");
                    return Boolean.FALSE;
                }
            }

            if (queueTakenCandidates.getCandidates().size() != 0) {
                updateMessage("Found Candidate!");
                uiAdapter.updateTaskStatus("Found Candidate!");
                for (Candidate candidate : queueTakenCandidates.getCandidates()) {
                    uiAdapter.addNewCandidate(candidate);
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
                uiAdapter.updateTaskStatus("Stopped...");
                return Boolean.FALSE;
            }


        }
        uiAdapter.updateTaskActiveStatus(false);
        updateMessage("Done...");
        uiAdapter.updateTaskStatus("Done...");
        return Boolean.TRUE;
    }
}
