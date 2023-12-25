package solutions.Day_13;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        URI inputUri = Objects.requireNonNull(solutions.Day_13.Main.class.getResource("input.txt")).toURI();
        String content = new String(Files.readAllBytes(Paths.get(inputUri)));

        System.out.println("The answer to Problem 1 is: " + new Problem1().solution(content));
        //System.out.println("The answer to Problem 2 is: " + new Problem2().solution(content));
    }
}

class Problem1 {
    public int solution(String content) {
        int result = 0;

        String[] sections = content.split("(\\r\\n){2}");

        for (String section : sections) {
            String[] sectionLines = section.split("\\r?\\n");
            int gridRows = sectionLines.length;
            int gridCols = sectionLines[0].length();

            char[][] grid = new char[gridRows][gridCols];

            for (int i = 0; i < gridRows; i++) {
                for (int j = 0; j < gridCols; j++) {
                    grid[i][j] = sectionLines[i].charAt(j);
                }
            }

            // used to keep track of which type of line of reflection is found; only one will exist in each grid
            boolean vert = true;

            // check horiz symm. first
            for (int rowIdx = 0; rowIdx < gridRows - 1; rowIdx++) {
                if (horizSymm(grid, rowIdx)) {
                    result += (rowIdx + 1) * 100;
                    vert = false;
                    break;
                }
            }

            if (vert) {
                char[][] transposedGrid = transpose(grid);
                for (int colIdx = 0; colIdx < gridCols - 1; colIdx++) {
                    if (horizSymm(transposedGrid, colIdx)) {
                        result += colIdx + 1;
                        break;
                    }
                }
            }
        }

        return result;
    }

    // checks if the grid is horizontally symm. about the line directly below a given row index
    // by checking if every column is symmetric about a row
    boolean horizSymm(char[][] grid, int row) {
        int gridRows = grid.length;
        int gridCols = grid[0].length;

        for (int colIdx = 0; colIdx < gridCols; colIdx++) {
            for (int rowIdx = 0; rowIdx < gridRows; rowIdx++) {
                int reflectedRowIdx = row * 2 + 1 - rowIdx;
                // reflected row is out of bounds; try next row
                if (reflectedRowIdx < 0 || reflectedRowIdx >= gridRows) {
                    continue;
                }
                if (grid[rowIdx][colIdx] != grid[reflectedRowIdx][colIdx]) {
                    return false;
                }
            }
        }

        return true;
    }

    // rotates the grid 90 degrees in order to check vertical symm. using horiz symm method
    public char[][] transpose(char[][] oldGrid) {
        int numRows = oldGrid.length;
        int numCols = oldGrid[0].length;

        char[][] newGrid = new char[numCols][numRows];

        for (int i = 0; i < numCols; i++) {
            for (int j = 0; j < numRows; j++) {
                newGrid[i][j] = oldGrid[j][i];
            }
        }

        return newGrid;
    }
}

//class Problem2 {
//    public int solution(String content) {
//        int result = 0;
//
//        return result;
//    }
//}
