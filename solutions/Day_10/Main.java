package solutions.Day_10;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        URI inputUri = Objects.requireNonNull(solutions.Day_10.Main.class.getResource("input.txt")).toURI();
        String content = new String(Files.readAllBytes(Paths.get(inputUri)));

        System.out.println("The answer to Problem 1 is: " + new Problem1().solution(content));
        //System.out.println("The answer to Problem 2 is: " + new Problem2().solution(content));
    }
}

// helper classes
class Point {
    int rowIdx;
    int colIdx;
    public Point(int rowIdx, int colIdx) {
        this.rowIdx = rowIdx;
        this.colIdx = colIdx;
    }
    public int getRowIdx() {
        return rowIdx;
    }
    public int getColIdx() {
        return colIdx;
    }

    public static Point add(Point a, Point b) {
        int addedRows = a.getRowIdx() + b.getRowIdx();
        int addedCols = a.getColIdx() + b.getColIdx();
        return new Point(addedRows, addedCols);
    }

    // need to override equals and hashcode methods to use this obj as a key in hashmaps;
    // just use the intellij wizard to do this next time (top menu -> code -> generate);
    // it didn't like me overriding hashcode() by hand for some reason
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Point pt)) {
            return false;
        }
        return this.rowIdx == pt.rowIdx && this.colIdx == pt.colIdx;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowIdx, colIdx);
    }
}

class PointDist {
    Point point;
    int dist;

    public PointDist(Point point, int dist) {
        this.point = point;
        this.dist = dist;
    }

    Point getPoint() {
        return point;
    }

    int getDist() {
        return dist;
    }
}

class Problem1 {
    static Map<String, Point> dirToPt = getDirMap();
    static Map<List<Point>, String> dirsToPipe = getDirsToPipeMap();
    static Map<String, List<Point>> pipeToDirs = getPipeToDirsMap();

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

        Point start = getStartingPt(grid);
        grid = updateStart(grid, start);
        Map<Point, List<Point>> adj = getAdjacency(grid);

        return bfs(adj, start);
    }

    public Point getStartingPt(char[][] grid) {
        int numRows = grid.length;
        int numCols = grid[0].length;

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (grid[i][j] == 'S') {
                    return new Point(i, j);
                }
            }
        }

        return null;
    }

    public char[][] updateStart(char[][] grid, Point start) {
        int rowIdx = start.getRowIdx();
        int colIdx = start.getColIdx();

        List<Point> connections = getConnections(grid, start);
        grid[rowIdx][colIdx] = dirsToPipe.get(connections).charAt(0);

        return grid;
    }

    public List<Point> getConnections(char[][] grid, Point pos) {
        List<Point> connections = new ArrayList<>();

        Point up = Point.add(pos, dirToPt.get("UP"));
        Point down = Point.add(pos, dirToPt.get("DOWN"));
        Point left = Point.add(pos, dirToPt.get("LEFT"));
        Point right = Point.add(pos, dirToPt.get("RIGHT"));

        List<Character> upPipes = Arrays.asList('|', '7', 'F');
        List<Character> downPipes = Arrays.asList('|', 'L', 'J');
        List<Character> leftPipes = Arrays.asList('-', 'F', 'L');
        List<Character> rightPipes = Arrays.asList('-', '7', 'J');

        if (upPipes.contains(grid[up.getRowIdx()][up.getColIdx()])) {
            connections.add(dirToPt.get("UP"));
        }
        if (downPipes.contains(grid[down.getRowIdx()][down.getColIdx()])) {
            connections.add(dirToPt.get("DOWN"));
        }
        if (leftPipes.contains(grid[left.getRowIdx()][left.getColIdx()])) {
            connections.add(dirToPt.get("LEFT"));
        }
        if (rightPipes.contains(grid[right.getRowIdx()][right.getColIdx()])) {
            connections.add(dirToPt.get("RIGHT"));
        }

        return connections;
    }

    public Map<Point, List<Point>> getAdjacency(char[][] grid) {
        Map<Point, List<Point>> adj = new HashMap<>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                String curr = Character.toString(grid[i][j]);
                Point currPt = new Point(i, j);

                if (pipeToDirs.containsKey(curr)) {
                    for (Point pt : pipeToDirs.get(curr)) {
                        List<Point> currAdj = adj.getOrDefault(currPt, new ArrayList<>());
                        currAdj.add(Point.add(currPt, pt));
                        adj.put(currPt, currAdj);
                    }
                }
            }
        }

        return adj;
    }

    public int bfs(Map<Point, List<Point>> adj, Point start) {
        List<Point> visited = new ArrayList<>();
        visited.add(start);

        int maxDist = 0;

        // queue with (Point, dist) key-val pairs
        Queue<PointDist> queue = new LinkedList<>();
        queue.add(new PointDist(start, 0));

        while (!queue.isEmpty()) {
            PointDist ptDist = queue.poll();
            Point pt = ptDist.getPoint();
            int dist = ptDist.getDist();

            maxDist = Math.max(maxDist, dist);

            for (Point nextPt : adj.get(pt)) {
                if (!visited.contains(nextPt)) {
                    visited.add(nextPt);
                    queue.add(new PointDist(nextPt, dist + 1));
                }
            }
        }

        return maxDist;
    }

    static Map<String, Point> getDirMap() {
        Map<String, Point> directions = new HashMap<>();
        directions.put("UP", new Point(-1, 0));
        directions.put("DOWN", new Point(1, 0));
        directions.put("LEFT", new Point(0, -1));
        directions.put("RIGHT", new Point(0, 1));

        return directions;
    }

    static Map<List<Point>, String> getDirsToPipeMap() {
        Map<List<Point>, String> pipes = new HashMap<>();
        pipes.put(Arrays.asList(dirToPt.get("UP"), dirToPt.get("RIGHT")), "L");
        pipes.put(Arrays.asList(dirToPt.get("UP"), dirToPt.get("LEFT")), "J");
        pipes.put(Arrays.asList(dirToPt.get("UP"), dirToPt.get("DOWN")), "|");
        pipes.put(Arrays.asList(dirToPt.get("DOWN"), dirToPt.get("RIGHT")), "F");
        pipes.put(Arrays.asList(dirToPt.get("DOWN"), dirToPt.get("LEFT")), "7");
        pipes.put(Arrays.asList(dirToPt.get("LEFT"), dirToPt.get("RIGHT")), "-");

        return pipes;
    }

    static Map<String, List<Point>> getPipeToDirsMap() {
        Map<String, List<Point>> pipes = new HashMap<>();
        pipes.put("L", Arrays.asList(dirToPt.get("UP"), dirToPt.get("RIGHT")));
        pipes.put("J", Arrays.asList(dirToPt.get("UP"), dirToPt.get("LEFT")));
        pipes.put("|", Arrays.asList(dirToPt.get("UP"), dirToPt.get("DOWN")));
        pipes.put("F", Arrays.asList(dirToPt.get("DOWN"), dirToPt.get("RIGHT")));
        pipes.put("7", Arrays.asList(dirToPt.get("DOWN"), dirToPt.get("LEFT")));
        pipes.put("-", Arrays.asList(dirToPt.get("LEFT"), dirToPt.get("RIGHT")));

        return pipes;
    }
}

//class Problem2 {
//    public int solution(String content) {
//        int result = 0;
//
//        return result;
//    }
//}


