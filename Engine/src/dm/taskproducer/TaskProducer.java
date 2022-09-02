package dm.taskproducer;

import dm.agent.AgentConclusion;
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
    private BlockingQueue<AgentConclusion> candidatesQueue;
    private UIAdapter uiAdapter;


    public TaskProducer(ExecutorService Pool, Machine machine, int taskSize, String textToDecipher,
                        Dictionary dictionary, BlockingQueue<AgentConclusion> candidatesQueue, UIAdapter uiAdapter) {
        this.Pool = Pool;
        this.machine = machine;
        this.alphabet = machine.getAlphabet();
        this.taskSize = taskSize;
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
                // set up first agentTask

                // easy mode so rotors doesn't change.
                List<Integer> inUseRotorsIDs = machine.getInUseRotorsIDs();
                // get the reflector
                int inUseReflectorID = machine.getInUseReflector().getId();
                List<Integer> nextWindowsOffsets = null;
                Machine copyOfMachine = null;

                while (!finishedAllTasks) {

                    if (currentTaskSubmitted) {

                        // first clone a machine to send to the agent
                        copyOfMachine = new EnigmaMachine((EnigmaMachine) machine); // Clone!

                        // the next window characters to set for the agent, based on last window characters
                        nextWindowsOffsets = getNextWindowsOffsets(taskSize, currentWindowsOffsets);
                        if (nextWindowsOffsets.size() == 0) {
                            finishedAllTasks = true;
                            continue; // exit the loop => kill the taskProducer.
                        }

                        // replace current list with next list
                        currentWindowsOffsets.clear();
                        currentWindowsOffsets.addAll(nextWindowsOffsets);
                    }

                    try {
                        Pool.execute(new AgentTask(inUseRotorsIDs, nextWindowsOffsets, inUseReflectorID,
                                copyOfMachine, taskSize, textToDecipher, dictionary, candidatesQueue, uiAdapter));
                        currentTaskSubmitted = true;
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
