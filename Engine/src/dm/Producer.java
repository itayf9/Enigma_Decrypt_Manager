package dm;

import dm.difficultylevel.DifficultyLevel;
import machine.Machine;
import machine.component.Rotor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static utill.Utility.decimalToRoman;

public class Producer implements Runnable {
    protected ExecutorService Pool = null;
    private Machine machine;
    private int taskSize;
    private DifficultyLevel difficulty;
    String currentWindowsCharacters;

    public Producer(ExecutorService Pool) {
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

                    // first clone a machine to send to the agent
                    Machine copyOfMachine = this.machine; // Clone!

                    // easy mode so rotors doesn't change.
                    List<Integer> rotorsIDs = machine.getInUseRotorsIDs();

                    // the next window characters to set for the agent, based on last window characters
                    String nextWindowsChars = getNextWindowsChars(taskSize, currentWindowsCharacters);
                    currentWindowsCharacters = nextWindowsChars;

                    List<Integer> windowOfssets = new ArrayList<>();

                    for (int i = 0; i < nextWindowsChars.length(); i++) {
                        Rotor nextRotor = machine.getRotorByID(rotorsIDs.get(i));
                        int offset = nextRotor.translateChar2Offset(nextWindowsChars.charAt(i));
                        windowOfssets.add(offset);
                    }

                    String inUseReflectorSymbol = decimalToRoman(machine.getInUseReflector().getId());


                    //Pool.execute(new Agent(rotorsIDs, windowOfssets, inUseReflectorSymbol, copyOfMachine));
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
