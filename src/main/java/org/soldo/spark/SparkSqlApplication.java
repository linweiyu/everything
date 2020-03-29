package org.soldo.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.time.LocalDate;
import java.util.Arrays;

public class SparkSqlApplication {
    public static void main(String[] args) throws Exception {
        SparkConf sparkConf = new SparkConf()
                .setAppName("SparkSqlApplication")
                .setMaster("spark://linweiyus-MacBook-Pro.local:7077");
        while (true) {

            SparkSession sparkSession = SparkSession.builder().config(sparkConf).getOrCreate();

            Dataset<Row> dataset = sparkSession.read().csv("test.csv");

            dataset.printSchema();

            sparkSession.close();

            Thread.sleep(5000);
        }


    }
}
