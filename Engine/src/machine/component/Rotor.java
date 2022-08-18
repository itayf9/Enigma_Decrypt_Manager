package machine.component;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Rotor implements Serializable {

    private int id; // id of rotor
    private final int originalNotchIndex; // the notch position from the window.
    private int offset = 0; // an offset to calculate current position according to window.
    private Map<Character, Integer> alphabetTranslator; // translate alphabet characters to matching integer value.
    private int alphabetLength; // alphabet length
    private List<Integer> forwardMapping; // forward table for rotor mappings.
    private List<Integer> backwardMapping; // backward table for rotor mappings.

    /**
     * constructor for Rotor
     *
     * @param id                 the unique id of the rotor
     * @param originalNotchIndex the index of the notch, in the original state of the rotor
     * @param alphabetTranslator the map that translates a character from the alphabet to an index
     * @param alphabetLength     the amount of letters in the alphabet
     * @param forwardMapping     the mapping from right to left
     * @param backwardMapping    the mapping from left to right
     */
    public Rotor(int id, int originalNotchIndex, Map<Character, Integer> alphabetTranslator, int alphabetLength, List<Integer> forwardMapping, List<Integer> backwardMapping) {
        this.id = id;
        this.originalNotchIndex = originalNotchIndex;
        this.alphabetTranslator = alphabetTranslator;
        this.alphabetLength = alphabetLength;
        this.forwardMapping = forwardMapping;
        this.backwardMapping = backwardMapping;

    }

    /**
     *
     * @return the unique id of the rotor
     */
    public int getId() {
        return id;
    }


    /**
     *
     * @return the index of the notch, in the original state of the rotor
     */
    public int getOriginalNotchIndex() {
        return originalNotchIndex;
    }

    /**
     *
     * @return the current offset of the rotor from its original position
     */
    public int getOffset() {
        return offset;
    }


    /**
     *
     * @param inputIndex an index on the right side of the rotor, as an entry
     * @return an index of the matching index on the left side of the rotor
     */
    public int getMatchForward ( int inputIndex ) {

        int original_inputIndex= (inputIndex + offset + alphabetLength) %  alphabetLength;
        int original_outputIndex= forwardMapping.get(original_inputIndex);
        return (original_outputIndex - offset + alphabetLength) %  alphabetLength;
    }

    /**
     *
     * @param inputIndex an index on the left side of the rotor, as an entry
     * @return an index of the matching index on the right side of the rotor
     */
    public int getMatchBackward ( int inputIndex ) {

        int current_inputIndex= (inputIndex + offset + alphabetLength) % alphabetLength;
        int original_charIndex= backwardMapping.get(current_inputIndex);
        return ((original_charIndex - offset + alphabetLength) % alphabetLength);
    }

    /**
     * rotates the rotor one step ahead of its current position
     */
    public void rotate() {
        offset = (offset+1) % alphabetLength;
    }

    /**
     * sets a new absolute offset to the rotor, regardless of its current position
     * @param offset the absolute new offset for the rotor
     */
    public void rotateToOffset(int offset) {
        this.offset = offset;
    }

    /**
     * translates an alphabet's letter, to its matching index, via 'alphabetTranslator'
     * @param currentChar the letter to be translated
     * @return the matching index
     */
    public int translateChar2Offset(char currentChar) {
        return alphabetTranslator.get(currentChar);
    }

    /**
     * translates an offset (similar to an index) , to its matching alphabet's letter, via 'alphabetTranslator'
     * @param offset the offset to be translated
     * @return the matching alphabet's letter
     */
    public Character translateOffset2Char(int offset) {

        for (Map.Entry<Character, Integer> entry : alphabetTranslator.entrySet()) {
            if (entry.getValue().equals(offset)) {
                return entry.getKey();
            }
        }

        /********************************************************************add exception********************/
        return '\n';
    }

    @Override
    public String toString() {
        return "Rotor{" +
                "id=" + id +
                '}';
    }

}
