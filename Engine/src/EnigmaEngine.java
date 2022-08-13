import javafx.util.Pair;
import machine.component.Reflector;
import machine.component.Rotor;
import dto.*;
import machine.EnigmaMachine;
import machine.jaxb.generated.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

import statistics.StatisticRecord;
import problem.Problem;

import static machine.EnigmaMachine.advanceCipherCounter;
import static machine.EnigmaMachine.getCipherCounter;
import static utill.Utility.*;


public class EnigmaEngine implements Engine {

    // The engine contains the Enigma Machine instance.
    private EnigmaMachine machine;

    private List<StatisticRecord> machineRecords = new ArrayList<>();

    // engine constructor
    public EnigmaEngine() {
    }

    // creating new machine instance using all the parts the machine needs.
    public void buildMachine(List<Rotor> availableRotors, List<Reflector> availableReflectors, int rotorsCount, String alphabet, Map<Character, Integer> character2index) {
        machine = new EnigmaMachine(availableRotors, availableReflectors, rotorsCount, alphabet, character2index);
    }
    /**
     * updating the current machine configurations.
     * based on String of input from the user.
     *
     * @param rotorsIDs    list of integers
     * @param windowsChars string of characters
     * @param reflectorID  integer
     * @param plugs        string of plugs
     */
    public void updateConfiguration(List<Integer> rotorsIDs, String windowsChars, int reflectorID, String plugs) {

        // build windowOffsets
        List<Integer> windowOfssets = new ArrayList<>();

        for (int i = 0; i < windowsChars.length(); i++) {
//            int j = windowsChars.length() - 1 - i;
            Rotor r = machine.getRotorByID(rotorsIDs.get(i));
            int offset = r.translateChar2Offset(windowsChars.charAt(i));
            windowOfssets.add(offset);
        }

        // add new configuration to statistical records
        StatisticRecord newRecord = new StatisticRecord(rotorsIDs, windowsChars, reflectorID, plugs);
        machineRecords.add(newRecord);

        machine.setMachineConfiguration(rotorsIDs, windowOfssets, reflectorID, plugs);
    }

    /**
     * ciphering text with the cipher method of "machine".
     *
     * @param toCipher string of text to cipher
     * @return string of cipher text.
     */
    public String cipherText(String toCipher) {
        StringBuilder cipheredText = new StringBuilder();

        for (Character currentChar : toCipher.toCharArray()) {
            cipheredText.append(machine.cipher(currentChar));
        }

        advanceCipherCounter();
        return cipheredText.toString();
    }

    /**
     * get the rotorsCount
     *
     * @return rotors count
     */
    @Override
    public int getRotorsCount() {
        return machine.getRotorsCount();
    }

    /**
     * get fileName from user and load Xml file to build new machine.
     *
     * @param fileName string - name of xml file
     */
    public DTOstatus buildMachineFromXmlFile(String fileName) {
        boolean isSucceeded = true;
        Problem details;

        if (!fileName.endsWith(".xml")) {
            isSucceeded = false;
            details = Problem.FILE_NOT_IN_FORMAT;
        } else {
            try {
                InputStream inputStream = new FileInputStream(fileName);
                CTEEnigma cteEnigma = deserializeFrom(inputStream);
                details = buildMachineFromCTEEnigma(cteEnigma);
                if (details != Problem.NO_PROBLEM) {
                    isSucceeded = false;
                }

            } catch (JAXBException e) {
                details = Problem.JAXB_ERROR;
                isSucceeded = false;
            } catch (FileNotFoundException e) {
                isSucceeded = false;
                details = Problem.FILE_NOT_FOUND;
            }
        }

        // resets the statistics when loading a new machine
        if (isSucceeded) {
            machineRecords.clear();
        }

        return new DTOstatus(isSucceeded, details);
    }

