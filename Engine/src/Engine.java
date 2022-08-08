import dto.*;

import java.util.List;

public interface Engine {
    public void buildMachineFromXmlFile(String fileName);

    public DTOspecs displayMachineSpecifications();

    public DTOstatus selectConfigurationManual(List<Integer> rotorsIDs, String windows, int reflectorID, List<String> plugs);

    public DTOsecretConfig selectConfigurationAuto();

    public DTOciphertext cipherInputText(String inputText);

    public DTOresetConfig resetConfiguration();

    public DTOstatus validateRotors(List<Integer> rotorsIDs);

    public DTOstatus validateWindowCharacters(String windowChars);

    public DTOstatus validateReflector(int reflectorID);

    public DTOstatus validatePlugs(List<String> plugs);


}