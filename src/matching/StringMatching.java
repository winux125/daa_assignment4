package matching;

import java.util.ArrayList;
import java.util.List;

public class StringMatching {
    public static class Result {
        public final List<Integer> indices = new ArrayList<>();
        public long comparisons;
    }

    private static Result emptyPattern(String text) {
        Result result = new Result();
        for (int i = 0; i <= text.length(); i++) result.indices.add(i);
        return result;
    }

    public static Result naive(String text, String pattern) {
        if (pattern.isEmpty()) return emptyPattern(text);
        Result result = new Result();
        for (int i = 0; i <= text.length() - pattern.length(); i++) {
            int j = 0;
            while (j < pattern.length()) {
                result.comparisons++;
                if (text.charAt(i + j) != pattern.charAt(j)) break;
                j++;
            }
            if (j == pattern.length()) result.indices.add(i);
        }
        return result;
    }

    public static Result kmp(String text, String pattern) {
        if (pattern.isEmpty()) return emptyPattern(text);
        Result result = new Result();
        int[] lps = new int[pattern.length()];
        for (int i = 1, length = 0; i < pattern.length();) {
            if (pattern.charAt(i) == pattern.charAt(length)) lps[i++] = ++length;
            else if (length > 0) length = lps[length - 1];
            else lps[i++] = 0;
        }
        for (int i = 0, j = 0; i < text.length();) {
            result.comparisons++;
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == pattern.length()) {
                    result.indices.add(i - j);
                    j = lps[j - 1];
                }
            } else if (j > 0) j = lps[j - 1];
            else i++;
        }
        return result;
    }

    public static Result rabinKarp(String text, String pattern) {
        if (pattern.isEmpty()) return emptyPattern(text);
        Result result = new Result();
        int n = text.length(), m = pattern.length();
        if (m > n) return result;
        long base = 256, mod = 1_000_000_007L;
        long power = 1, patternHash = 0, textHash = 0;
        for (int i = 1; i < m; i++) power = power * base % mod;
        for (int i = 0; i < m; i++) {
            patternHash = (patternHash * base + pattern.charAt(i)) % mod;
            textHash = (textHash * base + text.charAt(i)) % mod;
        }
        for (int i = 0; i <= n - m; i++) {
            if (patternHash == textHash) {
                int j = 0;
                while (j < m) {
                    result.comparisons++;
                    if (text.charAt(i + j) != pattern.charAt(j)) break;
                    j++;
                }
                if (j == m) result.indices.add(i);
            }
            if (i < n - m) {
                textHash = (textHash - text.charAt(i) * power % mod + mod) % mod;
                textHash = (textHash * base + text.charAt(i + m)) % mod;
            }
        }
        return result;
    }
}
