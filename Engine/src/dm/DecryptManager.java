package dm;


import dm.dictionary.Dictionary;
import dm.difficultylevel.DifficultyLevel;
import dm.taskproducer.TaskProducer;
import machine.Machine;

import java.util.concurrent.*;

public class DecryptManager {

    private String copyOfMachineLocation = "/dm/copyOfMachineLocation";

    private Machine enigmaMachine;

    private Dictionary dictionary;
    private int numberOfAgents;

    private DifficultyLevel difficultyLevel;

    private TaskProducer taskProducer;

    private ExecutorService threadExecutor = Executors.newFixedThreadPool(numberOfAgents);

    String textToDecipher;

    public DecryptManager(Dictionary dictionary, int numberOfAgents, Machine enigmaMachine) {
        this.dictionary = dictionary;
        this.numberOfAgents = numberOfAgents;
        this.enigmaMachine = enigmaMachine;
        this.difficultyLevel = DifficultyLevel.UNDEFINED;
        this.textToDecipher = "";
        // enigmaMachine.saveCopyOfMachine(copyOfMachineLocation);
    }

    public void startDecrypt(String textToDecipher, DifficultyLevel difficultyLevel) {

        Thread taskProducer = new Thread(new TaskProducer(threadExecutor, enigmaMachine, textToDecipher, dictionary));
    }
}
