
package spark;

import org.apache.spark.sql.*;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.regression.LinearRegression;
import org.apache.spark.ml.regression.LinearRegressionModel;



public class AirBnBWithSpark {

    public static void main(String[] args) {
        final SparkSession sparkSession = SparkSession.builder().appName("air bNb with spark").master("local[2]").
                getOrCreate();
        final DataFrameReader dataFrameReader = sparkSession.read();
        dataFrameReader.option("header", "true");
        Dataset<Row> df = dataFrameReader.csv("listings.csv");
        Dataset<Row> tempDf = df;
        df.printSchema();
        df = df.drop("id", "latitude", "longitude", "host_name", "host_id, name");
        df.printSchema();
        
        // or Create view and execute query to convert types as, by default, all
        // columns have string types
        tempDf.createOrReplaceTempView("DATASET");

        Dataset<Row> typedDf = sparkSession
                .sql("SELECT cast(bedrooms as float) bedrooms, " +
                            "cast(minimum_nights as float) minimum_nights, " +
                            "cast(number_of_reviews as float) number_of_reviews, " +
                            "cast(price as float) price FROM DATASET");
        
        typedDf.printSchema();
        typedDf = typedDf.na().drop();
        final VectorAssembler vectorAssmbler = new VectorAssembler()
                .setInputCols(new String[] {"bedrooms", "minimum_nights", "number_of_reviews"})
                .setOutputCol("features");
        final Dataset<Row> featuredData = vectorAssmbler.transform(typedDf);
        featuredData.printSchema();
        
        //dividing data into 80% training and 20% testing
        Dataset<Row> [] split = featuredData.randomSplit(new double[]{.8,.2}, 42);
        final Dataset<Row> trainingData = split[0];
        final Dataset<Row> testingData = split[1];
        trainingData.show(40);
        
        //defining model 
        LinearRegression linearRegression=new LinearRegression ();
        linearRegression.setFeaturesCol ("features");
        linearRegression.setLabelCol ("price");
        
        final LinearRegressionModel regression = linearRegression.fit(trainingData);
        final Dataset<Row> predictions = regression.transform(testingData);
        predictions.show();
        System.out.println(regression.summary().r2());
    }
    
}
