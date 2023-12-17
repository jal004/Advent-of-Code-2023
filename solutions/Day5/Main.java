package solutions.Day5;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        URI inputUri = Objects.requireNonNull(solutions.Day5.Main.class.getResource("input.txt")).toURI();
        String content = new String(Files.readAllBytes(Paths.get(inputUri)));

        System.out.println("The answer to Problem 1 is: " + new Problem1().lowestLocation(content));
        //System.out.println("The answer to Problem 2 is: " + new Problem2().solution(content));
    }
}

class Problem1 {
    long lowestLocation(String content) {
        String[] lines = content.split("(\\r\\n){2}");
        List<Long> locations = new ArrayList<>();

        String seedsLine = lines[0];
        List<String> seeds = new LinkedList<>(Arrays.asList(seedsLine.split("\\s+")));
        seeds.remove(0);

        for (String seed : seeds) {
            long currSrc = Long.parseLong(seed);

            for (int i = 1; i < lines.length; i++) {
                List<String> mapData = new LinkedList<>(Arrays.asList(lines[i].split("\\r?\\n")));
                mapData.remove(0);

                currSrc = computeDest(currSrc, mapData);
            }

            locations.add(currSrc);
        }

        return Collections.min(locations);
    }

    long computeDest(long source, List<String> mapData) {
        for (String data : mapData) {
            String[] components = data.split("\\s+");
            long destStart = Long.parseLong(components[0]);
            long srcStart = Long.parseLong(components[1]);
            long range = Long.parseLong(components[2]);

            if (source >= srcStart && source <= srcStart + range - 1) {
                long diff = Math.abs(srcStart - destStart);
                if (srcStart >= destStart) {
                    return source - diff;
                } else {
                    return source + diff;
                }
            }
        }

        return source;
    }
}

//class Problem2 {
//
//}