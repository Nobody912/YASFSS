
import javax.crypto.SecretKey;
import java.security.*;
import java.security.spec.*;
import java.nio.file.*;
import java.io.FileOutputStream;

public class Parcel
{
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

    public void sendData(String filePath, String publicKeyPath)
    {
        try
        {
            SecuredData encryptor = new SecuredData();
            CompressedData compressor = new CompressedData();

            compressor.compressGzipFile(file, gzipFile);

            byte[] rawFile = Files.readAllBytes(Paths.get(filePath));
            Object[] encrypted = encryptor.encryptData(rawFile);
            byte[] encryptedData = (byte[]) encrypted[0];
            SecretKey secretKey = (SecretKey) encrypted[1];

            byte[] publicKeyRaw = Files.readAllBytes(Paths.get(publicKeyPath));
            X509EncodedKeySpec ks = new X509EncodedKeySpec(publicKeyRaw);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey publicKey = kf.generatePublic(ks);
            byte[] encryptedKey = encryptor.encryptKey(publicKey, secretKey);
        }
        
        catch (Exception e)
        {
            System.out.println("exception processing data for sending: " + e.getMessage());
        }
    }

    public void receiveData(String filePath, String AESKeyPath, String privateKeyPath)
    {
        try
        {
            SecuredData encryptor = new SecuredData();
            CompressedData compressor = new CompressedData();

            byte[] encryptedFile = Files.readAllBytes(Paths.get(filePath));
            
        }
        
        catch (Exception e)
        {
            System.out.println("exception processing data for sending: " + e.getMessage());
        }
    }
}