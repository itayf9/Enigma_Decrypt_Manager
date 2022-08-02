import component.Reflector;
import component.Rotor;
import machine.EnigmaMachine;
import machine.jaxb.generated.CTEEnigma;
import machine.jaxb.generated.CTEMachine;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class EnigmaEngine implements Engine {

    private EnigmaMachine machine;

    public EnigmaEngine(){
    }

    public void buildMachine(ArrayList<Rotor> availableRotors, ArrayList<Reflector> availableReflectors, int rotorsCount, String alphabet){
        machine= new EnigmaMachine(availableRotors, availableReflectors, rotorsCount, alphabet);
    }

    public void updateConfiguration(ArrayList<Integer> rotorsIDs, int reflectorID ,String plugs){
        machine.updateConfiguration(rotorsIDs, reflectorID, plugs);
    }

    public String cipherText ( String toCipher ) {
        StringBuilder cipheredText = new StringBuilder();

        for (Character currentChar : toCipher.toCharArray()) {
            cipheredText.append(machine.cipher(currentChar));
        }

        return cipheredText.toString();
    }

    public void buildMachineFromXmlFile(String fileName) {
        try {
            InputStream inputStream = new FileInputStream(new File("Engine/src/ex1-sanity-small.xml"));
            CTEEnigma cteEnigma = deserializeFrom(inputStream);




            System.out.println(cteEnigma);
            // some stuff to convert to machine + buildMachine()
            cteEnigma









        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(in);
    }

    public static String JAXB_XML_PACKAGE_NAME = "machine.jaxb.generated";

    private void buildMachineFromCTEEnigma( CTEEnigma cteEnigma ){
        CTEMachine cteMachine = cteEnigma.getCTEMachine();

        //        ArrayList<Integer> mapR1 = new ArrayList<>();
//        mapR1.add(5);
//        mapR1.add(4);
//        mapR1.add(0);
//        mapR1.add(2);
//        mapR1.add(1);
//        mapR1.add(3);
//
//        ArrayList<Integer> mapR1back = new ArrayList<>();
//        mapR1back.add(2);
//        mapR1back.add(4);
//        mapR1back.add(3);
//        mapR1back.add(5);
//        mapR1back.add(1);
//        mapR1back.add(0);
//
//        ArrayList<Integer> mapR2 = new ArrayList<>();
//        mapR2.add(5);
//        mapR2.add(1);
//        mapR2.add(4);
//        mapR2.add(2);
//        mapR2.add(0);
//        mapR2.add(3);
//
//        ArrayList<Integer> mapR2back = new ArrayList<>();
//        mapR2back.add(4);
//        mapR2back.add(1);
//        mapR2back.add(3);
//        mapR2back.add(5);
//        mapR2back.add(2);
//        mapR2back.add(0);
//
//        ArrayList<Integer> t1= new ArrayList<>();
//
//
//        Rotor r1 = new Rotor(1, 3, t1,   ABC.length(), mapR1, mapR1back);
//        Rotor r2 = new Rotor(2, 0, t1,  ABC.length(), mapR2, mapR2back);
//
//        ArrayList<Integer> mapRef10= new ArrayList<>();
//        mapRef10.add(3);
//        mapRef10.add(4);
//        mapRef10.add(5);
//        mapRef10.add(0);
//        mapRef10.add(1);
//        mapRef10.add(2);
//
//        Reflector ref1= new Reflector(10, mapRef10);
//
//        ArrayList<Rotor> availableRotors = new ArrayList<>();
//        availableRotors.add(r1);
//        availableRotors.add(r2);
//
//        ArrayList<Reflector> availableReflectors = new ArrayList<>();
//        availableReflectors.add(ref1);
//
//        String plugs = "A|F";




        ArrayList<Rotor> availableRotors;
        ArrayList<Reflector> availableReflectors;
        int rotorsCount = cteMachine.getRotorsCount();
        String alphabet = cteMachine.getABC();
    }


    /*

    1.
    learn JAXB
    build machine from XML

    2.
    implement current config

    3 + 4 .
    decide on the fields of the config and where to put them

    5.
    test cipher methods
    implement "cipherText" in "Engine"
    limit - can't operate before 3 or 4
    check valid input

    6.

    7.

    8.
    write function 'exit'



     */


    @Override
    public String toString() {
        return "EnigmaEngine{" +
                "machine=" + machine +
                '}';
    }
}
