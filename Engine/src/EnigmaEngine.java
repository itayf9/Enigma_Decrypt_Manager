import component.Reflector;
import component.Rotor;
import machine.EnigmaMachine;

import java.util.ArrayList;

public class EnigmaEngine implements Engine {

    private EnigmaMachine machine;

    public EnigmaEngine(){
    }

    public void buildMachine(ArrayList<Rotor> availableRotors, ArrayList<Reflector> availableReflectors, int rotorsCount, String alphabet){
        this.machine= new EnigmaMachine(availableRotors, availableReflectors, rotorsCount, alphabet);
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
