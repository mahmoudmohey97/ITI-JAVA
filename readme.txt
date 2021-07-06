* This is a spring boot project with apache spark.
* Using wuzzuf_jubs.csv to perform some basic operations on it.
* On calling any endpoint to display chart, please wait 2 seconds for picture to load.
* list of availble endpoints to call:
    ("/")			: Reads dataset also.

    ("/read")			: Reads dataset and siplays first 20 rows of it.
    
    ("/getSummary")		: Shows summary of dataset but in console.

    ("/showSchema")		: Shows Schema of data.
    
    ("/cleanData")		: Removes Null and duplicates from data.

    ("/listDemandingCompanies")	: Shows list of companies offering jobs.
    
    ("/mostDemandingCompanies")	: Displays pie chart of top 15 demanding companies.
    
    ("/listPopularJobTitles")	: Shows list of most appeared job tittles in descending order.
    
    ("/mostAppearedJobTitles")	: Displays bar chart of most 20 appeared job titles.

    ("/listPopularAreas")	: Shows list of most areas appeared in job offers.
      
    ("/mostPopularAreas")	: Displays bar chart of most 20 appeared job titles.
    
    ("/skills")			: Shows list of most requested skills.
    
    ("/factorize")		: Factorizing yearsExp col in dataset and adding last column for factorized values.

    ("/kmeans")			: Running Kmeans on dataset on features "Title" and "Company" with 3 clusters and showing WSSSE score. 