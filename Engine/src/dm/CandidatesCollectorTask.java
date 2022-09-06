package dm;

import candidate.Candidate;
import dm.agent.AgentConclusion;
import javafx.concurrent.Task;
import org.omg.CORBA.BooleanHolder;
import ui.adapter.UIAdapter;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class CandidatesCollectorTask extends Task<Boolean> {

    private BlockingQueue<AgentConclusion> candidateQueue;
    private List<Candidate> allCandidates;
    private BooleanHolder allTaskAreDone;

    private long totalPossibleConfigurations;

    private long totalPossibleWindowsPositions;
    private UIAdapter uiAdapter;

    public CandidatesCollectorTask(BlockingQueue<AgentConclusion> candidateQueue, List<Candidate> allCandidates,
                                   BooleanHolder allTaskAreDone, long totalPossibleConfigurations, long totalPossibleWindowsPositions, UIAdapter uiAdapter) {
        this.candidateQueue = candidateQueue;
        this.allCandidates = allCandidates;
        this.allTaskAreDone = allTaskAreDone;
        this.totalPossibleConfigurations = totalPossibleConfigurations;
        this.totalPossibleWindowsPositions = totalPossibleWindowsPositions;
        this.uiAdapter = uiAdapter;
    }

    @Override
    protected Boolean call() throws Exception {
        final long[] wordCount = {1};

        updateMessage("Searching for Candidates...");


        while (!allTaskAreDone.value) {

            AgentConclusion queueTakenCandidates = null;
            try {
                queueTakenCandidates = candidateQueue.take();
                wordCount[0] += queueTakenCandidates.getNumOfScannedConfigurations();
                updateProgress(wordCount[0], totalPossibleConfigurations);
                uiAdapter.updateTotalProcessedConfigurations(queueTakenCandidates.getNumOfScannedConfigurations());
            } catch (InterruptedException e) {
                if (allTaskAreDone.value) {
                    return Boolean.TRUE;
                }
            }

            if (queueTakenCandidates.getCandidates().size() != 0) {
                for (Candidate candidate : queueTakenCandidates.getCandidates()) {
                    allCandidates.add(candidate);
                    updateMessage("Found Candidate!");
                }
            }
        }
        updateMessage("Done...");
        return Boolean.TRUE;
    }
}
