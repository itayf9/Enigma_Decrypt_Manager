package dto;

public class DTOsecretConfig {
    private String rotors;
    private String windows;
    private String reflector;
    private String plugs;

    public DTOsecretConfig(String rotors, String windows, String reflector, String plugs) {
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
