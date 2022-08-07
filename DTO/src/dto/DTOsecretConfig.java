package dto;

import java.util.List;

public class DTOsecretConfig {
    private List<Integer> rotors;
    private String windows;
    private int reflector;
    private List<String> plugs;

    public DTOsecretConfig(List<Integer> rotors, String windows, int reflector, List<String> plugs) {
        this.rotors = rotors;
        this.windows = windows;
        this.reflector = reflector;
        this.plugs = plugs;
    }

    public List<Integer> getRotors() {
        return rotors;
    }

    public String getWindows() {
        return windows;
    }

    public int getReflector() {
        return reflector;
    }

    public List<String> getPlugs() {
        return plugs;
    }
}
