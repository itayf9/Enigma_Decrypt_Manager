import machine.component.Reflector;
import machine.component.Rotor;
import dto.*;
import machine.EnigmaMachine;
import machine.jaxb.generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

import utill.CharacterPair;

import static machine.EnigmaMachine.advanceCipherCounter;
import static machine.EnigmaMachine.getCipherCounter;
import static utill.Utility.*;


public class EnigmaEngine implements Engine {

    // The engine contains the Enigma Machine instance.
    private EnigmaMachine machine;


    // engine constructor
    public EnigmaEngine(){
    }

    // creating new machine instance using all the parts the machine needs.
    public void buildMachine(ArrayList<Rotor> availableRotors, ArrayList<Reflector> availableReflectors, int rotorsCount, String alphabet, Map<Character, Integer> character2index){
        machine = new EnigmaMachine(availableRotors, availableReflectors, rotorsCount, alphabet, character2index);
    }

    // updating the current machine configurations.
    // based on String of input from the user.
    public void updateConfiguration(List<Integer> rotorsIDs, String windowsChars, int reflectorID, List<String> plugs){

        // build windowOffsets
        ArrayList<Integer> windowOfssets = new ArrayList<>();

        for (int i = windowsChars.length() - 1; i >= 0; i--) {
            int j = windowsChars.length() - 1 - i ;
            Rotor r = machine.getRotorByID( rotorsIDs.get(j));
            int offset = r.translateChar2Offset(windowsChars.charAt(i));
            windowOfssets.add(offset);
        }

        machine.setMachineConfiguration((ArrayList<Integer>) rotorsIDs, windowOfssets, reflectorID, plugs);
    }

    // ciphering text with the cipher method of "machine".
    public String cipherText ( String toCipher ) {
        StringBuilder cipheredText = new StringBuilder();

        for (Character currentChar : toCipher.toCharArray()) {
            cipheredText.append(machine.cipher(currentChar));
        }

        advanceCipherCounter();
        return cipheredText.toString();
    }

