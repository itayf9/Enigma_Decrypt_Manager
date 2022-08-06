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

    public String getRotors() {
        return rotors;
    }

    public String getWindows() {
        return windows;
    }

    public String getReflector() {
        return reflector;
    }

    public String getPlugs() {
        return plugs;
    }
}
