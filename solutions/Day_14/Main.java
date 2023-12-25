package solutions.Day_14;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        URI inputUri = Objects.requireNonNull(solutions.Day_14.Main.class.getResource("input.txt")).toURI();
        String content = new String(Files.readAllBytes(Paths.get(inputUri)));

        System.out.println("The answer to Problem 1 is: " + new Problem1().solution(content));
        //System.out.println("The answer to Problem 2 is: " + new Problem2().solution(content));
    }
}

class Problem1 {
    // iterate through all cols and find the load of the rocks in the col
    public int solution(String content) {
        int result = 0;

        String[] sections = content.split("\\r?\\n");
        int numCols = sections[0].length();

        for (int i = 0; i < numCols; i++) {
            result += colLoad(sections, i);
        }

        return result;
    }

    // returns the load of rocks at a given column index
    public int colLoad(String[] sections, int colIdx) {
        int numRows = sections.length;
        int result = 0;
        int rowIdx = 0;

        while (rowIdx < numRows) {
            // skip cubes
            if (sections[rowIdx].charAt(colIdx) == '#') {
                rowIdx++;
            }
            else {
                int numRocks = 0;
                int rowStart = rowIdx;

                // keep track of rocks until next cube is found
                while (rowIdx < numRows && sections[rowIdx].charAt(colIdx) != '#') {
                    if (sections[rowIdx].charAt(colIdx) == 'O') {
                        numRocks++;
                    }
                    rowIdx++;
                }

                // add load of rocks between cubes
                for (int i = rowStart; i < rowStart + numRocks; i++) {
                    result += numRows - i;
                }
            }
        }

        return result;
    }
}
