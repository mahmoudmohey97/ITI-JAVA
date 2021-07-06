/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.webservicesspringboot;

import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.clustering.KMeansModel;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class MachineLearning {
    private Dataset<Row> df;

    public MachineLearning(Dataset<Row> df) {
        this.df = df;
    }
    
    public Dataset factorizeYearsCol(){
        StringIndexer stringIndexer = new StringIndexer().
                                        setInputCol("YearsExp").
                                        setOutputCol("factorizedYears");
        Dataset<Row> factorizedDF = stringIndexer.fit(this.df).transform(this.df);
        return factorizedDF;
    }
    
    public void factorizeColsForKmeans(){
        StringIndexer stringIndexer = new StringIndexer().setInputCol("Title").setOutputCol("factorizedTitles");
        Dataset<Row> factorizedDF = stringIndexer.fit(this.df).transform(this.df);
        stringIndexer.setInputCol("Company").setOutputCol("factorizedCompanies");
        this.df = stringIndexer.fit(factorizedDF).transform(factorizedDF);
    }
    
    
    
    public String kmeans(){
        VectorAssembler vectorAssembler = new VectorAssembler()
                                .setInputCols(new String[]{"factorizedTitles", "factorizedCompanies"}).setOutputCol("features");
        final Dataset<Row> featuredData = vectorAssembler.transform(this.df);
        
        KMeans kmeans = new KMeans().setK(3).setSeed(1L);
        kmeans.setFeaturesCol("features");
        kmeans.setPredictionCol("pred");
        
         KMeansModel model = kmeans.fit(featuredData);

        double WSSSE = model.computeCost(featuredData);
        return("Trained Model has Within Set Sum of Squared Errors = " + WSSSE);    
    }
}