import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
/**
 @author Chelsea Dorich (Email: <a href="mailto:"robotqi@gmail.com>robotqi@gmail.com</a>)
 @version 1.1 05/02/2014
 @assignment.number A190-12
 @prgm.usage Called from the operating system
 @see "Gaddis, 2013, Starting out with Java, From Control Structures, 5th Edition"
 @see "<a href='http://docs.oracle.com/javase/7/docs/technotes/guides/javadoc/index.html'>JavaDoc Documentation</a>
 */

public class A19012 extends JDialog
{
    private JPanel contentPane;
    private JButton buttonOK;
    private JComboBox cboLocations;
    private JLabel lblDelay;
    private JLabel lblVisibility;
    private JLabel lblUpdated;
    private JButton updtReportButton;
    private JButton saveReportButton;
    private JLabel lbl;
    private JLabel lblTime;
    private JLabel lblStationID;
    private JLabel lblTempC;
    private JLabel lblTempF;
    private JLabel lblRH;
    private JLabel lblDewp;
    private JLabel lblWindDir;
    private JLabel lblWindSp;
    private JLabel labelImage;

    public A19012()
    {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        cboLocations.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                getSurfaceWeather(cboLocations.getSelectedItem().toString().substring(0, 3));

            }
        });
        updtReportButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                UpdtDatabase();
            }
        });
        saveReportButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    saveReport("C:\\Users\\Chelsea\\A19012\\Data\\FBOUT.txt");
                } catch (FileNotFoundException e1)
                {
                    e1.printStackTrace();
                }
            }
        });
    }

    /**
     * MAIN method, makes all the magic happen
     * @param args
     * @throws SQLException
     * @throws IOException
     */
    public static void main(String[] args) throws SQLException, IOException
    {
        DBUpdt db = new DBUpdt();
        INET net = new INET();
        String strWmdb = "jdbc:derby:Weather;create=true";
        Connection dbConn = DriverManager.getConnection(strWmdb);
        DriverManager.getConnection(strWmdb);
        Statement dbCmdText =  dbConn.createStatement();
//execute command
        dbCmdText.execute("DROP TABLE stations");
        try
        {
            dbCmdText.execute("CREATE TABLE stations (" +
                    "stationid CHAR(25), city CHAR(100),state CHAR(100), latitude CHAR(25), longitude CHAR(25), "
                    + "windsaloft CHAR(100),temperature CHAR(15), humidity CHAR(20),windspeed CHAR(100), " +
                    "winddirection CHAR(100), elevation CHAR(15), pressure CHAR(20), dewpoint CHAR(100) " +
                    ")");
            dbCmdText.close();
            db.openConnection("Weather");
            System.out.println("Table created");
        } catch (SQLException ex)
        {
            db.status("fail");
        }
        loadWindsAloftData("FBIN.txt");
        db.close();
        loadWorldData("C:\\Users\\Chelsea\\A19012\\Data\\Worlds.txt");
        A19012 dialog = new A19012();
        dialog.populateLocations();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    /**
     * populates locations in combo box
     */
    private void populateLocations()
    { DBUpdt db = new DBUpdt();

        db.openConnection("Weather");
        db.query("SELECT * FROM stations");
        while(db.moreRecords())
        {
            cboLocations.addItem(db.getField("stationid") + " - " + db.getField("city"));
        }
        Date d = new Date();
        lblTime.setText(d.toString());
        lblDewp.setText("");
        lblRH.setText("");
        lblTempC.setText("");
        lblTempF.setText("");
        lblWindDir.setText("");
        lblWindSp.setText("");
    }

    /**
     * loads winds aloft data from internet and saves to database
     * @param strFileName name of file to be opened
     * @throws IOException
     */
    private static void loadWindsAloftData(String strFileName) throws IOException
    {String strFb = "";
        DBUpdt db = new DBUpdt();
        NWSFB fb = new NWSFB(strFb);
        INET net = new INET();
        boolean blnFileExists = net.fileExists(strFileName);
        fb.getFile("C:\\Users\\Chelsea\\A19012\\Data\\Worlds.txt","http://weather.noaa.gov/data/nsd_bbsss.txt",true);
        fb.getFile("C:\\Users\\Chelsea\\A19012\\Data\\FBIN.txt","http://www.aviationweather.gov/products/nws/all",true);
        BufferedReader inputFile;
        db.openConnection("Weather");
        inputFile = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Chelsea\\A19012\\Data\\FBIN.txt")));


        while (inputFile.ready())
        {
            String strRecord = inputFile.readLine();


                    String strStaID = strRecord.substring(0,3).trim();
                    String strWAloft = strRecord.substring(3).trim();
                    db.addRecord("stations","stationid", strStaID);
                    db.setField("stations","stationid",strStaID,"windsaloft",strWAloft);
        }
        db.close();
    }

    /**
     * loads world data nto file
     * @param strFileName name of file to be saved to
     * @throws IOException
     */
    private static void loadWorldData(String strFileName) throws IOException
    {
        NWSFB fb = new NWSFB("");
        DBUpdt db = new DBUpdt();
        INETTemplate net = new INET();
        boolean blnFileExists = net.fileExists(strFileName);
        fb.getFile(strFileName,"http://weather.noaa.gov/data/nsd_bbsss.txt",blnFileExists);
        BufferedReader inputFile;
        inputFile = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Chelsea\\A19012\\Data\\Worlds.txt")));
        db.openConnection("Weather");
        while (inputFile.ready())
        {
            String strRecord = inputFile.readLine();

            if (strRecord.length()>9)
            {
            if(strRecord.substring(7,8).equals("K"))
            {
                String strStaField = "";
                int intField = 0;
                String[] strFieldsAry = strRecord.split(";");
                try
                {
                    strStaField= strFieldsAry[intField];
                    String strStaID = strFieldsAry[2].substring(1,4).trim();

                    String strCity = strFieldsAry[3];
                    String strState = strFieldsAry[4];
                    String strLatitude = strFieldsAry[8];
                    String strLongitude = strFieldsAry[7];
                    String strElevation = strFieldsAry[11];
                    try{
                    db.setField("stations","stationid",strStaID,"city", net.properCase(strCity));
                    db.setField("stations","stationid",strStaID,"state", strState);
                    db.setField("stations","stationid",strStaID,"latitude", strLatitude);
                    db.setField("stations","stationid",strStaID,"longitude", strLongitude);
                    db.setField("stations","stationid",strStaID,"elevation", strElevation);
                    }
                    catch(Exception e)
                    {db.status("fail");}

                }
                catch(ArrayIndexOutOfBoundsException e)
                {
                    strStaField = "";
                }
            }}

    }}

    /**
     * gets surface weather to loads it to form
     * @param strStationId string indicating selected station
     */
    public void getSurfaceWeather(String strStationId)

    {XMLRead xm = new XMLRead();
        DBUpdt db = new DBUpdt();
        db.openConnection("Weather");
        db.query("SELECT * FROM stations WHERE stationid='" +  strStationId + "'" );
        String strUrl = "http://w1.weather.gov/xml/current_obs/"+ "K" +strStationId +".xml";
        UpdateStationInfo(strStationId,true);
        try
        {
            xm.loadPage(strUrl);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            while(db.moreRecords()){
            lblUpdated.setText(xm.getField("observation_time"));
            lblVisibility.setText(xm.getField("visibility_mi"));
            lblDelay.setText(xm.getField("suggested_pickup"));
            lblStationID.setText(strStationId);
            lblDewp.setText(db.getField("dewpoint"));
            lblRH.setText(db.getField("humidity"));
            lblTempC.setText(xm.getField("temp_c"));
            lblTempF.setText(db.getField("temperature"));
            lblWindDir.setText(db.getField("winddirection"));
            lblWindSp.setText(db.getField("windspeed"));}
        } catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    /**
     * updates datbase with all of california stations info
     */
    private void UpdtDatabase()
    {
        DBUpdt db = new DBUpdt();
        db.openConnection("Weather");
        db.query("SELECT * FROM stations WHERE state = 'CA'");
        while( db.moreRecords())
        {
            UpdateStationInfo(db.getField("stationid").trim(), true);}
    }

    /**
     * uses xml methods to update dields in database for given station
     * @param strStationID station to be updated
     * @param blnUpdate bln for true or false
     */
    private void UpdateStationInfo(String strStationID, boolean blnUpdate)
    {
        DBUpdt db = new DBUpdt();
        db.openConnection("Weather");
        XMLRead xm = new XMLRead();
        String UrlId = "K" +strStationID;
        try
        { // update the fields for the given station
            xm.loadPage("http://w1.weather.gov/xml/current_obs/"+ UrlId +".xml");
            String strLat = (xm.getField("latitude"));
            String strLon = (xm.getField("longitude"));
            String strDew = (xm.getField("dewpoint_f"));
            String strHum = (xm.getField("relative_humidity"));
            String strTemp =(xm.getField("temp_f"));
            String strDir = (xm.getField("wind_dir"));
            String strSpeed = (xm.getField("wind_mph"));
            String strPress = (xm.getField("pressure_string"));
            db.setField("stations","stationid",strStationID,"latitude", strLat);
            db.setField("stations","stationid",strStationID,"longitude", strLon);
            db.setField("stations","stationid",strStationID,"temperature", strTemp);
            db.setField("stations","stationid",strStationID,"humidity", strHum);
            db.setField("stations","stationid",strStationID,"windspeed", strSpeed);
            db.setField("stations","stationid",strStationID,"winddirection", strDir);
            db.setField("stations","stationid",strStationID,"pressure", strPress);
            db.setField("stations","stationid",strStationID,"dewpoint", strDew);
            URL url = new URL(xm.getField("icon_url_base") + xm.getField("icon_url_name"));
            BufferedImage image = ImageIO.read(url);
            ImageIcon icon = new ImageIcon(image);
             labelImage.setIcon(icon);
        } catch (Exception e)
        {
            db.status("fail 88888");
        }



        //update the immage
    }

    /**
     * method that saves report consisting of entirety of all stations
     * @param strFileName nazme of file to be saved to
     * @throws FileNotFoundException
     */
    public void saveReport(String strFileName) throws FileNotFoundException
    {
     DBUpdt db = new DBUpdt();
        db.openConnection("Weather");
        db.query("SELECT * FROM stations");
        PrintWriter outputFile = new PrintWriter(strFileName);
        while(db.moreRecords())
        {
            outputFile.println(db.getField("stationid")+ " ; "+db.getField("city")+ " ; "+db.getField("pressure")+ " ; "+db.getField("dewpoint")+ " ; "+
                    db.getField("elevation")+ " ; "+db.getField("latitude")+ " ; "+db.getField("longitude")+"\r\n");
        }
    }

    /**
     * program generated constructor
     */
    private void createUIComponents()
    {
        // TODO: place custom component creation code here
    }

}