    // get fileName from user and load Xml file to build new machine.
    public void buildMachineFromXmlFile(String fileName) {
        try {
            InputStream inputStream = new FileInputStream(new File(fileName));
            CTEEnigma cteEnigma = deserializeFrom(inputStream);
            buildMachineFromCTEEnigma(cteEnigma);
            System.out.println(machine);

        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // unmarshalling the schema of provided Xml file to create jaxb classes
    // to help build the machine from a xml file
    private static CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(in);
    }

    public static String JAXB_XML_PACKAGE_NAME = "machine.jaxb.generated";

    // convert all data from jaxb classes to our own classes.
    private void buildMachineFromCTEEnigma( CTEEnigma cteEnigma ){
        CTEMachine cteMachine = cteEnigma.getCTEMachine();

        ArrayList<Rotor> availableRotors = new ArrayList<>();
        ArrayList<Reflector> availableReflectors = new ArrayList<>();
        int rotorsCount = cteMachine.getRotorsCount();

        // initialize alphabet and character2index
        String alphabet = cteMachine.getABC().trim();
        Map<Character, Integer> character2index= new HashMap<>();
        for (int i = 0; i < alphabet.length(); i++) {
            character2index.put(alphabet.charAt(i), i);
        }

        // initialize rotors
        for ( CTERotor cteRotor : cteMachine.getCTERotors().getCTERotor() ) {

            int currentID = cteRotor.getId();
            int currrentNotchIndex = cteRotor.getNotch() - 1;

            ArrayList<Integer> mapRotorForward = new ArrayList<>(Collections.nCopies(alphabet.length(), 0));
            ArrayList<Integer> mapRotorBackward = new ArrayList<>(Collections.nCopies(alphabet.length(), 0));
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
               mapRotorForward.set(indexOfRightSide, indexOfLeftSide);
               indexOfLeftSide++;
            }

            indexOfLeftSide = 0;

            // go through all right positions to build the backwarding map
            for (CTEPositioning ctePosition : cteRotor.getCTEPositioning()){
                int indexOfRightSide = backwardTranslatorChar2index.get(ctePosition.getRight().charAt(0));
                mapRotorBackward.set(indexOfRightSide, indexOfLeftSide);
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

            ArrayList<Integer> reflectorMapping = new ArrayList<>(Collections.nCopies(alphabet.length(), 0));

            // creating the mapping of the reflector
            for (CTEReflect cteReflect : cteReflector.getCTEReflect()){
                int input = cteReflect.getInput() -1;
                int output = cteReflect.getOutput() -1;
                reflectorMapping.set(input, output);
                reflectorMapping.set(output, input);
            }

            // creating a new reflector
            Reflector currentReflector = new Reflector(currentID, reflectorMapping);

            availableReflectors.add(currentReflector);
        }

        /** checkValidation */

        // sort rotors and reflector by id
        Comparator<Rotor> rotorComparator = (r1, r2) -> r1.getId() - r2.getId();
        availableRotors.sort(rotorComparator);
        Comparator<Reflector> reflectorComparator = (r1, r2) -> r1.getId() - r2.getId();
        availableReflectors.sort(reflectorComparator);

        buildMachine(availableRotors, availableReflectors, rotorsCount, alphabet, character2index);


    }


    /**
     * fetching the current machine specifications.
     * @return DTO object that represents the specs.
     */
    @Override
    public DTOspecs displayMachineSpecifications () {

        int availableRotorsCount = machine.getAvailableRotorsLen();
        int inUseRotorsCount = machine.getRotorsCount();
        List<Integer> notchPositions = machine.getAllNotchPositions();
        int availableReflectorsCount = machine.getAvailableReflectorsLen();
        int cipheredTextsCount = getCipherCounter();

        List<Integer> inUseRotorsIDs = machine.getInUseRotorsIDs();
        List<Character> windowsCharacters = machine.getAllWindowsCharacters();
        String inUseReflectorSymbol = decimalToRoman(machine.getInUseReflector().getId());
        List<CharacterPair> inUsePlugs = machine.getListOfPlugPairs();


        return new DTOspecs(availableRotorsCount, inUseRotorsCount, notchPositions,
                availableReflectorsCount, cipheredTextsCount, inUseRotorsIDs, windowsCharacters,
                inUseReflectorSymbol, inUsePlugs);
    }

    @Override
    public DTOstatus selectConfigurationManual (List<Integer> rotorsIDs, String windows, int reflectorID , List<String> plugs){
        boolean isSucceed = true;
        String details = null;

        updateConfiguration(rotorsIDs, windows, reflectorID, plugs);

        return new DTOstatus(isSucceed, details);
    }

    @Override
    public DTOsecretConfig selectConfigurationAuto (){
        String alphabet = machine.getAlphabet();

        List<Integer> randomGeneratedRotorIDs= new ArrayList<>();
        int randomGeneratedReflectorID;
        StringBuilder randomGeneratedWindowCharacters = new StringBuilder();
        int randomPlugsCount = (int)Math.floor(Math.random() * (alphabet.length() + 1)) / 2;
        List<String> randomGeneratedPlugs = new ArrayList<>(Collections.nCopies(randomPlugsCount, ""));
        List<Boolean> alreadyPluged = new ArrayList<>(Collections.nCopies(alphabet.length(), false));

        // randomizes rotors ID and order
        for (int i = 0; i < machine.getRotorsCount(); i++) {
            int randomRotorID = (int)Math.ceil( Math.random() * machine.getAvailableRotorsLen() );
            while( randomGeneratedRotorIDs.contains(randomRotorID)){
                randomRotorID = (int)Math.ceil( Math.random() * machine.getAvailableRotorsLen() );
            }

            randomGeneratedRotorIDs.add(randomRotorID);
        }

        // randomizes reflector ID
        randomGeneratedReflectorID = (int)Math.ceil( Math.random() * machine.getAvailableReflectorsLen() );

        // randomizes window offsets
        for (int i = 0; i < machine.getRotorsCount(); i++) {
            // get random index
            int randomIndex = (int)Math.floor( Math.random() * alphabet.length());
            // convert random index to Character from the alphabet.
            randomGeneratedWindowCharacters.append(alphabet.charAt(randomIndex));
        }

        // randomizes plugs
        for (int i = 0; i < randomPlugsCount; i++) {
            int firstInPlugIndex = (int)Math.floor( Math.random() * alphabet.length());
            int secondInPlugIndex = (int)Math.floor( Math.random() * alphabet.length());

            while (alreadyPluged.get( firstInPlugIndex)){
                firstInPlugIndex = (int)Math.floor( Math.random() * alphabet.length());
            }
            alreadyPluged.set( firstInPlugIndex, true);
            while (alreadyPluged.get( secondInPlugIndex)){
                secondInPlugIndex = (int)Math.floor( Math.random() * alphabet.length());
            }
            alreadyPluged.set( secondInPlugIndex, true);

            String currentPlug = "" + alphabet.charAt(firstInPlugIndex) + alphabet.charAt(secondInPlugIndex);
            randomGeneratedPlugs.set(i, currentPlug);
        }

        // updates the configuration
        updateConfiguration(randomGeneratedRotorIDs, randomGeneratedWindowCharacters.toString(),
                randomGeneratedReflectorID, randomGeneratedPlugs);

        return new DTOsecretConfig( randomGeneratedRotorIDs, randomGeneratedWindowCharacters.toString(),
                randomGeneratedReflectorID, randomGeneratedPlugs);
    }

    @Override
    public DTOciphertext cipherInputText (String inputText) {

        boolean isSucceed = true;
        String outputText;
        String problem = "";

        // check valid ABC
        //problem = isAllCharsInAlphabet(inputText);

        if (problem.equals("")){
            outputText = cipherText(inputText);
        }else {
            outputText = problem;
        }

        return new DTOciphertext(isSucceed , outputText);
    }

    @Override
    public DTOresetConfig resetConfiguration () {

        boolean isSucceed = true;
        String detail = "";

        for (int i = 0; i < machine.getRotorsCount(); i++) {

            int currentOffset = machine.getInUseWindowsOffsets().get(i);
            machine.getInUseRotors().get(i).rotateToOffset(currentOffset);
        }

        return new DTOresetConfig(isSucceed, detail);
    }

    @Override
    public DTOstatus validateRotors (List<Integer> rotorsIDs){
        boolean isSucceed = true;
        String details = null;

        // check if rotorsIDs size is exactly the required size.
        if (rotorsIDs.size() !=machine.getRotorsCount()){
            isSucceed = false;
        }
        else {
            for (Integer rotorID : rotorsIDs) {

                // check if the rotorID exists in this machine.
                if (rotorID <= 0 || rotorID > machine.getAvailableRotorsLen()) {
                    isSucceed = false;
                    break;
                }
            }
        }

        return new DTOstatus(isSucceed, details);
    }

    @Override
    public DTOstatus validateWindowCharacters (String windowChars){
        boolean isSucceed = true;
        String details = null;
        final int CHAR_NOT_FOUND = -1;

        for (Character currentWindowCharacter : windowChars.toCharArray()) {
            if (machine.getAlphabet().indexOf(currentWindowCharacter) == CHAR_NOT_FOUND) {
                isSucceed = false;
                break;
            }
        }

        return new DTOstatus(isSucceed, details);
    }

    @Override
    public DTOstatus validateReflector (int reflectorID){
        boolean isSucceed = true;
        String details = null;

        // check if the reflectorID exists in this machine.
        if (reflectorID <= 0 || reflectorID > machine.getAvailableReflectorsLen()) {
            isSucceed = false;
        }

        return new DTOstatus(isSucceed, details);
    }

    @Override
    public DTOstatus validatePlugs (List<String> plugs){
        boolean isSucceed = true;
        String details = null;
        List<Boolean> alreadyPluged = new ArrayList<>(Collections.nCopies(machine.getAlphabet().length(), false));
        final int CHAR_NOT_FOUND = -1;

        // goes through all the plugs
        for (String plug : plugs){

            int firstInPlugIndex = machine.getAlphabet().indexOf(plug.charAt(0));
            int secondInPlugIndex = machine.getAlphabet().indexOf(plug.charAt(1));

            // check if both characters are the same.
            if (firstInPlugIndex == secondInPlugIndex) {
                isSucceed = false;
                break;
            }

            // check if both characters in the current plug is in the alphabet.
            if (firstInPlugIndex == CHAR_NOT_FOUND || secondInPlugIndex == CHAR_NOT_FOUND){
                isSucceed = false;
                break;
            } else {
                // check if both characters are not plugged yet.
                if (!alreadyPluged.get( firstInPlugIndex) && !alreadyPluged.get( secondInPlugIndex)){
                    alreadyPluged.set( firstInPlugIndex, true);
                    alreadyPluged.set( secondInPlugIndex, true);
                } else {
                    isSucceed = false;
                    break;
                }
            }
        }

        return new DTOstatus(isSucceed, details);
    }


    @Override
    public String toString() {
        return "EnigmaEngine{" +
                "machine=" + machine +
                '}';
    }
}
