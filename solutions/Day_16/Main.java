package solutions.Day_16;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        URI inputUri = Objects.requireNonNull(solutions.Day_16.Main.class.getResource("input.txt")).toURI();
        String content = new String(Files.readAllBytes(Paths.get(inputUri)));

        System.out.println("The answer to Problem 1 is: " + new Problem1().solution(content));
        //System.out.println("The answer to Problem 2 is: " + new Problem2().solution(content));
    }
}

class Problem1 {
    public int solution(String content) {
        String[] sections = content.split("\\r?\\n");
        int numRows = sections.length;
        int numCols = sections[0].length();

        char[][] grid = new char[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                grid[i][j] = sections[i].charAt(j);
            }
        }

        Set<List<String>> states = beam(grid);

        Set<List<String>> uniqueStates = new HashSet<>();
        for (List<String> state : states) {
            uniqueStates.add(Arrays.asList(state.get(0), state.get(1)));
        }

        return uniqueStates.size();
    }

    // states are in stored in lists of format: {row, col, direction}
    public Set<List<String>> beam(char[][] grid) {
        int numRows = grid.length;
        int numCols = grid[0].length;

        Set<List<String>> accessed = new HashSet<>();
        Queue<List<String>> queue = new LinkedList<>();
        queue.add(Arrays.asList("0", "0", ">"));

        while (!queue.isEmpty()) {
            List<String> currState = queue.poll();

            if (!accessed.contains(currState)) {
                accessed.add(currState);
                int rowIdx = Integer.parseInt(currState.get(0));
                int colIdx = Integer.parseInt(currState.get(1));
                char symbol = grid[rowIdx][colIdx];

                List<List<String>> newStates = getNewStates(currState, symbol, numRows, numCols);

                if (!newStates.isEmpty()) {
                    queue.addAll(newStates);
                }
            }
        }

        return accessed;
    }

    public List<List<String>> getNewStates(List<String> currState, char symbol, int numRows, int numCols) {
        List<List<String>> result = new ArrayList<>();
        int rowIdx = Integer.parseInt(currState.get(0));
        int colIdx = Integer.parseInt(currState.get(1));
        char dir = currState.get(2).charAt(0);

        // splitter cases
        if (symbol == '|' || symbol == '-') {
            List<List<String>> beam1 = getNewStates(currState, '/', numRows, numCols);
            List<List<String>> beam2 = getNewStates(currState, '\\', numRows, numCols);
            if (symbol == '|') {
                // flat side; split into two
                if (dir == '>' || dir == '<') {
                    // add both beams to return
                    result = beam1;
                    result.addAll(beam2);
                    return result;
                } else { // go straight through it
                    return getNewStates(currState, '.', numRows, numCols);
                }
            }
            // '-' symbol case
            if (dir == '^' || dir == 'v') {
                result = beam1;
                result.addAll(beam2);
                return result;
            } else {
                return getNewStates(currState, '.', numRows, numCols);
            }
        }

        // otherwise, one of the other symbols:
        List<String> offsets = getOffsets(symbol, dir);
        int nextRowIdx = rowIdx + Integer.parseInt(offsets.get(0));
        int nextColIdx = colIdx + Integer.parseInt(offsets.get(1));
        char newDir = offsets.get(2).charAt(0);

        if (nextRowIdx >= 0 && nextRowIdx < numRows && nextColIdx >= 0 && nextColIdx < numCols) {
            result.add(Arrays.asList(Integer.toString(nextRowIdx), Integer.toString(nextColIdx), Character.toString(newDir)));
        }
        return result;
    }

    // offset format: (rowOffset, colOffset, newDir)
    public List<String> getOffsets(char symbol, char dir) {
        List<String> offsets = new ArrayList<>();
        switch (symbol) {
            case '.' -> {
                switch (dir) {
                    case '^' -> {
                        offsets.add("-1");
                        offsets.add("0");
                        offsets.add("^");
                    }
                    case 'v' -> {
                        offsets.add("1");
                        offsets.add("0");
                        offsets.add("v");
                    }
                    case '<' -> {
                        offsets.add("0");
                        offsets.add("-1");
                        offsets.add("<");
                    }
                    case '>' -> {
                        offsets.add("0");
                        offsets.add("1");
                        offsets.add(">");
                    }
                }
            }
            case '\\' -> {
                switch (dir) {
                    case '^' -> {
                        offsets.add("0");
                        offsets.add("-1");
                        offsets.add("<");
                    }
                    case 'v' -> {
                        offsets.add("0");
                        offsets.add("1");
                        offsets.add(">");
                    }
                    case '<' -> {
                        offsets.add("-1");
                        offsets.add("0");
                        offsets.add("^");
                    }
                    case '>' -> {
                        offsets.add("1");
                        offsets.add("0");
                        offsets.add("v");
                    }
                }
            }
            case '/' -> {
                switch (dir) {
                    case '^' -> {
                        offsets.add("0");
                        offsets.add("1");
                        offsets.add(">");
                    }
                    case 'v' -> {
                        offsets.add("0");
                        offsets.add("-1");
                        offsets.add("<");
                    }
                    case '<' -> {
                        offsets.add("1");
                        offsets.add("0");
                        offsets.add("v");
                    }
                    case '>' -> {
                        offsets.add("-1");
                        offsets.add("0");
                        offsets.add("^");
                    }
                }
            }
        }

        return offsets;
    }
}

//class Problem2 {
//    public int solution(String content) {
//        int result = 0;
//
//        return result;
//    }
//}
