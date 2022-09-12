package dm.candidatecollector;

import candidate.Candidate;
import dm.decryptmanager.DecryptManager;
import dm.agent.AgentConclusion;
import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import ui.adapter.UIAdapter;

import java.util.concurrent.BlockingQueue;

public class CandidatesCollectorTask extends Task<Boolean> {

    private final BlockingQueue<AgentConclusion> candidateQueue;
    private final long totalPossibleConfigurations;
    private final UIAdapter uiAdapter;

    private final DecryptManager dm;

    private final BooleanProperty isBruteForceActionPaused;

    private final BooleanProperty isBruteForceActionCancelled;


    public CandidatesCollectorTask(BlockingQueue<AgentConclusion> candidateQueue, long totalPossibleConfigurations,
                                   UIAdapter uiAdapter, DecryptManager dm, BooleanProperty isBruteForceActionCancelled, BooleanProperty isBruteForceActionPaused) {
        this.candidateQueue = candidateQueue;
        this.totalPossibleConfigurations = totalPossibleConfigurations;
        this.uiAdapter = uiAdapter;
        this.dm = dm;
        this.isBruteForceActionPaused = isBruteForceActionPaused;
        this.isBruteForceActionCancelled = isBruteForceActionCancelled;
    }

    @Override
    protected Boolean call() {

        long totalTasksProcessTime = 0;

        long scannedConfigsCount = 0;

        long tasksCounter = 0;

        double averageTasksProcessTime;

        uiAdapter.updateTotalConfigsPossible(totalPossibleConfigurations);

        updateMessage("Searching for Candidates...");
        uiAdapter.updateTaskStatus("Searching...");

        while (scannedConfigsCount < totalPossibleConfigurations && !isBruteForceActionCancelled.getValue()) {
            AgentConclusion queueTakenCandidates;
            try {
                queueTakenCandidates = candidateQueue.take();

                tasksCounter++;
                totalTasksProcessTime += queueTakenCandidates.getTimeTakenToDoTask();

                averageTasksProcessTime = (double) totalTasksProcessTime / (double) tasksCounter;

                uiAdapter.updateAverageTasksProcessTime(averageTasksProcessTime);

                scannedConfigsCount += queueTakenCandidates.getNumOfScannedConfigurations();

                updateProgress(scannedConfigsCount, totalPossibleConfigurations);
                uiAdapter.updateProgressBar((double) scannedConfigsCount / (double) totalPossibleConfigurations);

                uiAdapter.updateTotalProcessedConfigurations(queueTakenCandidates.getNumOfScannedConfigurations());
            } catch (InterruptedException e) {
                if (scannedConfigsCount >= totalPossibleConfigurations) {
                    System.out.println("collector died");
                    return Boolean.TRUE;
                } else {
                    uiAdapter.updateTaskStatus("Stopped...");
                    System.out.println("collector died");
                    return Boolean.FALSE;
                }
            }

            if (queueTakenCandidates.getCandidates().size() != 0) {

                for (Candidate candidate : queueTakenCandidates.getCandidates()) {
                    uiAdapter.addNewCandidate(candidate);
                }
            }

            if (isBruteForceActionPaused.getValue()) {
                synchronized (dm.getDummmy()) {
                    while (isBruteForceActionPaused.getValue()) {
                        try {
                            uiAdapter.updateTaskStatus("Paused...");
                            isBruteForceActionPaused.wait();
                        } catch (InterruptedException ignored) {
                            uiAdapter.updateTaskStatus("Stopped...");
                            System.out.println("collector died");
                            return Boolean.FALSE;
                        }
                        System.out.println("collector awake from wait");
                    }
                    System.out.println("collector out of while loop");
                }
            }


            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
                uiAdapter.updateTaskStatus("Stopped...");
                System.out.println("collector died");
                return Boolean.FALSE;
            }

        }
        uiAdapter.updateTaskActiveStatus(false);
        updateMessage("Done...");
        uiAdapter.updateTaskStatus("Done...");
        System.out.println("collector died");
        return Boolean.TRUE;
    }
}
