package org.soldo.kata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class kata0314 {
    List<Integer> aux = new ArrayList<>();
    //quick sort

    public void exchange(List<Integer> arr, int i, int j) {
        int temp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, temp);
    }

    //partition
    public int partition(List<Integer> arr, int i, int j) {
        int lo = i;
        int v = i;
        int hi = j + 1;
        while (true) {
            while (arr.get(v) >= arr.get(++lo))
                if (lo == j)
                    break;
            while (arr.get(v) <= arr.get(--hi))
                if (hi == i)
                    break;
            if (lo >= hi)
                break;
            exchange(arr, lo, hi);
        }
        exchange(arr, v, hi);
        return hi;
    }

    //quick Sort
    public void quickSort(List<Integer> arr, int i, int j) {
        if (i >= j)
            return;
        int k = partition(arr, i, j);
        quickSort(arr, i, k - 1);
        quickSort(arr, k + 1, j);
    }


    //merge sort
    private void merge(List<Integer> arr, int i, int mid, int j) {
        int left = i;
        int right = mid + 1;
        for(int k = i; k <= j; k++){
            aux.set(k, arr.get(k));
        }
        for (int k = i; k <= j; k++) {
            if (left > mid) {
                arr.set(k, aux.get(right++));
            } else if (right > j) {
                arr.set(k, aux.get(left++));
            } else if (aux.get(left) >= aux.get(right)) {
                arr.set(k, aux.get(right++));
            } else {
                arr.set(k, aux.get(left++));
            }
        }
    }

    public void mergeSort(List<Integer> arr, int i, int j) {
        if (i >= j) {
            return;
        }
        int mid = i + (j - i) / 2;
        mergeSort(arr, i, mid);
        mergeSort(arr, mid + 1, j);
        merge(arr, i, mid, j);
    }

    public kata0314(List<Integer> arr) {
        for (Integer item : arr) {
            aux.add(item);
        }
    }

    public kata0314() {
    }

    //new edition
    private static int[] aut;

    public static void sort(int[] a) {
        aut = new int[a.length];
        sort(a, 0, a.length - 1);
    }

    private static void merge(int[] a, int lo, int mid, int hi) {
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            aut[k] = a[k];
        }
        for (int t = lo; t <= hi; t++) {
            if(i > mid) a[t] = aut[j++];
            else if(j >hi) a[t] = aut[i++];
            else if(aut[i] <= aut[j]) a[t] = aut[i++];
            else a[t] = aut[j++];
        }
    }

    private static void sort(int[] a, int lo, int hi) {
        if (hi <= lo)
            return;
        int mid = lo + (hi - lo);
        sort(a, lo, mid);
        sort(a, mid + 1, hi);
    }


    public static void main(String[] args) {
//        kata0314 kata0314 = new kata0314();
//        List<Integer> arr = Arrays.asList(4, 2, 8, 5, 9, 2);
//        kata0314.quickSort(arr, 0, arr.size() - 1);
        //arr.forEach(System.out::println);
        List<Integer> arr2 = Arrays.asList(4, 2, 8, 5, 9, 2);

        kata0314 kata03141 = new kata0314(arr2);
        kata03141.mergeSort(arr2, 0, arr2.size() - 1);
        arr2.forEach(System.out::println);
//        int[] a = new int[]{4, 2, 8, 5, 9, 2};
//        kata0314.sort(a);
//        Arrays.stream(a).forEach(System.out::println);

    }

}
