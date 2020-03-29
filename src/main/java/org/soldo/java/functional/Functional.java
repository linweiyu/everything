package org.soldo.java.functional;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Functional {

    public static void eval(List<Integer> list, Predicate<Integer> predicate){
        for(Integer i : list){
            if(predicate.test(i))
                System.out.println(i);
        }
    }

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8);
        eval(list, n->true);
        System.out.println("even number");
        eval(list, n->n%2==0);
    }
}
