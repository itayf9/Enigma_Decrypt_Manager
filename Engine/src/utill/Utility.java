package utill;

public class Utility {

    public static final int NOT_VALID_ROMAN_TO_DECIMAL= -1;
    public static final String NOT_VALID_DECIMAL_TO_ROMAN= "NOT_VALID";

    /**
     * converts a roman number into a decimal number.
     * supports only 1-5.
     * @param roman a string that represents the roman number
     * @return the equivalent decimal number
     */
    public static int romanToDecimal(String roman) {
        int decimal;

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
                decimal= NOT_VALID_ROMAN_TO_DECIMAL;
        }

        return decimal;
    }

    /**
     * converts a decimal number, into a roman number
     * supports only 1-5.
     * @param decimal a decimal number
     * @return equivalent string that represents the roman number
     */
    public static String decimalToRoman(int decimal) {
        String roman= "";


        switch(decimal) {
            case 1:
                roman= "I";
                break;
            case 2:
                roman= "II";
                break;
            case 3:
                roman= "III";
                break;
            case 4:
                roman= "IV";
                break;
            case 5:
                roman= "V";
                break;
            default:
                // throw ?
                roman= NOT_VALID_DECIMAL_TO_ROMAN;
        }

        return roman;
    }
}
