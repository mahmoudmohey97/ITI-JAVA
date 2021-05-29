package day3;

import java.util.*;
import java.io.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;
import java.util.Comparator;

class Country{
    int countryCode;
    String name;
    List<City> cities = new ArrayList<>();

    public Country(int countryCode, String name) {
        this.countryCode = countryCode;
        this.name = name;
    }
    
    public int getCountryCode() {
        return countryCode;
    }

    public String getName() {
        return name;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }
    
    @Override
    public String toString() {
        return "Country{" + "countryCode=" + countryCode + ", name=" + name + ", cities=" + cities + '}';
    } 
}

class City{
    int population;
    double surfaceArea;
    int countryCode;
    String name;
    String Continent;

    public int getPopulation() {
        return population;
    }

    public double getSurfaceArea() {
        return surfaceArea;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public String getName() {
        return name;
    }

    public String getContinent() {
        return Continent;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public void setSurfaceArea(double surfaceArea) {
        this.surfaceArea = surfaceArea;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContinent(String Continent) {
        this.Continent = Continent;
    }

    @Override
    public String toString() {
        //return "City{" + "population=" + population + ", surfaceArea=" + surfaceArea + ", countryCode=" + countryCode + ", name=" + name + ", Continent=" + Continent + '}';
        return "City{" + "population=" + population +", countryCode=" + countryCode + ", name=" + name + '}';
    }

    public City(int population, double surfaceArea, int countryCode, String name, String Continent) {
        this.population = population;
        this.surfaceArea = surfaceArea;
        this.countryCode = countryCode;
        this.name = name;
        this.Continent = Continent;
    }
}

class CountryDAO{
    List<Country> countries = new ArrayList<>();
    Country tempCountry;
    public List<Country> readCountriesFromCSV(){
        try {
            String [] row ;
            File fileObj = new File("country.csv");
            Scanner fileReader = new Scanner(fileObj);
            fileReader.nextLine();
            while(fileReader.hasNext()){
                row = fileReader.nextLine().split(",");
                tempCountry = new Country(Integer.parseInt(row[1]), row[0]);
                countries.add(tempCountry);
            }
        } 
        catch (FileNotFoundException ex) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return countries;
    }
}

class CityDAO{
    List<City> cities = new ArrayList<>();
    City tempCity;
    public List<City> readCitiesFromCSV(){
        try {
            boolean cleanAttribute = false;
            String [] row ;
            File cityFileObj = new File("cities.csv");
            Scanner cityReader = new Scanner(cityFileObj);
            cityReader.nextLine();
            while(cityReader.hasNext()){
                row = cityReader.nextLine().split(",");
                for(String attribute: row){
                    if(attribute.isEmpty()){
                        cleanAttribute = false;
                        break;
                    }
                    else
                        cleanAttribute = true;
                }
                if(cleanAttribute){
                    tempCity = new City(Integer.parseInt(row[2]), Double.parseDouble(row[3]), Integer.parseInt(row[1]), row[0], row[4]);
                    cities.add(tempCity);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CityDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cities;
    }   
}

class listObjectComparator implements Comparator<City>{
        @Override
        public int compare(City a, City b){
            return a.population - b.population ;
        }
             
    }

public class Day3Assignments {
    
    //Assignment 2
    static class StringUtils{
        public static String betterString(String s1, String s2, BiPredicate<String, String> f){
            if(f.test(s1,s2))
                return s1;
            else
                return s2;
        }
        public static void properString(String s1, Predicate<String> p){
            if(p.test(s1))
                System.out.println("proper String");
            else
                System.out.println("not Proper");
        }
        public static boolean checkString(String s1){
            for(int i = 0; i < s1.length(); i++){
                char letter  = s1.charAt(i);
                if(!Character.isLetter(letter))
                    return false;
            }
            return true;
        }
    }
    
    public static void main(String[] args) {
        
        //Assignment 1
        CityDAO cityDAO = new CityDAO();
        CountryDAO countryDAO = new CountryDAO();
        List<City> cities = cityDAO.readCitiesFromCSV();
        List<Country> countries = countryDAO.readCountriesFromCSV();
        System.out.println(cities);
        
        for(Country cn: countries){
            int countryCode = cn.getCountryCode();
            List<City> citiesInCountry = new ArrayList<>();
            for(City city : cities){
                if(countryCode == city.countryCode)
                    citiesInCountry.add(city);
            }
            cn.setCities(citiesInCountry);
        }
        
        //adding counry code with corresponding cities
        Map<Integer, List<City>> countryCitiesMap = new HashMap<>();
        for(Country c : countries){
            countryCitiesMap.put(c.countryCode, c.cities);
        }
        
        //sorting
        countryCitiesMap.forEach((k,v)-> 
        Collections.sort(v, new listObjectComparator()));
        
        //viewing
        countryCitiesMap.forEach((k,v)-> 
        System.err.println(k + " " + v));
     
      
        
        //Assignment 2    
        String str1 = "hey";
        String str2 = "hola";
        //String betterStr = StringUtils.betterString(str1, str2, (s1, s2)-> StringUtils.isLonger(str1,str2));
        String Str = StringUtils.betterString(str1, str2, (s1, s2)-> s1.length()>s2.length());
        System.out.println(Str);
        StringUtils.properString(str1,(s1)->StringUtils.checkString(s1));
        StringUtils.properString(str1, StringUtils::checkString);
        
        

        //Assignment 3
        System.out.println(cities.stream().collect(Collectors.groupingBy(City::getCountryCode, Collectors.maxBy(Comparator.comparingInt(b -> b.getPopulation())))));
        System.out.println(cities.stream().collect(Collectors.groupingBy(City::getContinent, Collectors.maxBy(Comparator.comparingInt(b -> b.getPopulation())))));
        System.out.println(cities.stream().max(Comparator.comparingInt(c->c.getPopulation())));
        
        
    }
}
