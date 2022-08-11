package dto;

import utill.Problem;

public class DTOciphertext extends DTOstatus {

    String cipheredText;

    public DTOciphertext(boolean isSucceed, Problem details, String cipheredText) {
        super(isSucceed, details);
        this.cipheredText = cipheredText;
    }

    public String getCipheredText() {
        return cipheredText;
    }
}
