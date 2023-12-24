package solutions.Day_12;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        URI inputUri = Objects.requireNonNull(solutions.Day_12.Main.class.getResource("input.txt")).toURI();
        String content = new String(Files.readAllBytes(Paths.get(inputUri)));

        System.out.println("The answer to Problem 1 is: " + new Problem1().solution(content));
        //System.out.println("The answer to Problem 2 is: " + new Problem2().solution(content));
    }
}

class Problem1 {
    // brute force approach
    public int solution(String content) {
        int result = 0;

        String[] sections = content.split("\\r?\\n");

        for (String section : sections) {
            String[] components = section.split(" ");
            // padding for simpler recursion checks
            String config = "." + components[0] + ".";
            String[] lengths = components[1].split(",");

            List<Integer> groups = new LinkedList<>();
            for (String length : lengths) {
                groups.add(Integer.parseInt(length));
            }

            result += getCfgCounts(config, groups);
        }

        return result;
    }

    public int getCfgCounts(String config, List<Integer> groups) {
        if (groups.isEmpty()) {
            if (config.contains("#")) {
                return 0;
            }
            return 1;
        }

        int size = groups.get(0);
        groups = groups.subList(1, groups.size());

        int count = 0;
        for (int end = 0; end < config.length(); end++) {
            int start = end - size + 1;
            if (valid(config, start, end)) {
                count += getCfgCounts(config.substring(end + 1), groups);
            }
        }

        return count;
    }

    public boolean valid(String config, int start, int end) {
        if (start - 1 < 0 || end + 1 == config.length()) {
            return false;
        }
        if (config.charAt(start - 1) == '#' || config.charAt(end + 1) == '#') {
            return false;
        }
        if (config.substring(0, start).contains("#")) {
            return false;
        }
        for (int i = start; i < end + 1; i++) {
            if (config.charAt(i) == '.') {
                return false;
            }
        }

        return true;
    }
}
//class Problem2 {
//    public int solution(String content) {
//        int result = 0;
//
//        return result;
//    }
//}
