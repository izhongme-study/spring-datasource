package me.izhong.leetcode;

public class 最长回文子串 {
    public static void main(String[] args) {

        String s = "ac";
        String r = new Solution().longestPalindrome(s);
        System.out.println(r);
    }

    static  class Solution {
        public String longestPalindrome(String s) {
            if(s == null)
                return null;
            if(s.length() <=1)
                return s;
            int maxStart=-1, maxEnd = 0, maxGap = -1;
            for(int i=0; i<s.length(); i++ ){
                int start1 = process(s,i,i);
                int end1 = start1 + (i-start1) * 2;
                if(end1 - start1 > maxGap || maxGap == -1){
                    maxGap = end1 - start1;
                    maxStart = start1;
                    maxEnd = end1;
                }
                if(i>=0 && i+1 <s.length() && s.charAt(i) == s.charAt(i+1)) {
                    int start2 = process(s, i, i + 1);
                    int end2 = start2 + (i - start2) * 2 + 1;
                    if (end2 - start2 > maxGap || maxGap == -1) {
                        maxGap = end2 - start2;
                        maxStart = start2;
                        maxEnd = end2;
                    }
                }
            }
            return s.substring(maxStart,maxEnd+1);
        }

        public int process(String s,int i,int j) {
            int start = i;
            int end = j;
            while(s.charAt(start) == s.charAt(end)){
                if(start == 0 || end == s.length() -1)
                    break;
                start --;
                end ++;
            }
            if(s.charAt(start) != s.charAt(end)) {
                ++start;
            }
            return start;
        }
    }
}
