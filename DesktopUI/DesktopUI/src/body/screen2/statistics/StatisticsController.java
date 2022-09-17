package body.screen2.statistics;

import body.BodyController;
import dto.DTOstatistics;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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

        vBoxHistory.getChildren().clear();

        if (stats.getStats().size() == 0) {
            Label nextSecret = new Label("No statistics yet.");
            vBoxHistory.getChildren().add(nextSecret);
        }

        for (StatisticRecord record : stats.getStats()) {

            VBox nextRecord = new VBox();

            Label nextSecret = new Label(assembleConfiguration(record.getInUseRotors(), record.getWindowCharacters(),
                    Utility.decimalToRoman(record.getReflectorID()), record.getPlugs(), record.getOriginalNotchPositions()));

            Label textTitle = new Label("Ciphered Texts:");
            textTitle.getStyleClass().add("sub-title");

            nextRecord.getChildren().add(configTitle);
            nextRecord.getChildren().add(nextSecret);

            TableView<Pair<Pair<String, String>, Long>> cipherRecordTable;

            if (record.getCipherHistory().size() != 0) {
                cipherRecordTable = new TableView<>();
                cipherRecordTable.setPrefHeight(Region.USE_COMPUTED_SIZE);
                TableColumn<Pair<Pair<String, String>, Long>, String> inputColumn = new TableColumn<>("Input");

                inputColumn.setCellValueFactory(new PropertyValueFactory<>("key::key"));

                TableColumn<Pair<Pair<String, String>, Long>, String> outputColumn = new TableColumn<>("Output");

                outputColumn.setCellValueFactory(new PropertyValueFactory<>("key"));

                TableColumn<Pair<Pair<String, String>, Long>, String> timeTakenColumn = new TableColumn<>("Time Taken");

                timeTakenColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

                cipherRecordTable.getColumns().addAll(inputColumn, outputColumn, timeTakenColumn);

                for (Pair<Pair<String, String>, Long> currentCipherRecord : record.getCipherHistory()) {

                    cipherRecordTable.getItems().add(currentCipherRecord);
                }

                nextRecord.getChildren().add(cipherRecordTable);

            }

            vBoxHistory.getChildren().add(nextRecord);

            //for (Pair<Pair<String, String>, Long> currentCipherRecord : record.getCipherHistory()) {

            //   cipherRecordTable.getItems().add(currentCipherRecord);


                /*StringBuilder cipherRecordStr = new StringBuilder();

                cipherRecordStr.append("#. ")
                        .append("<")
                        .append(currentCipherRecord.getKey().getKey())
                        .append(">").append(" --> ").append("<")
                        .append(currentCipherRecord.getKey().getValue()).append(">")
                        .append(" (").append(currentCipherRecord.getValue()).append(" nano-seconds)");

                Label newCipherHistory = new Label(cipherRecordStr.toString());
                vBoxHistory.getChildren().add(newCipherHistory);*/

            // }
        }

    }

    private String assembleConfiguration(List<Integer> inUseRotorsIDs, String windowsCharacters, String inUseReflectorSymbol, String inUsePlugs, List<Integer> notchDistancesToWindow) {
        StringBuilder strConfig = new StringBuilder();

        // prints rotors' configuration
        // including the in use rotors' IDs, their order, and the current notch distances from the windows
        strConfig.append("<");
        for (int i = inUseRotorsIDs.size() - 1; i >= 0; i--) {
            strConfig.append(inUseRotorsIDs.get(i).toString());
            if (i != 0) {
                strConfig.append(",");
            }
        }
        strConfig.append(">");

        // prints windows characters configuration
        strConfig.append("<");
        for (int i = windowsCharacters.length() - 1; i >= 0; i--) {
            strConfig.append(windowsCharacters.charAt(i));
            if (notchDistancesToWindow.size() > 0) {
                strConfig.append("(")
                        .append(notchDistancesToWindow.get(i).toString())
                        .append(")");
            }
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
