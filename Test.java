import javax.crypto.*;
import javax.crypto.spec.*;
import java.nio.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;
import java.lang.reflect.Array; 

public class Test {
    public Test()
    {
        System.out.println("TESTING!");
        testHash();
        testAES();
        testCompression();
        //testKeyEncryption();
    }

    public void testHash()
    {
        try {
            System.out.println("### HASH TESTING ###");
            final String TEXT = "Hello World!";
            final String HASH = "ed076287532e86365e841e92bfc50d8c";
            
            SecuredData test = new SecuredData();
            String result = test.getHash(TEXT.getBytes("UTF-8"));
    
            System.out.println("CHECK: " + HASH + "\nHASH: " + result);
            System.out.println("RESULT VALID?: " + HASH.equals(result));
        }

        catch (Exception e) {
            System.out.println("exception decrypting data: " + e.getMessage());
        }
    }

    public void testAES() {
        final String TEXT = "Hello World!";
        try {
            byte[] data;
            SecretKey secretKey;

            System.out.println("### ENCRYPTION/DECRYPTION (AES/NOPASS) TESTING ###");
            
            SecuredData test = new SecuredData(2048, 128);

            PublicKey puKey = test.getPublicKey();
            PrivateKey prKey = test.getPrivateKey();

            Object[] encrypted = test.encryptData(TEXT.getBytes());
            data = (byte[]) encrypted[0];
            secretKey = (SecretKey) encrypted[1];

            byte[] encryptedKey = test.encryptKey(puKey, secretKey);
            
            // hand off
            String decryptedData;
            SecretKey decryptedKey = test.decryptKey(encryptedKey, prKey);            
            decryptedData = new String(test.decryptData(data, decryptedKey), "UTF-8");

            System.out.println("ORIGINAL: " + TEXT);
            System.out.println("EN/DECRYPTED: " + decryptedData);
            System.out.println("RESULT VALID?: " + TEXT.equals(decryptedData));
        }

        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void testTrade() {
        // 1) receiver generates keys
        SecuredData lol = new SecuredData(2048, 128);
        PrivateKey privateKey = lol.getPrivateKey();
        PublicKey publicKey = lol.getPublicKey();

        // 2) receiver sends the publicKey

        // 3)  sender generates AES key
        String secretStuff = "wow very cool";
        SecuredData pog = new SecuredData(2048, 128);
        Object[] encryptedStuff = pog.encryptData(secretStuff.getBytes());
        byte[] encryptedData = (byte[]) encryptedStuff[0];
        SecretKey key = (SecretKey) encryptedStuff[1];
        byte[] encryptedKey = pog.encryptKey(publicKey, key);

        // 4)  sender sends encrypted data and encrypted key
        
        // 5) receiver decrypts key and data
        SecuredData nice = new SecuredData(2048, 128);
        SecretKey decryptedKey = nice.decryptKey(encryptedKey, privateKey);
        byte[] decryptedData = nice.decryptData(encryptedData, decryptedKey);
    }
    
    public void testCompression()
    {
        CompressedData compressor = new CompressedData();
        System.out.println("Now Testing Compression");
        compressor.compressGzipFile("testFile.txt", "testFile.txt.gz");
        compressor.decompressGzipFile("testFile.txt.gz", "testFile.txt");
        System.out.println("Compression Complete");
        
    }

    public static void main(String args[])
    {
        new Test();
    }
}