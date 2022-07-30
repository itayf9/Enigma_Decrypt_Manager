package machine;

import component.PlugBoard;
import component.Reflector;
import component.Rotor;

import java.util.ArrayList;
import java.util.Map;

public class EnigmaMachine {

    private ArrayList<Rotor> inUseRotors;
    private Reflector reflector;
    private PlugBoard plugBoard;
    private String alphabet;

    // the map is used to replace alphabet.indexOf(srcChar)
    // with better complexity.
    private Map<Character, Integer> character2index;

    public EnigmaMachine(ArrayList<Rotor> inUseRotors, Reflector reflector, String alphabet, String plugs){
        this.inUseRotors = inUseRotors;
        this.reflector= reflector;
        this.alphabet= alphabet;
        buildAlphabetMap();
        this.plugBoard= new PlugBoard();
        plugBoard.initPlugBoardMap(character2index, plugs);


    }


    public char cipher( char srcChar ){


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
        currentCharIndex= reflector.reflect(currentCharIndex);

        // go through rotors
        for (Rotor rotor : inUseRotors){
            currentCharIndex = rotor.getMatchBackward(currentCharIndex);
        }

        // go to plug board for the second time
        if (plugBoard.isPlugged(currentCharIndex)){
            currentCharIndex= plugBoard.getMatch(currentCharIndex);
        }

        return alphabet.charAt(currentCharIndex);
    }

    public void buildAlphabetMap(){
        for (int i = 0; i < alphabet.length(); i++) {
            character2index.put(alphabet.charAt(i), i);
        }
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
}
