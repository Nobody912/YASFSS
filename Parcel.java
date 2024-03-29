import java.security.*;
import java.security.spec.*;
import java.nio.file.*;
import java.io.*;
import java.util.Arrays;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;

/**
 *  compresses and encrypts the data
 *
 *  @author  Erik Ji, Nathan Fang, Zeke Davidson
 *  @version May 23, 2020
 *  @author  Period: 3
 *  @author  Assignment: YASFSS
 */
public class Parcel
{
    /**
     * generates the rsa keypair
     * aka the public and private key
     */
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

    /**
     * encrypts and compresses the data
     * @param filePath filepath to file
     * @param publicKeyPath filepath to key
     */
    public String sendData(String filePath, String publicKeyPath)
    {
        String hash = null;
        try
        {
            hash = getHash(filePath);
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
        
        return hash;
    }

    /**
     * decompresses and decrypts the data
     * @param filePath filepath to file
     * @param AESKeyPath path to aes key
     * @param privateKeyPath path to private key
     */
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
            
            File temp = new File(filePath + ".gz");
            temp.delete();
        }
        
        catch (Exception e)
        {
            System.out.println("exception processing data for receiving: " + e.getMessage());
        }
    }

    /**
     * hashes the data
     * @param fileSample data to be hashed
     * @return a hash of fileSample
     */
    public String getHash(String filePath) {
        String hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(Files.readAllBytes(Paths.get(filePath)));
            byte[] digest = md.digest();
            hash = DatatypeConverter
              .printHexBinary(digest).toUpperCase(); 
        }

        catch (Exception e)
        {
            System.out.println("exception creating hash: " + e.getMessage());
        }

        return hash;
    }

    /**
     * verifies hash
     * @param filePath path to file
     * @param hash hash to be compared
     * @return boolean whether hashes are equal
     */
    public boolean verifyHash(String filePath, String hash) {
        String realHash = getHash(filePath);
        return hash.equals(realHash);
    }
}