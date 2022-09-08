package dm.dictionary;

import java.util.HashSet;
import java.util.Set;

public class Dictionary {

    private Set<String> words;
    private String excludeChars;

    public Dictionary(String wordsAsString, String excludeChars) {
        this.excludeChars = excludeChars;
        this.words = new HashSet<>();
        initializeDictionary(wordsAsString);
    }

    public void initializeDictionary(String wordsAsString) {

        StringBuilder wordsBuilder = new StringBuilder();

        // excludes all the excludedChars
        for (Character regularCharacter : wordsAsString.toCharArray()) {
            if (!excludeChars.contains(regularCharacter.toString())) {
                wordsBuilder.append(regularCharacter);
            }
        }

        String wordsWithoutExcludeChars = wordsBuilder.toString();

        // assuming no exclude characters here !!
        String[] dictionaryWords = wordsWithoutExcludeChars.split(" ");

        // inserts the words into a set
        // deletes duplications
        for (String word : dictionaryWords) {
            this.words.add(word);
        }

        System.out.println(words);
    }

    public boolean isAllWordsInDictionary(String text) {
        boolean res = true;
        String[] textWords = text.split(" ");

        for (String word : textWords) {
            if (!words.contains(word)) {
                res = false;
                break;
            }
        }
        return res;
    }

}
