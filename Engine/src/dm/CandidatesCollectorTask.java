package dm;

import candidate.Candidate;
import dm.agent.AgentConclusion;
import javafx.concurrent.Task;
import ui.adapter.UIAdapter;

import java.util.concurrent.BlockingQueue;

public class CandidatesCollectorTask extends Task<Boolean> {

    private BlockingQueue<AgentConclusion> candidateQueue;

    private long totalPossibleConfigurations;

    private UIAdapter uiAdapter;

    public CandidatesCollectorTask(BlockingQueue<AgentConclusion> candidateQueue, long totalPossibleConfigurations, UIAdapter uiAdapter) {
        this.candidateQueue = candidateQueue;
        this.totalPossibleConfigurations = totalPossibleConfigurations;
        this.uiAdapter = uiAdapter;
    }

    @Override
    protected Boolean call() throws Exception {
        final long[] wordCount = {0};

        updateMessage("Searching for Candidates...");

        while (wordCount[0] < totalPossibleConfigurations) {

            AgentConclusion queueTakenCandidates = null;
            try {
                queueTakenCandidates = candidateQueue.take();
                wordCount[0] += queueTakenCandidates.getNumOfScannedConfigurations();

                updateProgress(wordCount[0], totalPossibleConfigurations);

                uiAdapter.updateTotalProcessedConfigurations(queueTakenCandidates.getNumOfScannedConfigurations());
            } catch (InterruptedException e) {
                if (wordCount[0] == totalPossibleConfigurations) {
                    return Boolean.TRUE;
                }
            }

            if (queueTakenCandidates.getCandidates().size() != 0) {
                updateMessage("Found Candidate!");
                for (Candidate candidate : queueTakenCandidates.getCandidates()) {
                    uiAdapter.addNewCandidate(candidate);
                }
            }
        }
        updateMessage("Done...");
        return Boolean.TRUE;
    }
}
