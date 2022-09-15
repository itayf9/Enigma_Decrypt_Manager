package dm.candidatecollector;

import candidate.Candidate;
import dm.agent.AgentConclusion;
import javafx.beans.property.BooleanProperty;
import ui.adapter.UIAdapter;

import java.util.concurrent.BlockingQueue;

public class CandidatesCollector implements Runnable {

    private final BlockingQueue<AgentConclusion> candidateQueue;
    private final long totalPossibleConfigurations;
    private final UIAdapter uiAdapter;
    private final BooleanProperty isBruteForceActionPaused;
    private final BooleanProperty isBruteForceActionCancelled;

    public CandidatesCollector(BlockingQueue<AgentConclusion> candidateQueue, long totalPossibleConfigurations,
                               UIAdapter uiAdapter, BooleanProperty isBruteForceActionCancelled,
                               BooleanProperty isBruteForceActionPaused) {
        this.candidateQueue = candidateQueue;
        this.totalPossibleConfigurations = totalPossibleConfigurations;
        this.uiAdapter = uiAdapter;
        this.isBruteForceActionPaused = isBruteForceActionPaused;
        this.isBruteForceActionCancelled = isBruteForceActionCancelled;
    }

    @Override
    public void run() {

        long totalTasksProcessTime = 0;
        long scannedConfigsCount = 0;
        long tasksCounter = 0;
        double averageTasksProcessTime;

        uiAdapter.updateTotalConfigsPossible(totalPossibleConfigurations);
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

                uiAdapter.updateProgressBar((double) scannedConfigsCount / (double) totalPossibleConfigurations);
                uiAdapter.updateTotalProcessedConfigurations(queueTakenCandidates.getNumOfScannedConfigurations());

            } catch (InterruptedException e) {
                if (scannedConfigsCount >= totalPossibleConfigurations) {
                    return;
                } else {
                    uiAdapter.updateTaskStatus("Stopped...");
                    return;
                }
            }

            if (queueTakenCandidates.getCandidates().size() != 0) {

                for (Candidate candidate : queueTakenCandidates.getCandidates()) {
                    uiAdapter.addNewCandidate(candidate);
                }
            }

            if (isBruteForceActionPaused.getValue()) {
                synchronized (isBruteForceActionPaused) {
                    while (isBruteForceActionPaused.getValue()) {
                        try {
                            uiAdapter.updateTaskStatus("Paused...");
                            isBruteForceActionPaused.wait();
                        } catch (InterruptedException ignored) {
                            uiAdapter.updateTaskStatus("Stopped...");
                            return;
                        }
                    }
                    uiAdapter.updateTaskStatus("Searching...");
                }
            }

            try {
                Thread.sleep(25);
            } catch (InterruptedException ignored) {
                uiAdapter.updateTaskStatus("Stopped...");
                return;
            }
        }
        uiAdapter.updateTaskActiveStatus(false);
        uiAdapter.updateTaskStatus("Done...");
    }
}
