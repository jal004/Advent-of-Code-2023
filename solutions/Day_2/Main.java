package solutions.Day_2;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        URI inputUri = Objects.requireNonNull(Main.class.getResource("input.txt")).toURI();
        String content = new String(Files.readAllBytes(Paths.get(inputUri)));

        System.out.println("The answer to Problem 1 is: " + new Problem1().computeValidGamesSum(content));
        System.out.println("The answer to Problem 2 is: " + new Problem2().computePowerSum(content));
    }
}

class Problem1 {
    static int MAX_RED = 12;
    static int MAX_GREEN = 13;
    static int MAX_BLUE = 14;

    public int computeValidGamesSum(String content) {
        int result = 0;
        String[] rawGames = content.split("\\r?\\n");

        for (String rawGame : rawGames) {
            int gameNum = Integer.parseInt(rawGame.substring(5, rawGame.indexOf(":")));
            String[] hands = rawGame.substring(rawGame.indexOf(':') + 2).split("; ");
            boolean validGame = true;

            for (String hand : hands) {
                String[] rolls = hand.split(", ");
                boolean validHand = true;

                for (String roll : rolls) {
                    if (roll.contains("red")) {
                        int red = Integer.parseInt(roll.substring(0, roll.indexOf("red") - 1));
                        if (red > MAX_RED) {
                            validHand = false;
                            break;
                        }
                    } else if (roll.contains("green")) {
                        int green = Integer.parseInt(roll.substring(0, roll.indexOf("green") - 1));
                        if (green > MAX_GREEN) {
                            validHand = false;
                            break;
                        }
                    } else {
                        int blue = Integer.parseInt(roll.substring(0, roll.indexOf("blue") - 1));
                        if (blue > MAX_BLUE) {
                            validHand = false;
                            break;
                        }
                    }
                }

                if (!validHand) {
                    validGame = false;
                    break;
                }
            }
            if (validGame) {
                result += gameNum;
            }
        }

        return result;
    }
}

class Problem2 {
    public int computePowerSum(String content) {
        int result = 0;
        String[] rawGames = content.split("\\r?\\n");

        for (String rawGame : rawGames) {
            String[] hands = rawGame.substring(rawGame.indexOf(':') + 2).split("; ");
            int maxRed = 1;
            int maxGreen = 1;
            int maxBlue = 1;

            for (String hand : hands) {
                String[] rolls = hand.split(", ");

                for (String roll : rolls) {
                    if (roll.contains("red")) {
                        int red = Integer.parseInt(roll.substring(0, roll.indexOf("red") - 1));
                        if (red > maxRed) {
                            maxRed = red;
                        }
                    } else if (roll.contains("green")) {
                        int green = Integer.parseInt(roll.substring(0, roll.indexOf("green") - 1));
                        if (green > maxGreen) {
                            maxGreen = green;
                        }
                    } else {
                        int blue = Integer.parseInt(roll.substring(0, roll.indexOf("blue") - 1));
                        if (blue > maxBlue) {
                            maxBlue = blue;
                        }
                    }
                }
            }

            result += maxRed * maxGreen * maxBlue;
        }

        return result;
    }
}