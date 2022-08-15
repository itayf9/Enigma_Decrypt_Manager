package statistics;

import javafx.util.Pair;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class StatisticRecord implements Serializable {

    private List<Integer> inUseRotors;
    private String windowCharacters;
    private int reflectorID;
    private String plugs;

    private List<Integer> originalNotchPositions;

    private List<Pair<Pair<String, String>, Long>> cipherHistory;


    public StatisticRecord(List<Integer> inUseRotors, String windowCharacters, int reflectorID, String plugs, List<Integer> originalNotchPositions) {
        this.inUseRotors = inUseRotors;
        this.windowCharacters = windowCharacters;
        this.reflectorID = reflectorID;
        this.plugs = plugs;
        this.originalNotchPositions = originalNotchPositions;
        this.cipherHistory = new ArrayList<>();
    }

    public List<Integer> getInUseRotors() {
        return inUseRotors;
    }

    public String getWindowCharacters() {
        return windowCharacters;
    }

    public int getReflectorID() {
        return reflectorID;
    }

    public String getPlugs() {
        return plugs;
    }

    public List<Integer> getOriginalNotchPositions() {
        return originalNotchPositions;
    }

    public List<Pair<Pair<String, String>, Long>> getCipherHistory() {
        return cipherHistory;
    }

    @Override
    public String toString() {
        return "StatisticRecord{" +
                "inUseRotors=" + inUseRotors +
                ", windowCharacters='" + windowCharacters + '\'' +
                ", reflectorID=" + reflectorID +
                ", plugs='" + plugs + '\'' +
                ", cipherHistory=" + cipherHistory +
                '}';
    }
}
