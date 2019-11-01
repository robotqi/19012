/**
 @author Chelsea Dorich (Email: <a href="mailto:"robotqi@gmail.com>robotqi@gmail.com</a>)
 @version 1.1 03/19/2014
 @assignment.number A190-09
 @prgm.usage Called from the operating system
 @see "Gaddis, 2013, Starting out with Java, From Control Structures, 5th Edition"
 @see "<a href='http://docs.oracle.com/javase/7/docs/technotes/guides/javadoc/index.html'>JavaDoc Documentation</a>

 */
public interface INETTemplate
{
    /**
     * Accept a full file path and name and determine if it exists on disk. If it does, return a true. If it does not exist, return a false.
     * @param strFileName
     * @return
     */
    public Boolean fileExists(String strFileName);

    /**
     * Accept a full file path and name as a parameter and check to see if it exists using the fileExists method. If it does exist, read the file from disk and return the contents as a string. If the file does not exist, return a null.
     * @param strFileName
     * @return
     * @throws Exception
     */
    public String getFromFile(String strFileName) throws Exception;

    /**
     * Accept a full file path and name as a parameter and check to see if it exists using the fileExists method. If it does exist, read the file from disk and return the contents as a string. If the file does not exist, return a null.
     * @param strPage
     * @return
     */
    public String getPREData(String strPage);

    /**
     * Extract a small string from a larger one using a Regular Expression and the pattern specified as a parameter. If nothing is found, return a null.
     * @param strInput
     * @param strPatten
     * @return
     */
    public String getRegEx(String strInput, String strPatten);

    /**
     * Accept a string URL, create a string using the StringBuilder class and return it as one long string. If the page does not exist, return a null.
     * @param strURL
     * @return
     * @throws Exception
     */
    public String getURLRaw(String strURL) throws Exception;

    /**
     * Accept a string and convert the whole string into lower case then upper case only the first letter of each word.
     * @param strInput
     * @return
     */
    public String properCase(String strInput);

    /**
     * Accept a string and save it to a local file.
     * @param strFileName
     * @param strContent
     * @throws Exception
     */
    public void saveToFile(String strFileName, String strContent) throws Exception;

}
