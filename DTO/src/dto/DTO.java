package dto;

public class DTO {

    private boolean isSucceed;
    private String details;

    public DTO() {
    }

    public DTO(boolean isSucceed, String details) {
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