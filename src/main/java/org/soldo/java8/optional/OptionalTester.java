package org.soldo.java8.optional;

import java.util.Optional;

public class OptionalTester {
    public static void main(String[] args) {

    }

    public static Optional<Integer> sum(Optional<Integer> a, Optional<Integer> b) {
        if (!a.isPresent() && !b.isPresent()) {
            return Optional.ofNullable(null);
        } else  {
            return Optional.of(a.orElse(new Integer(0)) + b.orElse(new Integer(0)));
        }
    }
}
