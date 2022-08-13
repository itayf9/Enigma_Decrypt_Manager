import dto.*;

import java.io.IOException;

public interface Engine {

    int getRotorsCount();

    DTOstatus buildMachineFromXmlFile(String fileName);

    DTOspecs displayMachineSpecifications();

    DTOstatus selectConfigurationManual(String rotorsIDs, String windows, int reflectorID, String plugs);

    DTOsecretConfig selectConfigurationAuto();

    DTOciphertext cipherInputText(String inputText);

    DTOresetConfig resetConfiguration();

    DTOstatistics getHistoryAndStatistics();

    DTOstatus validateRotors(String rotorsIDs);

    DTOstatus validateWindowCharacters(String windowChars);

    DTOstatus validateReflector(int reflectorID);

    DTOstatus validatePlugs(String plugs);

    DTOstatus saveExistingMachineToFile(String fileName) throws IOException;

    DTOstatus loadExistingMachineFromFile(String fileName) throws IOException, ClassNotFoundException;

}