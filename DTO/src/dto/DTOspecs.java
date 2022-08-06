package dto;

import java.util.List;

public class DTOspecs extends DTO {
    private int availableRotorsCount;
    private int inUseRotorsCount;
    private List<Integer> notchPositions;
    private int availableReflectorsCount;
    private int cipheredTextsCount;

    private List<Integer> inUseRotorsIDs;
    private List<Character> windowsCharacters;
    private String inUseReflectorSymbol;
    private List<CharacterPair> inUsePlugs;

    public DTOspecs(int availableRotorsCount, int inUseRotorsCount, List<Integer> notchPositions,
                    int availableReflectorsCount, int cipheredTextsCount, List<Integer> inUseRotorsIDs,
                    List<Character> windowsCharacters, String inUseReflectorSymbol, List<CharacterPair> inUsePlugs) {
        super();
        this.availableRotorsCount = availableRotorsCount;
        this.inUseRotorsCount = inUseRotorsCount;
        this.notchPositions = notchPositions;
        this.availableReflectorsCount = availableReflectorsCount;
        this.cipheredTextsCount = cipheredTextsCount;
        this.inUseRotorsIDs = inUseRotorsIDs;
        this.windowsCharacters = windowsCharacters;
        this.inUseReflectorSymbol = inUseReflectorSymbol;
        this.inUsePlugs = inUsePlugs;
    }

    public int getAvailableRotorsCount() {
        return availableRotorsCount;
    }

    public int getInUseRotorsCount() {
        return inUseRotorsCount;
    }

    public List<Integer> getNotchPositions() {
        return notchPositions;
    }

    public int getAvailableReflectorsCount() {
        return availableReflectorsCount;
    }

    public int getCipheredTextsCount() {
        return cipheredTextsCount;
    }

    public List<Integer> getInUseRotorsIDs() {
        return inUseRotorsIDs;
    }

    public List<Character> getWindowsCharacters() {
        return windowsCharacters;
    }

    public String getInUseReflectorSymbol() {
        return inUseReflectorSymbol;
    }

    public List<CharacterPair> getInUsePlugs() {
        return inUsePlugs;
    }

    @Override
    public String toString() {
        return "DTOspecs{" + '\n' +
                "availableRotorsCount=" + availableRotorsCount + '\n' +
                ", inUseRotorsCount=" + inUseRotorsCount + '\n' +
                ", notchPositions=" + notchPositions + '\n' +
                ", availableReflectorsCount=" + availableReflectorsCount + '\n' +
                ", cipheredTextsCount=" + cipheredTextsCount + '\n' +
                ", inUseRotorsIDs=" + inUseRotorsIDs + '\n' +
                ", windowsCharacters=" + windowsCharacters + '\n' +
                ", inUseReflectorSymbol='" + inUseReflectorSymbol + '\'' + '\n' +
                ", inUsePlugs=" + inUsePlugs + '\n' +
                '}';
    }
}
