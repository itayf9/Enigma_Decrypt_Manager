package machine.component;

import java.io.Serializable;
import java.util.List;

public class Reflector implements Serializable {

    private int id;
    private List<Integer> mapping;


    /**
     * constructor for Reflector
     * @param id the unique id of the rotor
     * @param mapping a mapping that connects pairs of entries in the reflector
     */
    public Reflector(int id, List<Integer> mapping) {
        this.id = id;
        this.mapping = mapping;
    }


    /**
     *
     * @return the unique id of the rotor
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param inputIndex an index of one entry
     * @return the index of the matching entry
     */
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
