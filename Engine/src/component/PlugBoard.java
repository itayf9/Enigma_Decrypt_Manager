package component;

import dto.CharacterPair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlugBoard {

    private Map<Integer, Integer> plugMap;

    public Map<Integer, Integer> getPlugMap() {
        return plugMap;
    }

    public PlugBoard() {
        this.plugMap = new HashMap<>();
    }

    public boolean isPlugged(int keyIndex){
        return plugMap.containsKey(keyIndex);
    }

    public int getMatch(int keyIndex) {
        return plugMap.get(keyIndex);
    }


    public void initPlugBoardMap(Map<Character, Integer> character2index, String plugs){

        if (plugs.length() == 0)
        {
            return;
        }
        // the 'plugs' String contains info in the format => "A|Z,D|E"
        String[] seperatedPlugs = plugs.split(",");
        // after split, seperates 'plugs' to pairs => ["A|Z" , "D|E"]

        String[] currentPlugCharacters;

        for ( String currentPlug : seperatedPlugs ){
            currentPlugCharacters = currentPlug.split("\\|");
            // after split, seperates 'currentPlug' to pairs => ["A" , "Z"]
            // now converts the characters to index int.
            int currentPlug1 = character2index.get( currentPlugCharacters[0].charAt(0) );
            int currentPlug2 = character2index.get( currentPlugCharacters[1].charAt(0) );
            // add new plug to the plug board.
            plugMap.put(currentPlug1, currentPlug2);
            plugMap.put(currentPlug2, currentPlug1);
        }
    }

    @Override
    public String toString() {
        return "PlugBoard{" +
                "plugMap=" + plugMap +
                '}';
    }

}
