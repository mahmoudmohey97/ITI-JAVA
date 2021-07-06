package com.mycompany.webservicesspringboot;

import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.SparkSession;

public class SparkInitialize {
    private final DataFrameReader dataFrameReader;
    private final SparkSession session;
    private int numberOfCores;

    public SparkInitialize(int coresNumber){
        this.session = SparkSession.builder().appName("java-project").master("local["+Integer.toString(coresNumber)+"]").getOrCreate();
        this.dataFrameReader = this.session.read();
        this.dataFrameReader.option("header", "true");
    }
    
    public DataFrameReader getDataFrameReader() {
        return dataFrameReader;
    }

    public SparkSession getSession() {
        return session;
    }

    public int getNumberOfCores() {
        return numberOfCores;
    }
    
    public void setNumberOfCores(int number) {
       this.numberOfCores = number;
    }
    
}