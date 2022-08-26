package body.screen2.statistics;

import body.BodyController;
import dto.DTOstatistics;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import statistics.StatisticRecord;
import utill.Utility;

import java.util.List;


public class StatisticsController {

    private BodyController parentController;

    @FXML
    private VBox vBoxHistory;

    public void setParentController(BodyController parentController) {
        this.parentController = parentController;
    }

    public void displayStats(DTOstatistics stats) {

        if (stats.getStats().size() == 0) {
            Label nextSecret = new Label("No statistics yet.");
            vBoxHistory.getChildren().add(nextSecret);
        }

        for (StatisticRecord record : stats.getStats()) {

            Label nextSecret = new Label(assembleConfiguration(record.getInUseRotors(), record.getWindowCharacters(),
                    Utility.decimalToRoman(record.getReflectorID()), record.getPlugs(), record.getOriginalNotchPositions()));

            vBoxHistory.getChildren().add(nextSecret);

            for (Pair<Pair<String, String>, Long> currentCipherRecord : record.getCipherHistory()) {
                StringBuilder cipherRecordStr = new StringBuilder();

                cipherRecordStr.append("#. ")
                        .append("<")
                        .append(currentCipherRecord.getKey().getKey())
                        .append(">").append(" --> ").append("<")
                        .append(currentCipherRecord.getKey().getValue()).append(">")
                        .append(" (").append(currentCipherRecord.getValue()).append(" nano-seconds)");

                Label newCipherHistory = new Label(cipherRecordStr.toString());
                vBoxHistory.getChildren().add(newCipherHistory);

            }

        }
    }

    private String assembleConfiguration(List<Integer> inUseRotorsIDs, String windowsCharacters, String inUseReflectorSymbol, String inUsePlugs, List<Integer> notchDistancesToWindow) {
        StringBuilder strConfig = new StringBuilder();

        // prints rotors' configuration
        // including the in use rotors' IDs, their order, and the current notch distances from the windows
        strConfig.append("<");
        for (int i = inUseRotorsIDs.size() - 1; i >= 0; i--) {
            strConfig.append(inUseRotorsIDs.get(i).toString());
            if (notchDistancesToWindow.size() > 0) {
                strConfig.append("(")
                        .append(notchDistancesToWindow.get(i).toString())
                        .append(")");
            }
            if (i != 0) {
                strConfig.append(",");
            }
        }
        strConfig.append(">");

        // prints windows characters configuration
        strConfig.append("<");
        for (int i = windowsCharacters.length() - 1; i >= 0; i--) {
            strConfig.append(windowsCharacters.charAt(i));
        }
        strConfig.append(">");

        // prints reflector configuration
        strConfig.append("<")
                .append(inUseReflectorSymbol)
                .append(">");

        // prints plugs configuration
        if (inUsePlugs.length() > 0) {
            strConfig.append("<");
            for (int i = 0; i < inUsePlugs.length(); i += 2) {
                strConfig.append(inUsePlugs.charAt(i))
                        .append("|")
                        .append(inUsePlugs.charAt(i + 1));
                if (i != inUsePlugs.length() - 2) {
                    strConfig.append(",");
                }
            }
            strConfig.append(">");
        }

        return strConfig.toString();
    }
}
