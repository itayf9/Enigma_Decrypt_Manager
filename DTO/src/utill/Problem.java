package utill;

public enum Problem {
    // input rotors problems
    ROTOR_INPUT_NOT_ENOUGH_ELEMENTS, ROTOR_INPUT_TOO_MANY_ELEMENTS,
    ROTOR_INPUT_OUT_OF_RANGE_ID, NOT_IN_ALPHABET, NO_CONFIGURATION,
    ROTOR_DUPLICATION, ROTOR_VALIDATE_EMPTY_STRING,
    ROTOR_VALIDATE_NO_SEPERATOR,

    // input windows chars problems
    WINDOW_INPUT_TOO_MANY_LETTERS, WINDOW_INPUT_TOO_FEW_LETTERS,

    // input reflector problems
    REFLECTOR_INPUT_OUT_OF_RANGE_ID,

    // input plugs problems
    SELF_PLUGGING, ALREADY_PLUGGED,

    // XML file problems
    FILE_NOT_FOUND, JAXB_ERROR ,
    FILE_NOT_IN_FORMAT,

    // xml file Alphabet problems
    ODD_ALPHABET_AMOUNT,

    //  xml file rotors problems
    NOT_ENOUGH_ROTORS, ROTORS_COUNT_BELOW_TWO,
    ROTOR_MAPPING_NOT_IN_ALPHABET, ROTOR_MAPPING_NOT_A_LETTER,
    ROTOR_INVALID_ID_RANGE, OUT_OF_RANGE_NOTCH,

    //  xml file reflectors problems
    NUM_OF_REFLECTS_IS_NOT_HALF_OF_ABC , FILE_REFLECTOR_INVALID_ID_RANGE, REFLECTOR_SELF_MAPPING, REFLECTOR_ID_DUPLICATIONS,
    TOO_MANY_REFLECTORS, REFLECTOR_OUT_OF_RANGE_ID,

    // input cipher text problems
    CIPHER_INPUT_NOT_IN_ALPHABET,


    UNKNOWN, NO_PROBLEM;

    public void printMe() {
        System.out.println(this.name());
    }

}
