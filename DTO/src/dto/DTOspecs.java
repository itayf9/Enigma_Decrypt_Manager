package dto;

import javafx.util.Pair;
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
    private List<Pair<Character, Character>> inUsePlugs;

    public DTOspecs(boolean isSucceeded, Problem details, int availableRotorsCount, int inUseRotorsCount,
                    List<Integer> notchDistancesToWindow, int availableReflectorsCount, int cipheredTextsCount,
                    List<Integer> inUseRotorsIDs, List<Character> windowsCharacters, String inUseReflectorSymbol,
                    List<Pair<Character, Character>> inUsePlugs) {
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

    public List<Integer> getNotchDistancesToWindow() {
        return notchDistancesToWindow;
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

    public List<Pair<Character, Character>> getInUsePlugs() {
        return inUsePlugs;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("Specifications: \n")
                .append(" - Number Of Rotors (In Use / Available): ")
                .append(inUseRotorsCount)
                .append(" / ")
                .append(availableRotorsCount)
                .append('\n')
                .append(" - Number Of Reflectors (Available): ")
                .append(availableReflectorsCount)
                .append('\n')
                .append(" - Number Of Texts That Were Ciphered So Far: ")
                .append(cipheredTextsCount)
                .append('\n')
                .append("\n");


        if (super.getDetails().equals(Problem.NO_CONFIGURATION)) {
            res.append(" - No configuration has been chosen yet.");
        } else {
            res.append(" - Current Configuration: <");

            for (int i = inUseRotorsIDs.size() - 1; i >= 0; i--) {
                res.append(inUseRotorsIDs.get(i).toString());
                if (i != 0) {
                    res.append(",");
                }
            }
            res.append("><");
            for (int i = windowsCharacters.size() - 1; i >= 0; i--) {
                res.append(windowsCharacters.get(i));
            }
            res.append("><");

            res.append(inUseReflectorSymbol);
            res.append(">");


            if (inUsePlugs.size() > 0) {
                res.append("<");
                for (int i = 0; i < inUsePlugs.size(); i++) {
                    res.append(inUsePlugs.get(i).getKey());
                    res.append("|");
                    res.append(inUsePlugs.get(i).getValue());
                    if (i != inUsePlugs.size() - 1) {
                        res.append(",");
                    }
                }
                res.append(">");
            }
        }
        return res.toString();
    }
}
