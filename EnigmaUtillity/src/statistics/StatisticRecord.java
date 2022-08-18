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


    /**
     * constructor for StatisticRecord
     *
     * @param inUseRotors            a list of in used rotor's id's
     * @param windowCharacters       a String of windows characters
     * @param reflectorID            the refelector id as an integer
     * @param plugs                  a String of plugs without spaces of separators
     * @param originalNotchPositions a list of original notch distances from window for each rotor
     */
    public StatisticRecord(List<Integer> inUseRotors, String windowCharacters, int reflectorID, String plugs, List<Integer> originalNotchPositions) {
        this.inUseRotors = inUseRotors;
        this.windowCharacters = windowCharacters;
        this.reflectorID = reflectorID;
        this.plugs = plugs;
        this.originalNotchPositions = originalNotchPositions;
        this.cipherHistory = new ArrayList<>();
    }

    /**
     * @return a list of in use rotors
     */
    public List<Integer> getInUseRotors() {
        return inUseRotors;
    }

    /**
     * @return a String of the windows characters
     */
    public String getWindowCharacters() {
        return windowCharacters;
    }

    /**
     * @return the refletor id as an integer
     */
    public int getReflectorID() {
        return reflectorID;
    }

    /**
     * @return a string of plugs without spaces or separators
     */
    public String getPlugs() {
        return plugs;
    }

    /**
     * @return a list of original notch distances
     */
    public List<Integer> getOriginalNotchPositions() {
        return originalNotchPositions;
    }

    /**
     * @return a list of the history of ciphering
     */
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
