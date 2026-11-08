package dynamic;

public class LCS
{

    public static int LCS(String str1, String str2)
    {
        int len1 = str1.length(); int len2 = str2.length();
        int[][] c = new int[len1+1][len2+1];
        for(int i = 1; i<=len1; i++)
        {
            for(int j = 1; j<=len2; j++)
            {
                if(str1.charAt(i-1) == str2.charAt(j-1))
                {
                    c[i][j] =  c[i - 1][j - 1] + 1;
                    
                }
                else
                    c[i][j] = Math.max(c[i - 1][j], c[i][j - 1]);
            }
        }
        return c[len1][len2];
    }
}