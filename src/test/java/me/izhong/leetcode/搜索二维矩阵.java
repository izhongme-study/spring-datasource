package me.izhong.leetcode;

public class 搜索二维矩阵 {

    public static void main(String[] args) {
        int[][] matrix = new int[5][];
        matrix[0] = new int[]{1,   4,  7, 11, 15};
        matrix[1] = new int[]{2,   5,  8, 12, 19};
        matrix[2] = new int[]{3,   6,  9, 16, 22};
        matrix[3] = new int[]{10, 13, 14, 17, 24};
        matrix[4] = new int[]{18, 21, 23, 26, 30};
        int target = 20;

        boolean v = new Solution().searchMatrix(matrix,target);
        System.out.println(v);
    }

    static class Solution {
        public boolean searchMatrix(int[][] matrix, int target) {
            for(int i=0; i< matrix.length ;i++) {
                if(matrix[i][0] > target){
                    break;
                }
                for(int j=0; j< matrix[i].length;j++) {
                    int v = matrix[i][j];
                    if(v == target)
                        return true;
                    if(v > target){
                        break;
                    }
                }

            }
            return false;
        }
    }

}


