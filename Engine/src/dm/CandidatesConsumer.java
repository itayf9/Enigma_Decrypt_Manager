package dm;

import candidate.Candidate;
import org.omg.CORBA.BooleanHolder;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class CandidatesConsumer implements Runnable {

    private BlockingQueue<List<Candidate>> candidateQueue;
    private List<Candidate> allCandidates;
    private BooleanHolder allTaskAreDone;

    public CandidatesConsumer(BlockingQueue<List<Candidate>> candidateQueue, List<Candidate> allCandidates,
                              BooleanHolder allTaskAreDone) {
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

            if (queueTakenCandidates.size() != 0) {
                for (Candidate candidate : queueTakenCandidates) {
                    allCandidates.add(candidate);
                }
            }

        }
    }
}
