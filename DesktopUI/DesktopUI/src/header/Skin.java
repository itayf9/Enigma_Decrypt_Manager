package header;

import java.util.ArrayList;
import java.util.List;

public enum Skin {
    DEFAULT, DARK, SPECIAL;

    public String skinName() {

        switch (this) {
            case DEFAULT:
                return "default";
            case DARK:
                return "dark";
            case SPECIAL:
                return "special";
            default:
                return "default";
        }

    }

    public static List<String> getAllPossibleSkinNames() {
        List<String> allSkinNames = new ArrayList<>();
        allSkinNames.add(DEFAULT.skinName());
        allSkinNames.add(DARK.skinName());
        allSkinNames.add(SPECIAL.skinName());

        return allSkinNames;
    }
}
