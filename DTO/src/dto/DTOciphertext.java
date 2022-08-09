package dto;

import utill.Problem;

public class DTOciphertext extends DTOstatus {

    String ciperedText;

    public DTOciphertext(boolean isSucceed, Problem details, String ciperedText) {

        super(isSucceed, details);
        this.ciperedText= ciperedText;

    }

    public String getCiperedText() {
        return ciperedText;
    }
}
