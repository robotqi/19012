import java.io.*;
import javax.swing.JOptionPane;
/**
 @author Chelsea Dorich (Email: <a href="mailto:"robotqi@gmail.com>robotqi@gmail.com</a>)
 @version 1.1 05/02/2014
 @assignment.number A190-12
 @prgm.usage Called from the operating system
 @see "Gaddis, 2013, Starting out with Java, From Control Structures, 5th Edition"
 @see "<a href='http://docs.oracle.com/javase/7/docs/technotes/guides/javadoc/index.html'>JavaDoc Documentation</a>
 */
public class NWSFB
{
    /**
     * updates strVar
     */
    private static String strWeather = "";

    /**
     * NWSFB method accepts strVar and manipulates it to extract pertinant weather data.
     * @param strVar is a long string of data containing all aspects of weather at different
     *               altitudes at a gven location
     */
    public NWSFB(String strVar)
    {strVar = strWeather;

    }

    /**
     * update string updates main string from main method
     * @param strVar strVar is updates and set to equal strWeather
     */
    public void updateString(String strVar)
    {
        strWeather = strVar +"  ";
    }
    /** getPos() method. Accepts a string variable called strAlt which represents the altitude and returns an integer
     * representing the character position
     * @param strAlt - strAlt will...
     * @return Returns number representing the starting point for seven digit number group extraction relating to chosen
     * altitude
     */
    public static int getPos(String strAlt)
    {
        int intRet = 0;  // my return value
        int intAlt = Integer.parseInt(strAlt);
        switch (intAlt)
        {
            case 3000:
                intRet = 4;
                break;
            case 6000:
                intRet = 9;
                break;
            case 9000:
                intRet = 17;
                break;
            case 12000:
                intRet = 25;
                break;
            case 18000:
                intRet = 33;
                break;
            case 24000:
                intRet = 41;
                break;
            case 30000:
                intRet = 49;
                break;
            case 34000:
                intRet = 56;
                break;
            case 36000:
                intRet = 63;
            default:
                System.out.println("Invalid altitude");
        }
        return intRet;
    }
    /**
     * getAltitudeWeather method. uses the user input strAlt to resemble altitude to find correct block of numbers and return it
     * @param strAlt input the abreviated number to the program and use it to find the information  pertinant to the altitude
     * @return strAltWea return the block of information relevant to the chosen altitude
     */
    public String getAltitudeWeather(String strAlt)
    {
        int intRet;
        String strAltWea;
        intRet = getPos(strAlt);
        strAltWea = strWeather.substring(intRet, intRet + 7);
        return strAltWea;
    }

    /**
     * getWindDirection() method uses altitude apropriate seven digit string to discern wind direction.
     * @param strAlt uses altitude input to find string and extract correct direction/exception conditions.
     * @return returns wind direction or exception statement; clam or NA.
     */
    public String getWindDir(String strAlt)
    {
        String strAltWea;
        String strRet = " ";
        int intDir = 0;
        strAltWea = getAltitudeWeather(strAlt);
        if (strAltWea.startsWith("  "))
        {
            strRet = "N/A";
        }
        else if (strAltWea.substring(0, 4).equals("9900"))
        {strRet = "Calm";}
        else
        {intDir = Integer.parseInt(strAltWea.substring(0, 2).trim());
            if(intDir > 36)
            {
                intDir = intDir - 50;
                strRet = Integer.toString(intDir);
            }
            else
            {
                strRet = strAltWea.substring(0, 2)+"0";
            }
        }
        return strRet ;
    }

    /**
     * getWindSpeed() method uses altitude apropriate seven digit string to discern wind speed.
     * @param strAlt uses altitude input to find string and extract correct speed/exception conditions.
     * @return returns wind speed or exception statement; clam or NA.
     */
    public String getWindSpeed(String strAlt)
    {
        String strAltWea = "";
        String strRet  = "";

        strAltWea = getAltitudeWeather(strAlt);
        if(strAltWea.substring(2, 4).equals("  "))
        { strRet = "N/A";}
        else if (strAltWea.startsWith("9900"))
        {strRet = "Calm"; }


        else if (Integer.parseInt(strAltWea.substring(2, 4).trim()) > 36)
        {int intSpe = Integer.parseInt(strAltWea.substring(2, 4).trim());
            intSpe = intSpe + 100;
            strRet = Integer.toString(intSpe);
        }
        else
        {
            strRet = strAltWea.substring(2, 4);
        }
        return strRet;
    }

    /**
     * getWindTemp() method uses altitude apropriate seven digit string to discern wind temperature.
     * @param strAlt  uses altitude input to find string and extract correct temperature/exception conditions.
     * @return returns wind temperature or exception statement; clam or NA.
     */
    public String getWindTemp(String strAlt)
    {
        String strAltWea = "";
        String strRet= "";
        int intTem = 0;
        strAltWea = getAltitudeWeather(strAlt);
        if(strAltWea.substring(4, 5).equals(" "))
            strRet = "N/A";
        else
        {
            intTem = Integer.parseInt(strAltWea.substring(4, 7));
            if(getPos(strAlt) >= 24000)
            {
                intTem = intTem * -1;
                strRet = Integer.toString(intTem);
            }
            else
            {strRet = strAltWea.substring(4, 7); }
        }

        return strRet;
    }

    /**
     * getStationID gets weather string and extracts first three charachters for station ID
     * @return first three charachters of wether string, returns string return
     */

    public String getStationId()
    {
        String strRet = strWeather.substring(0,3);
        return strRet;
    }
    public static void getFile(String strFileName, String strURL, Boolean blnExtract)
    {
        String webPageContentsRaw = "";
        String webPageContentsCleaned = "";
        String webPageURL = strURL;
        INET net = new INET();

        if(!blnExtract)
        {
            System.out.println(strFileName);
            try {
                webPageContentsRaw = net.getURLRaw(webPageURL);
            } catch (Exception e) {
                e.printStackTrace();
            }

            webPageContentsCleaned = webPageContentsRaw.trim();

            if (strFileName.equals(strFileName.endsWith("data/FBIN.txt")||strFileName.endsWith("data/World.txt"))) {
                webPageContentsCleaned = net.getPREData(webPageContentsRaw);
            }

            webPageContentsCleaned = webPageContentsCleaned.trim();

            try {
                net.saveToFile(strFileName, webPageContentsCleaned);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}}
