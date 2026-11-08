package experiments;

import dynamic.LCS;
import dynamic.EditDistance;
import matching.StringMatching;
import suffix.Kasai;
import java.util.Arrays;
import java.util.Random;
import java.util.Locale;

public class Benchmark {
    private static volatile long sink;
    private static final Random random = new Random(42);

    private static String randomString(int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) result.append((char) ('a' + random.nextInt(4)));
        return result.toString();
    }

    private static int[] suffixArray(String text) {
        Integer[] indices = new Integer[text.length()];
        for (int i = 0; i < indices.length; i++) indices[i] = i;
        Arrays.sort(indices, (a, b) -> {
            int i = a, j = b;
            while (i < text.length() && j < text.length()) {
                int difference = text.charAt(i++) - text.charAt(j++);
                if (difference != 0) return difference;
            }
            return Integer.compare(text.length() - a, text.length() - b);
        });
        return Arrays.stream(indices).mapToInt(Integer::intValue).toArray();
    }

    private static StringMatching.Result search(int algorithm, String text, String pattern) {
        if (algorithm == 0) return StringMatching.naive(text, pattern);
        if (algorithm == 1) return StringMatching.kmp(text, pattern);
        return StringMatching.rabinKarp(text, pattern);
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        System.out.println("group,algorithm,size,case,average_ms,comparisons");
        for (int size : new int[]{100, 500, 1000}) {
            String a = randomString(size), b = randomString(size);
            for (int algorithm = 0; algorithm < 2; algorithm++) {
                for (int i = 0; i < 3; i++) sink = algorithm == 0 ? LCS.LCS(a, b) : EditDistance.calculate(a, b);
                long total = 0;
                for (int i = 0; i < 5; i++) {
                    long start = System.nanoTime();
                    sink = algorithm == 0 ? LCS.LCS(a, b) : EditDistance.calculate(a, b);
                    total += System.nanoTime() - start;
                }
                System.out.printf("dp,%s,%d,random,%.6f,%n", algorithm == 0 ? "LCS" : "Edit Distance", size, total / 5_000_000.0);
            }
        }
        for (int size : new int[]{10000, 50000, 100000}) {
            String text = randomString(size);
            String absent = randomString(16);
            while (text.contains(absent)) absent = randomString(16);
            String[] patterns = {text.substring(size / 2, size / 2 + 16), absent};
            for (int p = 0; p < patterns.length; p++) {
                for (int algorithm = 0; algorithm < 3; algorithm++) {
                    for (int i = 0; i < 5; i++) sink = search(algorithm, text, patterns[p]).indices.size();
                    long total = 0, comparisons = 0;
                    for (int i = 0; i < 5; i++) {
                        long start = System.nanoTime();
                        StringMatching.Result result = search(algorithm, text, patterns[p]);
                        total += System.nanoTime() - start;
                        sink = result.indices.size();
                        comparisons += result.comparisons;
                    }
                    String[] names = {"Naive", "KMP", "Rabin-Karp"};
                    System.out.printf("matching,%s,%d,%s,%.6f,%d%n", names[algorithm], size,
                            p == 0 ? "present" : "absent", total / 5_000_000.0, comparisons / 5);
                }
            }
        }
        for (int size : new int[]{1000, 5000, 10000}) {
            String text = randomString(size);
            int[] sa = suffixArray(text);
            for (int i = 0; i < 5; i++) sink = Kasai.lcp(text, sa).length;
            long total = 0;
            for (int i = 0; i < 5; i++) {
                long start = System.nanoTime();
                sink = Kasai.lcp(text, sa).length;
                total += System.nanoTime() - start;
            }
            System.out.printf("kasai,Kasai,%d,random,%.6f,%n", size, total / 5_000_000.0);
        }
    }
}

