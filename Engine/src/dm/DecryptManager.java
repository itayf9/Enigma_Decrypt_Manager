package dm;


import dm.dictionary.Dictionary;
import dm.difficultylevel.DifficultyLevel;
import dm.taskproducer.TaskProducer;
import machine.Machine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DecryptManager {

    private Machine enigmaMachine;

    private Dictionary dictionary;
    private int numberOfAgents;

    private DifficultyLevel difficultyLevel;

    private TaskProducer taskProducer;

    private ExecutorService threadExecutor = Executors.newFixedThreadPool(numberOfAgents);

    public DecryptManager(Dictionary dictionary, int numberOfAgents, Machine enigmaMachine) {
        this.dictionary = dictionary;
        this.numberOfAgents = numberOfAgents;
        this.enigmaMachine = enigmaMachine;
        this.difficultyLevel = DifficultyLevel.UNDEFINED;
    }

    public void startDecrypt() {

    }
}
