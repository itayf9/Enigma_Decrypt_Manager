package dto;

import javafx.util.Pair;
import utill.Problem;

import java.util.List;

public class DTOspecs extends DTOstatus {
    private int availableRotorsCount;
    private int inUseRotorsCount;
    private List<Integer> notchDistancesToWindow;
    private int availableReflectorsCount;
    private int cipheredTextsCount;

    private List<Integer> inUseRotorsIDs;
    private String windowsCharacters;
    private String inUseReflectorSymbol;
    private String inUsePlugs;

    public DTOspecs(boolean isSucceeded, Problem details, int availableRotorsCount, int inUseRotorsCount,
                    List<Integer> notchDistancesToWindow, int availableReflectorsCount, int cipheredTextsCount,
                    List<Integer> inUseRotorsIDs, String windowsCharacters, String inUseReflectorSymbol,
                    String inUsePlugs) {
        super(isSucceeded, details);
        this.availableRotorsCount = availableRotorsCount;
        this.inUseRotorsCount = inUseRotorsCount;
        this.notchDistancesToWindow = notchDistancesToWindow;
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

    public List<Integer> getNotchDistancesToWindow() {
        return notchDistancesToWindow;
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

    public String getWindowsCharacters() {
        return windowsCharacters;
    }

    public String getInUseReflectorSymbol() {
        return inUseReflectorSymbol;
    }

    public String getInUsePlugs() {
        return inUsePlugs;
    }

    @Override
    public String toString() {
        return "DTOspecs{" +
                "availableRotorsCount=" + availableRotorsCount +
                ", inUseRotorsCount=" + inUseRotorsCount +
                ", notchDistancesToWindow=" + notchDistancesToWindow +
                ", availableReflectorsCount=" + availableReflectorsCount +
                ", cipheredTextsCount=" + cipheredTextsCount +
                ", inUseRotorsIDs=" + inUseRotorsIDs +
                ", windowsCharacters=" + windowsCharacters +
                ", inUseReflectorSymbol='" + inUseReflectorSymbol + '\'' +
                ", inUsePlugs=" + inUsePlugs +
                '}';
    }
}
