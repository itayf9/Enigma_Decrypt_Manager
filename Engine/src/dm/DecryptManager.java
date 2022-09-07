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

    BlockingQueue<AgentConclusion> candidatesQueue;

    private final Machine enigmaMachine;
    private final Dictionary dictionary;
    private final int numOfAvailableAgents;
    private List<Candidate> allCandidates = new ArrayList<>();

    private long totalPossibleConfigurations;

    private long totalPossibleWindowsPositions;

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
        this.candidatesQueue = new LinkedBlockingQueue<>(1000);
        this.totalPossibleWindowsPositions = (long) Math.pow(enigmaMachine.getAlphabet().length(), enigmaMachine.getRotorsCount());
    }

    public void startDecrypt(int taskSize, int numOfSelectedAgents, String textToDecipher,
                             DifficultyLevel difficultyLevel, UIAdapter uiAdapter) {

        // updates the total configs property
        setTotalConfigs(difficultyLevel);

        // setting up the collector of the candidates
        Thread collector = new Thread(new CandidatesCollectorTask(candidatesQueue, totalPossibleConfigurations, uiAdapter));

        // starting the thread pool
        ThreadPoolExecutor threadExecutor = new ThreadPoolExecutor(numOfSelectedAgents, numOfSelectedAgents,
                0L, TimeUnit.MILLISECONDS, threadPoolBlockingQueue);

        threadExecutor.setThreadFactory(new ThreadFactory() {

            private int nameCounter = 0;

            @Override
            public Thread newThread(Runnable r) {
                nameCounter++;
                return new Thread(r, String.valueOf(nameCounter));
            }
        });


        // setting a thread that produces tasks
        Thread taskProducer = new Thread(new TaskProducer(threadPoolBlockingQueue, enigmaMachine, taskSize, difficultyLevel,
                textToDecipher, dictionary, candidatesQueue));

        // trigger the threads
        threadExecutor.prestartAllCoreThreads();

        taskProducer.start(); // thread is in the air starting the missions spread.
        collector.start();
        // main thread ends here
    }

    private void setTotalConfigs(DifficultyLevel difficultyLevel) {

        switch (difficultyLevel) {
            case EASY:
                totalPossibleConfigurations = (totalPossibleWindowsPositions);
                break;
            case MEDIUM:
                totalPossibleConfigurations = (totalPossibleWindowsPositions * enigmaMachine.getAvailableReflectorsLen());
                break;
            case HARD:
                totalPossibleConfigurations = (totalPossibleWindowsPositions *
                        enigmaMachine.getAvailableReflectorsLen() *
                        factorial(enigmaMachine.getRotorsCount()));
                break;
            case IMPOSSIBLE:
                // need to calculate then implement Too damm HARD
                break;
        }
    }

    public List<Candidate> getDecipherCandidates() {
        return allCandidates;
    }

    private long factorial(int number) {
        long fact = 1;
        for (int i = 1; i <= number; i++) {
            fact = fact * i;
        }
        return fact;
    }
}
