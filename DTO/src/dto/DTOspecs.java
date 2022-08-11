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

    @Override
    public String toString() {
       StringBuilder res = new StringBuilder();
       res.append("Specifications: \n")
               .append(" - Number Of Rotors (In Use / Available): ")
               .append(inUseRotorsCount)
               .append(" / ")
               .append(availableRotorsCount)
               .append('\n');

        for (int i = 0; i < notchDistancesToWindow.size(); i++) {
            if (i == 0){
                 res.append(" - Notch's Distances From Windows: The notch of rotor #")
                         .append(inUseRotorsIDs.get(i))
                         .append(" ---> ")
                         .append(notchDistancesToWindow.get(i))
                         .append(" steps away");
                 if (notchDistancesToWindow.get(i) == 0){
                     res.append("(The notch has reached the window)");
                 }
                 res.append(".\n");
            }
            else {
                res.append("                                      The notch of rotor #")
                        .append(inUseRotorsIDs.get(i))
                        .append(" ---> ")
                        .append(notchDistancesToWindow.get(i))
                        .append(" steps away");
                if (notchDistancesToWindow.get(i) == 0){
                    res.append("(The notch has reached the window)");
                }
                res.append(".\n");
            }
        }
        res.append("\n");


        res.append(" - Number Of Reflectors (Available): ").append(availableReflectorsCount).append('\n').append("Ciphered Texts Count: ").append(cipheredTextsCount).append('\n');

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
