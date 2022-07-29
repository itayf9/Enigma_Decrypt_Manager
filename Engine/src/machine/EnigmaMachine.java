package machine;

import component.PlugBoard;
import component.Reflector;
import component.Rotor;

import java.util.ArrayList;
import java.util.Map;

public class EnigmaMachine {

    private ArrayList<Rotor> rotors;
    private Reflector reflector;
    private PlugBoard plugBoard;
    private String alphabet;

    // the map is used to replace alphabet.indexOf(srcChar)
    // with better complexity.
    private Map<Character, Integer> character2index;

    public EnigmaMachine(){
    }





    public char cipher( char srcChar ){
        int currentCharIndex = character2index.get(srcChar);

        // go to plug board for the first time
        if (plugBoard.isPlugged(currentCharIndex)){
            currentCharIndex= plugBoard.getMatch(currentCharIndex);
        }

        // go through rotors
        for (Rotor rotor : rotors){
            currentCharIndex = rotor.getMatchForward(currentCharIndex);
        }

        // reflector
        currentCharIndex= reflector.reflect(currentCharIndex);

        // go through rotors
        for (Rotor rotor : rotors){
            currentCharIndex = rotor.getMatchBackward(currentCharIndex);
        }

        // go to plug board for the second time
        if (plugBoard.isPlugged(currentCharIndex)){
            currentCharIndex= plugBoard.getMatch(currentCharIndex);
        }

        return alphabet.charAt(currentCharIndex);
    }

    public void buildAlphabetMap(){
    }
}
