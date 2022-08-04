package machine;

import component.PlugBoard;
import component.Reflector;
import component.Rotor;
import dto.CharacterPair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnigmaMachine {

    private ArrayList<Rotor> availableRotors;
    private ArrayList<Reflector> availableReflectors;
    private int rotorsCount;
    private String alphabet;

    // the map is used to replace alphabet.indexOf(srcChar)
    // with better complexity.
    private Map<Character, Integer> character2index;

    private ArrayList<Integer> inUseWindowsOffsets;

    private PlugBoard plugBoard;


    // current configuration
    private ArrayList<Rotor> inUseRotors;
    private Reflector inUseReflector;

    private static int cipherCounter = 0;

    /*public EnigmaMachine(ArrayList<Rotor> inUseRotors, Reflector inUseReflector, String alphabet, String plugs){
        this.inUseRotors = inUseRotors;
        this.inUseReflector= inUseReflector;
        this.alphabet= alphabet;
        buildAlphabetMap();
        this.plugBoard= new PlugBoard();
        plugBoard.initPlugBoardMap(character2index, plugs);


    }*/

    public EnigmaMachine(ArrayList<Rotor> availableRotors, ArrayList<Reflector> availableReflectors, int rotorsCount, String alphabet, Map<Character, Integer> character2index) {
        this.availableRotors = availableRotors;
        this.availableReflectors = availableReflectors;
        this.rotorsCount = rotorsCount;
        this.alphabet = alphabet;
        this.character2index = character2index;
        this.plugBoard= new PlugBoard();
        this.inUseRotors= new ArrayList<>();
    }








    // need those getters??????????????????????????????????????????????????????????????///////

    public ArrayList<Rotor> getAvailableRotors() {
        return availableRotors;
    }

    public ArrayList<Reflector> getAvailableReflectors() {
        return availableReflectors;
    }

    public PlugBoard getPlugBoard() {
        return plugBoard;
    }

    public ArrayList<Rotor> getInUseRotors() {
        return inUseRotors;
    }

    public Reflector getInUseReflector() {
        return inUseReflector;
    }







    public int getAvailableRotorsLen () {
        return availableRotors.size();
    }

    public int getAvailableReflectorsLen () {
        return availableReflectors.size();
    }

    public int getRotorsCount() {
        return rotorsCount;
    }

    public List<Integer> getAllNotchPositions() {
        List<Integer> notchPositions = new ArrayList<>();

        for (Rotor rotor : availableRotors) {
            notchPositions.add(rotor.getOriginalNotchIndex()+1);
        }

        return notchPositions;
    }

    public void updateConfiguration(ArrayList<Integer> rotorsIDs, ArrayList<Integer> windowOffsets , int reflectorID, String plugs) {

        for (int i = 0; i < rotorsIDs.size(); i++) {
            inUseRotors.add(availableRotors.get(rotorsIDs.get(i) - 1));
            availableRotors.get(rotorsIDs.get(i) - 1).rotateToOffset(windowOffsets.get(i));
        }

        inUseWindowsOffsets = windowOffsets;

        inUseReflector = availableReflectors.get(reflectorID - 1);


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

    public Rotor getRotorByID(Integer id) {
        return availableRotors.get(id - 1);
    }

    public static int getCipherCounter() {
        return cipherCounter;
    }

    public static void advanceCipherCounter(){
        cipherCounter++;
    }

    public List<Integer> getInUseRotorsIDs() {
        List<Integer> inUseRotorsIDs= new ArrayList<>();

        for (Rotor rotor : inUseRotors){
            inUseRotorsIDs.add(rotor.getId());
        }

        return inUseRotorsIDs;
    }

    public List<Character> getAllWindowsCharacters() {
        List<Character> windowsCharacters = new ArrayList<>();

        for (int i = 0; i < inUseWindowsOffsets.size(); i++) {
            windowsCharacters.add( inUseRotors.get(i).translateOffset2Char( inUseWindowsOffsets.get(i) ) );
        }

        return windowsCharacters;
    }

    public List<CharacterPair> getListOfPlugPairs() {
        List<CharacterPair> plugPairs = new ArrayList<>();

        for (Map.Entry<Integer, Integer> plug : plugBoard.getPlugMap().entrySet()) {
            CharacterPair currentPlug = new CharacterPair( alphabet.charAt(plug.getKey()), alphabet.charAt(plug.getValue()));
            CharacterPair reversedCurrentPlug = new CharacterPair( alphabet.charAt(plug.getValue()) , alphabet.charAt(plug.getKey()) );

            if ((!plugPairs.contains(currentPlug)) && (!plugPairs.contains(reversedCurrentPlug))){
                plugPairs.add(currentPlug);
            }
        }

        return plugPairs;
    }
}
