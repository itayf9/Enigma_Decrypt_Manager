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

    public CandidatesCollectorTask(BlockingQueue<AgentConclusion> candidateQueue, List<Candidate> allCandidates,
                                   BooleanHolder allTaskAreDone, long totalPossibleConfigurations, UIAdapter uiAdapter) {
        this.candidateQueue = candidateQueue;
        this.allCandidates = allCandidates;
        this.allTaskAreDone = allTaskAreDone;
    }

    @Override
    public void run() {

        while (!allTaskAreDone.value) {

            List<Candidate> queueTakenCandidates = null;
            try {
                queueTakenCandidates = candidateQueue.take();
            } catch (InterruptedException e) {
                if (allTaskAreDone.value) {
                    return;
                }
            }

            if (queueTakenCandidates.getCandidates().size() != 0) {
                for (Candidate candidate : queueTakenCandidates.getCandidates()) {
                    allCandidates.add(candidate);
                }
            }

        }
        updateMessage("Done...");
        return Boolean.TRUE;
    }
}
