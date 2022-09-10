package header;

public enum Skin {
    DEFAULT, DARK;

    public String skinName() {

        switch (this) {
            case DEFAULT:
                return "default";
            case DARK:
                return "dark";
            default:
                return "default";
        }

    }
}
