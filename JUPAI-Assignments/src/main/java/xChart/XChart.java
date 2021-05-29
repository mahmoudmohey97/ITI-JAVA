/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xChart;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;


/**
 *
 * @author mody_
 */
public class XChart {
    public static void main(String[] args) {
        XChart xChart = new XChart();
        List<TitanicPassenger> allPassengers = xChart.getPassengersFromJsonFile ();
        xChart.graphSurvivalWithGender(allPassengers);
        
        
        try {
            System.in.read();
        } catch (IOException ex) {
            Logger.getLogger(XChart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<TitanicPassenger> getPassengersFromJsonFile() {
       List<TitanicPassenger> allPassengers = new ArrayList<TitanicPassenger> ();
       ObjectMapper objectMapper = new ObjectMapper ();
       objectMapper.configure (DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
       try (InputStream input = new FileInputStream ("titanic_csv.json")) {
           //Read JSON file
           allPassengers = objectMapper.readValue (input, new TypeReference<List<TitanicPassenger>>(){});
       } catch (FileNotFoundException e) {
           e.printStackTrace ();
       } catch (IOException e) {
           e.printStackTrace ();
       }
       return allPassengers;
    }
    
    public void graphPassengerAges(List<TitanicPassenger> passengerList){
        List<Float> pAges = passengerList.stream().map(TitanicPassenger::getAge).limit(8).collect(Collectors.toList());
        List<String> pNames = passengerList.stream().map(TitanicPassenger::getName).limit(8).collect(Collectors.toList());
        String [] names = new String[pNames.size()];
        Float [] ages = new Float[pAges.size()];
        names = pNames.toArray(names);
        ages = pAges.toArray(ages);
        CategoryChart chart = new CategoryChartBuilder().width(1024).height(768).title("Age").xAxisTitle("Names").yAxisTitle("Ages").build();
        //Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setHasAnnotations(true);
        chart.getStyler().setStacked(true);
        //Series
        chart.addSeries("Passenger's Ages", pNames, pAges);
        //Show it
        new SwingWrapper(chart).displayChart();
    }
    
    public void graphSurvivalWithGender(List<TitanicPassenger> passengerList){
        Map<String, Long> mp = passengerList.stream().filter(c->c.getAge() < 20).collect(Collectors.groupingBy(c->c.getSex(), Collectors.counting()));
        List<String> gender = new ArrayList<>(mp.keySet());
        //gender  =  (List<String>) mp.keySet();
        List<Long> survived = new ArrayList<>(mp.values());
        //survived = (List<Long>) mp.values();
        CategoryChart chart = new CategoryChartBuilder().width(1024).height(768).title("Survival").xAxisTitle("Genders").yAxisTitle("Survived").build();
        //Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setHasAnnotations(true);
        chart.getStyler().setStacked(true);
        //Series
        chart.addSeries("Survived", gender, survived);
        //Show it
        new SwingWrapper(chart).displayChart();
    }
}
