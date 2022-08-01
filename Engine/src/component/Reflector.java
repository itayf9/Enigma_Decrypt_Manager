package component;

import java.util.ArrayList;

public class Reflector {

    private int id;
    private ArrayList<Integer> mapping;

    public Reflector(int id, ArrayList<Integer> mapping) {
        this.id = id;
        this.mapping = mapping;
    }

    public int reflect(int inputIndex) {
        return mapping.get(inputIndex);
    }

    @Override
    public String toString() {
        return "Reflector{" +
                "id=" + id +
                '}';
    }
}
