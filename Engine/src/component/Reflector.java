package component;

import java.util.ArrayList;

public class Reflector {

    private int id;
    private ArrayList<Integer> mapping;


    public int reflect(int inputIndex) {
        return mapping.get(inputIndex);
    }
}
