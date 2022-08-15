package machine;

import javafx.util.Pair;
import machine.component.PlugBoard;
import machine.component.Reflector;
import machine.component.Rotor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnigmaMachine implements Serializable {

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

    private boolean isConfigured = false;
    private static int cipherCounter = 0;

    /**
     * Constructor of Enigma Machine
     *
     * @param availableRotors     a list of all available rotors
     * @param availableReflectors a list of all available reflectors
     * @param rotorsCount         the number of rotors that has should be configured inside the machine
     * @param alphabet            a string that represents the alphabet, where the characters are ordered as described in the XML file.
     * @param character2index     a map that translates each alphabet character to its matching index in alphabet string.
     */
    public EnigmaMachine(List<Rotor> availableRotors, List<Reflector> availableReflectors, int rotorsCount, String alphabet, Map<Character, Integer> character2index) {
        this.availableRotors = availableRotors;
        this.availableReflectors = availableReflectors;
        this.rotorsCount = rotorsCount;
        this.alphabet = alphabet;
        this.character2index = character2index;
        this.plugBoard = new PlugBoard();
        this.inUseRotors = new ArrayList<>();
        cipherCounter = 0;
    }

    /**
     * @return a list of all rotors that are currently used in the machine, preserving their order.
     * (right most rotor is the first rotor in the list).
     */
    public List<Rotor> getInUseRotors() {
        return inUseRotors;
    }

    /**
     * @return the reflector that is currently used in the machine.
     */
    public Reflector getInUseReflector() {
        return inUseReflector;
    }

    /**
     * @return a string that represents the alphabet
     */
    public String getAlphabet() {
        return alphabet;
    }

    public List<Integer> getInUseWindowsOffsets() {
        return inUseWindowsOffsets;
    }

    // getting size of the list of Available Rotors in the machine
    public int getAvailableRotorsLen() {
        return availableRotors.size();
    }

    // getting size of the list of Available Reflectors in the machine
    public int getAvailableReflectorsLen() {
        return availableReflectors.size();
    }

    // getting size of the rotors that is currently allowed to be in machine
    public int getRotorsCount() {
        return rotorsCount;
    }

    // getting all the notch positions of all in use rotors in machine
    public List<Integer> getInUseNotchDistanceToWindow() {
        List<Integer> notchPositions = new ArrayList<>();

        for (Rotor rotor : inUseRotors) {
            notchPositions.add((rotor.getOriginalNotchIndex() - rotor.getOffset() + alphabet.length()) % alphabet.length());
        }

        return notchPositions;
    }

    public List<Integer> getOriginalNotchPositions() {
        List<Integer> notchPositions = new ArrayList<>();

        for (int i = 0; i < inUseRotors.size(); i++) {
            notchPositions.add((inUseRotors.get(i).getOriginalNotchIndex() - inUseWindowsOffsets.get(i) + alphabet.length()) % alphabet.length());
        }

        return notchPositions;
    }

    public boolean isConfigured() {
        return isConfigured;
    }

    // updating the current config of the machine.
    // by sending the updated list of rotors, reflectors and plugs.
    public void setMachineConfiguration(List<Integer> rotorsIDs, List<Integer> windowOffsets, int reflectorID, String plugs) {

        if (inUseRotors.size() != 0) {
            inUseRotors.clear();
        }
        for (int i = 0; i < rotorsIDs.size(); i++) {
            inUseRotors.add(availableRotors.get(rotorsIDs.get(i) - 1));
            availableRotors.get(rotorsIDs.get(i) - 1).rotateToOffset(windowOffsets.get(i));
        }

        inUseWindowsOffsets = windowOffsets;

        inUseReflector = availableReflectors.get(reflectorID - 1);

        plugBoard.initPlugBoardMap(character2index, plugs);

        isConfigured = true;
    }

    // initialize cipher sequence based on: input->plugs->rotors-reflector-rotors-plugs->screen.
    public char cipher(char srcChar) {

        // rotates the rotors
        rotateRotors();

        int currentCharIndex = character2index.get(srcChar);

        // go to plug board for the first time
        if (plugBoard.isPlugged(currentCharIndex)) {
            currentCharIndex = plugBoard.getMatch(currentCharIndex);
        }

        // go through rotors
        for (Rotor rotor : inUseRotors) {
            currentCharIndex = rotor.getMatchForward(currentCharIndex);
        }

        // reflector
        currentCharIndex = inUseReflector.reflect(currentCharIndex);

        // go through rotors
        for (int i = inUseRotors.size() - 1; i >= 0; i--) {
            currentCharIndex = inUseRotors.get(i).getMatchBackward(currentCharIndex);
        }


        // go to plug board for the second time
        if (plugBoard.isPlugged(currentCharIndex)) {
            currentCharIndex = plugBoard.getMatch(currentCharIndex);
        }


        return alphabet.charAt(currentCharIndex);
    }

    /**
     * rotates the rotors in the machine. the first rotor is always being rotated.
     * for each rotor, it makes the next rotor to be rotated, if the notch has reached an absolute offset of 0.
     */
    public void rotateRotors() {

        // goes through all rotors
        // first rotor is always being rotated
        for (Rotor rotor : inUseRotors) {
            rotor.rotate();

            // check if it is needed to rotate next rotor
            if (rotor.getOriginalNotchIndex() - rotor.getOffset() != 0) {
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
     *
     * @param id an unique id of a rotor
     * @return the rotor with that id
     */
    public Rotor getRotorByID(Integer id) {
        return availableRotors.get(id - 1);
    }

    /**
     * @return how many ciphered texts the machine has been ciphering so far
     */
    public static int getCipherCounter() {
        return cipherCounter;
    }

    /**
     * increases the amount of ciphered texts that the machine has ciphered so far, by one.
     */
    public static void advanceCipherCounter() {
        cipherCounter++;
    }

    /**
     * @return a list of the ids of the in-use rotors, maintaining their order.
     */
    public List<Integer> getInUseRotorsIDs() {
        List<Integer> inUseRotorsIDs = new ArrayList<>();

        for (Rotor rotor : inUseRotors) {
            inUseRotorsIDs.add(rotor.getId());
        }

        return inUseRotorsIDs;
    }

    /**
     * @return a list of the alphabet letters, for each rotor, that are currently at the 'window' (meaning that the absolute position is 0)
     */
    public String getAllWindowsCharacters() {
        StringBuilder windowsCharacters = new StringBuilder();

        for (int i = 0; i < inUseWindowsOffsets.size(); i++) {
            windowsCharacters.append(inUseRotors.get(i).translateOffset2Char(inUseWindowsOffsets.get(i)));
        }

        return windowsCharacters.toString();
    }

    /**
     * @return a list of pairs of characters that are plugged in the plug board.
     */
    public String getAllPlugPairs() {
        List<Pair<Character, Character>> plugPairs = new ArrayList<>();

        // goes through the <key, value> pairs in the plug map
        for (Map.Entry<Integer, Integer> plug : plugBoard.getPlugMap().entrySet()) {

            // sets two new pairs of seperated characters.
            // first is extracted from the current <key, value> pair.
            // second is the opposite one to that <key, value> pair.
            Pair<Character, Character> currentPlug = new Pair<>(alphabet.charAt(plug.getKey()), alphabet.charAt(plug.getValue()));
            Pair<Character, Character> reversedCurrentPlug = new Pair<>(alphabet.charAt(plug.getValue()), alphabet.charAt(plug.getKey()));

            // in order to prevent duplicates, checks this pair is already in the output list.
            // if it's not, then the pair is added to the list.
            if ((!plugPairs.contains(currentPlug)) && (!plugPairs.contains(reversedCurrentPlug))) {
                plugPairs.add(currentPlug);
            }
        }

        // convert the list of plug pairs to one string
        StringBuilder allPlugPairsStr = new StringBuilder();

        for (Pair<Character, Character> plugPair : plugPairs) {
            allPlugPairsStr.append(plugPair.getKey())
                    .append(plugPair.getValue());
        }

        return allPlugPairsStr.toString();
    }
}