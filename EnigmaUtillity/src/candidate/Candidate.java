package candidate;

import java.util.List;

public class Candidate {

    private String decipheredText;
    private List<Integer> rotorsIDs;
    private String windowChars;
    private String reflectorSymbol;

    private List<Integer> notchPositions;
    private String processedByAgentName;


    public Candidate(String decipheredText, List<Integer> rotorsIDs, String windowChars, String reflectorSymbol, List<Integer> notchPositions, String processedByAgentName) {
        this.decipheredText = decipheredText;
        this.rotorsIDs = rotorsIDs;
        this.reflectorSymbol = reflectorSymbol;
        this.windowChars = windowChars;
        this.notchPositions = notchPositions;
        this.processedByAgentName = processedByAgentName;
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

    public List<Integer> getNotchPositions() {
        return notchPositions;
    }

    public String getProcessedByAgentName() {
        return processedByAgentName;
    }
}
