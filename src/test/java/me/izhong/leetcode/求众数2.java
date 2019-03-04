package me.izhong.leetcode;

import java.util.ArrayList;
import java.util.List;

public class 求众数2 {
    public static void main(String[] args) {
        int[] p = new int[]{3,2,3};
        List<Integer> v = new Solution().majorityElement(p);
        System.out.println(v);

        int[] p2 = new int[]{1,1,1,3,3,2,2,2};
        v = new Solution().majorityElement(p2);
        System.out.println(v);
    }

    static class Solution {
        public List<Integer> majorityElement(int[] nums) {
            if(nums == null)
                return null;
            ArrayList<Integer> list = new ArrayList<>();
            if(nums.length == 0) {
                return list;
            }
            if(nums.length == 1) {
                list.add(nums[0]);
                return list;
            }

            int count1 = 0;
            int count2 = 0;
            int v1 = nums[0];
            int v2 = nums[0];
            for(int num:nums){
                if(v1 == num) {
                    count1 ++;
                } else if(v2 == num) {
                    count2 ++;
                } else if (count1 == 0) {
                    //换掉1
                    v1 = num;
                    count1 =1;
                } else if (count2 == 0) {
                    v2 = num;
                    count2 =1;
                } else {
                    count1 --;
                    count2 --;
                }
            }

            count1 = 0;
            count2 = 0;
            for(int num : nums) {
                if(num == v1)
                    count1++;
                else if(num == v2)
                    count2++;
            }


            if(count1 > nums.length /3)
                list.add(v1);
            if(v1 != v2 && count2 > nums.length / 3)
                list.add(v2);
            return list;
        }
    }
}
