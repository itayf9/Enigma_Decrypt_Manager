package dm.taskproducer;

import dm.agent.AgentTask;
import candidate.Candidate;
import dm.dictionary.Dictionary;
import dm.difficultylevel.DifficultyLevel;
import machine.EnigmaMachine;
import machine.Machine;
import ui.adapter.UIAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

public class TaskProducer implements Runnable {

    // private String copyOfMachineLocation = "/dm/copyOfMachineLocation";
    private final String END_OF_TASKS = "END OF TASKS";
    protected ExecutorService Pool = null;
    private Machine machine;
    private String alphabet;
    private int taskSize;
    private DifficultyLevel difficulty = DifficultyLevel.EASY;
    private List<Integer> currentWindowsOffsets;
    private boolean currentTaskSubmitted = true;
    private String textToDecipher;
    private Dictionary dictionary;
    private BlockingQueue<List<Candidate>> candidatesQueue;
    private UIAdapter uiAdapter;


    public TaskProducer(ExecutorService Pool, Machine machine, String textToDecipher,
                        Dictionary dictionary, BlockingQueue<List<Candidate>> candidatesQueue, UIAdapter uiAdapter) {
        this.Pool = Pool;
        this.machine = machine;
        this.alphabet = machine.getAlphabet();
        this.textToDecipher = textToDecipher;
        this.dictionary = dictionary;
        this.candidatesQueue = candidatesQueue;
    }

    public void run() {
        List<Integer> currentWindowsOffsets = new ArrayList<>(Collections.nCopies(machine.getRotorsCount(), 0));

        boolean finishedAllTasks = false;
        // set the tasks and send them to the pool

        switch (difficulty) {
            case EASY:
                while (!finishedAllTasks) {

                    List<Integer> rotorsIDs = null;
                    int inUseReflectorID = 0;
                    List<Integer> nextWindowsOffsets = null;

                    // first clone a machine to send to the agent
                    EnigmaMachine copyOfMachine = (EnigmaMachine) new EnigmaMachine((EnigmaMachine) machine); // Clone!

                    if (currentTaskSubmitted) {

                        // easy mode so rotors doesn't change.
                        rotorsIDs = machine.getInUseRotorsIDs();

                        // the next window characters to set for the agent, based on last window characters
                        nextWindowsOffsets = getNextWindowsOffsets(taskSize, currentWindowsOffsets);
                        if (nextWindowsOffsets.size() == 0) {
                            finishedAllTasks = true;
                            continue; // exit the loop => kill the taskProducer.
                        }

                        // replace current list with next list
                        currentWindowsOffsets.clear();
                        currentWindowsOffsets.addAll(nextWindowsOffsets);

                        // get the reflector
                        inUseReflectorID = machine.getInUseReflector().getId();
                    }

                    try {
                        Pool.execute(new AgentTask(rotorsIDs, nextWindowsOffsets, inUseReflectorID,
                                copyOfMachine, taskSize, textToDecipher, dictionary, candidatesQueue, uiAdapter));
                    } catch (RejectedExecutionException e) {
                        currentTaskSubmitted = false;
                        Thread.yield();
                    }

                }
                break;
            case MEDIUM:
                break;
            case HARD:
                break;
            case IMPOSSIBLE:
                break;
            case UNDEFINED:
                break;
        }
    }

    private List<Integer> getNextWindowsOffsets(int taskSize, List<Integer> currentWindowsOffsets) {

        List<Integer> nextWindowsOffsets = new ArrayList<>(currentWindowsOffsets);

        for (int i = 0; i < taskSize; i++) {

            for (Integer windowOffset : nextWindowsOffsets) {
                windowOffset = rotateWindow(windowOffset);

                // check if it is needed to rotate next rotor
                if (windowOffset != 0) {
                    break;
                }
            }
        }
        return nextWindowsOffsets;
    }

    private int rotateWindow(Integer windowOffset) {
        return (windowOffset + 1 + alphabet.length()) % alphabet.length();
    }

}
