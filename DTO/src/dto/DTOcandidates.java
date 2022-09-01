package dto;

import candidate.Candidate;
import problem.Problem;

import java.util.List;

public class DTOcandidates extends DTOstatus {

    List<Candidate> allCandidates;

    public DTOcandidates(boolean isSucceed, Problem details, List<Candidate> allCandidates) {
        super(isSucceed, details);
        this.allCandidates = allCandidates;
    }

    public List<Candidate> getAllCandidates() {
        return allCandidates;
    }
}
