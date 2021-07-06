/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.webservicesspringboot;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.spark.sql.*;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import static org.apache.spark.sql.functions.desc;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.style.Styler;

//comparator to sort map
class ValueComparator implements Comparator<String> 
{
    Map<String, Integer> base;

    public ValueComparator(Map<String, Integer> base) {
        this.base = base;
    }
    
    @Override
    public int compare(String a, String b) {
        if (base.get(a) <= base.get(b)) {
            return 1;
        } else {
            return -1;
        } 
    }
}


public class DataExploration {
    private final Dataset<Row> df;
    private Map<String, Integer> skillsMap;
    
    public DataExploration(Dataset<Row> df) {
        this.df = df;
    }
    
    Dataset<Row> groupByCompany(){
        return this.df.groupBy("Company").count().sort(desc("count"));
    }
    
    Dataset<Row> groupByJobTitle(){
        return this.df.groupBy("Title").count().sort(desc("count"));
    }
    
    Dataset<Row> groupByArea(){
        return this.df.groupBy("Location").count().sort(desc("count"));
    }
    
    public String mostDemandingCompanies(Dataset<Row> companies){
        companies = companies.limit(15);
        List <String> companiesNameArray = companies.select("Company").collectAsList().stream().map(row -> row.getString(0)).collect(Collectors.toList());
        List <Long> companiesCount = companies.select("count").collectAsList().stream().map(row -> row.getLong(0)).collect(Collectors.toList());

        PieChart chart = new PieChartBuilder().width(1024).height(768).title("Top 15 companies offering jobs").build();
        //Series
        for(int i = 0; i < companiesNameArray.size(); i++){
            chart.addSeries(companiesNameArray.get(i), companiesCount.get(i));
        }
        //Show it
        //new SwingWrapper(chart).displayChart();
        try {
            BitmapEncoder.saveJPGWithQuality(chart, "./src/main/resources/static/Companies_Chart.jpg", 0.95f);
            return "/Companies_Chart.jpg";
        } catch (IOException ex) {
            Logger.getLogger(DataExploration.class.getName()).log(Level.SEVERE, null, ex);
        }
        //new SwingWrapper(chart).displayChart();*/
        return "";
        
    }
    
    public String mostAppearedJobTitles(Dataset<Row> mostFreqJobTittles){
        try {
            
            mostFreqJobTittles = mostFreqJobTittles.limit(20);
           
            List <String> jobTitles;
            jobTitles = mostFreqJobTittles.select("Title").collectAsList().stream().map(row->row.getString(0)).collect(Collectors.toList());

            List <Long> jobTitlesCount;
            jobTitlesCount = mostFreqJobTittles.select("count").collectAsList().stream().map(row->row.getLong(0)).collect(Collectors.toList());
            
            CategoryChart chart = new CategoryChartBuilder().xAxisTitle("Job title").yAxisTitle("Count").width(1024).height(768).build();
            chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
            chart.getStyler().setHasAnnotations(true);
            chart.getStyler().setStacked(true);
            chart.getStyler().setXAxisLabelRotation(90);
            chart.addSeries("Most 20 Demanding Jobs", jobTitles, jobTitlesCount);

            BitmapEncoder.saveJPGWithQuality(chart, "./src/main/resources/static/Sample_Chart_With_Quality.jpg", 0.95f);
            return "/Sample_Chart_With_Quality.jpg";
        } catch (IOException ex) {
            Logger.getLogger(DataExploration.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("com.mycompany.webservicesspringboot.DataExploration.mostAppearedJobTitles()");
            return "";
        }
    }
    
    public String mostPopularAreas(Dataset<Row> popularAreas){       
        popularAreas = popularAreas.limit(20);

        List <String> areaNames;
        areaNames = popularAreas.select("Location").collectAsList().stream().map(row->row.getString(0)).collect(Collectors.toList());

        List <Long> areaCount;
        areaCount = popularAreas.select("count").collectAsList().stream().map(row->row.getLong(0)).collect(Collectors.toList());

        CategoryChart chart = new CategoryChartBuilder().xAxisTitle("Area").yAxisTitle("Count").width(1024).height(768).build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setHasAnnotations(true);
        chart.getStyler().setStacked(true);
        chart.getStyler().setXAxisLabelRotation(90);

        chart.addSeries("Top 20 Job Areas", areaNames, areaCount);
        
        try {
            BitmapEncoder.saveJPGWithQuality(chart, "./src/main/resources/static/Areas_Chart.jpg", 0.95f);
            return "/Areas_Chart.jpg";
        } catch (IOException ex) {
            Logger.getLogger(DataExploration.class.getName()).log(Level.SEVERE, null, ex);
        }
        //new SwingWrapper(chart).displayChart();*/
        return "";
        //new SwingWrapper(chart).displayChart();
        
    }

    public void InitializeSkillsMap(){
        Map <String, Integer> skillsFrequency = new HashMap();
        this.df.select("Skills").collectAsList().stream()
            .forEach(row -> { 
                for(String key : row.getString(0).split(",")) { 
                    skillsFrequency.put(key.trim(), skillsFrequency.getOrDefault(key.trim(), 0)+1);
                }
            });
        this.skillsMap = skillsFrequency;
    }
    
    public String getSkills(){
        ValueComparator bvc = new ValueComparator(this.skillsMap);
        TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);
        sorted_map.putAll(this.skillsMap);
        return sorted_map.toString().replaceAll(",", "<br/>");
    }
}
