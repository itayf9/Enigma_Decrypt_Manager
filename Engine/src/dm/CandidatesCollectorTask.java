package dm;

import candidate.Candidate;
import dm.agent.AgentConclusion;
import javafx.concurrent.Task;
import ui.adapter.UIAdapter;

import java.util.concurrent.BlockingQueue;

public class CandidatesCollectorTask extends Task<Boolean> {

    private final BlockingQueue<AgentConclusion> candidateQueue;

    private final long totalPossibleConfigurations;

    private UIAdapter uiAdapter;

    public CandidatesCollectorTask(BlockingQueue<AgentConclusion> candidateQueue, long totalPossibleConfigurations, UIAdapter uiAdapter) {
        this.candidateQueue = candidateQueue;
        this.totalPossibleConfigurations = totalPossibleConfigurations;
        this.uiAdapter = uiAdapter;
    }

    @Override
    protected Boolean call() throws Exception {

        final long[] scannedConfigsCount = {0};
        uiAdapter.updateTotalConfigsPossible(totalPossibleConfigurations);

        updateMessage("Searching for Candidates...");
        uiAdapter.updateTaskStatus("Searching for Candidates...");

        while (scannedConfigsCount[0] < totalPossibleConfigurations) {
            AgentConclusion queueTakenCandidates = null;
            try {
                queueTakenCandidates = candidateQueue.take();

                scannedConfigsCount[0] += queueTakenCandidates.getNumOfScannedConfigurations();

                updateProgress(scannedConfigsCount[0], totalPossibleConfigurations);
                uiAdapter.updateProgressBar((double) scannedConfigsCount[0] / (double) totalPossibleConfigurations);

                uiAdapter.updateTotalProcessedConfigurations(queueTakenCandidates.getNumOfScannedConfigurations());
            } catch (InterruptedException e) {
                if (scannedConfigsCount[0] < totalPossibleConfigurations) {
                    return Boolean.TRUE;
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
                Thread.sleep(500);
            } catch (InterruptedException ignored) {

            }


        }
        uiAdapter.updateTaskActiveStatus(false);
        updateMessage("Done...");
        uiAdapter.updateTaskStatus("Done...");
        return Boolean.TRUE;
    }
}
