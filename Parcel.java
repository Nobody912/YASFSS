import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.crypto.SecretKey;

public class Parcel
{
    /**takes file and turns it into a byte array
     * https://mkyong.com/java/how-to-convert-file-into-an-array-of-bytes/
     * @param filePath
     * @return
     */
    public byte[] turnFileIntoByteArray(String filePath)
    {
        try
        {
            File file = new File(filePath);
            byte[] array = new byte[(int)file.length()];
            FileInputStream fis = new FileInputStream(file);
            fis.read(array);
            fis.close();
            
            return array;
        }
        catch (FileNotFoundException ex) 
        {
            System.err.format("The file %s does not exist", filePath);
        } 
        catch (IOException ex) 
        {
            System.err.println("I/O error: " + ex);
        }
        return null;
    }
    
    /**takes byte array and turns it into a file
     * https://stackoverflow.com/questions/23533695/java-convert-bytes-to-file
     * @param array
     * @param filePath
     * @return
     */
    public File turnByteArrayIntoFile(byte[] array, String filePath)
    {
        File output = new File(filePath);
        try
        {
            FileOutputStream fos = new FileOutputStream(output);
            fos.write(array);
            return output;
        }
        catch (FileNotFoundException ex) 
        {
            System.err.format("The file %s does not exist", filePath);
        } 
        catch (IOException ex) 
        {
            System.err.println("I/O error: " + ex);
        }
        return null;
    }
    
    
    
    /**
     * Turn file into byte[] >
     * compress >
     * encrypt >
     * turn back into file
     */
    public void exportData(String filePath, String secret)
    {
        CompressedData compressor = new CompressedData();
        SecuredData encryptor = new SecuredData();
        
        byte[] array = turnFileIntoByteArray(filePath);
        
        compressor.compressGzipFile(filePath, filePath.concat( ".gz" ));
        
//        array = encryptor.encryptData(array, secret); //is encrypt data supposed to return Object[]?
        
        File file = turnByteArrayIntoFile(array, filePath);
    }
    
    /**
     * Turn file into byte[]>
     * decrypt >
     * decompress >
     * turn back into file
     */
    public void importData(String filePath, String secret)
    {
        CompressedData compressor = new CompressedData();
        SecuredData encryptor = new SecuredData();
        
        byte[] array = turnFileIntoByteArray(filePath);
//        SecretKey key = encryptor.generateAESKey(array, secret);
        
//        encryptor.decryptData(array, key);
        
        compressor.decompressGzipFile(filePath, filePath);//change filePath
        
        
    }
}