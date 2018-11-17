package org.soldo.java8.lambda;

import java.util.Arrays;
import java.util.List;

public class NewFeaturesTester {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("hello", "world", "!");
        list.forEach(System.out::println);
    }


    public static void main1(String[] args) {
        NewFeaturesTester tester = new NewFeaturesTester();
        MathOperation addition = (int a, int b) -> a + b;
        System.out.println(tester.operate(10, 5, addition));

        GreetingService greetingService = message -> System.out.println("Hello! " + message);
        greetingService.sayMessage("lin");
    }

    interface MathOperation {
        int operation(int a, int b);
    }

    interface GreetingService {
        void sayMessage(String message);
    }

    private int operate(int a, int b, MathOperation mathOperation) {
        return mathOperation.operation(a, b);
    }
}
