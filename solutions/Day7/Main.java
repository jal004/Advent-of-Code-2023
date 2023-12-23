//package solutions.Day7;
//
//import java.io.IOException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.Objects;
//
//public class Main {
//    public static void main(String[] args) throws IOException, URISyntaxException {
//        URI inputUri = Objects.requireNonNull(solutions.Day7.Main.class.getResource("input.txt")).toURI();
//        String content = new String(Files.readAllBytes(Paths.get(inputUri)));
//
//        System.out.println("The answer to Problem 1 is: " + new Problem1().solution(content));
//        //System.out.println("The answer to Problem 2 is: " + new Problem2().solution(content));
//    }
//}
//
//class Problem1 {
//    public int solution(String content) {
//
//    }
//}
//
////class Problem2 {
////    public int solution(String content) {
////
////    }
////}
