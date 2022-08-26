package utill;

import dto.DTOsecretConfig;
import dto.DTOspecs;

public class Utillity {

    public static DTOsecretConfig getOriginalConfigFromSpecs(DTOspecs specs) {
        return new DTOsecretConfig(specs.isSucceed(), specs.getDetails(), specs.getInUseRotorsIDs(),
                specs.getOriginalWindowsCharacters(), specs.getInUseReflectorSymbol(), specs.getInUsePlugs(),
                specs.getOriginalNotchPositions());
    }

    public static DTOsecretConfig getCurrentConfigFromSpecs(DTOspecs specs) {
        return new DTOsecretConfig(specs.isSucceed(), specs.getDetails(), specs.getInUseRotorsIDs(),
                specs.getCurrentWindowsCharacters(), specs.getInUseReflectorSymbol(), specs.getInUsePlugs(),
                specs.getNotchDistancesToWindow());
    }
}
