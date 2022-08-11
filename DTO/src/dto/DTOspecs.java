package dto;

import utill.CharacterPair;
import utill.Problem;

import java.util.List;

public class DTOspecs extends DTOstatus {
    private int availableRotorsCount;
    private int inUseRotorsCount;
    private List<Integer> notchDistancesToWindow;
    private int availableReflectorsCount;
    private int cipheredTextsCount;

    private List<Integer> inUseRotorsIDs;
    private List<Character> windowsCharacters;
    private String inUseReflectorSymbol;
    private List<CharacterPair> inUsePlugs;

    public DTOspecs(boolean isSucceeded, Problem details, int availableRotorsCount, int inUseRotorsCount, List<Integer> notchDistancesToWindow,
                    int availableReflectorsCount, int cipheredTextsCount, List<Integer> inUseRotorsIDs,
                    List<Character> windowsCharacters, String inUseReflectorSymbol, List<CharacterPair> inUsePlugs) {
        super(isSucceeded, details);
        this.availableRotorsCount = availableRotorsCount;
        this.inUseRotorsCount = inUseRotorsCount;
        this.notchDistancesToWindow = notchDistancesToWindow;
        this.availableReflectorsCount = availableReflectorsCount;
        this.cipheredTextsCount = cipheredTextsCount;
        this.inUseRotorsIDs = inUseRotorsIDs;
        this.windowsCharacters = windowsCharacters;
        this.inUseReflectorSymbol = inUseReflectorSymbol;
        this.inUsePlugs = inUsePlugs;
    }

    public int getAvailableRotorsCount() {
        return availableRotorsCount;
    }

    public int getInUseRotorsCount() {
        return inUseRotorsCount;
    }

    public List<Integer> getNotchPositions() {
        return notchPositions;
    }

    public int getAvailableReflectorsCount() {
        return availableReflectorsCount;
    }

    public int getCipheredTextsCount() {
        return cipheredTextsCount;
    }

    public List<Integer> getInUseRotorsIDs() {
        return inUseRotorsIDs;
    }

    public List<Character> getWindowsCharacters() {
        return windowsCharacters;
    }

    public String getInUseReflectorSymbol() {
        return inUseReflectorSymbol;
    }

    public List<CharacterPair> getInUsePlugs() {
        return inUsePlugs;
    }

    @Override
    public String toString() {
       StringBuilder res = new StringBuilder();
       res.append("Available Rotors Count: " + availableRotorsCount + '\n' +
                "In Use Rotors Count: " + inUseRotorsCount + '\n');

        for (int i = 0; i < notchDistancesToWindow.size(); i++) {
            if (i == 0){
                 res.append("Notch Positions: Rotor #" + (i+1) + " ---> ");
            }
            else {
                res.append("                 Rotor #" + (i+1) + " ---> ");
            }


            res.append( notchPositions.get(i) + "\n");
        }
        res.append("\n");
        res.append("Available Reflectors Count: " + availableReflectorsCount + '\n' +
                "Ciphered Texts Count: " + cipheredTextsCount + '\n');

        if (super.getDetails().equals(Problem.NO_CONFIGURATION)){
            res.append(" - No configuration has been chosen yet.");
        } else {
            res.append(" - Current Configuration: <") ;

            for (int i = inUseRotorsIDs.size()-1; i >= 0 ; i--) {
                res.append(inUseRotorsIDs.get(i).toString());
                if (i != 0) {
                    res.append(",");
                }
            }
            res.append("><");

            for (int i = windowsCharacters.size()-1; i >=0 ; i--) {
                res.append(windowsCharacters.get(i));
            }
            res.append("><");

            res.append(inUseReflectorSymbol);
            res.append(">");


            if(inUsePlugs.size() > 0) {
                res.append("<");
                for (int i = 0; i < inUsePlugs.size(); i++) {
                    res.append(inUsePlugs.get(i).getFirst());
                    res.append("|");
                    res.append(inUsePlugs.get(i).getSecond());
                    if (i != inUsePlugs.size()-1) {
                        res.append(",");
                    }
                }
                res.append(">");
            }
        }
        return res.toString();
    }
}
