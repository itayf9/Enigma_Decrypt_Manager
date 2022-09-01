package dm.dictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Dictionary {

    private Set<String> words;
    private String excludeChars;

    public Dictionary(String wordsAsString, String excludeChars) {
        this.excludeChars = excludeChars;
        initializeDictionary(wordsAsString);
    }

    public void initializeDictionary(String wordsAsString) {

        // excludes all the excludedChars
        for (Character excludeChar : excludeChars.toCharArray()) {
            wordsAsString.replaceAll(excludeChar.toString(), "");
        }

        // assuming no exclude characters here !!
        String[] dictionaryWords = wordsAsString.split(",");

        // inserts the words into a set
        // deletes duplications
        for (String word : dictionaryWords) {
            this.words.add(word);
        }
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
