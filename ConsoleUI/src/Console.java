import java.util.InputMismatchException;
import java.util.Scanner;

public class Console {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        run();


    }

    public static void run(){
        Engine engine = new EnigmaEngine();
        String xmlFileName = "Engine/src/ex1-sanity-small.xml";
        boolean isExit = false;

        printMainMenu();
        Operation choice = getInputUserChoice();

        while (!isExit){
            switch(choice) {
                case READ_SYSTEM_DETAILS_FROM_XML:
                    System.out.println(choice.name());
                    // get XML fileName();
                    engine.buildMachineFromXmlFile(xmlFileName);
                    break;
                case DISPLAY_SPECS:
                    System.out.println(choice.name());
                    System.out.println(engine.displayMachineSpecifications());
                    break;
                case CHOOSE_INIT_CONFIG_MANUAL:
                    System.out.println(choice.name());
                    //engine.selectConfigurationManual();
                    break;
                case CHOOSE_INIT_CONFIG_AUTO:
                    System.out.println(choice.name());
                    System.out.println(engine.selectConfigurationAuto());
                    break;
                case PROCESS_INPUT:
                    System.out.println(choice.name());
                    System.out.println("Please enter text to cipher");
                    String inputText = getInputCipherText();
                    System.out.println(engine.cipherInputText(inputText).getDetails());
                    break;
                case RESET_CURRENT_CODE:
                    System.out.println(choice.name());
                    engine.resetConfiguration();
                    break;
                case HISTORY_AND_STATISTICS:
                    System.out.println(choice.name());
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

    private static String getInputCipherText() {

        boolean isValid = false;
        String cipherText = scanner.nextLine();

        while (!isValid) {
            if (cipherText.length() == 0) {
                System.out.println("Invalid input - please enter only one option number! ");
                cipherText = scanner.nextLine();
            }
            else {
                System.out.println(cipherText);
                isValid = true;
            }
        }

        return cipherText;
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








}

