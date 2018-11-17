package org.soldo.algorithm;

import java.util.Arrays;

public class BinarySearch {

    /**
     * 二分查找
     * @param key 目标值
     * @param a 数组 (需经过排序)
     * @return 目标值索引 若返回-1 则未找到
     */
    public static int rank(int key, int[] a){
        int lo = 0;
        int hi = a.length - 1;
        while (lo <= hi){
            int mid = lo + (hi - lo) / 2;
            if(key < a[mid])
                hi = mid - 1;
            else if(key > a[mid])
                lo = mid + 1;
            else
                return mid;
        }
        return -1;

    }

    public static void main(String[] args) {
        int[] target = new int[]{4,1,8,10,3};
        Arrays.sort(target);
        System.out.println(rank(1,target));
    }
}
