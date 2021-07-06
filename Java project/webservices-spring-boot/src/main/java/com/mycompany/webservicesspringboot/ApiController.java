package com.mycompany.webservicesspringboot;

import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ApiController {
    SparkInitialize session = new SparkInitialize(6);
    JobsDAO daoObject;
    DataExploration dataExplorationObject;
    MachineLearning ml;
    Dataset<Row> companiesDF;
    Dataset<Row> jobTitlesDF;
    Dataset<Row> areasDF;
    String pic = "";
    
    
    @RequestMapping("/")
    void index(HttpServletResponse response)throws IOException{
        response.sendRedirect("/read");
    }
    
    
    @RequestMapping("/read")
    void read_data(HttpServletResponse response) throws IOException{
        daoObject = new JobsDAO("Wuzzuf_Jobs.csv", this.session);
        daoObject.readData();
        ml = new MachineLearning(this.daoObject.getDataFrame());
        dataExplorationObject = new DataExploration(daoObject.getDataFrame());
        this.companiesDF = null;
        this.jobTitlesDF = null;
        this.areasDF = null;
        response.sendRedirect("/show");
    }
    
    @RequestMapping("/show")
    ResponseEntity <String> displayData(){
        String data = daoObject.showData();
        String html = String.format("<h5>Showing First 20 rows <br/> <br/> %s</h5>", data);
        return ResponseEntity.ok(html);
    }
    
    @RequestMapping("/getSummary")
    String summary(){
        daoObject.showSummary();
        return "Check console for report";
    }
    
    @RequestMapping("/showSchema")
    public ResponseEntity<String> showSchema(){
        String schema = daoObject.showSchema();
        String html = String.format("<h4>Schema <br/> %s</h4>", schema);
        return ResponseEntity.ok(html);
    }
    
    @RequestMapping("/cleanData")
    public String cleanData(){
        daoObject.removNA();
        daoObject.removeDuplicates();
        String lengthAfterClean = Long.toString(daoObject.getDataFrame().count());
        dataExplorationObject = new DataExploration(daoObject.getDataFrame());
        ml = new MachineLearning(this.daoObject.getDataFrame());
        return "Rows count after removing Duplicates & NA: " + lengthAfterClean;
    }
    
    
    @RequestMapping("/listDemandingCompanies")
    ResponseEntity <String> geMostDemandingCompanies(){
        if(this.companiesDF == null)
            this.companiesDF = dataExplorationObject.groupByCompany();
        
        String parse = this.companiesDF.collectAsList().toString().replace("[", "")
                                                                    .replace("],", "<br/> <br/>").replace("]]","")
                                                                    .replace(",", " : ");
        
        String html = String.format("<h5>Companies , Count <br/> %s</h5>", parse);
        return ResponseEntity.ok(html);
    }
     
    @RequestMapping("/sortCompanies")
    ResponseEntity<Integer> groupByCompany(HttpServletResponse response) throws IOException{
        this.companiesDF = dataExplorationObject.groupByCompany();
        response.sendRedirect("/mostDemandingCompanies");
        return ResponseEntity.ok(200);
    }
    
    @RequestMapping("/mostDemandingCompanies")
    ResponseEntity<Integer> showMostDemandingCompanies(HttpServletResponse response) throws IOException{
        if(this.companiesDF == null){
           response.sendRedirect("/sortCompanies");
        }
        else{
            if("".equals(this.pic) || ! new File("./src/main/resources/static/", "Companies_Chart.jpg").exists()){
                this.pic  = this.dataExplorationObject.mostDemandingCompanies(this.companiesDF);
            }
            else if(new File("./src/main/resources/static/", "Companies_Chart.jpg").exists()){
                this.pic = "/Companies_Chart.jpg";
            }
            response.sendRedirect("/showImage");
        }
        return ResponseEntity.ok(200);
    }

    
    @RequestMapping("/listPopularJobTitles")
    ResponseEntity <String> getMostPopularJobTitles(){
        if(this.jobTitlesDF == null)
            this.jobTitlesDF = dataExplorationObject.groupByJobTitle();
        
        String parse = this.jobTitlesDF.collectAsList().toString().replace("[", "")
                                                                    .replace("],", "<br/> <br/>").replace("]]","")
                                                                    .replace(",", " : ");
        
        String html = String.format("<h5>Job Titles, Count<br/> %s</h5>", parse);
        return ResponseEntity.ok(html);
    }
    
    @RequestMapping("/sortJobTitles")
    ResponseEntity groupByJobTitle(HttpServletResponse response) throws IOException{
        this.jobTitlesDF = dataExplorationObject.groupByJobTitle();
        response.sendRedirect("/mostAppearedJobTitles");
        return ResponseEntity.ok(200);
    }
    
    @RequestMapping("/mostAppearedJobTitles")
    ResponseEntity showMostAppearedJobTitles(HttpServletResponse response) throws IOException{
        if(this.jobTitlesDF == null)
            response.sendRedirect("/sortJobTitles");
        
        else{
            if("".equals(this.pic) || ! new File("./src/main/resources/static/", "Sample_Chart_With_Quality.jpg").exists()){
                this.pic  = this.dataExplorationObject.mostAppearedJobTitles(this.jobTitlesDF);
            }
            else if(new File("./src/main/resources/static/", "Sample_Chart_With_Quality.jpg").exists()){
                this.pic = "/Sample_Chart_With_Quality.jpg";
            }
            response.sendRedirect("/showImage");
        }
        return ResponseEntity.ok(200);
    }
    
    
    @RequestMapping("/listPopularAreas")
    ResponseEntity <String> geMostPopularAreas(){
        if(this.areasDF == null)
            this.areasDF = dataExplorationObject.groupByArea();
        
        String parse = this.areasDF.collectAsList().toString().replace("[", "")
                                                                    .replace("],", "<br/> <br/>").replace("]]","")
                                                                    .replace(",", " : ");
        
        String html = String.format("<h5>Areas , Count <br/> %s</h5>", parse);
        return ResponseEntity.ok(html);
    }
    
    @RequestMapping("/sortAreas")
    ResponseEntity<Integer> groupByAreas(HttpServletResponse response) throws IOException{
        this.areasDF = dataExplorationObject.groupByArea();
        response.sendRedirect("/mostPopularAreas");
        return ResponseEntity.ok(200);
    }
      
    @RequestMapping("/mostPopularAreas")
    ResponseEntity<Integer> showMostPopularAreas(HttpServletResponse response) throws IOException{
       if(this.areasDF == null)
           response.sendRedirect("/sortAreas");
       else{ 
        if("".equals(this.pic) || ! new File("./src/main/resources/static/", "Areas_Chart.jpg").exists()){
             this.pic  = this.dataExplorationObject.mostPopularAreas(this.areasDF);
         }
         else if(new File("./src/main/resources/static/", "Areas_Chart.jpg").exists()){
             this.pic = "/Areas_Chart.jpg";
         }
        response.sendRedirect("/showImage");
       }
        return ResponseEntity.ok(200);
    }
    
    
    @RequestMapping("/showImage")
    ResponseEntity<String> showImage(){
        String temp = this.pic;
        String html = String.format("<img id =\"im\" src=\"%s\" alt=\"Image 2\" width=\"800px \"> <script>\n" +
        "window.onload = function() {\n" +
        "	if(!window.location.hash) {\n" +
        "		setTimeout(function () {\n" +
        "            	window.location = window.location + '#loaded';\n" +
        "		window.location.reload();}, 2000);\n" +
        "	}\n" +
        "}\n" +
        "</script>", temp);

        return ResponseEntity.ok(html);
    }
    
    
    @RequestMapping("/skills")
    void mapSkills(HttpServletResponse response) throws IOException{
        dataExplorationObject.InitializeSkillsMap();
        response.sendRedirect("/showMostImportantSkills");
    }
    
    @RequestMapping("/showMostImportantSkills")
    ResponseEntity <String> showSkills(){
        String html = String.format("<h5>SKILLS , Count <br/> %s</h5>", dataExplorationObject.getSkills());
        return ResponseEntity.ok(html);
    }
    
    
    @RequestMapping("/factorize")
    void factorize(HttpServletResponse response) throws IOException{
        daoObject.setDataframe(ml.factorizeYearsCol());
        response.sendRedirect("/show");
    }
    
    @RequestMapping("/factorizeForKmeans")
    void factKMeans(HttpServletResponse response) throws IOException{
        ml.factorizeColsForKmeans();
        response.sendRedirect("/processKmeans");
    }
    
    @RequestMapping("/processKmeans")
    String processKMeans(){
        return ml.kmeans();
    }
    
    @RequestMapping("/kmeans")
    void startKmeans(HttpServletResponse response) throws IOException{
        response.sendRedirect("/factorizeForKmeans");
    }
}

