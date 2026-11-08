import dynamic.LCS;
import dynamic.EditDistance;
import matching.StringMatching;
import suffix.Kasai;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("LCS: " + LCS.LCS("ABCD", "BD"));
        System.out.println("Edit distance: " + EditDistance.calculate("HORSE", "HOSE"));

        String text = "banana";
        String pattern = "ana";
        System.out.println("Naive: " + StringMatching.naive(text, pattern).indices);
        System.out.println("KMP: " + StringMatching.kmp(text, pattern).indices);
        System.out.println("Rabin-Karp: " + StringMatching.rabinKarp(text, pattern).indices);

        int[] suffixArray = {5, 3, 1, 0, 4, 2};
        System.out.println("LCP: " + Arrays.toString(Kasai.lcp(text, suffixArray)));
    }
}
