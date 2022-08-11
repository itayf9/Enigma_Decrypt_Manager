import dto.DTOciphertext;
import dto.DTOresetConfig;
import dto.DTOspecs;
import dto.DTOstatus;

import java.util.*;

public class Console {
    private static final Engine engine = new EnigmaEngine();

    private static boolean isXmlLoaded = false;
    private static boolean isMachineConfigured = false;

    private static String xmlFileName = "Engine/src/ex1-sanity-small.xml";

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
            if (choice.length() > 1) {
                System.out.println("Invalid input - please enter only one option number! ");
                choice = scanner.nextLine().toUpperCase();
            } else if (choice.charAt(0) < '1' || choice.charAt(0) > '8') {
                System.out.println("Invalid option - try again ! with options from 1-8");
                choice = scanner.nextLine().toUpperCase();
            } else {
                isValid = true;

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
    public static List<Integer> getInputListOfIntegers(int numberOfIntegers) {

        boolean isValid = false;
        int currentRotorID = 0;
        List<Integer> listOfChoices = new ArrayList<>();

        System.out.println("There is a place for " + numberOfIntegers + " rotors in this machine.\n" +
                "Enter the rotors' ID's , counting from the right most rotor to the left most rotor.\n" +
                "(rotor #1 is the right most rotor, rotor #2 is to its left, etc.\n");

        for (int i = 0; i < numberOfIntegers; i++) {

            // String choice = scanner.nextLine();

            while (!isValid) {
                System.out.print("ID of rotor #" + (i + 1) + ":");
                try {
                    currentRotorID = scanner.nextInt();
                    isValid = true;
                } catch (InputMismatchException e) {
                    System.out.println("Error! Please enter numbers only.");
                    scanner.nextLine();
                }
            }

            isValid = false;
            listOfChoices.add(currentRotorID);

        }

        return listOfChoices;
    }

    /**
     * get alphabet characters from the user
     *
     * @param numberOfCharacters number of rotors count as well
     * @return String of characters
     */
    private static String getInputSequenceOfCharacters(int numberOfCharacters) {

        boolean isValid = false;
        StringBuilder stringOfChoices = new StringBuilder();

        System.out.println("Enter the rotors' window characters (the characters that will appear at the window for each rotor),\ncounting from the right most rotor to the left most rotor.\n" +
                "(rotor #1 is the right most rotor, rotor #2 is to its left, etc.\n");

        for (int i = 0; i < numberOfCharacters; i++) {
            System.out.print("Window character for rotor #" + (i+1) + ": ");
            String currentCharacter = scanner.nextLine().toUpperCase();

            while (!isValid) {
                if (currentCharacter.length() > 1) {
                    System.out.println("Error! You have entered more than one character. ");

                    System.out.print("Window character for rotor #" + (i+1) + ": ");
                    currentCharacter = scanner.nextLine().toUpperCase();
                } else {
                    isValid = true;
                }

            }

            isValid = false;
            stringOfChoices.append(currentCharacter);


        }

        return stringOfChoices.toString();

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
            displayMessege(buildMachineFromXMLStatus.getDetails());
        }
        else {
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
            System.out.println(specsStatus);
        }
    }

    /**
     * Q3 - choose new config manually.
     */
    static private void chooseConfigManual() {
        int rotorCount = engine.getRotorsCount();
        List<Integer> rotorsIDs = getInputListOfIntegers(rotorCount);
        DTOstatus rotorsIDsStatus = engine.validateRotors(rotorsIDs);
        while (!rotorsIDsStatus.isSucceed()) {
            switch (rotorsIDsStatus.getDetails()) {
                case NOT_ENOUGH_ELEMENTS:
                    System.out.println("The amount of IDs that you've entered is too small.");
                    break;
                case TOO_MANY_ELEMENTS:
                    System.out.println("The amount of IDs that you've entered is too high.");
                    break;
                case OUT_OF_RANGE_ID:
                    System.out.println("One or more of the IDs is not available in this machine.");
                    break;
                case UNKNOWN:
                    System.out.println("An Unknown Problem has occurred.");
            }

            rotorsIDs = getInputListOfIntegers(rotorCount);
            rotorsIDsStatus = engine.validateRotors(rotorsIDs);
        }
        System.out.println("All good.");
        scanner.nextLine();

        String windows = getInputSequenceOfCharacters(rotorCount);
        DTOstatus windowCharactersStatus = engine.validateWindowCharacters(windows);
        while (!windowCharactersStatus.isSucceed()) {
            switch (windowCharactersStatus.getDetails()) {
                case NOT_IN_ALPHABET:
                    System.out.println("One or more of the characters that you've entered is not in the alphabet.");
                    break;
                case UNKNOWN:
                    System.out.println("Unknown Error.");
                    break;
            }

            windows = getInputSequenceOfCharacters(rotorCount);
            windowCharactersStatus = engine.validateWindowCharacters(windows);
        }
        System.out.println("All good.");

        int reflectorID = getInputInteger();
        DTOstatus reflectorIDStatus = engine.validateReflector(reflectorID);
        while (!reflectorIDStatus.isSucceed()) {
            switch (reflectorIDStatus.getDetails()) {
                case OUT_OF_RANGE_ID:
                    System.out.println("Id is out of range.");
                    break;
                case UNKNOWN:
                    System.out.println("Unknown Error.");
                    break;
            }

            reflectorID = getInputInteger();
            reflectorIDStatus = engine.validateReflector(reflectorID);
        }
        System.out.println("All good.");

        String plugs = getInputListOfStrings();
        DTOstatus plugsStatus = engine.validatePlugs(plugs);
        while (!plugsStatus.isSucceed()) {
            switch (plugsStatus.getDetails()) {
                case OUT_OF_RANGE_ID:
                    System.out.println("Error - Id is out of range.");
                    break;
                case SELF_PLUGGING:
                    System.out.println("Error - One or more of the plugs entered is self plugged.");
                    break;
                case ALREADY_PLUGGED:
                    System.out.println("Error - One or more of the plugs entered more then once.");
                    break;
                case UNKNOWN:
                    System.out.println("Unknown Error.");
                    break;
            }

            plugs = getInputListOfStrings();
            plugsStatus = engine.validatePlugs(plugs);
        }
        System.out.println("All good.");

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
        System.out.println(engine.selectConfigurationAuto());
        isMachineConfigured = true;
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
        //
    }
}