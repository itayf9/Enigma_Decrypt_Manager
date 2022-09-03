package candidate;

import java.util.List;

public class Candidate {

    private String decipheredText;
    private List<Integer> rotorsIDs;
    private String windowChars;
    private String reflectorSymbol;
    private int processedByAgentID;


    public Candidate(String decipheredText, List<Integer> rotorsIDs, String windowChars, String reflectorSymbol) {
        this.decipheredText = decipheredText;
        this.rotorsIDs = rotorsIDs;
        this.reflectorSymbol = reflectorSymbol;
        this.windowChars = windowChars;
    }

    public String getDecipheredText() {
        return decipheredText;
    }

    public List<Integer> getRotorsIDs() {
        return rotorsIDs;
    }

    public String getWindowChars() {
        return windowChars;
    }

    public String getReflectorSymbol() {
        return reflectorSymbol;
    }

    public int getProcessedByAgentID() {
        return processedByAgentID;
    }
}
