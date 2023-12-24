package solutions.Day_07;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        URI inputUri = Objects.requireNonNull(solutions.Day_07.Main.class.getResource("input.txt")).toURI();
        String content = new String(Files.readAllBytes(Paths.get(inputUri)));

        System.out.println("The answer to Problem 1 is: " + new Problem1().solution(content));
        System.out.println("The answer to Problem 2 is: " + new Problem2().solution(content));
    }
}

class Problem1 {
    public long solution(String content) {
        long result = 0;
        String[] lines = content.split("\\r?\\n");

        // treemap sorts keys in ascending order; mapping hand value to bid
        TreeMap<Long, Integer> fiveOfAKind = new TreeMap<>();
        TreeMap<Long, Integer> fourOfAKind = new TreeMap<>();
        TreeMap<Long, Integer> fullHouse = new TreeMap<>();
        TreeMap<Long, Integer> threeOfAKind = new TreeMap<>();
        TreeMap<Long, Integer> twoPair = new TreeMap<>();
        TreeMap<Long, Integer> onePair = new TreeMap<>();
        TreeMap<Long, Integer> highCard = new TreeMap<>();

        for (String line : lines) {
            String[] sections = line.split("\\s");
            String hand = sections[0];
            int bid = Integer.parseInt(sections[1]);

            Map<Character, Integer> cardFreqs = new HashMap<>();

            // getting frequency of each card in a hand
            for (char c : hand.toCharArray()) {
                int c_freq = cardFreqs.getOrDefault(c, 0) + 1;
                cardFreqs.put(c, c_freq);
            }

            if (cardFreqs.containsValue(5)) {
                fiveOfAKind.put(handValue(hand), bid);
            }
            else if (cardFreqs.containsValue(4)) {
                fourOfAKind.put(handValue(hand), bid);
            }
            else if (cardFreqs.containsValue(3) && cardFreqs.containsValue(2)) {
                fullHouse.put(handValue(hand), bid);
            }
            else if (cardFreqs.containsValue(3)) {
                threeOfAKind.put(handValue(hand), bid);
            }
            else if (cardFreqs.containsValue(2)) {
                // could be one pair or two pair
                int numPairs = Collections.frequency(cardFreqs.values(), 2);
                if (numPairs == 2) {
                    twoPair.put(handValue(hand), bid);
                } else {
                    onePair.put(handValue(hand), bid);
                }
            } else {
                highCard.put(handValue(hand), bid);
            }
        }

        int rank = 1;
        for (Map.Entry<Long, Integer> e : highCard.entrySet()) {
            result += (long) e.getValue() * rank;
            rank++;
        }

        for (Map.Entry<Long, Integer> e : onePair.entrySet()) {
            result += (long) e.getValue() * rank;
            rank++;
        }

        for (Map.Entry<Long, Integer> e : twoPair.entrySet()) {
            result += (long) e.getValue() * rank;
            rank++;
        }

        for (Map.Entry<Long, Integer> e : threeOfAKind.entrySet()) {
            result += (long) e.getValue() * rank;
            rank++;
        }

        for (Map.Entry<Long, Integer> e : fullHouse.entrySet()) {
            result += (long) e.getValue() * rank;
            rank++;
        }

        for (Map.Entry<Long, Integer> e : fourOfAKind.entrySet()) {
            result += (long) e.getValue() * rank;
            rank++;
        }

        for (Map.Entry<Long, Integer> e : fiveOfAKind.entrySet()) {
            result += (long) e.getValue() * rank;
            rank++;
        }

        return result;
    }

    public long handValue(String hand) {
        long result = 0;
        int weight = 100000000;

        for (char c : hand.toCharArray()) {
            result += (long) cardValue(c) * weight;
            weight /= 100;
        }

        return result;
    }

    public int cardValue(char card) {
        // enhanced switch; expr -> val does the break in each non-default case implicitly
        return switch (card) {
            case 'A' -> 14;
            case 'K' -> 13;
            case 'Q' -> 12;
            case 'J' -> 11;
            case 'T' -> 10;
            default -> Character.getNumericValue(card);
        };
    }

}

