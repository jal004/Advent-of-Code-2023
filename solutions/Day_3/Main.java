package solutions.Day_3;

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
        URI inputUri = Objects.requireNonNull(solutions.Day_3.Main.class.getResource("input.txt")).toURI();
        String content = new String(Files.readAllBytes(Paths.get(inputUri)));

        System.out.println("The answer to Problem 1 is: " + new Problem1().solution(content));
        System.out.println("The answer to Problem 2 is: " + new Problem2().solution(content));
    }
}

class Problem1 {
    // find each numerical value in the input, then look for symbols that are adjacent to the number;
    // any number with at least one adjacent symbol will be added to a var to return
    public int solution(String content) {
        int result = 0;
        String[] sections = content.split("\\r?\\n");
        int numRows = sections.length;
        int numCols = sections[0].length();

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                char currChar = sections[i].charAt(j);
                if (Character.isDigit(currChar)) {
                    StringBuilder currNum = new StringBuilder();
                    currNum.append(currChar);
                    int startIdx = j - 1 == -1 ? j : j - 1;

                    while (++j != numCols && Character.isDigit(sections[i].charAt(j))) {
                         currNum.append(sections[i].charAt(j));
                    }

                    int endIdx = j == numCols ? j - 1 : j;

                    // finding adjacent symbols
                    // 1. curr row
                    char currStart = sections[i].charAt(startIdx);
                    char currEnd = sections[i].charAt(endIdx);
                    if (isSymbol(currStart) || isSymbol(currEnd)) {
                        result += Integer.parseInt(currNum.toString());
                        continue;
                    }

                    // 2. top row
                    if (i - 1 != -1) {
                        boolean topFound = false;
                        for (int top = startIdx; top <= endIdx; top++) {
                            char topChar = sections[i - 1].charAt(top);
                            if (isSymbol(topChar)) {
                                result += Integer.parseInt(currNum.toString());
                                topFound = true;
                                break;
                            }
                        }

                        if (topFound) {
                            continue;
                        }
                    }

                    // 3. bottom row
                    if (i + 1 != numRows) {
                        for (int bot = startIdx; bot <= endIdx; bot++) {
                            char botChar = sections[i + 1].charAt(bot);
                            if (isSymbol(botChar)) {
                                result += Integer.parseInt(currNum.toString());
                                break;
                            }
                        }
                    }

                }
            }
        }

        return result;
    }

    // a "symbol" is a character that is neither a digit nor a period
    public boolean isSymbol(char c) {
        return !(Character.isDigit(c) || c == '.');
    }
}

class Problem2 {
    // uses part 1 solution, but keeps track of the gears in each position and their adjacent number(s);
    // return the product of numbers adjacent to a gear if exactly two are adjacent
    public int solution(String content) {
        int result = 0;

        String[] sections = content.split("\\r?\\n");
        int numRows = sections.length;
        int numCols = sections[0].length();

        // init gears 2d array of lists
        List<Integer>[][] gears = new ArrayList[numRows][numCols];
        for (int gr = 0; gr < numRows; gr++) {
            for (int gc = 0; gc < numCols; gc++) {
                gears[gr][gc] = new ArrayList<>(2);
            }
        }

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                char currChar = sections[i].charAt(j);
                if (Character.isDigit(currChar)) {
                    StringBuilder currNum = new StringBuilder();
                    currNum.append(currChar);
                    int startIdx = j - 1 == -1 ? j : j - 1;

                    while (++j != numCols && Character.isDigit(sections[i].charAt(j))) {
                        currNum.append(sections[i].charAt(j));
                    }

                    int endIdx = j == numCols ? j - 1 : j;

                    // finding adjacent symbols
                    // 1. curr row
                    char currStart = sections[i].charAt(startIdx);
                    char currEnd = sections[i].charAt(endIdx);

                    if (isGear(currStart)) {
                        gears[i][startIdx].add(Integer.parseInt(currNum.toString()));
                    }

                    if (isGear(currEnd)) {
                        gears[i][endIdx].add(Integer.parseInt(currNum.toString()));
                    }

                    // 2. top row
                    if (i - 1 != -1) {
                        boolean topFound = false;
                        for (int top = startIdx; top <= endIdx; top++) {
                            char topChar = sections[i - 1].charAt(top);
                            if (isGear(topChar)) {
                                gears[i - 1][top].add(Integer.parseInt(currNum.toString()));
                            }
                        }
                    }

                    // 3. bottom row
                    if (i + 1 != numRows) {
                        for (int bot = startIdx; bot <= endIdx; bot++) {
                            char botChar = sections[i + 1].charAt(bot);
                            if (isGear(botChar)) {
                                gears[i + 1][bot].add(Integer.parseInt(currNum.toString()));
                            }
                        }
                    }

                }
            }
        }

        for (int k = 0; k < numRows; k++) {
            for (int l = 0; l < numCols; l++) {
                List<Integer> pos = gears[k][l];
                if (pos.size() == 2) {
                    result += pos.get(0) * pos.get(1);
                }
            }
        }

        return result;
    }

    public boolean isGear(char c) {
        return c == '*';
    }
}
