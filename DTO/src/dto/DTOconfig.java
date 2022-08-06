package dto;

public class DTOconfig extends DTO {
    private boolean isRotorsOK;
    private boolean isWidowsOK;
    private boolean isReflectorOK;
    private boolean isPlugsOK;
    private String rotorsProblem;
    private String windowsProblem;
    private String reflectorProblem;
    private String plugsProblem;

    public DTOconfig(boolean isSucceed, String details, boolean isRotorsOK, boolean isWidowsOK, boolean isReflectorOK, boolean isPlugsOK,
                     String rotorsProblem, String windowsProblem, String reflectorProblem, String plugsProblem) {
        super(isSucceed, details);
        this.isRotorsOK = isRotorsOK;
        this.isWidowsOK = isWidowsOK;
        this.isReflectorOK = isReflectorOK;
        this.isPlugsOK = isPlugsOK;
        this.rotorsProblem = rotorsProblem;
        this.windowsProblem = windowsProblem;
        this.reflectorProblem = reflectorProblem;
        this.plugsProblem = plugsProblem;
    }



    public String getRotorsProblem() {
        return rotorsProblem;
    }

    public String getWindowsProblem() {
        return windowsProblem;
    }

    public String getReflectorProblem() {
        return reflectorProblem;
    }

    public String getPlugsProblem() {
        return plugsProblem;
    }
}
