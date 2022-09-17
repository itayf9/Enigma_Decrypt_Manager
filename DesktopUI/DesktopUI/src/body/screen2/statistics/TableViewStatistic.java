package body.screen2.statistics;

import javafx.util.Pair;

public class TableViewStatistic {

    private Pair<String, String> cipherInputOutput;
    private Long timeTaken;

    public TableViewStatistic(Pair<String, String> cipherInputOutput, Long timeTaken) {
        this.cipherInputOutput = cipherInputOutput;
        this.timeTaken = timeTaken;
    }

    public String getInput() {
        return cipherInputOutput.getKey();
    }

    public String getOutput() {
        return cipherInputOutput.getValue();
    }

    public Long getTimeTaken() {
        return timeTaken;
    }
}
