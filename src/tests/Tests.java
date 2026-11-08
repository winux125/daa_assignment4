package tests;

import dynamic.EditDistance;
import dynamic.LCS;
import matching.StringMatching;
import suffix.Kasai;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Tests {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static void match(String text, String pattern, Integer... expected) {
        List<Integer> answer = Arrays.asList(expected);
        check(StringMatching.naive(text, pattern).indices.equals(answer), "Naive: " + text + ", " + pattern);
        check(StringMatching.kmp(text, pattern).indices.equals(answer), "KMP: " + text + ", " + pattern);
        check(StringMatching.rabinKarp(text, pattern).indices.equals(answer), "Rabin-Karp: " + text + ", " + pattern);
    }

    public static void main(String[] args) {
        check(LCS.LCS("abc", "abc") == 3, "LCS identical");
        check(LCS.LCS("abc", "xyz") == 0, "LCS different");
        check(LCS.LCS("", "abc") == 0, "LCS empty");
        check(LCS.LCS("ABCBDAB", "BDCABA") == 4, "LCS subsequence");
        check(EditDistance.calculate("abc", "abc") == 0, "Edit identical");
        check(EditDistance.calculate("", "abc") == 3, "Edit empty");
        check(EditDistance.calculate("cat", "cut") == 1, "Substitution");
        check(EditDistance.calculate("cat", "cart") == 1, "Insertion");
        check(EditDistance.calculate("cart", "cat") == 1, "Deletion");
        match("abcdef", "ab", 0);
        match("abcdef", "cd", 2);
        match("abcdef", "ef", 4);
        match("aaaaa", "aa", 0, 1, 2, 3);
        match("abcdef", "xy");
        match("abc", "abcd");
        match("abc", "", 0, 1, 2, 3);
        match("", "", 0);
        match("", "a");
        check(Arrays.equals(Kasai.lcp("banana", new int[]{5, 3, 1, 0, 4, 2}),
                new int[]{0, 1, 3, 0, 0, 2}), "Kasai banana");
        check(Arrays.equals(Kasai.lcp("", new int[]{}), new int[]{}), "Kasai empty");
        Random random = new Random(42);
        for (int t = 0; t < 1000; t++) {
            StringBuilder text = new StringBuilder(), pattern = new StringBuilder();
            int n = random.nextInt(30), m = random.nextInt(10);
            for (int i = 0; i < n; i++) text.append((char) ('a' + random.nextInt(3)));
            for (int i = 0; i < m; i++) pattern.append((char) ('a' + random.nextInt(3)));
            List<Integer> expected = StringMatching.naive(text.toString(), pattern.toString()).indices;
            check(StringMatching.kmp(text.toString(), pattern.toString()).indices.equals(expected), "Random KMP");
            check(StringMatching.rabinKarp(text.toString(), pattern.toString()).indices.equals(expected), "Random Rabin-Karp");
        }
        System.out.println("All tests passed");
    }
}
