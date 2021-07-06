/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.webservicesspringboot;

import org.apache.spark.sql.*;

public class JobsDAO{
    private final String fileName;
    private Dataset<Row> dataframe;
    private final SparkInitialize sparkInstance;
    
    public JobsDAO(String fileName, SparkInitialize sparkSession){
        this.fileName = fileName;
        this.sparkInstance = sparkSession;
    }
   
    public void readData(){
        this.dataframe = this.sparkInstance.getDataFrameReader().csv(this.fileName);
    }
    
    public Dataset<Row> getDataFrame(){
        return this.dataframe;
    }

    public void setDataframe(Dataset<Row> dataframe) {
        this.dataframe = dataframe;
    }
    
    public void removeDuplicates(){
        this.dataframe = this.dataframe.distinct();
    }
    
    public void removNA(){
        this.dataframe = this.dataframe.na().drop();
    }
    
    public void showDataInsights(){
        System.out.println("*****************************************");
        this.dataframe.printSchema();
        System.out.println("*****************************************");
        this.dataframe.show();
        System.out.println("*****************************************");
        this.dataframe.describe().show();
    }
    
    public String showSchema(){
        return this.dataframe.schema().treeString().replace("root", "").replace("|--", "<br/>");
    }
    
    public String showData(){
        String sampleData = this.dataframe.limit(20).collectAsList().toString().replace("[", "")
                                                                    .replace("],", "<br/> <br/>").replace("]]","")
                                                                    .replace(",", " , ");
        return sampleData;
    }
    
    public void showSummary(){
        this.dataframe.describe();
    }
}