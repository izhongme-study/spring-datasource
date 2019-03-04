package me.izhong.leetcode;

public class 求众数 {
    public static void main(String[] args) {
        int[] p = new int[]{3,2,3};
        int v = new Solution().majorityElement(p);
        System.out.println(v);
    }

    static class Solution {
        public int majorityElement(int[] nums) {
            if(nums == null)
                return 0;
            if(nums.length == 0)
                return nums[0];
            int count = 0;
            int v = nums[0];
            for(int i=0;i<nums.length;i++){
                if(v == nums[i]) {
                    count ++;
                } else {
                    count --;
                    if(count == 0)
                        v = nums[i +1];
                }
            }
            return v;
        }
    }
}
