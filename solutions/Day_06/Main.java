package solutions.Day_06;

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
        URI inputUri = Objects.requireNonNull(solutions.Day_06.Main.class.getResource("input.txt")).toURI();
        String content = new String(Files.readAllBytes(Paths.get(inputUri)));

        System.out.println("The answer to Problem 1 is: " + new Problem1().waysProduct(content));
        System.out.println("The answer to Problem 2 is: " + new Problem2().computeWays(content));
    }
}

class Problem1 {
    // brute force approach
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

class Problem2 {
    // quadratic solution
    public long computeWays(String content) {
        String[] sections = content.split("\\r?\\n");

        List<String> times = new LinkedList<>(Arrays.asList(sections[0].split("\\s+")));
        times.remove(0);

        List<String> distances = new LinkedList<>(Arrays.asList(sections[1].split("\\s+")));
        distances.remove(0);

        StringBuilder timeSB = new StringBuilder();
        StringBuilder distanceSB = new StringBuilder();

        for (String time : times) {
            timeSB.append(time);
        }

        for (String dist : distances) {
            distanceSB.append(dist);
        }

        long trueTime = Long.parseLong(timeSB.toString());
        long trueDist = Long.parseLong(distanceSB.toString());

        // solve for integers h s.t. h(T - h) >= D; where T and D are constant ints for trueTime and trueDists
        // => find integers h s.t. h^2 -Th + D <= 0
        //    h >= [T - sqrt(T^2 - 4D)] / 2 (lower bound; take ceil)
        //    h <= [T + sqrt(T^2 - 4D)] / 2 (upper bound; take floor)

        double sqrt = Math.sqrt(Math.pow(trueTime, 2) - 4 * trueDist);
        long lowerBound = (long) Math.ceil((trueTime - sqrt) / 2);
        long upperBound = (long) Math.floor((trueTime + sqrt) / 2);

        return upperBound - lowerBound + 1;
    }
}
