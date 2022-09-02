package dm;

import candidate.Candidate;
import dm.agent.AgentConclusion;
import dm.dictionary.Dictionary;
import dm.difficultylevel.DifficultyLevel;
import dm.taskproducer.TaskProducer;
import machine.Machine;
import org.omg.CORBA.BooleanHolder;
import ui.adapter.UIAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class DecryptManager {

    private Machine enigmaMachine;
    private Dictionary dictionary;
    private int numberOfAgents;
    private DifficultyLevel difficultyLevel;
    private ExecutorService threadExecutor;
    private BlockingQueue<AgentConclusion> candidatesQueue = new LinkedBlockingQueue<>();
    private String textToDecipher;
    private BooleanHolder allTaskAreDone;
    private List<Candidate> allCandidates = new ArrayList<>();
    private UIAdapter uiAdapter;

    public DecryptManager(Dictionary dictionary, int numberOfAgents, Machine enigmaMachine) {
        this.dictionary = dictionary;
        this.numberOfAgents = numberOfAgents;
        this.enigmaMachine = enigmaMachine;
        this.difficultyLevel = DifficultyLevel.UNDEFINED;
        this.threadExecutor = Executors.newFixedThreadPool(numberOfAgents);
        this.textToDecipher = "";
    }

    public void startDecrypt(String textToDecipher, DifficultyLevel difficultyLevel) {
        allTaskAreDone.value = false;

        Thread taskProducer = new Thread(new TaskProducer(threadExecutor, enigmaMachine, taskSize,
                textToDecipher, dictionary, candidatesQueue, uiAdapter));

        Thread candidatesCollectorTask = new Thread(new CandidatesCollectorTask(candidatesQueue,
                allCandidates, allTaskAreDone, totalPossibleConfigurations, uiAdapter));

        taskProducer.start(); // thread is in the air starting the missions spread.
        candidatesConsumer.start();

        // main thread ends here
    }

    public List<Candidate> getDecipherCandidates() {
        return allCandidates;
    }
}