class Problem2 {
    // like solution 1,
    // but add each J to the card with the first card with the largest frequency in cardFreqs determining hand type
    public long solution(String content) {
        long result = 0;
        String[] lines = content.split("\\r?\\n");

        // treemap sorts keys in ascending order; mapping hand value to bid
        TreeMap<Long, Integer> fiveOfAKind = new TreeMap<>();
        TreeMap<Long, Integer> fourOfAKind = new TreeMap<>();
        TreeMap<Long, Integer> fullHouse = new TreeMap<>();
        TreeMap<Long, Integer> threeOfAKind = new TreeMap<>();
        TreeMap<Long, Integer> twoPair = new TreeMap<>();
        TreeMap<Long, Integer> onePair = new TreeMap<>();
        TreeMap<Long, Integer> highCard = new TreeMap<>();

        for (String line : lines) {
            String[] sections = line.split("\\s");
            String hand = sections[0];
            int bid = Integer.parseInt(sections[1]);

            Map<Character, Integer> cardFreqs = new HashMap<>();

            // getting frequency of each card in a hand
            for (char c : hand.toCharArray()) {
                int c_freq = cardFreqs.getOrDefault(c, 0) + 1;
                cardFreqs.put(c, c_freq);
            }


            char maxChar = 'A';
            int maxCharFreq = 0;
            for (Map.Entry<Character, Integer> entry : cardFreqs.entrySet()){
                if (entry.getKey() != 'J' && entry.getValue() > maxCharFreq){
                    maxCharFreq = entry.getValue();
                    maxChar = entry.getKey();
                }
            }

            // accounting for Jokers before filtering into hand types
            int numJokers = cardFreqs.getOrDefault('J', 0);
            if (numJokers != 0) {
                cardFreqs.put(maxChar, maxCharFreq + numJokers);
                cardFreqs.remove('J');
            }

            if (cardFreqs.containsValue(5)) {
                fiveOfAKind.put(handValue2(hand), bid);
            }
            else if (cardFreqs.containsValue(4)) {
                fourOfAKind.put(handValue2(hand), bid);
            }
            else if (cardFreqs.containsValue(3) && cardFreqs.containsValue(2)) {
                fullHouse.put(handValue2(hand), bid);
            }
            else if (cardFreqs.containsValue(3)) {
                threeOfAKind.put(handValue2(hand), bid);
            }
            else if (cardFreqs.containsValue(2)) {
                // could be one pair or two pair
                int numPairs = Collections.frequency(cardFreqs.values(), 2);
                if (numPairs == 2) {
                    twoPair.put(handValue2(hand), bid);
                } else {
                    onePair.put(handValue2(hand), bid);
                }
            } else {
                highCard.put(handValue2(hand), bid);
            }
        }

        int rank = 1;
        for (Map.Entry<Long, Integer> e : highCard.entrySet()) {
            result += (long) e.getValue() * rank;
            rank++;
        }

        for (Map.Entry<Long, Integer> e : onePair.entrySet()) {
            result += (long) e.getValue() * rank;
            rank++;
        }

        for (Map.Entry<Long, Integer> e : twoPair.entrySet()) {
            result += (long) e.getValue() * rank;
            rank++;
        }

        for (Map.Entry<Long, Integer> e : threeOfAKind.entrySet()) {
            result += (long) e.getValue() * rank;
            rank++;
        }

        for (Map.Entry<Long, Integer> e : fullHouse.entrySet()) {
            result += (long) e.getValue() * rank;
            rank++;
        }

        for (Map.Entry<Long, Integer> e : fourOfAKind.entrySet()) {
            result += (long) e.getValue() * rank;
            rank++;
        }

        for (Map.Entry<Long, Integer> e : fiveOfAKind.entrySet()) {
            result += (long) e.getValue() * rank;
            rank++;
        }

        return result;
    }

    public long handValue2(String hand) {
        long result = 0;
        int weight = 100000000;

        for (char c : hand.toCharArray()) {
            result += (long) cardValue2(c) * weight;
            weight /= 100;
        }

        return result;
    }

    // J now has the weakest value (1)
    public int cardValue2(char card) {
        return switch (card) {
            case 'A' -> 14;
            case 'K' -> 13;
            case 'Q' -> 12;
            case 'J' -> 1;
            case 'T' -> 10;
            default -> Character.getNumericValue(card);
        };
    }
}
