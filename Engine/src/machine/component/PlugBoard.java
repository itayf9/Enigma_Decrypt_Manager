package machine.component;

import dto.CharacterPair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlugBoard {

    private Map<Integer, Integer> plugMap;


    /**
     * constructor for PlugBoard
     * creates an empty plugMap
     */
    public PlugBoard() {
        this.plugMap = new HashMap<>();
    }

    /**
     *
     * @return a map of indices that represents the pairs of letters that are currently plugged
     */
    public Map<Integer, Integer> getPlugMap() {
        return plugMap;
    }

    /**
     * finds out if a letter has a plug in the plug board
     * @param keyIndex index that represents a letter to be checked
     * @return 'true' if the letter is currently plugged, 'false' otherwise
     */
    public boolean isPlugged(int keyIndex){
        return plugMap.containsKey(keyIndex);
    }


    /**
     * finds out the value of the matching letter, which is plugged to the given letter
     * assuming the pair exists in the plug board
     * @param keyIndex index that represents a letter to be checked
     * @return index that represents the matching pair
     */
    public int getMatch(int keyIndex) {
        return plugMap.get(keyIndex);
    }


    /**
     * initializes the plug board mapping
     * @param character2index a map that translates each letter in the alphabet, to an index
     * @param plugs a string that represents the plug pairs.
     *              each pair is seperated with ','
     *              a pair is divided with '|'
     *              for example, "A|Z,D|E" means that there are two pairs.
     *              first pair : A and Z. second pair : D and E.
     */
    public void initPlugBoardMap(Map<Character, Integer> character2index, List<String> plugs){
/*
        if (plugs.size() == 0)
        {
            return;
        }
        // the 'plugs' String contains info in the format => "A|Z,D|E"
        //String[] seperatedPlugs = plugs.split(",");
         after split, seperates 'plugs' to pairs => ["A|Z" , "D|E"]

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
        }*/
        // run through all plugs
        for (String plug : plugs) {

            // convert char to index
            int firstInPlugIndex = character2index.get(plug.charAt(0));
            int secondInPlugIndex = character2index.get(plug.charAt(1));

            // build  plug map
            plugMap.put(secondInPlugIndex, firstInPlugIndex);
            plugMap.put(firstInPlugIndex, secondInPlugIndex);
        }
    }

    @Override
    public String toString() {
        return "PlugBoard{" +
                "plugMap=" + plugMap +
                '}';
    }

}
