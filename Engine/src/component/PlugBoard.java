package component;

import java.util.Map;

public class PlugBoard {

    private Map<Integer, Integer> plugMap;

    public PlugBoard() {
    }

    public boolean isPlugged(int keyIndex){
        return plugMap.containsKey(keyIndex);
    }

    public int getMatch(int keyIndex) {
        return plugMap.get(keyIndex);
    }
}
