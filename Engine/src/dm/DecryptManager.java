package dm;

import candidate.Candidate;
import dm.agent.AgentConclusion;
import dm.dictionary.Dictionary;
import dm.difficultylevel.DifficultyLevel;
import dm.taskproducer.TaskProducer;
import machine.Machine;
import ui.adapter.UIAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class DecryptManager {

    private final Machine enigmaMachine;
    private final Dictionary dictionary;
    private final int numOfAvailableAgents;
    private List<Candidate> allCandidates = new ArrayList<>();

    private final BlockingQueue<Runnable> threadPoolBlockingQueue;

    public int getNumOfAvailableAgents() {
        return numOfAvailableAgents;
    }

    public DecryptManager(Dictionary dictionary, int numOfAvailableAgents, Machine enigmaMachine) {
        int LIMIT_NUMBER_OF_TASK = 1000;
        this.threadPoolBlockingQueue = new LinkedBlockingQueue<>(LIMIT_NUMBER_OF_TASK);
        this.dictionary = dictionary;
        this.numOfAvailableAgents = numOfAvailableAgents;
        this.enigmaMachine = enigmaMachine;
    }

    public void startDecrypt(int taskSize, int numOfSelectedAgents, String textToDecipher, DifficultyLevel difficultyLevel,
                             UIAdapter uiAdapter, BlockingQueue<AgentConclusion> candidatesQueue) {

        // starting the thread pool
        ExecutorService threadExecutor = new ThreadPoolExecutor(numOfSelectedAgents, numOfSelectedAgents,
                0L, TimeUnit.MILLISECONDS, threadPoolBlockingQueue);

        // setting a thread that produces tasks
        Thread taskProducer = new Thread(new TaskProducer(threadPoolBlockingQueue, enigmaMachine, taskSize, difficultyLevel,
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
