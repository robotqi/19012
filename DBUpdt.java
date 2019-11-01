import java.sql.*;

/**
 @author Chelsea Dorich (Email: <a href="mailto:"robotqi@gmail.com>robotqi@gmail.com</a>)
 @version 1.1 04/17/2014
 @assignment.number A190-10
 @prgm.usage Called from the operating system
 @see "Gaddis, 2013, Starting out with Java, From Control Structures, 5th Edition"
 @see "<a href='http://docs.oracle.com/javase/7/docs/technotes/guides/javadoc/index.html'>JavaDoc Documentation</a>

 */
public class DBUpdt implements DBTemplate

{
    // Class Level Variables
    Connection dbConn;
    Statement  dbCmdText;
    ResultSet  dbRecordset;
    String     dbTable;
    String     dbKeyField;
    public DBUpdt()
    {
    }

    /**
     * Adds record to derby database
     * @param strTable string representing the tble name
     * @param strKeyName Name of the key field the record is to be added to
     * @param strKeyContents contents to be added to key
     * @return return a bln indicating the success of the operation
     */
    @Override
    public boolean addRecord(String strTable,
                             String strKeyName, String strKeyContents)
    {

//        try
//        {
//            dbCmdText.execute( "INSERT INTO stations "+"(stationid) VALUES ('" + strKeyContents + "')");
//            status("win");
//        } catch (SQLException e)
//        {
//            e.printStackTrace();
//        }
        String strSQL;
        boolean blnStatus;
        try
        {
            // check to see if the record exists
            dbCmdText = dbConn.createStatement();
            strSQL = "SELECT * FROM " + strTable + " WHERE " + strKeyName + "='" + strKeyContents + "'";
            status(strSQL);
            dbRecordset = dbCmdText.executeQuery(strSQL);
            if(!moreRecords())
            {
                // the record does not exist, therefore add it to the database
                strSQL = "INSERT INTO " + strTable + " (" + strKeyName + ") VALUES ('" + strKeyContents + "')";
                status(strSQL);
                dbCmdText.executeUpdate(strSQL);
                //dbRecordset.close();
                status("Record added");
                blnStatus = true;
            }
            else
            {
                status("Record NOT added");
                blnStatus = false;
            }
        }
        catch (Exception e)
        {
            blnStatus = false;
        }
        return blnStatus;
    }

    /**
     *  delets all records from given table
     * @param strTable table name to be deleted
     * @return  return a bln indicating the success of the operation
     */

    @Override
    public boolean deleteAll(String strTable)
    { try
    { String strSQL = "DELETE * FROM " + strTable;
        status(strSQL);
        dbCmdText.executeUpdate(strSQL);

           // dbRecordset.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * closes database
     */
    @Override
    public void close()
    {
        try
        {
            dbConn.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            status("doom");
        }
    }

    /**
     * gets contents from specified field and returns it
     * @param strFieldName field to be retrieved
     * @return contense of field
     */
    @Override
    public String getField(String strFieldName)
    {
        String strRet;
        try
        {
            strRet = dbRecordset.getString(strFieldName);
        } catch (SQLException e)
        {
            e.printStackTrace();
            strRet = "";
        }
        return strRet;
    }

    /**
     * moves database reader down one record
     * @return return a bln indicating the success of the operation
     */
    @Override
    public Boolean moreRecords()
    {
        Boolean blnRet = false;
        try
        {
            blnRet = dbRecordset.next();
        }
        catch (Exception e)
        {
            blnRet = false;
        }
        return blnRet; // only one RETURN in each function!
    }

    /**
     * opens connection to given dtabase
     * @param strDataSourceName name of database to be opened
     */
    @Override
    public void openConnection(String strDataSourceName)
    {
        boolean blnStatus;
        blnStatus = false;
        try
        {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            dbConn=DriverManager.getConnection("jdbc:derby:" + strDataSourceName);
            if (dbConn == null)
            {
                status("Connection Failed");
            }
            else
            {
                status("Connection Successful");
                dbCmdText = dbConn.createStatement();
                blnStatus = true;
            }
        }
        catch (Exception e)
        {
            status("Problems opening connection");
        }
    }

    /**
     * executes a query to the database
     * @param strSQL written query to be performed
     */
    @Override
    public void query(String strSQL)
    {
        try
        {
            dbRecordset = dbCmdText.executeQuery(strSQL);
        }
        catch (Exception ex)
        {
        }
    }

    /**
     * sets specefied field with given contents
     * @param strTable name of table to be modified
     * @param strKeyName name of key
     * @param strKeyContents key contents to be found
     * @param strFieldName field to be retrieved
     * @param strFieldContents contents to be added to indicated field
     * @return
     */
    @Override
    public Boolean setField(String strTable, String strKeyName, String strKeyContents, String strFieldName, String strFieldContents)
    {   boolean blnSet = false;
        try
        {
            dbCmdText = dbConn.createStatement();
// goal is = UPDATE customer SET city='SAN DIEGO' WHERE customerID='2100'

        String strSQL = "UPDATE " + strTable + " SET " + strFieldName + "='" + strFieldContents + "' " +
                " WHERE " + strKeyName + "='" + strKeyContents + "' ";
        dbCmdText.executeUpdate(strSQL);

            blnSet=true;
        } catch (SQLException e)
    {
        e.printStackTrace();

    }
        return blnSet;
    }

    /**
     * prints out a status message
     * @param strVar message to be displayed
     */
    @Override
    public void status(String strVar)
    {
        System.out.println(strVar);
    }

    /**
     * executes db command
     * @param strSQL command to be carried out
     * @return return a bln indicating completion status
     */
    public Boolean execute(String strSQL)
    {
        Boolean blnStatus = false;

        try
        {
            dbCmdText.execute(strSQL);
            blnStatus = true;
        }
        catch (Exception ex)
        {
            status("Execute command failed " + strSQL);

        }
        return blnStatus;
    }
}
