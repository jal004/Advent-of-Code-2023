package solutions.Day_8;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        URI inputUri = Objects.requireNonNull(solutions.Day_8.Main.class.getResource("input.txt")).toURI();
        String content = new String(Files.readAllBytes(Paths.get(inputUri)));

        System.out.println("The answer to Problem 1 is: " + new Problem1().solution(content));
        //System.out.println("The answer to Problem 2 is: " + new Problem2().solution(content));
    }
}

class Problem1 {
    public int solution(String content) {
        int result = 0;

        String[] sections = content.split("(\\r\\n){2}");

        String instrSection = sections[0];
        char[] dirs = instrSection.toCharArray(); // directions to go from start to dest

        String pathsSection = sections[1];
        String[] paths = pathsSection.split("\\r?\\n");

        Map<String, List<String>> pathsMap = new HashMap<>();

        for (String path : paths) {
            String[] pathSections = path.split(" = ");
            String src = pathSections[0];
            List<String> dests = new ArrayList<>(
                    Arrays.asList(pathSections[1]
                            .substring(1, pathSections[1].length() - 1) // getting rid of parentheses
                            .split(", "))
            );

            pathsMap.put(src, dests);
        }

        int currDirIdx = 0;
        char currDir = dirs[currDirIdx];
        String currStart = "AAA";
        List<String> currDests = pathsMap.get(currStart);

        while (!currStart.equals("ZZZ")) {
            if (currDir == 'L') {
                currStart = currDests.get(0);
            } else { // move right
                currStart = currDests.get(1);
            }

            result++;
            currDests = pathsMap.get(currStart);

            // ensure that directions are traversed circularly (end loops back to start)
            currDirIdx++;
            if (currDirIdx == dirs.length) {
                currDirIdx = 0;
            }

            currDir = dirs[currDirIdx];
        }

        return result;
    }
}

//class Problem2 {
//    public int solution(String content) {
//
//    }
//}


