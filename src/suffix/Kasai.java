package suffix;

public class Kasai {
    public static int[] lcp(String text, int[] suffixArray) {
        int n = text.length();
        if (suffixArray.length != n) throw new IllegalArgumentException("Wrong suffix array length");
        int[] rank = new int[n];
        int[] lcp = new int[n];
        for (int i = 0; i < n; i++) rank[suffixArray[i]] = i;
        int length = 0;
        for (int i = 0; i < n; i++) {
            int position = rank[i];
            if (position == 0) {
                length = 0;
                continue;
            }
            int previous = suffixArray[position - 1];
            while (i + length < n && previous + length < n
                    && text.charAt(i + length) == text.charAt(previous + length)) length++;
            lcp[position] = length;
            if (length > 0) length--;
        }
        return lcp;
    }
}
