package solutions.Day_04;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        URI inputUri = Objects.requireNonNull(solutions.Day_04.Main.class.getResource("input.txt")).toURI();
        String content = new String(Files.readAllBytes(Paths.get(inputUri)));

        System.out.println("The answer to Problem 1 is: " + new Problem1().computePoints(content));
        System.out.println("The answer to Problem 2 is: " + new Problem2().computeInstances(content));
    }
}

class Problem1 {
    public int computePoints(String content) {
        int result = 0;
        String[] cards = content.split("\\r?\\n");

        for (String card : cards) {
            int winStart = card.indexOf(':') + 2;
            if (card.charAt(winStart) == ' ') {
                winStart++;
            }
            int winEnd = card.indexOf('|') - 1;

            int currStart = winEnd + 3;
            if (card.charAt(currStart) == ' ') {
                currStart++;
            }

            String[] winNums = card.substring(winStart, winEnd).split("\\s+");
            List<String> currNums = Arrays.asList(card.substring(currStart).split("\\s+"));
            int numMatches = 0;

            for (String winNum : winNums) {
                if (currNums.contains(winNum)) {
                    numMatches++;
                }
            }

            result += Math.pow(2, (numMatches - 1));
        }

        return result;
    }
}

class Problem2 {
    public int computeInstances(String content) {
        Map<Integer, Integer> instancesMap = new HashMap<>();
        String[] cards = content.split("\\r?\\n");
        int result = 0;
        int cardNum = 1;

        for (String card : cards) {
            int currInstances = instancesMap.getOrDefault(cardNum, 0) + 1;
            instancesMap.put(cardNum, currInstances);

            int winStart = card.indexOf(':') + 2;
            if (card.charAt(winStart) == ' ') {
                winStart++;
            }
            int winEnd = card.indexOf('|') - 1;

            int currStart = winEnd + 3;
            if (card.charAt(currStart) == ' ') {
                currStart++;
            }

            String[] winNums = card.substring(winStart, winEnd).split("\\s+");
            List<String> currNums = Arrays.asList(card.substring(currStart).split("\\s+"));
            int numMatches = 0;

            for (String winNum : winNums) {
                if (currNums.contains(winNum)) {
                    numMatches++;
                }
            }

            int nextNum = cardNum + 1;
            for (int i = 0; i < numMatches; i++) {
                int nextInstances = instancesMap.getOrDefault(nextNum, 0);
                instancesMap.put(nextNum, nextInstances + currInstances);
                nextNum++;
            }

            cardNum++;
        }

        for (int value : instancesMap.values()) {
            result += value;
        }

        return result;
    }
}