    /**
     * validates the CTEngine
     *
     * @return detailed problem
     */
    private Problem validateCTEEnigma(CTEEnigma cteEnigma) {
        Problem problem = Problem.NO_PROBLEM;
        CTEMachine cteMachine = cteEnigma.getCTEMachine();

        String abc = cteMachine.getABC().trim();
        abc = convertXMLSpecialCharsInSeq(abc);

        // check for alphabet length to be even
        if (abc.length() % 2 == 1) {
            return Problem.FILE_ODD_ALPHABET_AMOUNT;
        }

        // check if rotors count is not higher than 99.
        if (cteMachine.getRotorsCount() > 99) {
            return Problem.FILE_ROTOR_COUNT_HIGHER_THAN_99;
        }

        // check if rotors count is less than available rotors.
        if (cteMachine.getCTERotors().getCTERotor().size() < cteMachine.getRotorsCount()) {
            return Problem.FILE_NOT_ENOUGH_ROTORS;
        }

        // check if rotors count is less than 2
        if (cteMachine.getRotorsCount() < 2) {
            return Problem.FILE_ROTORS_COUNT_BELOW_TWO;
        }

        // check if all rotors ids are being a running counting from 1-N

        Comparator<CTERotor> CTERotorComparator = Comparator.comparingInt(CTERotor::getId);
        List<CTERotor> cteRotors = cteMachine.getCTERotors().getCTERotor();
        cteRotors.sort(CTERotorComparator);
        for (int i = 0; i < cteRotors.size(); i++) {
            if (cteRotors.get(i).getId() != i + 1) {
                return Problem.FILE_ROTOR_INVALID_ID_RANGE;
            }
            // check notch positions in cteRotors
            if (cteRotors.get(i).getNotch() > cteMachine.getABC().length() || cteRotors.get(i).getNotch() < 1) {
                return Problem.FILE_OUT_OF_RANGE_NOTCH;
            }
        }

        //check for duplicate mapping in every rotor.
        List<Boolean> rotorMappingFlags = new ArrayList<>(Collections.nCopies(abc.length(), false));
        for (CTERotor currentRotor : cteRotors) {

            // check if the current rotor's mapping is at the size of the alphabet length
            if ( currentRotor.getCTEPositioning().size() != abc.length()) {
                return Problem.FILE_ROTOR_MAPPING_NOT_EQUAL_TO_ALPHABET_LENGTH;
            }

            // goes through all positions
            for (CTEPositioning currentPosition : currentRotor.getCTEPositioning()) {

                if (currentPosition.getRight().length() != 1) {
                    return Problem.FILE_ROTOR_MAPPING_NOT_A_SINGLE_LETTER;
                }
                else if (abc.indexOf(currentPosition.getRight().charAt(0)) == -1) {
                    return Problem.FILE_ROTOR_MAPPING_NOT_IN_ALPHABET;
                }
                else {
                    if (!rotorMappingFlags.get(abc.indexOf(currentPosition.getRight().charAt(0)))) {
                        rotorMappingFlags.set(abc.indexOf(currentPosition.getRight().charAt(0)), true);
                    }
                }
            }

        }
        // if we got here safely then mapping is OK!


        // check if all reflectors ids are being a running counting from 1-N
        List<CTEReflector> cteReflectors = cteMachine.getCTEReflectors().getCTEReflector();
        List<Boolean> reflectorIDFlags = new ArrayList<>(Collections.nCopies(5, false));


        // check for reflector count < 5

        if (cteReflectors.size() > 5) {
            return Problem.FILE_TOO_MANY_REFLECTORS;
        }

        // goes through all reflectors
        for (CTEReflector cteReflector : cteReflectors) {

            int currentID = romanToDecimal(cteReflector.getId());

            // fill reflectorID flag list
            if (currentID == NOT_VALID_ROMAN_TO_DECIMAL) {
                return Problem.FILE_REFLECTOR_OUT_OF_RANGE_ID;
            }
            else if (reflectorIDFlags.get(currentID - 1)) {
                return Problem.FILE_REFLECTOR_ID_DUPLICATIONS;
            }
            else {
                reflectorIDFlags.set(currentID - 1, true);
            }

            List<CTEReflect> cteReflectPairs = cteReflector.getCTEReflect();

            // check for number of reflect pairs in each reflector
            if (cteReflectPairs.size() != abc.length() / 2) {
                return Problem.FILE_NUM_OF_REFLECTS_IS_NOT_HALF_OF_ABC;
            }

            // check for self mapping in each reflector
            for (CTEReflect cteReflectPair : cteReflectPairs) {

                if (cteReflectPair.getInput() == cteReflectPair.getOutput()) {
                    return Problem.FILE_REFLECTOR_SELF_MAPPING;
                }
            }
        }

        int firstFalse = reflectorIDFlags.indexOf(false);

        if (firstFalse != -1) {
            for (int i = firstFalse + 1; i < reflectorIDFlags.size(); i++) {
                if (reflectorIDFlags.get(i)) {
                    return Problem.FILE_REFLECTOR_INVALID_ID_RANGE;
                }
            }
        }


        return problem;
    }

