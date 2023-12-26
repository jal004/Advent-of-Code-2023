package solutions.Day_15;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        URI inputUri = Objects.requireNonNull(solutions.Day_15.Main.class.getResource("input.txt")).toURI();
        String content = new String(Files.readAllBytes(Paths.get(inputUri)));

        System.out.println("The answer to Problem 1 is: " + new Problem1().solution(content));
        System.out.println("The answer to Problem 2 is: " + new Problem2().solution(content));
    }
}

class Problem1 {
    public int solution(String content) {
        int result = 0;

        String[] values = content.split(",");
        for (String value : values) {
            result += hash(value);
        }

        return result;
    }

    public int hash(String value) {
        char[] chars = value.toCharArray();
        int result = 0;

        for (char c : chars) {
            result += c;
            result *= 17;
            result %= 256;
        }

        return result;
    }
}

class Problem2 {
    public int solution(String content) {
        Problem1 p1 = new Problem1();

        int result = 0;
        // elements in each box are strings of format: "<key> <value>"
        List<List<String>> boxes = new ArrayList<>(256);
        for (int i = 0; i < 256; i++) {
            boxes.add(new LinkedList<>());
        }

        String[] values = content.split(",");
        for (String value : values) {
            // delete lens from box
            if (value.contains("-")) {
                String lensKey = value.split("-")[0];

                int boxNum = p1.hash(lensKey);
                List<String> box = boxes.get(boxNum);

                int removeIdx = indexOfLens(box, lensKey);
                if (removeIdx != -1) {
                    box.remove(removeIdx);
                }
                continue;
            }
            // add / update lens in box
            if (value.contains("=")) {
                String lensKey = value.split("=")[0];
                String lensValue = value.split("=")[1];
                String lens = lensKey + " " + lensValue;

                int boxNum = p1.hash(lensKey);
                List<String> box = boxes.get(boxNum);
                int addIdx = indexOfLens(box, lensKey);

                // update
                if (addIdx != -1) {
                    box.set(addIdx, lens);
                } else { // add
                    box.add(lens);
                }
            }
        }


        int boxNum = 1; // keeps track of which box we are currently on
        for (List<String> box : boxes) {
            int lensNum = 1; // keeps track of which lens in the box we are currently on
            for (String lens : box) {
                int focal = Integer.parseInt(lens.split(" ")[1]);
                result += boxNum * lensNum * focal;
                lensNum++;
            }
            boxNum++;
        }

        return result;
    }

    // finds the index of the lens in a box based on the key; returns -1 if dne
    public int indexOfLens(List<String> box, String key) {
        int idx = 0;
        // find idx in list based on first word in string (before space char)
        for (String lens : box) {
            String lensKey = lens.split(" ")[0];
            if (!key.equals(lensKey)) {
                idx++;
            } else {
                return idx;
            }
        }

        // value does not exist in list
        return -1;
    }
}


