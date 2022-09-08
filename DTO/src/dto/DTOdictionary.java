package dto;

import java.util.Set;

public class DTOdictionary extends DTOstatus{

    private Set<String> dictionaryWords;

    public DTOdictionary(Set<String> dictionaryWords) {this.dictionaryWords = dictionaryWords;}
}

