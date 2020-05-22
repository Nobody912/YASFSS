import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * This is all based on a site I saw on compression in Java
 * https://www.codejava.net/java-se/file-io/how-to-compress-files-in-zip-format-in-java
 * 
 * This compresses files into a zip file,which might not be what we want. So
 * I'll try to find something else if we need it.
 * -Zeke
 */
public class CompressedData
{
    public static void compress(String filePath)
    {
        try 
        {
            File file = new File(filePath);
            String fileName = file.getName().concat(".zip");
            
            FileOutputStream fos = new FileOutputStream(fileName);
            ZipOutputStream zos = new ZipOutputStream(fos);
 
            zos.putNextEntry(new ZipEntry(file.getName()));
 
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            zos.write(bytes, 0, bytes.length);
            zos.closeEntry();
            zos.close();
 
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
 
    //commented out bc i made a cooler, newer class just for mains
    //-nathan
    /*
     * public static void main(String[] args) { String filePath = args[0];
     * compress(filePath); }
     */
}