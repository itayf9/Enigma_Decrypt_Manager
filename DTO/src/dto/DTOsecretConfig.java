package dto;

import java.util.List;

public class DTOsecretConfig {
    private List<Integer> rotors;
    private String windows;
    private String reflectorSymbol;
    private String plugs;

    public DTOsecretConfig(List<Integer> rotors, String windows, String reflectorSymbol, String plugs) {
        this.rotors = rotors;
        this.windows = windows;
        this.reflectorSymbol = reflectorSymbol;
        this.plugs = plugs;
    }

    public String toString() {
        StringBuilder res = new StringBuilder();

        res.append("Configuration: <");

        for (int i = rotors.size() - 1; i >= 0; i--) {
            res.append(rotors.get(i).toString());
            if (i != 0) {
                res.append(",");
            }
        }
        res.append("><");

        for (int i = windows.length() - 1; i >= 0; i--) {
            res.append(windows.charAt(i));
        }
        res.append("><");

        res.append(reflectorSymbol);
        res.append(">");


        if (plugs.length() > 0) {
            res.append("<");
            for (int i = 0; i < plugs.length(); i += 2) {
                res.append(plugs.charAt(i));
                res.append("|");
                res.append(plugs.charAt(i + 1));
                if (i != plugs.length() - 2) {
                    res.append(",");
                }
            }
            res.append(">");
        }
        return res.toString();
    }
}
