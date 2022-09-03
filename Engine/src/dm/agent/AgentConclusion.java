package dm.agent;

import candidate.Candidate;

import java.util.List;

public class AgentConclusion {

    private List<Candidate> candidates;
    private int numOfScannedConfigurations;

    private long timeTakenToDoTask;

    public AgentConclusion(List<Candidate> candidates, int numOfScannedConfigurations) {
        this.candidates = candidates;
        this.numOfScannedConfigurations = numOfScannedConfigurations;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public int getNumOfScannedConfigurations() {
        return numOfScannedConfigurations;
    }
}
