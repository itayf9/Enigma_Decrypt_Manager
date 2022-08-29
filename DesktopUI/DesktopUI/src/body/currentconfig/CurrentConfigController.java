package body.currentconfig;

import body.BodyController;
import dto.DTOsecretConfig;
import dto.DTOspecs;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import problem.Problem;

import java.util.List;

public class CurrentConfigController {
    private BodyController parentController;

    @FXML
    private Label rotors;

    @FXML
    private Label windows;

    @FXML
    private Label reflector;

    @FXML
    private Label plugs;

    @FXML
    private Label noConfigMsg;

    @FXML
    private Label configTitle;


    public void displayCurrentConfig(DTOsecretConfig configStatus) {

        if (configStatus.getDetails().equals(Problem.NO_CONFIGURATION)) {
            rotors.setText("");
            windows.setText("");
            reflector.setText("");
            plugs.setText("");
            noConfigMsg.setText("No Configuration yet.");
            return;
        }


        List<Integer> inUseRotorsIDs = configStatus.getRotors();
        List<Integer> notchDistancesToWindow = configStatus.getNotchDistances();
        StringBuilder rotorsStr = new StringBuilder();

        rotorsStr.append("<");
        for (int i = inUseRotorsIDs.size() - 1; i >= 0; i--) {
            rotorsStr.append(inUseRotorsIDs.get(i).toString());
            if (notchDistancesToWindow.size() > 0) {
                rotorsStr.append("(")
                        .append(notchDistancesToWindow.get(i).toString())
                        .append(")");
            }
            if (i != 0) {
                rotorsStr.append(",");
            }
        }
        rotorsStr.append(">");

        // display rotors ids
        this.rotors.setText(rotorsStr.toString());


        // display windows
        this.windows.setText("<" + configStatus.getWindows() + ">");

        // display reflector
        this.reflector.setText("<" + configStatus.getReflectorSymbol() + ">");

        StringBuilder plugsBuilder = new StringBuilder();
        String inUsePlugs = configStatus.getPlugs();

        if (inUsePlugs.length() > 0) {
            plugsBuilder.append("<");
            for (int i = 0; i < inUsePlugs.length(); i += 2) {
                plugsBuilder.append(inUsePlugs.charAt(i))
                        .append("|")
                        .append(inUsePlugs.charAt(i + 1));
                if (i != inUsePlugs.length() - 2) {
                    plugsBuilder.append(",");
                }
            }
            plugsBuilder.append(">");
        }

        // display plugs
        this.plugs.setText(plugsBuilder.toString());

        this.noConfigMsg.setText("");
    }

    public void setParentController(BodyController parentController) {
        this.parentController = parentController;
    }


    public void setTitle(String title) {
        configTitle.setText(title);
    }
}
