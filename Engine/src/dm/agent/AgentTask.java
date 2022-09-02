package dm.agent;

import ui.adapter.UIAdapter;
import candidate.Candidate;
import dm.dictionary.Dictionary;
import machine.Machine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static utill.Utility.decimalToRoman;

public class AgentTask implements Runnable {

    private Machine machine;
    private int taskSize;

    private Dictionary dictionary;
    private String textToDecipher;

    private List<Integer> rotorsIDs;
    private List<Integer> windowOffsets;
    private int inUseReflectorID;
    private BlockingQueue<AgentConclusion> candidatesQueue;
    private UIAdapter uiAdapter;

    public AgentTask(List<Integer> rotorsIDs, List<Integer> windowOffsets, int inUseReflectorID,
                     Machine copyOfMachine, int taskSize, String textToDecipher, Dictionary dictionary,
                     BlockingQueue<AgentConclusion> candidatesQueue, UIAdapter uiAdapter) {
        machine = copyOfMachine;
        this.taskSize = taskSize;
        this.textToDecipher = textToDecipher;
        this.dictionary = dictionary;
        this.windowOffsets = windowOffsets;
        this.rotorsIDs = rotorsIDs;
        this.inUseReflectorID = inUseReflectorID;
        this.candidatesQueue = candidatesQueue;
        this.uiAdapter = uiAdapter;
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

        List<Candidate> candidates = new ArrayList<>();
        int i;
        for (i = 0; i < taskSize; i++) {

            if (AllWindowsOffsetsAtBeginning())
                break;
        }

        // sets machine to the next configuration
        // changes only the window offsets
        machine.setMachineConfiguration(rotorsIDs, windowOffsets, inUseReflectorID, "");

        // ciphers the text
        String decipherResult = decipherLine(textToDecipher);
        resetConfig();

        // check dictionary
        if (dictionary.isAllWordsInDictionary(decipherResult)) {

            // convert windows offsets to characters.
            String windowCharacters = machine.getOriginalWindowsCharacters(); // I trust this !

            // convert reflector ID to Roman number.
            String nextCandidateReflectorSymbol = decimalToRoman(inUseReflectorID);

            Candidate nextCandidate = new Candidate(decipherResult, rotorsIDs, windowCharacters, nextCandidateReflectorSymbol);
            candidates.add(nextCandidate);

            uiAdapter.addNewCandidate(nextCandidate);
        }

        // moves to the next configuration
        advanceWindow();

        // send conclusion to DM
        try {
            candidatesQueue.put(new AgentConclusion(candidates, i));
        } catch (InterruptedException ignored) {

        }
    }

    private boolean AllWindowsOffsetsAtBeginning() {
        return windowOffsets.stream().allMatch(offset -> offset == 0);
    }
}
