package solutions.Day_11;

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
        URI inputUri = Objects.requireNonNull(solutions.Day_11.Main.class.getResource("input.txt")).toURI();
        String content = new String(Files.readAllBytes(Paths.get(inputUri)));

        System.out.println("The answer to Problem 1 is: " + new Solution().solution(content, true));
        System.out.println("The answer to Problem 2 is: " + new Solution().solution(content, false));
    }
}

class Solution {
    public long solution(String content, boolean part1) {
        long result = 0;

        String[] sections = content.split("\\r?\\n");
        int numRows = sections.length;
        int numCols = sections[0].length();

        List<Integer[]> galaxyCoords = findGalaxyCoords(sections);
        int numGalaxies = galaxyCoords.size();

        List<Integer> emptyRows = findEmptyRows(sections);
        List<Integer> emptyCols = findEmptyCols(sections);

        for (int i = 0; i < numGalaxies; i++) {
            for (int j = i + 1; j < numGalaxies; j++) {
                long dist = computeDist(part1, galaxyCoords.get(i), galaxyCoords.get(j), emptyRows, emptyCols);
                result += dist;
            }
        }

        return result;
    }

    public List<Integer> findEmptyRows(String[] sections) {
        List<Integer> emptyRows = new ArrayList<>();
        int numRows = sections.length;
        int numCols = sections[0].length();
        for (int i = 0; i < numRows; i++) {
            boolean isEmpty = true;
            for (int j = 0; j < numCols; j++) {
                if (sections[i].charAt(j) != '.') {
                    isEmpty = false;
                    break;
                }
            }

            if (isEmpty) {
                emptyRows.add(i);
            }
        }

        return emptyRows;
    }

    public List<Integer> findEmptyCols(String[] sections) {
        List<Integer> emptyCols = new ArrayList<>();
        int numRows = sections.length;
        int numCols = sections[0].length();
        for (int i = 0; i < numCols; i++) {
            boolean isEmpty = true;
            for (String section : sections) {
                if (section.charAt(i) != '.') {
                    isEmpty = false;
                    break;
                }
            }

            if (isEmpty) {
                emptyCols.add(i);
            }
        }

        return emptyCols;
    }

    public List<Integer[]> findGalaxyCoords(String[] sections) {
        List<Integer[]> coords = new ArrayList<>();
        int numRows = sections.length;
        int numCols = sections[0].length();

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (sections[i].charAt(j) == '#') {
                    Integer[] coord = {i, j};

                    coords.add(coord);
                }
            }
        }

        return coords;
    }

    // finds Manhattan dist between two galaxies + accounts for galaxy spread (twice as large)
    public long computeDist(boolean part1, Integer[] coordA, Integer[] coordB, List<Integer> emptyRows, List<Integer> emptyCols) {
        // finding bounds wrt. galaxies
        int lowerRow = Math.min(coordA[0], coordB[0]);
        int upperRow = Math.max(coordA[0], coordB[0]);

        int lowerCol = Math.min(coordA[1], coordB[1]);
        int upperCol = Math.max(coordA[1], coordB[1]);

        long dist = 0;

        dist = getDist(part1, emptyRows, lowerRow, upperRow, dist);

        dist = getDist(part1, emptyCols, lowerCol, upperCol, dist);

        return dist;
    }

    private long getDist(boolean part1, List<Integer> emptyComponents, int lowerCol, int upperCol, long dist) {
        for (int i = lowerCol; i < upperCol; i++) {
            dist++;
            // accounting for galaxy spread in cols (diff between 1 and factor: factor - 1)
            if (emptyComponents.contains(i)) {
                if (part1) {
                    dist++;
                } else {
                    dist += Math.pow(10, 6) - 1;
                }
            }
        }
        return dist;
    }
}
