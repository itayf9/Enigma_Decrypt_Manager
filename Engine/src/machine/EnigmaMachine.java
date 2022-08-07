package machine;

import machine.component.PlugBoard;
import machine.component.Reflector;
import machine.component.Rotor;
import utill.CharacterPair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnigmaMachine {

    // the machine contains a list of Rotors.
    private List<Rotor> availableRotors;
    // the machine contains also a list of Reflectors.
    private List<Reflector> availableReflectors;
    // we use rotorsCount as a field which describes the current number of allowed Rotors.
    private int rotorsCount;
    // we use alphabet as a field that describes and converts the indexes to "ABC Characters".
    private String alphabet;

    // this map is used to replace alphabet.indexOf(srcChar)
    // with better complexity.
    private Map<Character, Integer> character2index;




    // This area describes the current configuration of the machine.

    // we represent the current Rotors offset from the window of the machine.
    // using list of offset values.
    private List<Integer> inUseWindowsOffsets;
    // we represent the plugBoard with plugBoard instance that contains a map of configured plugs.
    private PlugBoard plugBoard;
    // current Rotors configured in the machine to work with.
    private List<Rotor> inUseRotors;
    // current Reflector configured in the machine to work with.
    private Reflector inUseReflector;
    // current text that got ciphered in the machine.
    private static int cipherCounter = 0;

    /*public EnigmaMachine(ArrayList<Rotor> inUseRotors, Reflector inUseReflector, String alphabet, String plugs){
        this.inUseRotors = inUseRotors;
        this.inUseReflector= inUseReflector;
        this.alphabet= alphabet;
        buildAlphabetMap();
        this.plugBoard= new PlugBoard();
        plugBoard.initPlugBoardMap(character2index, plugs);


    }*/


    // Constructor of Enigma Machine
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

    // getters
    public List<Rotor> getAvailableRotors() {
        return availableRotors;
    }

    public List<Reflector> getAvailableReflectors() {
        return availableReflectors;
    }

    public PlugBoard getPlugBoard() {
        return plugBoard;
    }

    public List<Rotor> getInUseRotors() {
        return inUseRotors;
    }

    public Reflector getInUseReflector() {
        return inUseReflector;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public List<Integer> getInUseWindowsOffsets() {
        return inUseWindowsOffsets;
    }

    // getting size of the list of Available Rotors in the machine
    public int getAvailableRotorsLen () {
        return availableRotors.size();
    }
    // getting size of the list of Available Reflectors in the machine
    public int getAvailableReflectorsLen () {
        return availableReflectors.size();
    }

    // getting size of the rotors that is currently allowed to be in machine
    public int getRotorsCount() {
        return rotorsCount;
    }
    // getting all the notch positions of all in use rotors in machine
    public List<Integer> getAllNotchPositions() {
        List<Integer> notchPositions = new ArrayList<>();

        for (Rotor rotor : availableRotors) {
            notchPositions.add(rotor.getOriginalNotchIndex()+1);
        }

        return notchPositions;
    }

    // updating the current config of the machine.
    // by sending the updated list of rotors, reflectors and plugs.
    public void setMachineConfiguration(List<Integer> rotorsIDs, List<Integer> windowOffsets , int reflectorID, List<String> plugs) {

        for (int i = 0; i < rotorsIDs.size(); i++) {
            inUseRotors.add(availableRotors.get(rotorsIDs.get(i) - 1));
            availableRotors.get(rotorsIDs.get(i) - 1).rotateToOffset(windowOffsets.get(i));
        }

        inUseWindowsOffsets = windowOffsets;

        inUseReflector = availableReflectors.get(reflectorID - 1);


        plugBoard.initPlugBoardMap(character2index, plugs);
    }

    // initialize cipher sequence based on: input->plugs->rotors-reflector-rotors-plugs->screen.
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


    /**
     * rotates the rotors in the machine. the first rotor is always being rotated.
     * for each rotor, it makes the next rotor to be rotated, if the notch has reached an absolute offset of 0.
     */
    public void rotateRotors(){

        // goes through all rotors
        // fisrt rotor is always being rotated
        for (Rotor rotor : inUseRotors) {
            rotor.rotate();

            // checks if needs to rotate next rotor
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

    /**
     * finds a rotor by a specific id
     * @param id an unique id of a rotor
     * @return the rotor with that id
     */
    public Rotor getRotorByID(Integer id) {
        return availableRotors.get(id - 1);
    }

    /**
     *
     * @return how many ciphered texts the machine has been ciphering so far
     */
    public static int getCipherCounter() {
        return cipherCounter;
    }

    /**
     * increases the amount of ciphered texts that the machine has ciphered so far, by one.
     */
    public static void advanceCipherCounter(){
        cipherCounter++;
    }

    /**
     *
     * @return a list of the ids of the in-use rotors, maintaining their order.
     */
    public List<Integer> getInUseRotorsIDs() {
        List<Integer> inUseRotorsIDs= new ArrayList<>();

        for (Rotor rotor : inUseRotors){
            inUseRotorsIDs.add(rotor.getId());
        }

        return inUseRotorsIDs;
    }

    /**
     *
     * @return a list of the alphabet letters, for each rotor, that are currently at the 'window' (meaning that the absolute position is 0)
     */
    public List<Character> getAllWindowsCharacters() {
        List<Character> windowsCharacters = new ArrayList<>();

        for (int i = 0; i < inUseWindowsOffsets.size(); i++) {
            windowsCharacters.add( inUseRotors.get(i).translateOffset2Char( inUseWindowsOffsets.get(i) ) );
        }

        return windowsCharacters;
    }

    /**
     *
     * @return a list of pairs of characters that are plugged in the plug board.
     */
    public List<CharacterPair> getListOfPlugPairs() {
        List<CharacterPair> plugPairs = new ArrayList<>();

        // goes through the <key, value> pairs in the plug map
        for (Map.Entry<Integer, Integer> plug : plugBoard.getPlugMap().entrySet()) {

            // sets two new pairs of seperated characters.
            // first is extracted from the current <key, value> pair.
            // second is the opposite one to that <key, value> pair.
            CharacterPair currentPlug = new CharacterPair( alphabet.charAt(plug.getKey()), alphabet.charAt(plug.getValue()));
            CharacterPair reversedCurrentPlug = new CharacterPair( alphabet.charAt(plug.getValue()) , alphabet.charAt(plug.getKey()) );

            // in order to prevent duplicates, checks this pair is already in the output list.
            // if it's not, then the pair is added to the list.
            if ((!plugPairs.contains(currentPlug)) && (!plugPairs.contains(reversedCurrentPlug))){
                plugPairs.add(currentPlug);
            }
        }

        return plugPairs;
    }
}
