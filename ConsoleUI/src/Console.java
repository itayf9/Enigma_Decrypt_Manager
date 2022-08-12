import dto.*;
import javafx.util.Pair;
import statistics.StatisticRecord;
import utill.Problem;
import utill.Utility;

import java.util.*;

public class Console {
    private static final Engine engine = new EnigmaEngine();

    private static boolean isXmlLoaded = false;
    private static boolean isMachineConfigured = false;

    private static String xmlFileName = "Engine/src/resource/ex1-sanity-paper-enigma.xml";

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        run();
    }

    /**
     * runs the Console UI
     */
    public static void run() {

        boolean isExit = false;
        System.out.println("Welcome to Cracking The Enigma!!\n");

        printMainMenu();
        Operation choice = getInputUserChoice();

        while (!isExit) {
            switch (choice) {
                case READ_SYSTEM_DETAILS_FROM_XML:
                    loadXmlFile();
                    break;
                case DISPLAY_SPECS:
                    if (isXmlLoaded) {
                        displaySpecifications();
                    } else {
                        System.out.println("Please load machine from a file before selecting further operation.");
                    }

                    break;
                case CHOOSE_INIT_CONFIG_MANUAL:
                    if (isXmlLoaded) {
                        chooseConfigManual();
                    } else {
                        System.out.println("Please load machine from a file before selecting further operation.");
                    }
                    break;
                case CHOOSE_INIT_CONFIG_AUTO:
                    if (isXmlLoaded) {
                        chooseConfigAuto();
                    } else {
                        System.out.println("Please load machine from a file before selecting further operation.");
                    }
                    break;
                case PROCESS_INPUT:
                    if (isMachineConfigured) {
                        processInput();
                    } else {
                        System.out.println("Please enter configuration, before ciphering some text.");
                    }

                    break;
                case RESET_CURRENT_CODE:
                    if (isMachineConfigured) {
                        resetConfig();
                    } else {
                        System.out.println("Please enter configuration, before ciphering some text.");
                    }
                    break;
                case HISTORY_AND_STATISTICS:
                    getHistoryAndStats();
                    break;
                case EXIT_SYSTEM:
                    isExit = true;
                    continue;
                default:
                    break;
            }
            printMainMenu();
            choice = getInputUserChoice();
        }
    }

    /**
     * the menu operations
     */
    public static void printMainMenu() {
        System.out.println("\nSelect an operation:");
        System.out.println("1 - Load System Details From File.\n" +
                "2 - Display Enigma Machine Specifications.\n" +
                "3 - Set Configuration - Manually.\n" +
                "4 - Set Configuration - Automatically.\n" +
                "5 - Cipher.\n" +
                "6 - Reset Configuration.\n" +
                "7 - History And Statistics.\n" +
                "8 - Exit.\n");
    }

    /**
     * getting user choice from the menu
     *
     * @return Enum operation
     */
    public static Operation getInputUserChoice() {

        boolean isValid = false;

        String choice = scanner.nextLine().toUpperCase();

        while (!isValid) {
            if (choice.length() == 0){
                System.out.println("No input was given. Please enter your choice (select 1 - 8).");
            }
            else if (choice.length() > 1) {
                System.out.println("Invalid input - please enter only one option number! ");

            } else if (choice.charAt(0) < '1' || choice.charAt(0) > '8') {
                System.out.println("Invalid option - try again ! with options from 1-8");
            } else {
                isValid = true;
            }



            if (!isValid){
                choice = scanner.nextLine().toUpperCase();
            }
        }

        return Operation.getOperation(choice.charAt(0));
    }

    /**
     * get rotors ids from the user
     *
     * @param numberOfIntegers the rotors count
     * @return list of integers representing the rotor's id's
     */
    public static String getInputStringOfIntegers(int numberOfIntegers) {
        boolean isValid = false;
        String StringOfChoices;

        System.out.println("There is a place for " + numberOfIntegers + " rotors in this machine.\n" +
                "Enter the rotors' ID's with \",\" between each rotor, counting from the left most rotor to the right most rotor.\n");
        System.out.println("e.g - 1,2");
        StringOfChoices = scanner.nextLine();

        while (!isValid) {
            if (StringOfChoices.length() != 0) {
                isValid = true;
                continue;
            }
            System.out.println("Error! Cant be 0 Rotors. Try Again.");
            StringOfChoices = scanner.nextLine();
        }

        return StringOfChoices;
    }

    /**
     * get alphabet characters from the user
     *
     * @return String of characters
     */
    private static String getInputSequenceOfCharacters() {

        boolean isValid = false;
        String stringOfChoices;

        System.out.println("Enter the rotors' window characters (the characters that will appear at the window for each rotor),\ncounting from the left most rotor to the right most rotor.\n");
        System.out.println("e.g - ABC");

        stringOfChoices = scanner.nextLine().toUpperCase();

        while (!isValid) {
            if (stringOfChoices.length() != 0) {
                isValid = true;
                continue;
            }
            System.out.println("Error! Cant be 0 Rotors. Try Again.");
            stringOfChoices = scanner.nextLine().toUpperCase();
        }

        return stringOfChoices;

    }

    /**
     * get reflector input from user.
     *
     * @return integer representing reflector id.
     */
    private static int getInputInteger() {
        boolean isValid = false;

        System.out.println("Enter the number of the wanted reflector (select 1-5).");
        System.out.println("1. I\n2. II\n3. III\n4. IV\n5. V\n");
        String choice = scanner.nextLine().toUpperCase();

        while (!isValid) {
            if (choice.length() > 1) {
                System.out.println("Invalid input. enter only the number that matches you choice.");
                choice = scanner.nextLine().toUpperCase();
            } else if (choice.charAt(0) < '1' || choice.charAt(0) > '5') {
                System.out.println("Invalid option - try again ! with options from 1-5");
                choice = scanner.nextLine().toUpperCase();
            } else {
                isValid = true;
            }
        }
        return Integer.parseInt(choice);
    }

    /**
     * get the plugs from the user.
     *
     * @return a list of strings, each string contains 2 characters representing a Plug.
     */
    private static String getInputListOfStrings() {
        boolean isValid = false;
        String plugs = "";

        while (!isValid) {
            System.out.println("Please enter your choice plugs. e.g - ABED");
            plugs = scanner.nextLine().toUpperCase();
            isValid = true;
        }

        return plugs;

    }

    /**
     * get text to cipher from user
     *
     * @return string of text.
     */
    private static String getInputCipherText() {

        boolean isValid = false;
        String cipherText = scanner.nextLine().toUpperCase();

        while (!isValid) {
            if (cipherText.length() == 0) {
                System.out.println("Error! No input was given.");
                cipherText = scanner.nextLine().toUpperCase();
            } else {
                isValid = true;
            }
        }

        return cipherText;
    }

    /**
     * get fileName from user.
     *
     * @return string represents the file name.
     */
    private static String getXMLFileName() {
        System.out.println("Please enter full path of file name to load machine from (XML file).");
        String res = scanner.nextLine();
        while (!res.endsWith(".xml")) {
            System.out.println("Please enter full path of file name to load machine from (XML file).");
            res = scanner.nextLine();
        }
        System.out.println("All OK");
        return res;
    }

    /**
     * Q1 - loads xml file and builds the enigma machine.
     */

    static private void loadXmlFile() {
        //String xmlFileName = getXMLFileName();
        DTOstatus buildMachineFromXMLStatus = engine.buildMachineFromXmlFile(xmlFileName);

        if (!buildMachineFromXMLStatus.isSucceed()) {
            displayMessage(buildMachineFromXMLStatus.getDetails());
        } else {
            isXmlLoaded = true;
            System.out.println("The machine was built successfully!");
        }

    }

    /**
     * Q2 - display the machine specifications.
     */
    static private void displaySpecifications() {
        DTOspecs specsStatus = engine.displayMachineSpecifications();
        if (specsStatus.isSucceed()) {
            printSpecifications(specsStatus);
        }
    }

    /**
     * Q3 - choose new config manually.
     */
    static private void chooseConfigManual() {
        int rotorCount = engine.getRotorsCount();
        String rotorsIDs = getInputStringOfIntegers(rotorCount);
        DTOstatus rotorsIDsStatus = engine.validateRotors(rotorsIDs);
        while (!rotorsIDsStatus.isSucceed()) {
            displayMessage(rotorsIDsStatus.getDetails());
            rotorsIDs = getInputStringOfIntegers(rotorCount);
            rotorsIDsStatus = engine.validateRotors(rotorsIDs);
        }
        System.out.println("All good.\n");

        String windows = getInputSequenceOfCharacters();
        DTOstatus windowCharactersStatus = engine.validateWindowCharacters(windows);
        while (!windowCharactersStatus.isSucceed()) {
            displayMessage(windowCharactersStatus.getDetails());
            windows = getInputSequenceOfCharacters();
            windowCharactersStatus = engine.validateWindowCharacters(windows);
        }
        System.out.println("All good.\n");

        int reflectorID = getInputInteger();
        DTOstatus reflectorIDStatus = engine.validateReflector(reflectorID);
        while (!reflectorIDStatus.isSucceed()) {
            displayMessage(reflectorIDStatus.getDetails());
            reflectorID = getInputInteger();
            reflectorIDStatus = engine.validateReflector(reflectorID);
        }
        System.out.println("All good.\n");

        String plugs = getInputListOfStrings();
        DTOstatus plugsStatus = engine.validatePlugs(plugs);
        while (!plugsStatus.isSucceed()) {
            displayMessage(plugsStatus.getDetails());
            plugs = getInputListOfStrings();
            plugsStatus = engine.validatePlugs(plugs);
        }
        System.out.println("All good.\n");

        DTOstatus configStatus = engine.selectConfigurationManual(rotorsIDs, windows, reflectorID, plugs);
        if (configStatus.isSucceed()) {
            System.out.println("Machine has been configured successfully.");
            isMachineConfigured = true;
        }
    }

    /**
     * Q4 - choose new config automatically.
     */
    static private void chooseConfigAuto() {
        DTOsecretConfig configStatus = engine.selectConfigurationAuto();
        isMachineConfigured = true;

//        List<Character> convertedWindowsChars = new ArrayList<>();
//
//        for(Character windowChar : configStatus.getWindows().toCharArray()) {
//            convertedWindowsChars.add(windowChar);
//        }
//
//        List<Pair<Character, Character>> convertedPlugs = new ArrayList<>();
//        for (int i = 0; i < configStatus.getPlugs().length(); i+=2) {
//            convertedPlugs.add(new Pair<>(configStatus.getPlugs().charAt(i), configStatus.getPlugs().charAt(i+1)));
//        }

        printConfiguration(configStatus.getRotors(), configStatus.getWindows(),
                configStatus.getReflectorSymbol(), configStatus.getPlugs(), configStatus.getNotchDistances());
    }

    /**
     * Q5 - gets input from user and send it to machine to cipher.
     */
    static private void processInput() {
        System.out.println("Please enter text to cipher: ");
        String inputText = getInputCipherText();
        DTOciphertext cipherStatus = engine.cipherInputText(inputText);
        while (!cipherStatus.isSucceed()) {

            switch (cipherStatus.getDetails()) {
                case NOT_IN_ALPHABET:
                    System.out.println("Error! One or more of the characters in this text is not in the alphabet.\n" +
                            "Please enter text that contains only letters from the alphabet.");
                    break;
                case UNKNOWN:
                    System.out.println("Unknown Error.");
                    break;
            }

            inputText = getInputCipherText();
            cipherStatus = engine.cipherInputText(inputText);
        }

        System.out.println("_____________________________");
        System.out.println(cipherStatus.getCipheredText());
        System.out.println("_____________________________");


    }

    /**
     * Q6 - reset machine config to it's original last config.
     */
    static private void resetConfig() {
        DTOresetConfig resetStatus = engine.resetConfiguration();
        if (resetStatus.isSucceed()) {
            System.out.println("Configuration has reset successfully.");
        }

    }

    /**
     * Q7 - gets history and stats from the machine and display to user.
     */
    static private void getHistoryAndStats() {
        DTOstatistics statisticsStatus = engine.getHistoryAndStatistics();
        if (!statisticsStatus.isSucceed()) {
            displayMessage(statisticsStatus.getDetails());
        } else {
            printStatistics(statisticsStatus);
        }
    }

    public static void displayMessage(Problem problem) {
        switch (problem) {
            case NOT_ENOUGH_ELEMENTS:
                System.out.println();
                break;
            case TOO_MANY_ELEMENTS:
                System.out.println();
                break;
            case OUT_OF_RANGE_ID:
                System.out.println();
                break;
            case NOT_IN_ALPHABET:
                System.out.println();
                break;
            case NO_CONFIGURATION:
                System.out.println();
                break;
            case SELF_PLUGGING:
                System.out.println();
                break;
            case ALREADY_PLUGGED:
                System.out.println();
                break;
            case FILE_NOT_FOUND:
                System.out.println();
                break;
            case JAXB_ERROR:
                System.out.println();
                break;
            case FILE_NOT_IN_FORMAT:
                System.out.println();
                break;
            case ODD_ALPHABET_AMOUNT:
                System.out.println();
                break;
            case NOT_ENOUGH_ROTORS:
                System.out.println();
                break;
            case ROTORS_COUNT_BELOW_TWO:
                System.out.println();
                break;
            case ROTOR_MAPPING_NOT_IN_ALPHABET:
                System.out.println();
                break;
            case ROTOR_MAPPING_NOT_A_LETTER:
                System.out.println();
                break;
            case ROTOR_INVALID_ID_RANGE:
                System.out.println();
                break;
            case OUT_OF_RANGE_NOTCH:
                System.out.println();
                break;
            case NUM_OF_REFLECTS_IS_NOT_HALF_OF_ABC:
                System.out.println();
                break;
            case REFLECTOR_INVALID_ID_RANGE:
                System.out.println();
                break;
            case REFLECTOR_SELF_MAPPING:
                System.out.println();
                break;
            case REFLECTOR_ID_DUPLICATIONS:
                System.out.println();
                break;
            case TOO_MANY_REFLECTORS:
                System.out.println();
                break;
            case REFLECTOR_OUT_OF_RANGE_ID:
                System.out.println();
                break;
            case UNKNOWN:
                System.out.println();
                break;
            case NO_PROBLEM:
                System.out.println();
                break;
        }
        problem.printMe();

    }

    public static void printSpecifications (DTOspecs dtoSpecs){
        StringBuilder strSpecs = new StringBuilder();

        strSpecs.append("Specifications: \n")
                .append(" - Number Of Rotors (In Use / Available): ")
                .append(dtoSpecs.getInUseRotorsCount())
                .append(" / ")
                .append(dtoSpecs.getAvailableRotorsCount())
                .append('\n')
                .append(" - Number Of Reflectors (Available): ")
                .append(dtoSpecs.getAvailableReflectorsCount())
                .append('\n')
                .append(" - Number Of Texts That Were Ciphered So Far: ")
                .append(dtoSpecs.getCipheredTextsCount())
                .append('\n');

        System.out.println(strSpecs.toString());

        if (dtoSpecs.getDetails().equals(Problem.NO_CONFIGURATION)){
            System.out.println(" - No configuration has been chosen yet.");
        } else {
            System.out.print(" - Current Configuration: ");
            printConfiguration(dtoSpecs.getInUseRotorsIDs(), dtoSpecs.getWindowsCharacters(), dtoSpecs.getInUseReflectorSymbol(), dtoSpecs.getInUsePlugs(), dtoSpecs.getNotchDistancesToWindow());
        }

    }

    public static void printConfiguration (List<Integer> inUseRotorsIDs, String windowsCharacters, String inUseReflectorSymbol, String inUsePlugs, List<Integer> notchDistancesToWindow) {
        StringBuilder strConfig = new StringBuilder();

        // prints rotors' configuration
        // including the in use rotors' IDs, their order, and the current notch distances from the windows
        strConfig.append("<");
        for (int i = inUseRotorsIDs.size() - 1; i >= 0; i--) {
            strConfig.append(inUseRotorsIDs.get(i).toString());
            if (notchDistancesToWindow.size() > 0){
                strConfig.append("(")
                        .append(notchDistancesToWindow.get(i).toString())
                        .append(")");
            }
            if (i != 0) {
                strConfig.append(",");
            }
        }
        strConfig.append(">");

        // prints windows characters configuration
        strConfig.append("<");
        for (int i = windowsCharacters.length() - 1; i >= 0; i--) {
            strConfig.append(windowsCharacters.charAt(i));
        }
        strConfig.append(">");

        // prints reflector configuration
        strConfig.append("<")
                .append(inUseReflectorSymbol)
                .append(">");

        // prints plugs configuration
        if (inUsePlugs.length() > 0) {
            strConfig.append("<");
            for (int i = 0; i < inUsePlugs.length(); i+=2) {
                strConfig.append(inUsePlugs.charAt(i))
                        .append("|")
                        .append(inUsePlugs.charAt(i+1));
                if (i != inUsePlugs.length() - 2) {
                    strConfig.append(",");
                }
            }
            strConfig.append(">");
        }

        System.out.println(strConfig.toString());
    }

    public static void printStatistics (DTOstatistics stats) {

        // get notch distances from windows

        for (StatisticRecord record : stats.getStats()) {

            StringBuilder cipherRecordStr = new StringBuilder();

            printConfiguration(record.getInUseRotors(), record.getWindowCharacters(),
                    Utility.decimalToRoman(record.getReflectorID()), record.getPlugs(), new ArrayList<>());

            for (Pair<String, String> currentCipherRecord : record.getCipherHistory()){
                cipherRecordStr.append("#. ")
                        .append("<")
                        .append(currentCipherRecord.getKey())
                        .append(">").append(" --> ").append("<")
                        .append(currentCipherRecord.getValue()).append(">").append("\n");
            }

            System.out.println(cipherRecordStr.toString());
        }
    }
}