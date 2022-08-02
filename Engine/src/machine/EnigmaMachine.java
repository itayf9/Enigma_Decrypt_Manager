package machine;

import component.PlugBoard;
import component.Reflector;
import component.Rotor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EnigmaMachine {

    private ArrayList<Rotor> availableRotors;
    private ArrayList<Reflector> availableReflectors;
    private int rotorsCount;
    private String alphabet;

    // the map is used to replace alphabet.indexOf(srcChar)
    // with better complexity.
    private Map<Character, Integer> character2index;


    private PlugBoard plugBoard;


    // current configuration
    private ArrayList<Rotor> inUseRotors;
    private Reflector inUseReflector;

    /*public EnigmaMachine(ArrayList<Rotor> inUseRotors, Reflector inUseReflector, String alphabet, String plugs){
        this.inUseRotors = inUseRotors;
        this.inUseReflector= inUseReflector;
        this.alphabet= alphabet;
        buildAlphabetMap();
        this.plugBoard= new PlugBoard();
        plugBoard.initPlugBoardMap(character2index, plugs);


    }*/

    public EnigmaMachine(ArrayList<Rotor> availableRotors, ArrayList<Reflector> availableReflectors, int rotorsCount, String alphabet) {
        this.availableRotors = availableRotors;
        this.availableReflectors = availableReflectors;
        this.rotorsCount = rotorsCount;
        this.alphabet = alphabet;
        // transfer char2index;
        this.plugBoard= new PlugBoard();
        this.inUseRotors= new ArrayList<>();
    }

    public void updateConfiguration(ArrayList<Integer> rotorsIDs, int reflectorID, String plugs) {

        for (int rotorID : rotorsIDs){
            inUseRotors.add(availableRotors.get(rotorID));
        }

        inUseReflector = availableReflectors.get(reflectorID);

        plugBoard.initPlugBoardMap(character2index, plugs);
    }

    public char cipher(char srcChar ){

        // rotates the rotors
        rotateRotors();

        int currentCharIndex = character2index.get(srcChar);

        // go to plug board for the first time
        if (plugBoard.isPlugged(currentCharIndex)){
            currentCharIndex= plugBoard.getMatch(currentCharIndex);
        }

        // go through rotors
        for (Rotor rotor : inUseRotors){
            currentCharIndex = rotor.getMatchForward(currentCharIndex);
        }

        // reflector
        currentCharIndex= inUseReflector.reflect(currentCharIndex);

        // go through rotors
        for (int i = inUseRotors.size() - 1; i >= 0; i--) {
            currentCharIndex = inUseRotors.get(i).getMatchBackward(currentCharIndex);
        }


        // go to plug board for the second time
        if (plugBoard.isPlugged(currentCharIndex)){
            currentCharIndex= plugBoard.getMatch(currentCharIndex);
        }


        return alphabet.charAt(currentCharIndex);
    }



    public void rotateRotors(){

        for (Rotor rotor : inUseRotors) {
            rotor.rotate();

            // check if need to rotate next rotor
            if ( rotor.getOriginalNotchIndex() - rotor.getOffset() != 0){
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "EnigmaMachine{" + '\n' +
                "availableRotors=" + availableRotors + '\n' +
                ", availableReflectors=" + availableReflectors + '\n' +
                ", rotorsCount=" + rotorsCount + '\n' +
                ", alphabet='" + alphabet + '\'' + '\n' +
                ", character2index=" + character2index + '\n' +
                ", plugBoard=" + plugBoard + '\n' +
                ", inUseRotors=" + inUseRotors + '\n' +
                ", inUseReflector=" + inUseReflector + '\n' +
                '}';
    }
}
