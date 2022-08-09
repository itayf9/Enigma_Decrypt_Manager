import dto.DTOstatus;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Console {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        run();


    }

    public static void run() {
        Engine engine = new EnigmaEngine();
        String xmlFileName = "Engine/src/ex1-sanity-small.xml";
        boolean isExit = false;

        printMainMenu();
        Operation choice = getInputUserChoice();

        while (!isExit) {
            switch (choice) {
                case READ_SYSTEM_DETAILS_FROM_XML:
                    // get XML fileName();
                    engine.buildMachineFromXmlFile(xmlFileName);
                    break;
                case DISPLAY_SPECS:
                    System.out.println(engine.displayMachineSpecifications());
                    break;
                case CHOOSE_INIT_CONFIG_MANUAL:
                    chooseConfigManual();
                    break;
                case CHOOSE_INIT_CONFIG_AUTO:
                    System.out.println(engine.selectConfigurationAuto());
                    break;
                case PROCESS_INPUT:
                    System.out.println("Please enter text to cipher");
                    String inputText = getInputCipherText();
                    System.out.println(engine.cipherInputText(inputText).getDetails());
                    break;
                case RESET_CURRENT_CODE:
                    engine.resetConfiguration();
                    break;
                case HISTORY_AND_STATISTICS:
                    // engine.history();
                    break;
                case EXIT_SYSTEM:
                    isExit = true;
                    continue;
                    // System.exit(1);
                default:
                    break;
            }

            printMainMenu();
            choice = getInputUserChoice();
        }


    }

    public static void printMainMenu () {
        System.out.println("Welcome to Cracking The Enigma!!\n\n");
        System.out.println("Select an operation:\n\n");
        System.out.println("1 - Load System Details From File.\n" +
                "2 - Display Enigma Machine Specifications.\n" +
                "3 - Set Configuration - Manually.\n" +
                "4 - Set Configuration - Automatically.\n" +
                "5 - Cipher.\n" +
                "6 - Reset Configuration.\n" +
                "7 - History And Statistics.\n" +
                "8 - Exit.\n");
    }

    public static Operation getInputUserChoice() {

        boolean isValid= false;

        String choice = scanner.nextLine();

        while (!isValid) {
            if (choice.length() > 1) {
                System.out.println("Invalid input - please enter only one option number! ");
                choice = scanner.nextLine();
            }
            else if (choice.charAt(0) < '1' || choice.charAt(0) > '8') {
                System.out.println("invalid option - try again ! with options from 1-8");
                choice = scanner.nextLine();
            } else {
                System.out.println(choice);
                isValid = true;

            }
        }

        return Operation.getOperation(choice.charAt(0));
    }

    public static List<Integer> getInputListOfIntegers(int numberOfIntegers){

        boolean isValid= false;
        int currentRotorID = 0;
        List<Integer> listOfChoices = new ArrayList<>();

        System.out.println("Please enter " + numberOfIntegers + " rotors by their ID's." );

        for (int i = 0; i < numberOfIntegers; i++) {

            // String choice = scanner.nextLine();

            while (!isValid) {
                System.out.println("Please enter the ID of rotor #" + i + " (from right to left).");
                try {
                    currentRotorID = scanner.nextInt();
                    isValid = true;
                } catch (InputMismatchException e) {
                    System.out.println("Error! Please enter numbers only.");
                }
            }

            isValid = false;
            listOfChoices.add(currentRotorID);

        }

        return listOfChoices;
    }

    private static String getInputSequenceOfCharacters(int numberOfCharacters) {

        scanner.nextLine();
        boolean isValid= false;
        StringBuilder stringOfChoices = new StringBuilder();

        System.out.println("Please enter " + numberOfCharacters + " letters that will be the window's letters." );

        for (int i = 0; i < numberOfCharacters; i++) {
            System.out.println("Please enter the window letter of rotor #" + i + " (from right to left.");
            String currentCharacter = scanner.nextLine();

            while (!isValid){
                if (currentCharacter.length() > 1) {
                    System.out.println("Error! You have entered more than one character. ");

                    System.out.println("Please enter the window letter of rotor #" + i + " (from right to left.");
                    currentCharacter = scanner.nextLine();
                }
                else {
                    isValid = true;
                }

            }

            isValid = false;
            stringOfChoices.append(currentCharacter);


        }

        return stringOfChoices.toString();

    }

    private static int getInputInteger() {
        boolean isValid= false;

        System.out.println("Please enter the number of the wanted reflector.");
        System.out.println("1. I\n2. II\n3. III\n4. IV\n5. V\n");
        String choice = scanner.nextLine();

        while (!isValid) {
            if (choice.length() > 1) {
                System.out.println("Invalid input. try again.");
                choice = scanner.nextLine();
            }
            else if (choice.charAt(0) < '1' || choice.charAt(0) > '5') {
                System.out.println("invalid option - try again ! with options from 1-5");
                choice = scanner.nextLine();
            } else {
                isValid = true;
            }
        }
        return Integer.parseInt(choice);
    }

    private static List<String> getInputListOfStrings() {
        boolean isValid = false;
        int numOfPlugs = 0;
        List<String> plugsList = new ArrayList<>();

        while(!isValid) {
            System.out.println("Please enter num of plugs you want to add to the machine.");
            try{
                numOfPlugs = scanner.nextInt();
                isValid = true;

            } catch(InputMismatchException e) {
                System.out.println("Input is not a number. try again.");
            }
        }
        // clear the buffer after integer input
        scanner.nextLine();


        for (int i = 0; i < numOfPlugs; i++) {
            boolean validPlug = false;
            while(!validPlug) {
                String currentPlug = scanner.nextLine();
                if(currentPlug.length() > 2) {
                    System.out.println("Invalid Plug! - please enter 2 letters from the alphabet and press enter. e.g: AB");
                } else if(currentPlug.length() < 2) {
                    System.out.println("Invalid Plug! - please enter 2 letters from the alphabet and press enter. e.g: AB");
                } else {
                    validPlug = true;
                    plugsList.add(currentPlug);
                }
            }
        }
        return plugsList;
    }


    private static String getInputCipherText() {

        boolean isValid = false;
        String cipherText = scanner.nextLine();

        while (!isValid) {
            if (cipherText.length() == 0) {
                System.out.println("Invalid input");
                cipherText = scanner.nextLine();
            }
            else {
                System.out.println(cipherText);
                isValid = true;
            }
        }

        return cipherText;
    }
}