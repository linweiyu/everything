package org.soldo.java.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

public class DateTimeTester {
//        System.out.println(getFirstDayOfYear());
    public static void main(String[] args) {
        System.out.println(System.getProperty("sun.jnu.encoding"));
        System.out.println(System.getProperty("file.encoding"));
    }

    public static void testLocalDateTime(){
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println("Date And Time " + localDateTime);

        System.out.println("Date " + localDateTime.toLocalDate());



    }

    /**
     * 获取当前年的第一天
     */
    public static String getFirstDayOfYear(){
        LocalDate localDate = LocalDate.of(LocalDate.now().getYear(), Month.JANUARY, 1);
        return localDate.toString();
    }
}
