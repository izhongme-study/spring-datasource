package me.izhong.leetcode;

public class Z字形变换 {


//    示例 1:
//
//    输入: s = "LEETCODEISHIRING", numRows = 3
//    输出: "LCIRETOESIIGEDHN"
//    L   C   I   R
//    E T O E S I I G
//    E   D   H   N
//    之后，你的输出需要从左往右逐行读取，产生出一个新的字符串，比如："LCIRETOESIIGEDHN"。
//
//    请你实现这个将字符串进行指定行数变换的函数：
//
//    string convert(string s, int numRows);



//    示例 2:
//
//    输入: s = "LEETCODEISHIRING", numRows = 4
//    输出: "LDREOEIIECIHNTSG"
//    解释:
//
//    L     D     R
//    E   O E   I I
//    E C   I H   N
//    T     S     G



    public static void main(String[] args) {
        String input = "LEETCODEISHIRING";
        int numRows = 3;
        String v = new Solution().convert(input,numRows);
        System.out.println(v);
    }

    static class Solution {
        public String convert(String s, int numRows) {
            StringBuilder sb = new StringBuilder();
            int step = numRows * 2 - 2;
            for(int i = 0; i< s.length() + step;i = i + step) {
                //for (int j = 0; j < numRows; j++) {
                    int firstStep = numRows * 2 - 2;
                    int num1 = i * firstStep;
                    sb.append(s.charAt(num1));
//                    int secondStep = num1 + 1;
//                    int secondStep2 = num1 + numRows;
                //}
            }
            return sb.toString();
        }
    }
}
