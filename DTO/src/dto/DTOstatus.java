package dto;

import problem.Problem;

public class DTOstatus {

    private boolean isSucceed;
    private Problem details;

    public DTOstatus() {
    }

    public DTOstatus(boolean isSucceed, Problem details) {
        this.isSucceed = isSucceed;
        this.details = details;
    }

    public boolean isSucceed() {
        return isSucceed;
    }

    public Problem getDetails() {
        return details;
    }
}