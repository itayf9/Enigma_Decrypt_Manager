package dm.candidatecollector;

import candidate.Candidate;
import dm.agent.AgentConclusion;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import ui.adapter.UIAdapter;

import java.util.concurrent.BlockingQueue;

public class CandidatesCollector implements Runnable {

    private final BlockingQueue<AgentConclusion> candidateQueue;
    private final long totalPossibleConfigurations;

    private LongProperty totalTimeDecryptProperty;
    private final UIAdapter uiAdapter;
    private final BooleanProperty isBruteForceActionPaused;
    private final BooleanProperty isBruteForceActionCancelled;

    private long pauseMeasuring;

    public CandidatesCollector(BlockingQueue<AgentConclusion> candidateQueue, long totalPossibleConfigurations,
                               UIAdapter uiAdapter, LongProperty totalTimeDecryptProperty, BooleanProperty isBruteForceActionCancelled,
                               BooleanProperty isBruteForceActionPaused) {
        this.candidateQueue = candidateQueue;
        this.totalPossibleConfigurations = totalPossibleConfigurations;
        this.totalTimeDecryptProperty = totalTimeDecryptProperty;
        this.uiAdapter = uiAdapter;
        this.isBruteForceActionPaused = isBruteForceActionPaused;
        this.isBruteForceActionCancelled = isBruteForceActionCancelled;
        this.pauseMeasuring = 0;
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
                    uiAdapter.updateTotalTimeDecrypt(System.nanoTime() - totalTimeDecryptProperty.getValue() + pauseMeasuring);
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
                            pauseMeasuring += System.nanoTime() - totalTimeDecryptProperty.getValue();
                            isBruteForceActionPaused.wait();
                        } catch (InterruptedException ignored) {
                            uiAdapter.updateTaskStatus("Stopped...");
                            uiAdapter.updateTotalTimeDecrypt(System.nanoTime() - totalTimeDecryptProperty.getValue() + pauseMeasuring);
                            return;
                        }
                    }
                    uiAdapter.updateTaskStatus("Searching...");
                    totalTimeDecryptProperty.set(System.nanoTime());
                }
            }

            try {
                Thread.sleep(25);
            } catch (InterruptedException ignored) {
                uiAdapter.updateTaskStatus("Stopped...");
                uiAdapter.updateTotalTimeDecrypt(System.nanoTime() - totalTimeDecryptProperty.getValue() + pauseMeasuring);
                return;
            }
        }
        uiAdapter.updateTaskActiveStatus(false);
        uiAdapter.updateTaskStatus("Done...");
        uiAdapter.updateTotalTimeDecrypt(System.nanoTime() - totalTimeDecryptProperty.getValue() + pauseMeasuring);
    }
}
