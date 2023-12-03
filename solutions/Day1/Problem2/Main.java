package solutions.Day1.Problem2;

import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        URI inputUri = Objects.requireNonNull(Main.class.getResource("input.txt")).toURI();
        String content = new String(Files.readAllBytes(Paths.get(inputUri)));

        int result = new solutions.Day1.Problem2.Solution().computeCalibrationSum(content);
        System.out.println("\nThe answer is: " + result);
    }
}

class Solution {
    public int computeCalibrationSum(String calibrationDoc) {
        int sum = 0;
        List<String> calibrations = processCalibrations(calibrationDoc);

        for (String calibration : calibrations) {
            String firstDigit = Character.toString(calibration.charAt(0));
            String lastDigit = Character.toString(calibration.charAt(calibration.length() - 1));
            sum += Integer.parseInt(firstDigit + lastDigit);
        }

        return sum;
    }

    public List<String> processCalibrations(String calibrationDoc) {
        // values from validDigits that the possibleDigit currently matches
        List<String> matchedDigits = new ArrayList<>();
        // alphabetical characters from each raw calibration that could match a valid digit
        StringBuilder possibleDigit = new StringBuilder();

        // each calibration before processing
        String[] rawCalibrations = calibrationDoc.split("\\r?\\n");
        // processed calibration digits
        List<String> calibrationDigits = new ArrayList<>();

        for (String rawCalibration : rawCalibrations) {
            // processing each raw calibration char by char
            StringBuilder processed = new StringBuilder();
            for (int i = 0; i < rawCalibration.length(); i++) {
                char calibrationChar = rawCalibration.charAt(i);
                // digit character; add to processed calibration and erase any matching
                if (Character.isDigit(calibrationChar)) {
                    processed.append(calibrationChar);
                    possibleDigit.setLength(0);
                    matchedDigits.clear();
                }
                // non-digit character; try to match with valid digit words
                else {
                    possibleDigit.append(calibrationChar);
                    matchedDigits = getMatchingDigits(possibleDigit.toString(), matchedDigits);
                    // current string does not return any matches; remove oldest char
                    if (matchedDigits.size() == 0) {
                        possibleDigit.deleteCharAt(0);
                    }
                    // matched value
                    if (isValidDigit(possibleDigit.toString(), matchedDigits)) {
                        processed.append(convertToDigit(possibleDigit.toString()));
                        possibleDigit.setLength(0);
                        possibleDigit.append(calibrationChar);
                        matchedDigits.clear();
                    }
                }
            }

            // edge case where very last characters are a digit
            matchedDigits = getMatchingDigits(possibleDigit.toString(), matchedDigits);
            if (isValidDigit(possibleDigit.toString(), matchedDigits)) {
                processed.append(convertToDigit(possibleDigit.toString()));
            }

            possibleDigit.setLength(0);
            matchedDigits.clear();
            calibrationDigits.add(processed.toString());
        }

        return calibrationDigits;
    }

    // finds the digit words that the current string is a substring of
    public List<String> getMatchingDigits(String digitStr, List<String> matchedDigits) {
        List<String> validDigits = Arrays.asList("one", "two", "three", "four", "five", "six", "seven", "eight", "nine");
        for (String validDigit : validDigits) {
            if (validDigit.startsWith(digitStr)) {
                if (!matchedDigits.contains(validDigit)) {
                    matchedDigits.add(validDigit);
                }
            } else {
                matchedDigits.remove(validDigit);
            }
        }

        return matchedDigits;
    }

    // checks if the current string matches exactly one digit word
    public boolean isValidDigit(String digitStr, List<String> matchedDigits) {
        return (matchedDigits.size() == 1) && (digitStr.length() == matchedDigits.get(0).length());
    }

    public String convertToDigit(String digitStr) {
        String digit = "";
        switch (digitStr) {
            case "one" -> {
                digit += "1";
            }
            case "two" -> {
                digit += "2";
            }
            case "three" -> {
                digit += "3";
            }
            case "four" -> {
                digit += "4";
            }
            case "five" -> {
                digit += "5";
            }
            case "six" -> {
                digit += "6";
            }
            case "seven" -> {
                digit += "7";
            }
            case "eight" -> {
                digit += "8";
            }
            case "nine" -> {
                digit += "9";
            }
        }

        return digit;
    }
}
