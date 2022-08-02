import component.Reflector;
import component.Rotor;
import machine.EnigmaMachine;
import machine.jaxb.generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utill.Utility.romanToDecimal;

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

        ArrayList<Rotor> availableRotors = new ArrayList<>();
        ArrayList<Reflector> availableReflectors = new ArrayList<>();
        int rotorsCount = cteMachine.getRotorsCount();

        // initialize alphabet and character2index
        String alphabet = cteMachine.getABC();
        Map<Character, Integer> character2index= new HashMap<>();
        for (int i = 0; i < alphabet.length(); i++) {
            character2index.put(alphabet.charAt(i), i);
        }

        // initialize rotors
        for ( CTERotor cteRotor : cteMachine.getCTERotors().getCTERotor() ) {

            int currentID = cteRotor.getId();
            int currrentNotchIndex = cteRotor.getNotch() - 1;

            ArrayList<Integer> mapRotorForward = new ArrayList<>(alphabet.length());
            ArrayList<Integer> mapRotorBackward = new ArrayList<>(alphabet.length());
            Map<Character, Integer> forwardTranslatorChar2index = new HashMap<>();
            Map<Character, Integer> backwardTranslatorChar2index = new HashMap<>();
            int index = 0;

            // go through all right positions to build the translators Char to index
            for (CTEPositioning ctePosition : cteRotor.getCTEPositioning()){
                forwardTranslatorChar2index.put(ctePosition.getRight().charAt(0), index);
                backwardTranslatorChar2index.put(ctePosition.getLeft().charAt(0), index);
                index++;
            }

            int indexOfLeftSide = 0;

            // go through all left positions to build the forwarding map
            for (CTEPositioning ctePosition : cteRotor.getCTEPositioning()){
               int indexOfRightSide = forwardTranslatorChar2index.get(ctePosition.getLeft().charAt(0));
               mapRotorForward.add(indexOfRightSide, indexOfLeftSide);
               indexOfLeftSide++;
            }

            indexOfLeftSide = 0;

            // go through all right positions to build the backwarding map
            for (CTEPositioning ctePosition : cteRotor.getCTEPositioning()){
                int indexOfRightSide = backwardTranslatorChar2index.get(ctePosition.getRight().charAt(0));
                mapRotorForward.add(indexOfRightSide, indexOfLeftSide);
                indexOfLeftSide++;
            }

            // creating a new rotor
            Rotor currentRotor = new Rotor(currentID, currrentNotchIndex,
                    forwardTranslatorChar2index, alphabet.length(),
                    mapRotorForward, mapRotorBackward);

            availableRotors.add(currentRotor);
        }

        //initialize reflectors
        for ( CTEReflector cteReflector : cteMachine.getCTEReflectors().getCTEReflector() ){

            int currentID = romanToDecimal( cteReflector.getId() );

            ArrayList<Integer> reflectorMapping = new ArrayList<>(alphabet.length());

            // creating the mapping of the reflector
            for (CTEReflect cteReflect : cteReflector.getCTEReflect()){
                int input = cteReflect.getInput() -1;
                int output = cteReflect.getOutput() -1;
                reflectorMapping.add(input, output);
                reflectorMapping.add(output, input);
            }

            // creating a new reflector
            Reflector currentReflector = new Reflector(currentID, reflectorMapping);

            availableReflectors.add(currentReflector);
        }

        /** checkValidation */

        buildMachine(availableRotors, availableReflectors, rotorsCount, alphabet);


    }



    public void buildAlphabetMap(){

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