    /**
     * unmarshalling the schema of provided Xml file to create jaxb classes.
     * to help build the machine from a xml file.
     *
     * @param in InputStream
     * @return jaxb generated engine
     * @throws JAXBException jaxb exception..
     */
    private static CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(in);
    }

    public static String JAXB_XML_PACKAGE_NAME = "machine.jaxb.generated";

    /**
     * convert all data from jaxb classes to our own classes.
     *
     * @param cteEnigma the engine generated from jaxb
     */
    private Problem buildMachineFromCTEEnigma(CTEEnigma cteEnigma) {

        Problem problem = validateCTEEnigma(cteEnigma);
        if (problem != Problem.NO_PROBLEM) {
            return problem;
        }
        CTEMachine cteMachine = cteEnigma.getCTEMachine();

        List<Rotor> availableRotors = new ArrayList<>();
        List<Reflector> availableReflectors = new ArrayList<>();
        int rotorsCount = cteMachine.getRotorsCount();

        // initialize alphabet and characters-2-index
        String alphabet = cteMachine.getABC().trim().toUpperCase();

        alphabet = convertXMLSpecialCharsInSeq(alphabet);

        Map<Character, Integer> character2index = new HashMap<>();
        for (int i = 0; i < alphabet.length(); i++) {
            character2index.put(alphabet.charAt(i), i);
        }

        // initialize rotors
        for (CTERotor cteRotor : cteMachine.getCTERotors().getCTERotor()) {

            int currentID = cteRotor.getId();
            int currentNotchIndex = cteRotor.getNotch() - 1;

            List<Integer> mapRotorForward = new ArrayList<>(Collections.nCopies(alphabet.length(), 0));
            List<Integer> mapRotorBackward = new ArrayList<>(Collections.nCopies(alphabet.length(), 0));
            Map<Character, Integer> forwardTranslatorChar2index = new HashMap<>();
            Map<Character, Integer> backwardTranslatorChar2index = new HashMap<>();
            int index = 0;

            // go through all right positions to build the translators Char to index
            for (CTEPositioning ctePosition : cteRotor.getCTEPositioning()) {
                forwardTranslatorChar2index.put(ctePosition.getRight().charAt(0), index);
                backwardTranslatorChar2index.put(ctePosition.getLeft().charAt(0), index);
                index++;
            }

            int indexOfLeftSide = 0;

            // go through all left positions to build the forwarding map
            for (CTEPositioning ctePosition : cteRotor.getCTEPositioning()) {
                int indexOfRightSide = forwardTranslatorChar2index.get(ctePosition.getLeft().charAt(0));
                mapRotorForward.set(indexOfRightSide, indexOfLeftSide);
                indexOfLeftSide++;
            }

            indexOfLeftSide = 0;

            // go through all right positions to build the backward map
            for (CTEPositioning ctePosition : cteRotor.getCTEPositioning()) {
                int indexOfRightSide = backwardTranslatorChar2index.get(ctePosition.getRight().charAt(0));
                mapRotorBackward.set(indexOfRightSide, indexOfLeftSide);
                indexOfLeftSide++;
            }

            // creating a new rotor
            Rotor currentRotor = new Rotor(currentID, currentNotchIndex,
                    forwardTranslatorChar2index, alphabet.length(),
                    mapRotorForward, mapRotorBackward);

            availableRotors.add(currentRotor);
        }

        //initialize reflectors
        for (CTEReflector cteReflector : cteMachine.getCTEReflectors().getCTEReflector()) {

            int currentID = romanToDecimal(cteReflector.getId());

            List<Integer> reflectorMapping = new ArrayList<>(Collections.nCopies(alphabet.length(), 0));

            // creating the mapping of the reflector
            for (CTEReflect cteReflect : cteReflector.getCTEReflect()) {
                int input = cteReflect.getInput() - 1;
                int output = cteReflect.getOutput() - 1;
                reflectorMapping.set(input, output);
                reflectorMapping.set(output, input);
            }

            // creating a new reflector
            Reflector currentReflector = new Reflector(currentID, reflectorMapping);

            availableReflectors.add(currentReflector);
        }

        // sort rotors and reflector by id
        Comparator<Rotor> rotorComparator = Comparator.comparingInt(Rotor::getId);
        availableRotors.sort(rotorComparator);
        Comparator<Reflector> reflectorComparator = Comparator.comparingInt(Reflector::getId);
        availableReflectors.sort(reflectorComparator);

        buildMachine(availableRotors, availableReflectors, rotorsCount, alphabet, character2index);

        return problem;
    }

    /**
     * fetching the current machine specifications.
     *
     * @return DTO object that represents the specs.
     */
    @Override
    public DTOspecs displayMachineSpecifications() {

        boolean isSucceeded = true;
        Problem details = Problem.NO_PROBLEM;

        List<Integer> inUseRotorsIDs = new ArrayList<>();
        String windowsCharacters = "";
        String inUseReflectorSymbol = "";
        String inUsePlugs = "";
        List<Integer> notchDistancesToWindow = new ArrayList<>();
        List<Integer> originalNotchPositions = new ArrayList<>();

        int availableRotorsCount = machine.getAvailableRotorsLen();
        int inUseRotorsCount = machine.getRotorsCount();
        int availableReflectorsCount = machine.getAvailableReflectorsLen();
        int cipheredTextsCount = getCipherCounter();

        if (machine.isConfigured()) {
            inUseRotorsIDs = machine.getInUseRotorsIDs();
            windowsCharacters = machine.getAllWindowsCharacters();
            inUseReflectorSymbol = decimalToRoman(machine.getInUseReflector().getId());
            inUsePlugs = machine.getAllPlugPairs();
            notchDistancesToWindow = machine.getInUseNotchDistanceToWindow();
            originalNotchPositions = machine.getOriginalNotchPositions();
        } else {
            details = Problem.NO_CONFIGURATION;
        }

        return new DTOspecs(isSucceeded, details, availableRotorsCount, inUseRotorsCount,
                notchDistancesToWindow, originalNotchPositions, availableReflectorsCount, cipheredTextsCount,
                inUseRotorsIDs, windowsCharacters, inUseReflectorSymbol, inUsePlugs);
    }

    /**
     * get new config from user and updating the machine configuration.
     *
     * @param rotorsIDs   list of integers
     * @param windows     string of characters
     * @param reflectorID integer
     * @param plugs       string of plugs
     * @return dto status
     */
    @Override
    public DTOstatus selectConfigurationManual(String rotorsIDs, String windows, int reflectorID, String plugs) {
        boolean isSucceed = true;
        Problem details = Problem.NO_PROBLEM;

        // convert rotors from string to list of Integers here because problem with user
        // and not with random generating so only this function get effected.

        List<Integer> rotorsIDList = new ArrayList<>();

        String[] arrayOfStringRotorsIds = rotorsIDs.split(",");

        for (int i = arrayOfStringRotorsIds.length - 1; i >= 0; i--) {

            rotorsIDList.add(Integer.parseInt(arrayOfStringRotorsIds[i]));
        }
        // here we have listOf integers representing rotors ids
        updateConfiguration(rotorsIDList, windows, reflectorID, plugs);

        return new DTOstatus(isSucceed, details);
    }

    /**
     * get new config randomly and updating the machine config.
     *
     * @return DTO object
     */
    @Override
    public DTOsecretConfig selectConfigurationAuto() {
        String alphabet = machine.getAlphabet();

        List<Integer> randomGeneratedRotorIDs = new ArrayList<>();
        int randomGeneratedReflectorID;
        StringBuilder randomGeneratedWindowCharacters = new StringBuilder();
        int randomPlugsCount = (int) Math.floor(Math.random() * (alphabet.length() + 1)) / 2;
        StringBuilder randomGeneratedPlugs = new StringBuilder();
        List<Boolean> alreadyPlugged = new ArrayList<>(Collections.nCopies(alphabet.length(), false));

        // randomizes rotors ID and order
        for (int i = 0; i < machine.getRotorsCount(); i++) {
            int randomRotorID = (int) Math.ceil(Math.random() * machine.getAvailableRotorsLen());
            while (randomGeneratedRotorIDs.contains(randomRotorID)) {
                randomRotorID = (int) Math.ceil(Math.random() * machine.getAvailableRotorsLen());
            }

            randomGeneratedRotorIDs.add(randomRotorID);
        }

        // randomizes reflector ID
        randomGeneratedReflectorID = (int) Math.ceil(Math.random() * machine.getAvailableReflectorsLen());

        // randomizes window offsets
        for (int i = 0; i < machine.getRotorsCount(); i++) {
            // get random index
            int randomIndex = (int) Math.floor(Math.random() * alphabet.length());
            // convert random index to Character from the alphabet.
            randomGeneratedWindowCharacters.append(alphabet.charAt(randomIndex));
        }

        // randomizes plugs
        for (int i = 0; i < randomPlugsCount; i++) {
            int firstInPlugIndex = (int) Math.floor(Math.random() * alphabet.length());
            int secondInPlugIndex = (int) Math.floor(Math.random() * alphabet.length());

            while (alreadyPlugged.get(firstInPlugIndex)) {
                firstInPlugIndex = (int) Math.floor(Math.random() * alphabet.length());
            }
            alreadyPlugged.set(firstInPlugIndex, true);

            while (alreadyPlugged.get(secondInPlugIndex)) {
                secondInPlugIndex = (int) Math.floor(Math.random() * alphabet.length());
            }
            alreadyPlugged.set(secondInPlugIndex, true);

            String currentPlug = "" + alphabet.charAt(firstInPlugIndex) + alphabet.charAt(secondInPlugIndex);
            randomGeneratedPlugs.append(currentPlug);
        }

        // updates the configuration
        updateConfiguration(randomGeneratedRotorIDs, randomGeneratedWindowCharacters.toString(),
                randomGeneratedReflectorID, randomGeneratedPlugs.toString());

        // get Notch Distances from window to display to user.
        List<Integer> notchDistances = new ArrayList<>();

        for (Integer rotorId : randomGeneratedRotorIDs) {
            Rotor currentRotor = machine.getRotorByID(rotorId);
            int currentNotchDistance = (currentRotor.getOriginalNotchIndex() - currentRotor.getOffset()
                    + machine.getAlphabet().length()) % machine.getAlphabet().length();
            notchDistances.add(currentNotchDistance);
        }

        return new DTOsecretConfig(randomGeneratedRotorIDs, randomGeneratedWindowCharacters.toString(),
                decimalToRoman(randomGeneratedReflectorID), randomGeneratedPlugs.toString(), notchDistances);
    }

    /**
     * validates the input text from the user and calls method "cipherText" to cipher the text.
     *
     * @param inputText string of inputted text
     * @return DTO which has the ciphered text
     */
    @Override
    public DTOciphertext cipherInputText(String inputText) {

        boolean isSucceed = false;
        String outputText = "";
        Problem problem;

        // check valid ABC
        problem = isAllCharsInAlphabet(inputText);

        if (problem.equals(Problem.NO_PROBLEM)) {
            isSucceed = true;

            long startMeasureTime = System.nanoTime();
            outputText = cipherText(inputText);
            long timeElapsed = System.nanoTime() - startMeasureTime;
            Pair<Pair<String, String>, Long> inputTextToOutputTextToTimeElapsed = new Pair<>( new Pair<>(inputText, outputText) , timeElapsed) ;

            machineRecords.get(machineRecords.size() - 1).getCipherHistory().add(inputTextToOutputTextToTimeElapsed);
        }

        return new DTOciphertext(isSucceed, problem, outputText);
    }

    private Problem isAllCharsInAlphabet(String inputText) {
        final int NOT_FOUND = -1;

        for (Character currentCharacter : inputText.toCharArray()) {
            if (machine.getAlphabet().indexOf(currentCharacter) == NOT_FOUND) {
                return Problem.CIPHER_INPUT_NOT_IN_ALPHABET;
            }
        }

        return Problem.NO_PROBLEM;
    }

    /**
     * resetting the offset of each rotor in configuration of machine to its original values.
     *
     * @return DTOResetConfig
     */
    @Override
    public DTOresetConfig resetConfiguration() {

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
     *
     * @param rotorsIDs list of integers
     * @return DTO status
     */
    @Override
    public DTOstatus validateRotors(String rotorsIDs) {
        boolean isSucceed = true;
        Problem details = Problem.NO_PROBLEM;


        // check for empty string input
        if (rotorsIDs.length() == 0) {
            isSucceed = false;
            details = Problem.ROTOR_VALIDATE_EMPTY_STRING;
        } else if (!rotorsIDs.contains(",")) {
            isSucceed = false;
            details = Problem.ROTOR_VALIDATE_NO_SEPERATOR;
        } else if (rotorsIDs.contains(" ")) {
            isSucceed = false;
            details = Problem.ROTOR_INPUT_HAS_SPACE;
        } else {
            List<Boolean> rotorIdFlags = new ArrayList<>(Collections.nCopies(machine.getAvailableRotorsLen(), false));

            // create list of Strings contains rotors id's to validate through
            String[] rotorsIdArray = rotorsIDs.split(",");


            // check if rotorsIDs size is exactly the required size.
            if (rotorsIdArray.length < machine.getRotorsCount()) {
                isSucceed = false;
                details = Problem.ROTOR_INPUT_NOT_ENOUGH_ELEMENTS;
            } else if (rotorsIdArray.length > machine.getRotorsCount()) {
                isSucceed = false;
                details = Problem.ROTOR_INPUT_TOO_MANY_ELEMENTS;
            } else {
                //check for duplicates rotors in list
                for (String rotorID : rotorsIdArray) {
                    int integerRotorId;

                    try {
                        integerRotorId = Integer.parseInt(rotorID);
                    } catch (NumberFormatException e) {
                        isSucceed = false;
                        details = Problem.ROTOR_INPUT_NUMBER_FORMAT_EXCEPTION;
                        break;
                    }

                    // check if the rotorID exists in this machine.
                    if (integerRotorId <= 0 || integerRotorId > machine.getAvailableRotorsLen()) {
                        isSucceed = false;
                        details = Problem.ROTOR_INPUT_OUT_OF_RANGE_ID;
                        break;
                    }
                    if (!rotorIdFlags.get(integerRotorId - 1)) {
                        rotorIdFlags.set(integerRotorId - 1, true);
                    } else {
                        isSucceed = false;
                        details = Problem.ROTOR_DUPLICATION;
                        break;
                    }
                }
            }
        }

        return new DTOstatus(isSucceed, details);
    }

    /**
     * validates window characters offsets input from user.
     *
     * @param windowChars string of characters
     * @return DTO-status
     */
    @Override
    public DTOstatus validateWindowCharacters(String windowChars) {
        boolean isSucceed = true;
        Problem details = Problem.NO_PROBLEM;
        final int CHAR_NOT_FOUND = -1;

        if (windowChars.length() > machine.getRotorsCount()) {
            isSucceed = false;
            details = Problem.WINDOW_INPUT_TOO_MANY_LETTERS;
        } else if (windowChars.length() < machine.getRotorsCount()) {
            isSucceed = false;
            details = Problem.WINDOW_INPUT_TOO_FEW_LETTERS;
        } else {
            for (Character currentWindowCharacter : windowChars.toCharArray()) {
                if (machine.getAlphabet().indexOf(currentWindowCharacter) == CHAR_NOT_FOUND) {
                    isSucceed = false;
                    details = Problem.WINDOWS_INPUT_NOT_IN_ALPHABET;
                    break;
                }
            }
        }

        return new DTOstatus(isSucceed, details);
    }

    /**
     * validate reflector input from the user.
     *
     * @param reflectorID integer
     * @return DTO status
     */
    @Override
    public DTOstatus validateReflector(int reflectorID) {
        boolean isSucceed = true;
        Problem details = Problem.NO_PROBLEM;

        // check if the reflectorID exists in this machine.
        if (reflectorID <= 0 || reflectorID > machine.getAvailableReflectorsLen()) {
            isSucceed = false;
            details = Problem.REFLECTOR_INPUT_OUT_OF_RANGE_ID;
        }

        return new DTOstatus(isSucceed, details);
    }

    /**
     * validate plugs input from the user.
     *
     * @param plugs string of plugs..
     * @return DTO-status
     */
    @Override
    public DTOstatus validatePlugs(String plugs) {
        boolean isSucceed = true;
        Problem details = Problem.NO_PROBLEM;
        List<Boolean> alreadyPlugged = new ArrayList<>(Collections.nCopies(machine.getAlphabet().length(), false));
        final int CHAR_NOT_FOUND = -1;


        if (plugs.length() % 2 == 1) {
            isSucceed = false;
            details = Problem.PLUGS_INPUT_ODD_ALPHABET_AMOUNT;
        } else {
            // goes through all the plugs (go through pairs)
            for (int i = 0; i < plugs.length(); i += 2) {
                int firstInPlugIndex = machine.getAlphabet().indexOf(plugs.charAt(i));
                int secondInPlugIndex = machine.getAlphabet().indexOf(plugs.charAt(i + 1));

                // check if both characters are the same.
                if (firstInPlugIndex == secondInPlugIndex) {
                    isSucceed = false;
                    details = Problem.SELF_PLUGGING;
                    break;
                }

                // check if both characters in the current plug is in the alphabet.
                if (firstInPlugIndex == CHAR_NOT_FOUND || secondInPlugIndex == CHAR_NOT_FOUND) {
                    isSucceed = false;
                    details = Problem.PLUGS_INPUT_NOT_IN_ALPHABET;
                    break;
                } else {
                    // check if both characters are not plugged yet.
                    if (!alreadyPlugged.get(firstInPlugIndex) && !alreadyPlugged.get(secondInPlugIndex)) {
                        alreadyPlugged.set(firstInPlugIndex, true);
                        alreadyPlugged.set(secondInPlugIndex, true);
                    } else {
                        isSucceed = false;
                        details = Problem.ALREADY_PLUGGED;
                        break;
                    }
                }

            }
        }

        return new DTOstatus(isSucceed, details);
    }

    public DTOstatistics getHistoryAndStatistics() {
        boolean isSucceeded = true;
        Problem details = Problem.NO_PROBLEM;

        return new DTOstatistics(isSucceeded, details, machineRecords);
    }

    @Override
    public String toString() {
        return "EnigmaEngine{" +
                "machine=" + machine +
                '}';
    }
}