/**
 @author Chelsea Dorich (Email: <a href="mailto:"robotqi@gmail.com>robotqi@gmail.com</a>)
 @version 1.1 04/17/2014
 @assignment.number A190-10
 @prgm.usage Called from the operating system
 @see "Gaddis, 2013, Starting out with Java, From Control Structures, 5th Edition"
 @see "<a href='http://docs.oracle.com/javase/7/docs/technotes/guides/javadoc/index.html'>JavaDoc Documentation</a>

 */
public interface DBTemplate
{
    public boolean addRecord(String strTable,
                             String strKeyName,
                             String strKeyContents);
    public boolean deleteAll(String strTable);

    public void close();
    public  String getField(String strFieldName);
    public Boolean moreRecords();
    public void openConnection(String strDataSourceName);
    public void query(String strSQL);
    public Boolean setField(String strTable,
                            String strKeyName,
                            String strKeyContents,
                            String strFieldName,
                            String strFieldContents);
    public void status(String strVar);

}
