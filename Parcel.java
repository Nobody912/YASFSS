import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Parcel 
{
    //exports byte array and turns it into a file
    public File exportData(byte[] array)
    {
        File file = new File(file);
        return file;
    }
    
    //imports file and turns it into a byte array
    public Object[] importData(String filePath)
    {
        try
        {
            File file = new File(filePath);
            
        }
        catch (FileNotFoundException ex) 
        {
            System.err.format("The file %s does not exist", filePath);
        } 
        catch (IOException ex) 
        {
            System.err.println("I/O error: " + ex);
        }
    }
}