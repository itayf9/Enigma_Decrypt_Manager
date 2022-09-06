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
    private int numOfAvailableAgents;
    private DifficultyLevel difficultyLevel;
    private ExecutorService threadExecutor;
    private BlockingQueue<AgentConclusion> candidatesQueue;
    private String textToDecipher;
    private BooleanHolder allTaskAreDone;
    private List<Candidate> allCandidates = new ArrayList<>();

    private UIAdapter uiAdapter;

    private long totalPossibleConfigurations = 0;

    private long totalPossibleWindowsPositions;
    private BlockingQueue<Runnable> threadPoolBlockingQueue;

    public int getNumOfAvailableAgents() {
        return numOfAvailableAgents;
    }


    public DecryptManager(Dictionary dictionary, int numOfAvailableAgents, Machine enigmaMachine) {
        this.threadPoolBlockingQueue = new LinkedBlockingQueue<>(LIMIT_NUMBER_OF_TASK);
        this.dictionary = dictionary;
        this.numOfAvailableAgents = numOfAvailableAgents;
        this.enigmaMachine = enigmaMachine;
        this.difficultyLevel = DifficultyLevel.UNDEFINED;
        this.textToDecipher = "";
        this.totalPossibleWindowsPositions = (long) Math.pow(enigmaMachine.getAlphabet().length(), enigmaMachine.getRotorsCount());
    }

    public void startDecrypt(int taskSize, int numOfSelectedAgents, String textToDecipher, DifficultyLevel difficultyLevel,
                             UIAdapter uiAdapter, BlockingQueue<AgentConclusion> candidatesQueue) {

        this.candidatesQueue = candidatesQueue;

        // starting the thread pool
        this.threadExecutor = new ThreadPoolExecutor(numOfSelectedAgents, numOfSelectedAgents,
                0L, TimeUnit.MILLISECONDS, threadPoolBlockingQueue);

        // setting a thread that produces tasks
        Thread taskProducer = new Thread(new TaskProducer(threadPoolBlockingQueue, enigmaMachine, taskSize,
                textToDecipher, dictionary, candidatesQueue, uiAdapter));

        // trigger the threads
        threadExecutor.execute(() -> {
        });
        taskProducer.start(); // thread is in the air starting the missions spread.

        // main thread ends here
    }

    public List<Candidate> getDecipherCandidates() {
        return allCandidates;
    }

}
