package component;

import java.util.ArrayList;
import java.util.List;

public class Rotor {

    private int id;
    private int notchIndex;

    private int offset;
    private int alphabetLength;
    private ArrayList<Integer> forwardMapping;
    private ArrayList<Integer> backwardMapping;
    private int currentWindowPosition;

    public Rotor(int id, int notchIndex, ArrayList<Integer> forwardMapping, ArrayList<Integer> backwardMapping, int currentWindowPosition){
        this.id = id;
        this.notchIndex = notchIndex;
        this.forwardMapping= forwardMapping;
        this.backwardMapping= backwardMapping;
        this.currentWindowPosition = currentWindowPosition;
    }


    public int getMatchForward ( int inputIndex ) {

        int original_inputIndex= (inputIndex + offset) %  alphabetLength;
        int original_outputIndex= forwardMapping.get(original_inputIndex);
        return (original_outputIndex - offset) %  alphabetLength;
    }


    public int getMatchBackward ( int inputIndex ) {

        int current_inputIndex= (inputIndex + offset) %  alphabetLength;
        int original_charIndex= backwardMapping.get(current_inputIndex);
        return (original_charIndex - offset) %  alphabetLength;
    }
}
