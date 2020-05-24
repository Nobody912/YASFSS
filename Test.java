import javax.crypto.*;
import java.security.*;


public class Test {
    public Test()
    {
        System.out.println("TESTING!");
        //testHash();
        //testAES();
        testFinal();
        //testCompression();
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
    
    public void testCompression()
    {
        CompressedData compressor = new CompressedData();
        System.out.println("Now Testing Compression");
        compressor.compressGzipFile("testFile.txt", "testFile.txt.gz");
        compressor.decompressGzipFile("testFile.txt.gz", "testFile.txt");
        System.out.println("Compression Complete");
        
    }

    public void testFinal() {
        Parcel testParcel = new Parcel();
        testParcel.generateRSAKeyPair();
        testParcel.sendData("test.txt", "public.key");
        testParcel.receiveData("test.txt.enc", "securedkey.key", "private.p8");
    }
    public static void main(String args[])
    {
        new Test();
    }
}