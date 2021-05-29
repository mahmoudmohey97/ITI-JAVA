package spark;

import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class FileAnalysisSparkSql {

    public static void main(String[] args) {
        final SparkSession sparkSession = SparkSession.builder().appName("spark with sql demo").master("local[2]")
                .getOrCreate();
        
        final DataFrameReader dataFrameReader = sparkSession.read();
        dataFrameReader.option("header", "true");
        
        final Dataset<Row> csvDataFrame = dataFrameReader.csv("src/main/resources/data.csv");
        csvDataFrame.printSchema();
        
        csvDataFrame.createOrReplaceTempView("rooms_occupancy_raw");
        final Dataset<Row> roomOccupancy = sparkSession.sql(
                "SELECT CAST(id as int) id, CAST(date as string) date, CAST(Temperature as float) Temperature, "
                        + "CAST(Humidity as float) Humidity, CAST(Light as float) Light, CAST(CO2 as float) CO2, "
                        + "CAST(HumidityRatio as float) HumidityRatio, CAST(Occupancy as int) Occupancy FROM rooms_occupancy_raw");
    
        roomOccupancy.printSchema();
        
    }
    
}
