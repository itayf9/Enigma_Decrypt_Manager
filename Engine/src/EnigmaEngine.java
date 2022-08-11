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
import utill.Problem;

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

    /**
     * updating the current machine configurations.
     * based on String of input from the user.
     * @param rotorsIDs
     * @param windowsChars
     * @param reflectorID
     * @param plugs
     */
    public void updateConfiguration(List<Integer> rotorsIDs, String windowsChars, int reflectorID, String plugs) {

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

    /**
     * ciphering text with the cipher method of "machine".
     * @param toCipher
     * @return string of cipher text.
     */
    public String cipherText ( String toCipher ) {
        StringBuilder cipheredText = new StringBuilder();

        for (Character currentChar : toCipher.toCharArray()) {
            cipheredText.append(machine.cipher(currentChar));
        }

        advanceCipherCounter();
        return cipheredText.toString();
    }

    /**
     * get the rotorsCount
     * @return rotors count
     */
    @Override
    public int getRotorsCount (){
        return machine.getRotorsCount();
    }

    /**
     * get fileName from user and load Xml file to build new machine.
     * @param fileName
     */
    public DTOstatus buildMachineFromXmlFile(String fileName) {
        boolean isSucceeded = false;
        Problem details = Problem.NO_PROBLEM;

        if (!fileName.endsWith(".xml")) {
            isSucceeded = false;
            details = Problem.FILE_NOT_IN_FORMAT;
        }

        try {
            InputStream inputStream = new FileInputStream(new File(fileName));
            CTEEnigma cteEnigma = deserializeFrom(inputStream);
            details = validateCTEEnigma(cteEnigma);
            buildMachineFromCTEEnigma(cteEnigma);

            } catch (JAXBException e) {
                details = Problem.JAXB_ERROR;
                isSucceeded = false;
            } catch (FileNotFoundException e) {
                isSucceeded = false;
                details = Problem.FILE_NOT_FOUND;
            }
        }

        return new DTOstatus(isSucceeded, details);
    }

    /**
     * valitdates the CTEengine
     * @return detailed problem
     */
    private Problem validateCTEEnigma(CTEEnigma cteEnigma ) {
        Problem problem = Problem.NO_PROBLEM;

        CTEMachine cteMachine = cteEnigma.getCTEMachine();

        // check for alphabet length to be even
        if (cteMachine.getABC().length() % 2 == 1) {
            problem = Problem.ODD_ALPHABET_AMOUNT;
            //return
        }

        // check if rotors count is less than available rotors.
        if (cteMachine.getCTERotors().getCTERotor().size() < cteMachine.getRotorsCount()) {
            problem = Problem.NOT_ENOUGH_ROTORS;
        }

        // check if rotors count is less than 2
        if (cteMachine.getRotorsCount() < 2) {
            problem = Problem.ROTORS_COUNT_BELOW_TWO;
        }


        return problem;
    }

    /**
     *  unmarshalling the schema of provided Xml file to create jaxb classes.
     *  to help build the machine from a xml file.
     * @param in
     * @return jaxb generated engine
     * @throws JAXBException
     */
    private static CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(in);
    }

    public static String JAXB_XML_PACKAGE_NAME = "machine.jaxb.generated";

    /**
     * convert all data from jaxb classes to our own classes.
     * @param cteEnigma
     */
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

        boolean isSucceeded = true;
        Problem details = Problem.NO_PROBLEM;

        List<Integer> inUseRotorsIDs = new ArrayList<>();
        List<Character> windowsCharacters = new ArrayList<>();
        String inUseReflectorSymbol = null;
        List<CharacterPair> inUsePlugs = new ArrayList<>();

        int availableRotorsCount = machine.getAvailableRotorsLen();
        int inUseRotorsCount = machine.getRotorsCount();
        List<Integer> notchDistancesToWindow = machine.getInUseNotchDistanceToWindow();
        int availableReflectorsCount = machine.getAvailableReflectorsLen();
        int cipheredTextsCount = getCipherCounter();

        if (machine.isConfigured()) {
            inUseRotorsIDs = machine.getInUseRotorsIDs();
            windowsCharacters = machine.getAllWindowsCharacters();
            inUseReflectorSymbol = decimalToRoman(machine.getInUseReflector().getId());
            inUsePlugs = machine.getListOfPlugPairs();
        } else {
            details = Problem.NO_CONFIGURATION;
        }


        return new DTOspecs(isSucceeded, details, availableRotorsCount, inUseRotorsCount, notchDistancesToWindow,
                availableReflectorsCount, cipheredTextsCount, inUseRotorsIDs, windowsCharacters,
                inUseReflectorSymbol, inUsePlugs);
    }

    /**
     * get new config from user and updating the machine configuration.
     * @param rotorsIDs
     * @param windows
     * @param reflectorID
     * @param plugs
     * @return
     */
    @Override
    public DTOstatus selectConfigurationManual(List<Integer> rotorsIDs, String windows, int reflectorID, String plugs) {
        boolean isSucceed = true;
        Problem details = Problem.NO_PROBLEM;

        updateConfiguration(rotorsIDs, windows, reflectorID, plugs);

        return new DTOstatus(isSucceed, details);
    }

    /**
     * get new config randomly and updating the machine config.
     * @return
     */
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
                decimalToRoman(randomGeneratedReflectorID), randomGeneratedPlugs);
    }

    /**
     * validates the input text from the user and calls method "cipherText" to cipher the text.
     * @param inputText
     * @return DTO which has the ciphered text
     */
    @Override
    public DTOciphertext cipherInputText (String inputText) {

        boolean isSucceed = false;
        String outputText = "";
        Problem problem = Problem.NO_PROBLEM;

        // check valid ABC
        problem = isAllCharsInAlphabet(inputText);

        if (problem.equals(Problem.NO_PROBLEM)){
            isSucceed = true;
            outputText = cipherText(inputText);
        }

        return new DTOciphertext(isSucceed, problem, outputText);
    }

    private Problem isAllCharsInAlphabet(String inputText) {
        final int NOT_FOUND = -1;

        for (Character currentCharacter: inputText.toCharArray()){
            if (machine.getAlphabet().indexOf(currentCharacter) == NOT_FOUND){
                return Problem.NOT_IN_ALPHABET;
            }
        }

        return Problem.NO_PROBLEM;
    }

    /**
     * resetting the offset of each rotor in configuration of machine to its original values.
     * @return DTOresetConfig
     */
    @Override
    public DTOresetConfig resetConfiguration () {

        boolean isSucceed = true;
        Problem details = Problem.NO_PROBLEM;

        for (int i = 0; i < machine.getRotorsCount(); i++) {

            int currentOffset = machine.getInUseWindowsOffsets().get(i);
            machine.getInUseRotors().get(i).rotateToOffset(currentOffset);
        }

        return new DTOresetConfig(isSucceed, details);
    }

    /**
     * validates rotors input from user.
     * @param rotorsIDs
     * @return DTO status
     */
    @Override
    public DTOstatus validateRotors (List<Integer> rotorsIDs){
        boolean isSucceed = true;
        Problem details = Problem.NO_PROBLEM;

        // check if rotorsIDs size is exactly the required size.
        if (rotorsIDs.size() < machine.getRotorsCount()){
            isSucceed = false;
            details = Problem.NOT_ENOUGH_ELEMENTS;
        }
        else if (rotorsIDs.size() > machine.getRotorsCount()){
            isSucceed = false;
            details = Problem.TOO_MANY_ELEMENTS;
        }
        else {
            for (Integer rotorID : rotorsIDs) {

                // check if the rotorID exists in this machine.
                if (rotorID <= 0 || rotorID > machine.getAvailableRotorsLen()) {
                    isSucceed = false;
                    details = Problem.OUT_OF_RANGE_ID;
                    break;
                }
            }
        }

        return new DTOstatus(isSucceed, details);
    }

    /**
     * validates window characters offsets input from user.
     * @param windowChars
     * @return DTOstatus
     */
    @Override
    public DTOstatus validateWindowCharacters (String windowChars){
        boolean isSucceed = true;
        Problem details = Problem.NO_PROBLEM;
        final int CHAR_NOT_FOUND = -1;

        for (Character currentWindowCharacter : windowChars.toCharArray()) {
            if (machine.getAlphabet().indexOf(currentWindowCharacter) == CHAR_NOT_FOUND) {
                isSucceed = false;
                details = Problem.NOT_IN_ALPHABET;
                break;
            }
        }

        return new DTOstatus(isSucceed, details);
    }

    /**
     * validate reflector input from the user.
     * @param reflectorID
     * @return DTO status
     */
    @Override
    public DTOstatus validateReflector (int reflectorID){
        boolean isSucceed = true;
        Problem details = Problem.NO_PROBLEM;

        // check if the reflectorID exists in this machine.
        if (reflectorID <= 0 || reflectorID > machine.getAvailableReflectorsLen()) {
            isSucceed = false;
            details = Problem.OUT_OF_RANGE_ID;
        }

        return new DTOstatus(isSucceed, details);
    }

    /**
     * validate plugs input from the user.
     * @param plugs
     * @return DTOstatus
     */
    @Override
    public DTOstatus validatePlugs (List<String> plugs){
        boolean isSucceed = true;
        Problem details = Problem.NO_PROBLEM;
        List<Boolean> alreadyPluged = new ArrayList<>(Collections.nCopies(machine.getAlphabet().length(), false));
        final int CHAR_NOT_FOUND = -1;

        // goes through all the plugs
        for (String plug : plugs){

            int firstInPlugIndex = machine.getAlphabet().indexOf(plug.charAt(0));
            int secondInPlugIndex = machine.getAlphabet().indexOf(plug.charAt(1));

            // check if both characters are the same.
            if (firstInPlugIndex == secondInPlugIndex) {
                isSucceed = false;
                details = Problem.SELF_PLUGGING;
                break;
            }

            // check if both characters in the current plug is in the alphabet.
            if (firstInPlugIndex == CHAR_NOT_FOUND || secondInPlugIndex == CHAR_NOT_FOUND){
                isSucceed = false;
                details = Problem.NOT_IN_ALPHABET;
                break;
            } else {
                // check if both characters are not plugged yet.
                if (!alreadyPluged.get( firstInPlugIndex) && !alreadyPluged.get( secondInPlugIndex)){
                    alreadyPluged.set( firstInPlugIndex, true);
                    alreadyPluged.set( secondInPlugIndex, true);
                } else {
                    isSucceed = false;
                    details = Problem.ALREADY_PLUGGED;
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