public class Main {
    public static void main(String[] args) {
        boolean isExit = false;

        printMainMenu();

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
}

