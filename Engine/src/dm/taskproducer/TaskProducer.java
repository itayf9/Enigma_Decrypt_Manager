package dm.taskproducer;

import dm.agent.AgentTask;
import dm.dictionary.Dictionary;
import dm.difficultylevel.DifficultyLevel;
import machine.EnigmaMachine;
import machine.Machine;
import machine.component.Rotor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

public class Producer implements Runnable {
    protected ExecutorService Pool = null;
    private Machine machine;
    private int taskSize;
    private DifficultyLevel difficulty;
    String currentWindowsCharacters;

    public TaskProducer(ExecutorService Pool, Machine machine, String textToDecipher, Dictionary dictionary) {
        this.Pool = Pool;
    }

    public void run() {
        StringBuilder initWindows = new StringBuilder();
        // init currentWindowsCharacters to "AAA according to size of rotors"
        for (int i = 0; i < machine.getRotorsCount(); i++) {
            initWindows.append(machine.getAlphabet().charAt(0));
        }
        currentWindowsCharacters = initWindows.toString();

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
                        Pool.execute(new AgentTask(rotorsIDs, nextWindowsOffsets, inUseReflectorID, copyOfMachine, taskSize, textToDecipher, dictionary));
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


        //setMachineConfiguration(List<Integer> rotorsIDs, List<Integer> windowOfssets, int reflectorID, String plugs);
    }

    private String getNextWindowsChars(int taskSize, String currentWindowsCharacters) {
        String nextWindowsChar = "";

        return nextWindowsChar;
    }

}
