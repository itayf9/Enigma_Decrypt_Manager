import component.Reflector;
import component.Rotor;
import machine.EnigmaMachine;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        Engine engine= new EnigmaEngine();

        String ABC = "ABCDEF";

        ArrayList<Integer> mapR1 = new ArrayList<>();
        mapR1.add(5);
        mapR1.add(4);
        mapR1.add(3);
        mapR1.add(2);
        mapR1.add(1);
        mapR1.add(0);

        ArrayList<Integer> mapR1back = new ArrayList<>();
        mapR1back.add(5);
        mapR1back.add(4);
        mapR1back.add(3);
        mapR1back.add(2);
        mapR1back.add(1);
        mapR1back.add(0);

        ArrayList<Integer> mapR2 = new ArrayList<>();
        mapR2.add(5);
        mapR2.add(1);
        mapR2.add(4);
        mapR2.add(2);
        mapR2.add(0);
        mapR2.add(3);

        ArrayList<Integer> mapR2back = new ArrayList<>();
        mapR2back.add(4);
        mapR2back.add(1);
        mapR2back.add(3);
        mapR2back.add(5);
        mapR2back.add(2);
        mapR2back.add(0);

        Rotor r1 = new Rotor(1, 3, ABC.length(), mapR1, mapR1back);
        Rotor r2 = new Rotor(2, 0, ABC.length(), mapR2, mapR2back);

        ArrayList<Integer> mapRef10= new ArrayList<>();
        mapRef10.add(3);
        mapRef10.add(4);
        mapRef10.add(5);
        mapRef10.add(0);
        mapRef10.add(1);
        mapRef10.add(2);

        Reflector ref1= new Reflector(10, mapRef10);

        ArrayList<Rotor> availableRotors = new ArrayList<>();
        availableRotors.add(r1);
        availableRotors.add(r2);

        ArrayList<Reflector> availableReflectors = new ArrayList<>();
        availableReflectors.add(ref1);

        String plugs = "A|F";

        ((EnigmaEngine) engine).buildMachine(availableRotors, availableReflectors, 2, ABC);

        ArrayList<Integer> rotorsIDs = new ArrayList<>();
        rotorsIDs.add(0);
        rotorsIDs.add(1);

        int reflectorID = 0;

        ((EnigmaEngine) engine).updateConfiguration(rotorsIDs, reflectorID, plugs);

        System.out.println(engine);

        System.out.println(((EnigmaEngine) engine).cipherText("AAAAAAAAAAAAAA"));
        System.out.println('\n');

        System.out.println(((EnigmaEngine) engine).cipherText("CEFDABCEFFCEDABABD").equals("AAAEEEBBBDDDCCCFFF"));


    }
}
