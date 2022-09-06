import engine.EnigmaEngine;

public class Main {

    public static int factorial(int number) {
        int fact = 1;
        for (int i = 1; i <= number; i++) {
            fact = fact * i;
        }
        return fact;
    }

    public static void main(String[] args) {

        EnigmaEngine engine = new EnigmaEngine();

        engine.buildMachineFromXmlFile("C:/Users/itayf/IdeaProjects/Cracking the Enigma Machine/Engine/src/resource/ex2/ex2-basic.xml");
        for (int i = 0; i < 10; i++) {
            System.out.println(factorial(i));
        }
    }
}

