package solutions.Day6;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        URI inputUri = Objects.requireNonNull(solutions.Day6.Main.class.getResource("input.txt")).toURI();
        String content = new String(Files.readAllBytes(Paths.get(inputUri)));

        System.out.println("The answer to Problem 1 is: " + new Problem1().waysProduct(content));
        //System.out.println("The answer to Problem 2 is: " + new Problem2().solution(content));
    }
}

class Problem1 {
    public int waysProduct(String content) {
        int result = 1;
        String[] sections = content.split("\\r?\\n");

        List<String> times = new LinkedList<>(Arrays.asList(sections[0].split("\\s+")));
        times.remove(0);

        List<String> distances = new LinkedList<>(Arrays.asList(sections[1].split("\\s+")));
        distances.remove(0);

        for (int i = 0; i < times.size(); i++) {
            int ways = 0;
            int holdTime = 1;
            int time = Integer.parseInt(times.get(i));
            int dist = Integer.parseInt(distances.get(i));

            while (holdTime < time) {
                // d = rt; d = travel, r = holdTime, t = time - holdTime
                int travel = holdTime * (time - holdTime);

                if (travel >= dist) {
                    ways++;
                }

                holdTime++;
            }

            result *= ways;
        }

        return result;
    }
}

//class Problem2 {
//
//}
