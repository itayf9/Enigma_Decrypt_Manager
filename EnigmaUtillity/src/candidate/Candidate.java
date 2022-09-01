package candidate;

import java.util.List;

public class Candidate {

    private String decipheredText;
    private List<Integer> rotorsIDs;
    private String windowChars;
    private String reflectorSymbol;
    // time measuring


    public Candidate(String decipheredText, List<Integer> rotorsIDs, String windowChars, String reflectorSymbol) {
        this.decipheredText = decipheredText;
        this.rotorsIDs = rotorsIDs;
        this.reflectorSymbol = reflectorSymbol;
        this.windowChars = windowChars;
    }

    public String getDecipheredText() {
        return decipheredText;
    }
}
