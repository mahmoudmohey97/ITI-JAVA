package day3;
import day2.*;
import java.io.*;
import java.util.*;
    
class Pyramid{
    private String pahroh;
    private String site;
    private String modernName;
    private double height;

    public Pyramid(String pharoh, String modernName, String site, double height)
    {
        this.height = height;
        this.modernName = modernName;
        this.pahroh = pharoh;
        this.site = site;
    }

    public void setPahroh(String pahroh) {
        this.pahroh = pahroh;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setModernName(String modernName) {
        this.modernName = modernName;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getPahroh() {
        return pahroh;
    }

    public String getSite() {
        return site;
    }

    public String getModernName() {
        return modernName;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "Pyramid{" + "pahroh=" + pahroh + ", site=" + site + ", modernName=" + modernName + ", height=" + height + '}';
    }   
}

class PyramidCSVDAO
{
    List<Pyramid> pyramids = new ArrayList<>();
    public PyramidCSVDAO(){
     }
    public List<Pyramid> readPyramidsFromCSV(String filePath)
    {
        boolean cleanAttribute = false;
        try
            {               
                String [] tempPyramid;
                File fileObj= new File(filePath);
                Scanner fileReader = new Scanner(fileObj);
                fileReader.nextLine();
                while(fileReader.hasNextLine())
                {
                    tempPyramid = fileReader.nextLine().split(",");
                    for(int j = 0; j < tempPyramid.length; j++)
                    {
                        if((tempPyramid[j] == null || tempPyramid[j].isEmpty()) && (j == 0 || j==2 || j==4 || j==7)){
                            cleanAttribute = false;
                            break;
                        }
                        else
                        {
                            tempPyramid[j] = tempPyramid[j].trim();
                            cleanAttribute = true;
                        }
                    }
                    if(cleanAttribute)
                    {
                        Pyramid newPyramid = createPyramid(tempPyramid);
                        pyramids.add(newPyramid);
                    }
                    
                }
            }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        return pyramids;
    }
    Pyramid createPyramid(String[] pyramidAtrributes){
        Pyramid p = new Pyramid(pyramidAtrributes[0], pyramidAtrributes[2], pyramidAtrributes[4], Double.valueOf(pyramidAtrributes[7]));
        return p;
    }
}

public class PyramidsAssignment {
    public static void main(String[] args) {
        PyramidCSVDAO pDAO = new PyramidCSVDAO();
        List<Pyramid> pyramidsList = pDAO.readPyramidsFromCSV("pyramids.csv");
        
        ArrayList<Double> quartiles = calculateQuartiles(pyramidsList);
        System.out.println("Average = " + quartiles.get(0));
        System.out.println("Q1 = " + quartiles.get(2));
        System.out.println("Q2 = " + quartiles.get(1));
        System.out.println("Q3 = " + quartiles.get(3));
        
    }
    
    public static ArrayList<Double> calculateQuartiles(List<Pyramid> pyramids) {

        ArrayList<Double> results = new ArrayList<>();
        ArrayList<Double> pyramidsHeights = new ArrayList<>();

        pyramids.stream().forEach((pyramid) -> {
            pyramidsHeights.add((double) pyramid.getHeight());
        });

        int heightsLength = pyramidsHeights.size();
        int splitIndex = heightsLength / 2;
        pyramidsHeights.sort((x, y) -> Double.compare(x, y));
        results.add(pyramidsHeights.stream().reduce(0.0, (x, y) -> x + y) / heightsLength);
        results.add(getDataMedian(pyramidsHeights));
        results.add(getDataMedian(pyramidsHeights.subList(0, splitIndex)));
        if (heightsLength % 2 != 0)
            splitIndex++;
        results.add(getDataMedian(pyramidsHeights.subList(splitIndex, heightsLength)));
        return results;
    }

    private static double  getDataMedian(List<Double> arr) {
        int citiesNumber = arr.size();
        double median = citiesNumber % 2 == 0
                ? (arr.get((int) (citiesNumber / 2)) + arr.get((int) (citiesNumber / 2) - 1)) / 2
                : arr.get((int) (citiesNumber / 2));

        return median;

    }

}

