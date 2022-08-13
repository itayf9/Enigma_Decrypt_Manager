import dto.*;
import javafx.util.Pair;
import statistics.StatisticRecord;
import problem.Problem;
import utill.Utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.regex.Pattern;

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
                        System.out.println("Please enter configuration, before resetting it.");
                    }
                    break;
                case HISTORY_AND_STATISTICS:
                    if (isXmlLoaded) {
                        getHistoryAndStats();
                    } else {
                        System.out.println("Please load machine from a file before checking for statistics.");
                    }

                    break;
                case EXIT_SYSTEM:
                    isExit = true;
                    continue;
                case READ_EXISTING_MACHINE_FROM_FILE:
                    isXmlLoaded = true;
                    loadExistingMachineFromFile();
                    break;
                case WRITE_EXISTING_MACHINE_TO_FILE:
                    saveExistingMachineToFile();
                    break;
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
        System.out.println("1  -  Load System Details From File.\n" +
                "2  -  Display Enigma Machine Specifications.\n" +
                "3  -  Set Configuration - Manually.\n" +
                "4  -  Set Configuration - Automatically.\n" +
                "5  -  Cipher.\n" +
                "6  -  Reset Configuration.\n" +
                "7  -  History And Statistics.\n" +
                "8  -  Exit.\n" +
                "9  -  Load an existing machine.\n" +
                "10 -  Save an existing machine.");
    }

    /**
     * getting the user's choice from the menu
     *
     * @return Operation that represents the user's choice
     */
    public static Operation getInputUserChoice() {

        int userChoiceNum;

        try {
            String userChoiceStr = scanner.nextLine().trim();

            while (userChoiceStr.length() == 0) {
                System.out.println("No input was given. Please enter your choice (select 1 - 10).");
                userChoiceStr = scanner.nextLine().trim();
            }

            userChoiceNum = Integer.parseInt(userChoiceStr);

            if (userChoiceNum < 1 || userChoiceNum > 10) {
                System.out.println("The option you've chosen does not exist.");
                System.out.println("Select an option from 1-10.");
                userChoiceNum = Operation.INVALID_OPERATION;
            }


        } catch (NumberFormatException e) {
            System.out.println("The option you've chosen is not a number.");
            System.out.println("Select an option from 1-10.");
            userChoiceNum = Operation.INVALID_OPERATION;
        }


        return Operation.getOperation(userChoiceNum);
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
        String ESC = ("" + (char) 027);

        System.out.println("There is a place for " + numberOfIntegers + " rotors in this machine.\n" +
                "Enter the rotors' ID's with \",\" between each rotor, counting from the left most rotor to the right most rotor.");
        System.out.println("e.g - 1,2");
        StringOfChoices = scanner.useDelimiter(ESC).nextLine();

        while (!isValid) {
            if (StringOfChoices.contains(ESC)) {
                break;
            } else if (StringOfChoices.length() != 0) {
                isValid = true;
                continue;
            }
            System.out.println("Error! Cant be 0 Rotors. Try Again. or press ESC to go back to main menu.");
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

        System.out.println("Enter the rotors' window characters (the characters that will appear at the window for each rotor),\ncounting from the left most rotor to the right most rotor.");
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
        System.out.println("Please enter the full path of the file to load machine from (XML file).");

        return scanner.nextLine();
    }

    /**
     * Q1 - loads xml file and builds the enigma machine.
     */
    static private void loadXmlFile() {
        // String xmlFileName = getXMLFileName();
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
        if (!cipherStatus.isSucceed()) {
            displayMessage(cipherStatus.getDetails());
        } else {
            System.out.println("-->");
            System.out.println(cipherStatus.getCipheredText());
            System.out.println("-->");
        }

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

    /**
     * display message for the user.
     *
     * @param problem a detailed enum representing a problem at the code.
     */
    public static void displayMessage(Problem problem) {
        switch (problem) {
            case ROTOR_INPUT_NOT_ENOUGH_ELEMENTS:
                System.out.println("You've entered less IDs than expected.");
                break;
            case ROTOR_INPUT_TOO_MANY_ELEMENTS:
                System.out.println("You've entered more IDs than expected.");
                break;
            case ROTOR_INPUT_OUT_OF_RANGE_ID:
                System.out.println("One or more of the IDs you've entered doesn't exist in this machine.");
                break;
            case ROTOR_DUPLICATION:
                System.out.println("You have entered the same rotor twice or more.");
                break;
            case ROTOR_INPUT_HAS_SPACE:
                System.out.println("You have entered a space somewhere in the rotors ids.");
                break;
            case ROTOR_INPUT_NUMBER_FORMAT_EXCEPTION:
                System.out.println("At least one of the IDs you've entered isn't a number.");
                break;
            case ROTOR_VALIDATE_EMPTY_STRING:
                System.out.println("You haven't entered any IDs.");
                break;
            case ROTOR_VALIDATE_NO_SEPERATOR:
                System.out.println("You haven't seperated all the IDs with a \",\".");
                break;
            case NO_CONFIGURATION:
                System.out.println("The machine's configuration hasn't been set yet.");
                break;
            case SELF_PLUGGING:
                System.out.println("One or more of the plugs you've entered is a 'self plug' (that is, a plug where the input and output are the same, e.g AA, DD....).");
                break;
            case ALREADY_PLUGGED:
                System.out.println("Among the plugs you've entered, at least one input leads to multiple outputs, or multiple inputs lead to one output , for example (AB and AC), or (DR and FR).");
                break;
            case PLUGS_INPUT_NOT_IN_ALPHABET:
                System.out.println("Among the plugs you've entered, at least one character is not in the alphabet.");
                break;
            case PLUGS_INPUT_ODD_ALPHABET_AMOUNT:
                System.out.println("You've entered an odd number of characters.");
                break;
            case WINDOW_INPUT_TOO_MANY_LETTERS:
                System.out.println("You've entered more characters than expected.");
                break;
            case WINDOW_INPUT_TOO_FEW_LETTERS:
                System.out.println("You've entered less characters than expected.");
                break;
            case WINDOWS_INPUT_NOT_IN_ALPHABET:
                System.out.println("Among the characters you've entered, at least one character is not in the alphabet.");
                break;
            case REFLECTOR_INPUT_OUT_OF_RANGE_ID:
                System.out.println("The ID you've entered doesn't exist in this machine.");
                break;
            case FILE_NOT_FOUND:
                System.out.println("The files you've requested couldn't be found.");
                break;
            case JAXB_ERROR:
                System.out.println("Internal Problem.");
                break;
            case FILE_NOT_IN_FORMAT:
                System.out.println("The file you've requested isn't in the required format (only .xml files are valid).");
                break;
            case FILE_ODD_ALPHABET_AMOUNT:
                System.out.println("This file is invalid. The amount of the alphabet letters is odd. Only an even number of letters is allowed.");
                break;
            case FILE_NOT_ENOUGH_ROTORS:
                System.out.println("This file is invalid. The machine requires more rotors, than available ones.");
                break;
            case FILE_ROTORS_COUNT_BELOW_TWO:
                System.out.println("This file is invalid. The number of places for rotors inside the machine (referred to \"rotors count\" in the file), is smaller than 2. Machines should have at least 2 rotors.");
                break;
            case FILE_ROTOR_COUNT_HIGHER_THAN_99:
                System.out.println("This file is invalid. The number of rotors count is higher than the machine supports (referred to \"rotors count\" in the file).");
                break;
            case FILE_ROTOR_MAPPING_NOT_IN_ALPHABET:
                System.out.println("This file is invalid. Among the mappings of the rotors, at least one character is not in the alphabet.");
                break;
            case FILE_ROTOR_MAPPING_NOT_EQUAL_TO_ALPHABET_LENGTH:
                System.out.println("This file is invalid. Among the mappings of the rotors, at least one rotor has more or less mappings then the alphabet characters.");
                break;
            case FILE_ROTOR_MAPPING_NOT_A_SINGLE_LETTER:
                System.out.println("This file is invalid. Among the mapping of the rotors, at lease one entry doesn't contains a single letter (e.g, contains more than one letter like \"ABC\" --> \"F\"...).");
                break;
            case FILE_ROTOR_INVALID_ID_RANGE:
                System.out.println("The Rotors at the loaded file aren't maintaining a \"running-counter\" ids from 1.");
                break;
            case FILE_OUT_OF_RANGE_NOTCH:
                System.out.println("There is a at-least one rotor at the file loaded, that has a notch position that is in an unreachable location. ");
                break;
            case FILE_NUM_OF_REFLECTS_IS_NOT_HALF_OF_ABC:
                System.out.println("There is a reflector in the file that is loaded that does not have exactly half the length of the alphabet the amount of reflects.");
                break;
            case FILE_REFLECTOR_INVALID_ID_RANGE:
                System.out.println("There is a reflector in the loaded file that has an ID that is not supported by this machine.");
                break;
            case FILE_REFLECTOR_SELF_MAPPING:
                System.out.println("At-least one reflector has a self mapping row in it.");
                break;
            case FILE_REFLECTOR_ID_DUPLICATIONS:
                System.out.println("There is 2 or more reflectors at the loaded file with the same id.");
                break;
            case FILE_TOO_MANY_REFLECTORS:
                System.out.println("There is more then 5 reflectors at the file that was loaded.");
                break;
            case FILE_REFLECTOR_OUT_OF_RANGE_ID:
                System.out.println("One of the reflectors at the loaded-file is out of possible range of ids.");
                break;
            case CIPHER_INPUT_NOT_IN_ALPHABET:
                System.out.println("At-least one letter isn't on the alphabet on this machine.");
                break;
            case MACHINE_LOAD_FAILED:
                System.out.println("Error while trying to load machine.");
                break;
            case MACHINE_SAVE_FAILED:
                System.out.println("Error while trying to save machine.");
                break;
            case UNKNOWN:
                System.out.println("There is an unknown error.");
                break;
            case NO_PROBLEM:
                System.out.println("All OK.");
                break;
        }
    }

    /**
     * print the dto specs to the user as required
     *
     * @param dtoSpecs the dto containing the specs of the machine.
     */
    public static void printSpecifications(DTOspecs dtoSpecs) {
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

        System.out.println(strSpecs);

        if (dtoSpecs.getDetails().equals(Problem.NO_CONFIGURATION)) {
            System.out.println(" - No configuration has been chosen yet.");
        } else {
            System.out.print(" - Original Configuration: ");
            printConfiguration(dtoSpecs.getInUseRotorsIDs(), dtoSpecs.getWindowsCharacters(), dtoSpecs.getInUseReflectorSymbol(), dtoSpecs.getInUsePlugs(), dtoSpecs.getOriginalNotchPositions());
            System.out.print(" - Current Configuration:  ");
            printConfiguration(dtoSpecs.getInUseRotorsIDs(), dtoSpecs.getWindowsCharacters(), dtoSpecs.getInUseReflectorSymbol(), dtoSpecs.getInUsePlugs(), dtoSpecs.getNotchDistancesToWindow());
        }
    }

    /**
     * print the Secret to the user.
     *
     * @param inUseRotorsIDs         the rotors ids
     * @param windowsCharacters      window characters
     * @param inUseReflectorSymbol   the reflector symbol
     * @param inUsePlugs             the plugs in use
     * @param notchDistancesToWindow the current notch positions
     */
    public static void printConfiguration(List<Integer> inUseRotorsIDs, String windowsCharacters, String inUseReflectorSymbol, String inUsePlugs, List<Integer> notchDistancesToWindow) {
        StringBuilder strConfig = new StringBuilder();

        // prints rotors' configuration
        // including the in use rotors' IDs, their order, and the current notch distances from the windows
        strConfig.append("<");
        for (int i = inUseRotorsIDs.size() - 1; i >= 0; i--) {
            strConfig.append(inUseRotorsIDs.get(i).toString());
            if (notchDistancesToWindow.size() > 0) {
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
            for (int i = 0; i < inUsePlugs.length(); i += 2) {
                strConfig.append(inUsePlugs.charAt(i))
                        .append("|")
                        .append(inUsePlugs.charAt(i + 1));
                if (i != inUsePlugs.length() - 2) {
                    strConfig.append(",");
                }
            }
            strConfig.append(">");
        }

        System.out.println(strConfig);
    }

    /**
     * print all machine stats
     *
     * @param stats dto contains machine records
     */
    public static void printStatistics(DTOstatistics stats) {
        // get notch distances from windows

        for (StatisticRecord record : stats.getStats()) {

            StringBuilder cipherRecordStr = new StringBuilder();

            printConfiguration(record.getInUseRotors(), record.getWindowCharacters(),
                    Utility.decimalToRoman(record.getReflectorID()), record.getPlugs(), new ArrayList<>());

            for (Pair<Pair<String, String>, Long> currentCipherRecord : record.getCipherHistory()) {
                cipherRecordStr.append("#. ")
                        .append("<")
                        .append(currentCipherRecord.getKey().getKey())
                        .append(">").append(" --> ").append("<")
                        .append(currentCipherRecord.getKey().getValue()).append(">")
                        .append(" (").append(currentCipherRecord.getValue()).append(" nano-seconds)").append("\n");
            }

            System.out.println(cipherRecordStr);
        }
    }

    public static void loadExistingMachineFromFile() {
        String fileName = getXMLFileName();
        try {
            engine.loadExistingMachineFromFile(fileName);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    public static void saveExistingMachineToFile() {
        String fileName = getXMLFileName();
        try {
            engine.saveExistingMachineToFile(fileName);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}