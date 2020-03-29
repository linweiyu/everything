package org.soldo.kata;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class kata0224 {


    public static void quickSort(List<Integer> arr, int i, int j) {
        if (j <= i)
            return;
        int k = partition(arr, i, j);
        quickSort(arr, i, k - 1);
        quickSort(arr, k + 1, j);
    }

    public static void exchange(List<Integer> arr, int i, int j) {
        int temp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, temp);
    }

    public static int partition(List<Integer> arr, int i, int j) {
        int lo = i, hi = j + 1, v = i;
        while (true) {
            while (arr.get(++lo) <= arr.get(v)) {
                if (lo == j)
                    break;
            }
            while (arr.get(--hi) >= arr.get(v)) {
                if (hi == i)
                    break;
            }
            if (lo >= hi)
                break;
            exchange(arr, lo, hi);
        }
        exchange(arr, i, hi);
        return hi;

    }

    public static void main(String[] args) {
        List<Integer> arr = Arrays.asList(4, 2, 8, 5, 9, 2);
        quickSort(arr, 0, arr.size() - 1);
        arr.stream().forEach(item->System.out.println(item));

//        Map<String, Integer> map = new HashMap<>();
//        map.put("123",0);
//        map.put("123",2);
//        map.forEach((key,value) -> System.out.println(key + " " + value));


    }
}
