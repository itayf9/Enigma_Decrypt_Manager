package dto;

import java.util.Set;

public class DTOdictionary extends DTOstatus {

    private Set<String> dictionaryWords;

    public DTOdictionary(Set<String> dictionaryWords) {
        this.dictionaryWords = dictionaryWords;
    }

    /**
     * getter of dictionary words
     *
     * @return the Set of words contains in the dictionary
     */
    public Set<String> getDictionary() {
        return dictionaryWords;
    }
}

