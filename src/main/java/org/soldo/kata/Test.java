package org.soldo.kata;

import java.util.ArrayList;
import java.util.Stack;

import javax.servlet.http.HttpServlet;

public class Test extends HttpServlet {
    public static void main(String[] args) {
//        System.out.println(reverse(1534236469));
        // System.out.println(-123 % 10);
        // System.out.println(-123 / 10);
        // System.out.println(-1 / 10);
        Integer a = Integer.valueOf("123");
        Integer b = Integer.valueOf("123");
        b = 5;
        System.out.println("a " + a);
        System.out.println("b " + b);
//        String a = String.valueOf("123");
//        String b = String.valueOf("123");
//        b = "5";
//        System.out.println("a " + a);
//        System.out.println("b " + b);

    }

    public static int reverse(int x) {
        boolean isPositive = x > 0;
        Stack<Integer> stack = new Stack<>();
        do {
            stack.push(Math.abs(x  % 10));
        } while((x /= 10) != 0);
        int i = 0, sum = 0;
        while(!stack.empty()){
            int b = sum, c = ((int)Math.pow(10, i++)) * stack.pop();
            sum = b + c;
            if (((b ^ sum) & (c ^ sum)) < 0) {
                return 0;
            }
        }
        return isPositive ? sum : -sum;
    }
}
