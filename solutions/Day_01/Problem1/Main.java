package solutions.Day_01.Problem1;

import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        URI inputUri = Objects.requireNonNull(solutions.Day_01.Problem2.Main.class.getResource("input.txt")).toURI();
        String content = new String(Files.readAllBytes(Paths.get(inputUri)));

        int result = new solutions.Day_01.Problem1.Solution().calibrationSum(content);
        System.out.println("\nThe answer is: " + result);
    }
}

class Solution {
    public int calibrationSum(String calibrationDoc) {
        int sum = 0;
        String[] calibrations = calibrationDoc.split("\\r?\\n");

        for (String calibration : calibrations) {
            // removing all non-digit chars
            String calibrationDigits = calibration.replaceAll("\\D", "");
            String firstDigit = Character.toString(calibrationDigits.charAt(0));
            String lastDigit = Character.toString(calibrationDigits.charAt(calibrationDigits.length() - 1));

            sum += Integer.parseInt(firstDigit + lastDigit);
        }

        return sum;
    }
}
