import dto.*;

import java.util.List;

public interface Engine {

    int getRotorsCount();

    void buildMachineFromXmlFile(String fileName);

    DTOspecs displayMachineSpecifications();

    DTOstatus selectConfigurationManual(List<Integer> rotorsIDs, String windows, int reflectorID, List<String> plugs);

    DTOsecretConfig selectConfigurationAuto();

    DTOciphertext cipherInputText(String inputText);

    DTOresetConfig resetConfiguration();

    DTOstatus validateRotors(List<Integer> rotorsIDs);

    DTOstatus validateWindowCharacters(String windowChars);

    DTOstatus validateReflector(int reflectorID);

    DTOstatus validatePlugs(List<String> plugs);


}