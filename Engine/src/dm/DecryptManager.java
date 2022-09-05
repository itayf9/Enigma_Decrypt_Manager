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

    private final int LIMIT_NUMBER_OF_TASK = 1000;
    private Machine enigmaMachine;
    private Dictionary dictionary;
    private int numberOfAgents;
    private DifficultyLevel difficultyLevel;
    private ExecutorService threadExecutor;
    private BlockingQueue<AgentConclusion> candidatesQueue;
    private String textToDecipher;
    private BooleanHolder allTaskAreDone;
    private List<Candidate> allCandidates = new ArrayList<>();
    private UIAdapter uiAdapter;
    private long totalPossibleConfigurations;
    private BlockingQueue<Runnable> threadPoolBlockingQueue;


    public DecryptManager(Dictionary dictionary, int numberOfAgents, Machine enigmaMachine) {
        this.candidatesQueue = new LinkedBlockingQueue<>();
        this.threadPoolBlockingQueue = new LinkedBlockingQueue<>(LIMIT_NUMBER_OF_TASK);
        this.dictionary = dictionary;
        this.numberOfAgents = numberOfAgents;
        this.enigmaMachine = enigmaMachine;
        this.difficultyLevel = DifficultyLevel.UNDEFINED;
        this.threadExecutor = new ThreadPoolExecutor(numberOfAgents, numberOfAgents,
                0L, TimeUnit.MILLISECONDS, threadPoolBlockingQueue);
        this.textToDecipher = "";
        this.totalPossibleConfigurations = (long) Math.pow(enigmaMachine.getAlphabet().length(), enigmaMachine.getRotorsCount());
    }

    public void startDecrypt(int taskSize, String textToDecipher, DifficultyLevel difficultyLevel, UIAdapter uiAdapter) {
        allTaskAreDone.value = false;

        Thread taskProducer = new Thread(new TaskProducer(threadPoolBlockingQueue, enigmaMachine, taskSize,
                textToDecipher, dictionary, candidatesQueue, uiAdapter));

        Thread candidatesCollectorTask = new Thread(new CandidatesCollectorTask(candidatesQueue,
                allCandidates, allTaskAreDone, totalPossibleConfigurations, uiAdapter));

        // trigger the threads
        threadExecutor.execute(() -> {
        });
        taskProducer.start(); // thread is in the air starting the missions spread.
        candidatesCollectorTask.start();


        // main thread ends here
    }

    public List<Candidate> getDecipherCandidates() {
        return allCandidates;
    }
}
