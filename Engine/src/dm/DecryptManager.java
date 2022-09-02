package dm;

import candidate.Candidate;
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

    private String copyOfMachineLocation = "/dm/copyOfMachineLocation";
    private Machine enigmaMachine;
    private Dictionary dictionary;
    private int numberOfAgents;
    private DifficultyLevel difficultyLevel;
    private ExecutorService threadExecutor = Executors.newFixedThreadPool(numberOfAgents);
    private BlockingQueue<List<Candidate>> candidatesQueue = new LinkedBlockingQueue<>();
    private String textToDecipher;
    private BooleanHolder allTaskAreDone;
    private List<Candidate> allCandidates = new ArrayList<>();
    private UIAdapter uiAdapter;

    public DecryptManager(Dictionary dictionary, int numberOfAgents, Machine enigmaMachine) {
        this.dictionary = dictionary;
        this.numberOfAgents = numberOfAgents;
        this.enigmaMachine = enigmaMachine;
        this.difficultyLevel = DifficultyLevel.UNDEFINED;
        this.textToDecipher = "";
        // enigmaMachine.saveCopyOfMachine(copyOfMachineLocation);
    }

    public void startDecrypt(String textToDecipher, DifficultyLevel difficultyLevel) {
        allTaskAreDone.value = false;

        Thread taskProducer = new Thread(new TaskProducer(threadExecutor, enigmaMachine,
                textToDecipher, dictionary, candidatesQueue, uiAdapter));

        Thread candidatesConsumer = new Thread(new CandidatesConsumer(candidatesQueue, allCandidates, allTaskAreDone));

        taskProducer.start(); // thread is in the air starting the missions spread.
        candidatesConsumer.start();

        // main thread ends here
    }

    public List<Candidate> getDecipherCandidates() {
        return allCandidates;
    }
}
