package day4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import joinery.DataFrame;
import joinery.DataFrame.JoinType;
import static tech.tablesaw.aggregate.AggregateFunctions.mean;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.selection.Selection;

public class Day4 {
    public static void main(String[] args) {
        try {            
                Table titanicTableSaw = Table.read().csv("titanic.csv");
                Table temp_table = titanicTableSaw.copy();

                temp_table.removeColumns("passengerId","cabin","ticket");
                titanicTableSaw.removeColumns("name","passengerId","cabin","ticket");

                System.out.println(titanicTableSaw.columnNames());
                System.out.println(titanicTableSaw.summary());
                System.out.println(titanicTableSaw.last(10));

                IntColumn tempCol = IntColumn.create("temp coloumn");
                for(int i = 0; i < titanicTableSaw.doubleColumn("age").size() ; i++)
                    tempCol.append(i + 5);

                titanicTableSaw.addColumns(tempCol);
                System.out.println(titanicTableSaw.summary());

                //average of survival grouped by sex
                Table survBySex = titanicTableSaw.copy().retainColumns("sex", "survived").summarize("survived", mean).by("sex");
                System.out.println(survBySex);

                //joining
                Table t1 = temp_table.copy().retainColumns("name", "survived").dropRowsWithMissingValues();
                Table t2 = temp_table.copy().retainColumns("name", "sex").dropRowsWithMissingValues();
                Table mergedTable = t1.joinOn("name").inner(t2);    
                System.out.println(mergedTable.print(5));
                      
                //using joinery
                DataFrame<Object> df = DataFrame.readCsv("titanic.csv");
                System.out.println(df.describe());
                List<Object> tmpColumn = new ArrayList<Object>();

                for(int i=0; i< df.col("Age").size(); i++)
                    tmpColumn.add(i+5-2);
                df.add("temporary column", tmpColumn);
                System.out.println(df.head(5));

                DataFrame<Object> survivedByGender = df.retain("Survived","Sex");
                System.out.println(survivedByGender.groupBy(x->x.get(1)).mean());

                DataFrame<Object> df1 = df.retain("Name", "Age").dropna().unique("Name");
                DataFrame<Object> df2 = df.retain("Name", "Sex").dropna().unique("Name");
                DataFrame<Object> df3 = df1.joinOn(df2, JoinType.INNER, "Name").resetIndex();
                System.out.println(df3.head(10));
                System.out.println(df.describe());
        } catch (IOException ex) {
            Logger.getLogger(Day4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
