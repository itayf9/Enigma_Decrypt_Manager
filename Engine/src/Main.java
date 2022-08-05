import component.Reflector;
import component.Rotor;
import machine.EnigmaMachine;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String xmlFileName = "Engine/src/ex1-sanity-small.xml";

        EnigmaEngine engine= new EnigmaEngine();

        engine.buildMachineFromXmlFile(xmlFileName);

        engine.updateConfiguration("2,1", "CC", "I", "A|F");
        //System.out.println(engine);

        //System.out.println(((EnigmaEngine) engine).cipherText("CEFDABCEFFCEDABABD").equals("AAAEEEBBBDDDCCCFFF"));

        System.out.println( engine.displayMachineSpecifications());


    }
}
