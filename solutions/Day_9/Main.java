package solutions.Day_9;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        URI inputUri = Objects.requireNonNull(solutions.Day_9.Main.class.getResource("input.txt")).toURI();
        String content = new String(Files.readAllBytes(Paths.get(inputUri)));

        System.out.println("The answer to Problem 1 is: " + new Problem1().solution(content));
        System.out.println("The answer to Problem 2 is: " + new Problem2().solution(content));
    }
}

class Problem1 {
    // recursive approach
    public int solution(String content) {
        int result = 0;

        String[] lines = content.split("\\r?\\n");

        for (String line : lines) {
            String[] numbers = line.split(" ");
            int[] seq = new int[numbers.length];

            for (int i = 0; i < seq.length; i++) {
                seq[i] = Integer.parseInt(numbers[i]);
            }

            int next = seq[seq.length - 1] + extrapolate(seq);
            result += next;
        }

        return result;
    }

    public int extrapolate(int[] seq) {
        int[] diffs = new int[seq.length - 1];

        // upper bound is length of seq; iterating over all elements in seq
        for (int i = 1; i < seq.length; i++) {
            diffs[i - 1] = seq[i] - seq[i - 1];
        }

        // check if all diffs are 0
        if (Arrays.stream(diffs).allMatch(x -> x == 0)) {
            return 0;
        }

        return diffs[diffs.length - 1] + extrapolate(diffs);
    }
}

class Problem2 {
    // subtract first number from recursed result now
    public int solution(String content) {
        int result = 0;

        String[] lines = content.split("\\r?\\n");

        for (String line : lines) {
            String[] numbers = line.split(" ");
            int[] seq = new int[numbers.length];

            for (int i = 0; i < seq.length; i++) {
                seq[i] = Integer.parseInt(numbers[i]);
            }

            int next = seq[0] - extrapolate2(seq);
            result += next;
        }

        return result;
    }

    public int extrapolate2(int[] seq) {
        int[] diffs = new int[seq.length - 1];

        // upper bound is length of seq; iterating over all elements in seq
        for (int i = 1; i < seq.length; i++) {
            diffs[i - 1] = seq[i] - seq[i - 1];
        }

        // check if all diffs are 0
        if (Arrays.stream(diffs).allMatch(x -> x == 0)) {
            return 0;
        }

        return diffs[0] - extrapolate2(diffs);
    }
}
