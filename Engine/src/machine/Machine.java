package machine;

import machine.component.Reflector;
import machine.component.Rotor;

import java.util.List;

public interface Machine {

    Rotor getRotorByID(Integer id);

    void setMachineConfiguration(List<Integer> rotorsIDs, List<Integer> windowOfssets, int reflectorID, String plugs);

    char cipher(char srcChar);

    boolean isConfigured();

    String getAlphabet();

    int getRotorsCount();

    int getAvailableReflectorsLen();

    int getAvailableRotorsLen();

    List<Integer> getInUseRotorsIDs();

    List<Integer> getOriginalNotchPositions();

    String getOriginalWindowsCharacters();

    List<Integer> getInUseNotchDistanceToWindow();

    Reflector getInUseReflector();

    String getAllPlugPairs();

    List<Integer> getInUseWindowsOffsets();

    List<Rotor> getInUseRotors();

    String getCurrentWindowsCharacters();
}
