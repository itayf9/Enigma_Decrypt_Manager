package dm.agent;

import dm.dictionary.Dictionary;
import machine.Machine;

import java.util.ArrayList;
import java.util.List;

public class AgentTask implements Runnable {

    private Machine machine;
    private int taskSize;

    private Dictionary dictionary;
    private String textToDecipher;

    private List<Integer> rotorsIDs;
    private List<Integer> windowOffsets;
    private int inUseReflectorID;

    public AgentTask(List<Integer> rotorsIDs, List<Integer> windowOffsets, int inUseReflectorID,
                     Machine copyOfMachine, int taskSize, String textToDecipher, Dictionary dictionary) {
        machine = copyOfMachine;
        this.taskSize = taskSize;
        this.textToDecipher = textToDecipher;
        this.dictionary = dictionary;
        this.windowOffsets = windowOffsets;
        this.rotorsIDs = rotorsIDs;
        this.inUseReflectorID = inUseReflectorID;
    }

    private String decipherLine(String LineToDecipher) {
        StringBuilder cipheredText = new StringBuilder();

        // goes through the character in the string
        for (Character currentChar : LineToDecipher.toCharArray()) {
            cipheredText.append(machine.cipher(currentChar));
        }

        return cipheredText.toString();
    }

    private void resetConfig() {
        for (int i = 0; i < machine.getRotorsCount(); i++) {

            int currentOffset = machine.getInUseWindowsOffsets().get(i);
            machine.getInUseRotors().get(i).rotateToOffset(currentOffset);
        }
    }

    private void advanceWindow() {

        for (Integer windowOffset : windowOffsets) {
            windowOffset = (windowOffset + 1 + machine.getAlphabet().length()) % machine.getAlphabet().length();

            // check if it is needed to rotate next rotor
            if (windowOffset != 0) {
                break;
            }
        }
    }


    @Override
    public void run() {
        List<String> Candidates = new ArrayList<>();

        for (int i = 0; i < taskSize; i++) {

            // sets machine to the next configuration
            // changes only the window offsets
            machine.setMachineConfiguration(rotorsIDs, windowOffsets, inUseReflectorID, "");

            // ciphers the text
            String candidate = decipherLine(textToDecipher);
            resetConfig();

            // check dictionary
            if (dictionary.isAllWordsInDictionary(candidate)) {
                Candidates.add(candidate);
            }

            // moves to the next configuration
            advanceWindow();
        }

        // send candidate list to DM

    }
}
