package utill;

public class Utility {
    public static int romanToDecimal(String roman) {
        int decimal;
        final int NOT_VALID= -1;

        switch(roman) {
            case "I":
                decimal= 1;
                break;
            case "II":
                decimal= 2;
                break;
            case "III":
                decimal= 3;
                break;
            case "IV":
                decimal= 4;
                break;
            case "V":
                decimal= 5;
                break;
            default:
                // throw ?
                decimal= NOT_VALID;
        }

        return decimal;
    }
}
