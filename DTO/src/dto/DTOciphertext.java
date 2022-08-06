package dto;

public class DTOciphertext {
    private boolean isSucceed;
    private String details;

    public DTOciphertext(boolean isSucceed, String details) {
        this.isSucceed = isSucceed;
        this.details = details;
    }

    public boolean isSucceed() {
        return isSucceed;
    }

    public String getDetails() {
        return details;
    }
}
