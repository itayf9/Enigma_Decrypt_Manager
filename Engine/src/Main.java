import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static List<List<Integer>> generate(int n, int k) {
        List<List<Integer>> combinations = new ArrayList<>();
        ArrayList<Integer> combination = new ArrayList<>(Collections.nCopies(k, 0));

        // initialize with the lowest lexicographic combination
        for (int i = 0; i < k; i++) {
            combination.set(i, i);
        }

        while (combination.get(k - 1) < n) {
            combinations.add((List<Integer>) combination.clone());

            // generate next combination in lexicographic order
            int t = k - 1;
            while (t != 0 && combination.get(t) == n - k + t) {
                t--;
            }
            combination.set(t, combination.get(t) + 1);
            for (int i = t + 1; i < k; i++) {
                combination.set(i, combination.get(i - 1) + 1);
            }
        }


        for (List<Integer> updatedCombination : combinations) {
            updatedCombination.replaceAll(integer -> integer + 1);
        }

        return combinations;
    }


    public static void main(String[] args) {

        // EnigmaEngine engine = new EnigmaEngine();
        // engine.buildMachineFromXmlFile("C:/Users/kingd/IdeaProjects/Cracking_the_Enigma_Machine/Engine/src/resource/ex2/ex2-basic.xml");

        List<List<Integer>> list = generate(5, 3);


        for (List<Integer> comb : list) {
            System.out.println(comb);
        }
    }
}

