package dm.agent;

import candidate.Candidate;
import dm.decryptmanager.DecryptManager;
import dm.dictionary.Dictionary;
import machine.Machine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static utill.Utility.decimalToRoman;

public class AgentTask implements Runnable {

    private final Machine machine;
    private final int taskSize;
    private final Dictionary dictionary;
    private final String textToDecipher;
    private final List<Integer> rotorsIDs;
    private final List<Integer> windowOffsets;
    private final int inUseReflectorID;

    private final DecryptManager dm;
    private final BlockingQueue<AgentConclusion> candidatesQueue;

    public AgentTask(List<Integer> rotorsIDs, List<Integer> windowOffsets, int inUseReflectorID,
                     Machine copyOfMachine, DecryptManager dm, int taskSize, String textToDecipher, Dictionary dictionary,
                     BlockingQueue<AgentConclusion> candidatesQueue) {
        machine = copyOfMachine;
        this.taskSize = taskSize;
        this.textToDecipher = textToDecipher;
        this.dictionary = dictionary;
        this.windowOffsets = windowOffsets;
        this.rotorsIDs = rotorsIDs;
        this.inUseReflectorID = inUseReflectorID;
        this.dm = dm;
        this.candidatesQueue = candidatesQueue;
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

        for (int i = 0; i < windowOffsets.size(); i++) {
            windowOffsets.set(i, (windowOffsets.get(i) + 1 + machine.getAlphabet().length()) % machine.getAlphabet().length());

            // check if it is needed to rotate next rotor
            if (windowOffsets.get(i) != 0) {
                break;
            }
        }
    }

    @Override
    public void run() {
        long startMeasureTime = System.nanoTime();

        List<Candidate> candidates = new ArrayList<>();
        int numOfConfigScanned = 0;


        for (int i = 0; i < taskSize && !dm.isIsBruteForceActionCancelled(); i++) {
            numOfConfigScanned++;

            // sets machine to the next configuration
            // changes only the window offsets

            machine.setMachineConfiguration(rotorsIDs, windowOffsets, inUseReflectorID, "");

            // ciphers the text
            String decipherResult = decipherLine(textToDecipher);

            // check dictionary
            if (dictionary.isAllWordsInDictionary(decipherResult)) {
                // convert windows offsets to characters.
                String windowCharacters = machine.getOriginalWindowsCharacters(); // I trust this !

                // convert reflector ID to Roman number.
                String nextCandidateReflectorSymbol = decimalToRoman(inUseReflectorID);

                // fetch the notch positions
                List<Integer> notchPositions = machine.getOriginalNotchPositions(); // I trust this !

                // fetch the current thread's name
                String processedByAgentName = Thread.currentThread().getName();

                Candidate nextCandidate = new Candidate(decipherResult, rotorsIDs, windowCharacters, nextCandidateReflectorSymbol, notchPositions, processedByAgentName);
                candidates.add(nextCandidate);
            }

            resetConfig();

            // moves to the next configuration
            advanceWindow();


            if (AllWindowsOffsetsAtBeginning()) {
                break;
            }

/*            System.out.println("For agent " + Thread.currentThread().getName() + "is brute paused = " + dm.isIsBruteForceActionPaused());
            if (dm.isIsBruteForceActionPaused()) {

                System.out.println("about to sync with key :" + dm.getDummmy());
                System.out.println("current is brute paused = " + dm.isIsBruteForceActionPaused());
                synchronized (dm.getDummmy()) {
                    System.out.println("agent " + Thread.currentThread().getName() + " got the key : " + dm.getDummmy());

                    while (dm.isIsBruteForceActionPaused()) {
                        try {
                            System.out.println("agent waits" + Thread.currentThread().getName() + " with key : " + dm.getDummmy());
                            wait();
                            System.out.println("agent stopped waiting " + Thread.currentThread().getName() + " with key : " + dm.getDummmy());
                        } catch (InterruptedException ignored) {

                        }
                        System.out.println("agent awaken");
                    }
                    System.out.println("out of while loop");

                }
            }*/

        }
        // send conclusion to DM
        try {
            long timeElapsed = System.nanoTime() - startMeasureTime;
            candidatesQueue.put(new AgentConclusion(candidates, numOfConfigScanned, timeElapsed));
        } catch (InterruptedException ignored) {

        }
    }

    private boolean AllWindowsOffsetsAtBeginning() {
        for (Integer offset : windowOffsets) {
            if (offset != 0) {
                return false;
            }
        }
        return true;
    }
}
