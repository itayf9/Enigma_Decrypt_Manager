import engine.Engine;
import engine.EnigmaEngine;
import machine.EnigmaMachine;
import machine.Machine;

public class Main {
    public static void main(String[] args) {

        EnigmaEngine engine = new EnigmaEngine();

        engine.buildMachineFromXmlFile("C:/Users/itayf/IdeaProjects/Cracking the Enigma Machine/Engine/src/resource/ex2/ex2-basic.xml");

        engine.selectConfigurationAuto();

        System.out.println(engine.getMachine().toString());

        Machine machineRef = engine.getMachine();

        Machine machineCopy = new EnigmaMachine((EnigmaMachine) machineRef);

        engine.selectConfigurationAuto();

        System.out.println(machineRef.toString());

        System.out.println(machineCopy.toString());

    }
}

