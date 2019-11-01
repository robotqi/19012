import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 @author Chelsea Dorich (Email: <a href="mailto:"robotqi@gmail.com>robotqi@gmail.com</a>)
 @version 1.1 05/02/2014
 @assignment.number A190-12
 @prgm.usage Called from the operating system
 @see "Gaddis, 2013, Starting out with Java, From Control Structures, 5th Edition"
 @see "<a href='http://docs.oracle.com/javase/7/docs/technotes/guides/javadoc/index.html'>JavaDoc Documentation</a>
 */
public class INET
        implements INETTemplate
{
    /**
     * Accept a full file path and name and determine if it exists on disk. If it does, return a true. If it does not exist, return a false.
     *
     * @param strFileName accepts the file name to look for
     * @return doesFileExist boolean to signify exsistance of file
     */
    @Override
    public Boolean fileExists(String strFileName)
    {
        //initialise boolean to false
        boolean doesFileExist = false;
        //create instance of input file with strfilename
        File inputFile = new File(strFileName);
        //if file exists, enter if statement and change bln to true
        if (inputFile.exists())
        {
            doesFileExist = true;
        }
        //return whatever value the bln contains
        return doesFileExist;
    }


    /**
     * Accept a full file path and name as a parameter and check to see if it exists using the fileExists method. If it does exist, read the file from disk and return the contents as a string. If the file does not exist, return a null.
     *
     * @param strFileName
     * @return
     * @throws Exception
     */
    @Override
    public String getFromFile(String strFileName) throws Exception
    {
        //check to see if file exists
        if (fileExists(strFileName))
        {
            //create new reader to read from file name in paramater
            BufferedReader readFile = new BufferedReader(new FileReader(strFileName));
            //create a string builder to use to rebuild modified string
            StringBuilder stbFileContent = new StringBuilder("");
            //while the file still has more records
            while(readFile.ready())
            {
                // read a line and
                String strRecord = readFile.readLine() + "\r\n";
                //append it to strContent
                stbFileContent.append(strRecord);
            }
            //return whatevers in the string
            return stbFileContent.toString();
        }
        //if nothing exists where the file is supposed to be,
        else
        {
            //fill the string with  n/a statement
            String fileDoesNotExist = "N/A";
            //and return that as an error
            return fileDoesNotExist;
        }
    }

    /**
     * Accept a full file path and name as a parameter and check to see if it exists using the fileExists method. If it does exist, read the file from disk and return the contents as a string. If the file does not exist, return a null.
     *
     * @param strPage is the block of raw data to be cleaned with this method
     * @return strData; the cleaned modified block of information
     */
    @Override
    public String getPREData(String strPage)
    {
        //create string and fill it using te getregx method,handing it the paramater and a command for pre tags
        String strData = getRegEx(strPage, "<PRE>(.)*</PRE>");
        //return the data from the regex function
        return strData;
    }

    /**
     * Extract a small string from a larger one using a Regular Expression and the pattern specified as a parameter. If nothing is found, return a null.
     *
     * @param strInput is the raw data to be modified using regex
     * @param strPattern is the regex command that will manipulate given data
     * @return strRet the modified data, trimmed of excess spaces.
     */
    @Override
    public String getRegEx(String strInput, String strPattern)
    {
        //start with an empty string
        String strRet = "";
        //instantiate pattern form and give it some commands
        Pattern pattern = Pattern.compile(strPattern,
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        //instantiate matcher and use it with pattern
        Matcher matcher = pattern.matcher(strInput);
        //while the matcher is working
        while (matcher.find())
        {
            //fill strRet with origonal, carrige return, and matcher find
            strRet = strRet + "\n" + matcher.group();
        }
        //if thee string returned is less than 1
        if (strRet.length() < 1 )
        {
            //return 'not found'
            strRet = "String Not Found";
        }
        //return trimmed string
        return strRet.trim();
    }

    /**
     * Accept a string URL, create a string using the StringBuilder class and return it as one long string. If the page does not exist, return a null.
     *
     * @param strURL the url to the web page you are looking for
     * @return  raw data from the given url
     * @throws Exception
     */
    @Override
    public String getURLRaw(String strURL) throws Exception
    {
        //declare an empty string builder
        StringBuilder stbContent = new StringBuilder("");
        //for run time security, use a try catch statement to implement this block of code
        try
        {
            //instantiate url
            URL myURL = new URL(strURL);
            //instantiate open connection
            URLConnection myURLConnection = myURL.openConnection();
            //instantiate input stream
            InputStream urlDownload = myURLConnection.getInputStream();
            //create buffered reder to read from url
            BufferedReader inputFile = new BufferedReader (new InputStreamReader(urlDownload));
            //create empty string for content to be held
            String strContent="";
            //while the input file is ready
            while(inputFile.ready())
            {
                // read a line and append it to strContent
                String strRecord = inputFile.readLine() + "\r\n";
                //append to the string builder
                stbContent.append(strRecord);
            }
        }
        //if the try fails, this catch will occour
        catch (IOException errnum)
        {
            //an error will print on the screen
            System.out.println(errnum.getMessage());
        }
        //return the string builders content, converted back to a regular string
        return stbContent.toString();
    }

    /**
     * Accept a string and convert the whole string into lower case then upper case only the first letter of each word.
     *
     * @param strInput is the string to be modified
     * @return modified string builder to string
     */
    @Override
    public String properCase(String strInput)
    {
        //convert all input to lower case
        strInput = strInput.toLowerCase();
        //declare a tokenizer and hand it the input string, using thed space as a deliminater, and reinserting
        //them as the program goes
        StringTokenizer strTokenizer = new StringTokenizer(strInput, " ", true);
        //create new string builder
        StringBuilder stbContent = new StringBuilder("");
        //while there are more tokens
        while(strTokenizer.hasMoreTokens())
        {//get next token and hand tostring
            String strToken = strTokenizer.nextToken();
            //using substrings, change the first letter tonupper case and append the rest back
            strToken = strToken.substring(0,1).toUpperCase()+ strToken.substring(1);
            //add the modified token to the string builder
            stbContent.append(strToken);
        }
        //return the builder converted to string
        return stbContent.toString();
    }

    /**
     * Accept a string and save it to a local file.
     *
     * @param strFileName the path of the intended file
     * @param strContent the data to be saved to the file
     * @throws Exception
     */
    @Override
    public void saveToFile(String strFileName, String strContent) throws Exception
    {//create new print writer
        PrintWriter outputFile = new PrintWriter(strFileName);
        //print content to output file
        outputFile.println(strContent);
        //close and save output file
        outputFile.close();
    }
}