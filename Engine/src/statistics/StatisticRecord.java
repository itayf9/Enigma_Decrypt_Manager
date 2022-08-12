package statistics;

import javafx.util.Pair;

import java.util.List;

public class StatisticRecord {

    private List<Integer> inUseRotors;
    private String windowCharacters;
    private int reflectorID;
    private String plugs;

    private List<Pair<String, String>> cipherHistory;


    public StatisticRecord(List<Integer> inUseRotors, String windowCharacters, int reflectorID, String plugs) {
        this.inUseRotors = inUseRotors;
        this.windowCharacters = windowCharacters;
        this.reflectorID = reflectorID;
        this.plugs = plugs;
    }
}
