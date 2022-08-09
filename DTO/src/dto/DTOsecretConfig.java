package dto;

import java.util.List;

public class DTOsecretConfig {
    private List<Integer> rotors;
    private String windows;
    private int reflector;
    private List<String> plugs;

    public DTOsecretConfig(List<Integer> rotors, String windows, String reflectorSymbol, List<String> plugs) {
        this.rotors = rotors;
        this.windows = windows;
        this.reflectorSymbol = reflectorSymbol;
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

    public String toString() {
        StringBuilder res = new StringBuilder();

        res.append("Configuration: <");

        for (int i = rotors.size()-1; i >= 0 ; i--) {
            res.append(rotors.get(i).toString());
            if (i != 0) {
                res.append(",");
            }
        }
        res.append("><");

        for (int i = windows.length()-1; i >=0 ; i--) {
            res.append(windows.charAt(i));
        }
        res.append("><");

        res.append(reflector);
        res.append(">");


        if(plugs.size() > 0) {
            res.append("<");
            for (int i = 0; i < plugs.size(); i++) {
                res.append(plugs.get(i).charAt(0));
                res.append("|");
                res.append(plugs.get(i).charAt(1));
                if (i != plugs.size()-1) {
                    res.append(",");
                }
            }
            res.append(">");
        }
        return res.toString();
    }
}
