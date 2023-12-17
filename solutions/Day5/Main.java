package solutions.Day5;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        URI inputUri = Objects.requireNonNull(solutions.Day5.Main.class.getResource("input.txt")).toURI();
        String content = new String(Files.readAllBytes(Paths.get(inputUri)));

        System.out.println("The answer to Problem 1 is: " + new Problem1().lowestLocation(content));
        System.out.println("The answer to Problem 2 is: " + new Problem2().lowestLocation2(content));
    }
}

class Problem1 {
    public long lowestLocation(String content) {
        String[] sections = content.split("(\\r\\n){2}");
        List<Long> locations = new ArrayList<>();

        String seedsLine = sections[0];
        List<String> seeds = new LinkedList<>(Arrays.asList(seedsLine.split("\\s+")));
        seeds.remove(0);

        for (String seed : seeds) {
            long currSrc = Long.parseLong(seed);

            for (int i = 1; i < sections.length; i++) {
                List<String> mapData = new LinkedList<>(Arrays.asList(sections[i].split("\\r?\\n")));
                mapData.remove(0);

                currSrc = computeDest(currSrc, mapData);
            }

            locations.add(currSrc);
        }

        return Collections.min(locations);
    }

    public long computeDest(long source, List<String> mapData) {
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

class Problem2 {
    // working backwards approach; brute force, but a lot better (~2.5m iterations)
    public long lowestLocation2(String content) {
        long location = 0;
        String[] sections = content.split("(\\r\\n){2}");

        String seedsLine = sections[0];
        List<String> seeds = new LinkedList<>(Arrays.asList(seedsLine.split("\\s+")));
        seeds.remove(0);

        List<List<Long>> seedRanges = new ArrayList<>();

        for (int i = 0; i < seeds.size(); i += 2) {
            List<Long> seedRange = new ArrayList<>();

            long seedStart = Long.parseLong(seeds.get(i));
            long range = Long.parseLong(seeds.get(i + 1));
            long seedEnd = seedStart + range - 1;

            seedRange.add(seedStart);
            seedRange.add(seedEnd);

            seedRanges.add(seedRange);
        }

        while (true) {
            long currDest = location;
            for (int j = sections.length - 1; j >= 0; j--) {
                List<String> mapData = new LinkedList<>(Arrays.asList(sections[j].split("\\r?\\n")));
                mapData.remove(0);

                currDest = computeSrc(currDest, mapData);
            }

            for (List<Long> seedRange : seedRanges) {
                long seedStart = seedRange.get(0);
                long seedEnd = seedRange.get(1);

                if (currDest >= seedStart && currDest <= seedEnd) {
                    return location;
                }
            }

            location++;
        }
    }

    // used to find seed number based on destination; moving backwards through maps
    public long computeSrc(long dest, List<String> mapData) {
        for (String data : mapData) {
            String[] components = data.split("\\s+");
            long destStart = Long.parseLong(components[0]);
            long srcStart = Long.parseLong(components[1]);
            long range = Long.parseLong(components[2]);

            if (dest >= destStart && dest <= destStart + range - 1) {
                long diff = Math.abs(srcStart - destStart);
                if (srcStart >= destStart) {
                    return dest + diff;
                } else {
                    return dest - diff;
                }
            }
        }

        return dest;
    }
}