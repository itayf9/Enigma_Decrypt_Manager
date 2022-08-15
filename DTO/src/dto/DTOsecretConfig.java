package dto;

import problem.Problem;

import java.util.List;

public class DTOsecretConfig extends DTOstatus {
    private List<Integer> rotors;
    private String windows;
    private String reflectorSymbol;
    private String plugs;
    private List<Integer> notchDistancesToWindow;

    public DTOsecretConfig(boolean isSucceeded, Problem details, List<Integer> rotors, String windows, String reflectorSymbol,
                           String plugs, List<Integer> notchDistancesToWindow) {
        super(isSucceeded, details);
        this.rotors = rotors;
        this.windows = windows;
        this.reflectorSymbol = reflectorSymbol;
        this.plugs = plugs;
        this.notchDistancesToWindow = notchDistancesToWindow;
    }

    public List<Integer> getNotchDistances() {
        return notchDistancesToWindow;
    }

    public List<Integer> getRotors() {
        return rotors;
    }

    public String getWindows() {
        return windows;
    }

    public String getReflectorSymbol() {
        return reflectorSymbol;
    }

    public String getPlugs() {
        return plugs;
    }

    @Override
    public String toString() {
        return "DTOsecretConfig{" +
                "rotors=" + rotors +
                ", windows='" + windows + '\'' +
                ", reflectorSymbol='" + reflectorSymbol + '\'' +
                ", plugs='" + plugs + '\'' +
                ", notchDistancesToWindow=" + notchDistancesToWindow +
                '}';
    }
}