import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.crypto.SecretKey;
import java.security.*;
import java.security.spec.*;

public class Parcel
{
    /**
     * takes file and turns it into a byte array
     * @param filePath
     * @return
     */
    private byte[] turnFileIntoByteArray(String filePath)
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
    
    /**
     * takes byte array and turns it into a file
     * @param array
     * @param filePath
     * @return
     */
    private File turnByteArrayIntoFile(byte[] array, String filePath)
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
    private void exportData(String filePath, String keyAESPath, String keyPublicRSAPath)
    {
        CompressedData compressor = new CompressedData();
        SecuredData encryptor = new SecuredData();
        

    }
    
    /**
     * Turn file into byte[] >
     * decrypt >
     * decompress >
     * turn back into file
     */
    private void importData(String filePath, String keyEncryptedAESPath)
    {
        CompressedData compressor = new CompressedData();
        SecuredData encryptor = new SecuredData();

    }

    public void generateRSAKeyPair()
    {
        try
        {
            SecuredData encryptor = new SecuredData();
            PublicKey publicKey = encryptor.getPublicKey();
            PrivateKey privateKey = encryptor.getPrivateKey();
    
            FileOutputStream out = new FileOutputStream("private.p8");
            out.write(privateKey.getEncoded());
            out.close();
    
            out = new FileOutputStream("public.key");
            out.write(publicKey.getEncoded());
            out.close();
        }
        
        catch (Exception e)
        {
            System.out.println("exception generating RSA keypairs: " + e.getMessage());
        }
    }

    public void sendData()
    {
        try
        {
            SecuredData encryptor = new SecuredData();

        }
        
        catch (Exception e)
        {
            System.out.println("exception generating RSA keypairs: " + e.getMessage());
        }
    }
}