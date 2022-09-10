import engine.EnigmaEngine;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static List<List<Integer>> permute(List<Integer> nums) {
        List<List<Integer>> results = new ArrayList<>();
        if (nums == null || nums.size() == 0) {
            return results;
        }
        List<Integer> result = new ArrayList<>();
        dfs(nums, results, result);
        return results;
    }

    public static void dfs(List<Integer> nums, List<List<Integer>> results, List<Integer> result) {
        if (nums.size() == result.size()) {
            List<Integer> temp = new ArrayList<>(result);
            results.add(temp);
        }
        for (int i = 0; i < nums.size(); i++) {
            if (!result.contains(nums.get(i))) {
                result.add(nums.get(i));
                dfs(nums, results, result);
                result.remove(result.size() - 1);
            }
        }
    }


    public static void main(String[] args) {

        List<Integer> list = new ArrayList<>();

        list.add(1);
        list.add(2);
        list.add(3);

        System.out.println(permute(list));

        // EnigmaEngine engine = new EnigmaEngine();
        // engine.buildMachineFromXmlFile("C:/Users/itayf/IdeaProjects/Cracking the Enigma Machine/Engine/src/resource/ex2/ex2-basic.xml");


    }
}

