import component.Reflector;
import component.Rotor;
import machine.EnigmaMachine;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        EnigmaEngine engine= new EnigmaEngine();

        engine.buildMachineFromXmlFile("ddv");
        ArrayList<Integer> rotorsID= new ArrayList<>();
        rotorsID.add(0);
        rotorsID.add(1);
        int reflectorID = 0;
        engine.updateConfiguration(rotorsID, reflectorID, "A|F");
        System.out.println(engine);
        System.out.println(((EnigmaEngine) engine).cipherText("AAAAAAAAAAAAAA"));
        System.out.println('\n');

        System.out.println(((EnigmaEngine) engine).cipherText("CEFDABCEFFCEDABABD").equals("AAAEEEBBBDDDCCCFFF"));




    }
}
