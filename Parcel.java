
import javax.crypto.SecretKey;
import java.security.*;
import java.security.spec.*;
import java.nio.file.*;
import java.io.File;
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
    
            FileOutputStream out = new FileOutputStream("private.key");
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

            compressor.compressGzipFile(filePath, filePath + ".gz");

            byte[] rawFile = Files.readAllBytes(Paths.get(filePath + ".gz"));
            Object[] encrypted = encryptor.encryptData(rawFile);
            byte[] encryptedData = (byte[]) encrypted[0];
            SecretKey secretKey = (SecretKey) encrypted[1];

            byte[] publicKeyRaw = Files.readAllBytes(Paths.get(publicKeyPath));
            X509EncodedKeySpec ks = new X509EncodedKeySpec(publicKeyRaw);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey publicKey = kf.generatePublic(ks);
            byte[] encryptedKey = encryptor.encryptKey(publicKey, secretKey);

            // write out encryptedData as "filePath.extension.enc"
            FileOutputStream out = new FileOutputStream(filePath + ".enc");
            out.write(encryptedData);
            out.close();

            out = new FileOutputStream("secured.key");
            out.write(encryptedKey);
            out.close();

            // remove filePath.gz file
            File temp = new File(filePath + ".gz");
            temp.delete();
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

            byte[] encryptedData = Files.readAllBytes(Paths.get(filePath));
            byte[] encryptedKey = Files.readAllBytes(Paths.get(AESKeyPath));

            byte[] privateKeyRaw = Files.readAllBytes(Paths.get(privateKeyPath));
            PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(privateKeyRaw);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = kf.generatePrivate(ks);
            
            SecretKey secretKey = encryptor.decryptKey(encryptedKey, privateKey);

            byte[] decryptedData = encryptor.decryptData(encryptedData, secretKey);

            FileOutputStream out = new FileOutputStream(filePath.substring(0, filePath.lastIndexOf(".")) + ".gz");
            out.write(decryptedData);
            out.close();

            compressor.decompressGzipFile(filePath.substring(0, filePath.lastIndexOf(".")) + ".gz", filePath.substring(0, filePath.lastIndexOf(".")));
        }
        
        catch (Exception e)
        {
            System.out.println("exception processing data for sending: " + e.getMessage());
        }
    }
}