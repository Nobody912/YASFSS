import javax.crypto.*;
import javax.crypto.spec.*;
import java.nio.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;

public class SecuredData {
    private KeyPair keyPair;
    private int keyLengthAES;

    // constructors
    public SecuredData(int keyLengthRSA, int keyLengthAES) {
        try
        {
            // setting key length
            this.keyLengthAES = keyLengthAES;

            // generate RSA keys
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(keyLengthRSA);
            this.keyPair = keyGen.generateKeyPair();
        }
        
        catch (Exception e) {
            System.out.println("exception creating EncryptedData object: " + e.getMessage());
        }
    }

    public SecuredData() {
        try
        {
            // setting key length
            this.keyLengthAES = 128;

            // generate RSA keys
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            this.keyPair = keyGen.generateKeyPair();
        }
        
        catch (Exception e) {
            System.out.println("exception creating EncryptedData object: " + e.getMessage());
        }
    }

    // methods
    /**
     * generateAESKey
     * @param iv initialization vector
     * @param password 
     * @return SecretKey
     */
    private SecretKey generateAESKey(byte[] iv, String secret) {
        SecretKey result = null; 
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String processedPassword = Arrays.copyOfRange(digest.digest(secret.getBytes("UTF-8")), 0, 16).toString();

            KeySpec spec = new PBEKeySpec(processedPassword.toCharArray(), iv, 200000, this.keyLengthAES);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] key = secretKeyFactory.generateSecret(spec).getEncoded();
            new SecretKeySpec(key, "AES");
            result = new SecretKeySpec(key, "AES");
        }

        catch (Exception e) {
            System.out.println("exception generating AES key: " + e.getMessage());
        }
        return result;
    }

    public Object[] encryptData(byte[] input)
    {
        SecureRandom secureRandom = new SecureRandom();
        byte[] temp = new byte[16];
        secureRandom.nextBytes(temp);
        String secret = temp.toString();
        return encryptDataHelper(input, secret);
    }

    public Object[] encryptData(byte[] input, String secret)
    {
        return encryptDataHelper(input, secret);
    }

    /**
     * encryptData
     * @param input data input
     * @param password AES password
     * @return Object[]{byte[] data, SecretKey key}
     */
    private Object[] encryptDataHelper(byte[] input, String secret) {
        ByteBuffer byteBuffer = null;
        SecretKey key = null;

        try {
            // prepping nonce
            SecureRandom secureRandom = new SecureRandom();
            byte[] iv = new byte[12];
            secureRandom.nextBytes(iv);

            //prepping keys
            key = generateAESKey(iv, secret);

            // creating cipher
            Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec parameterSpec = new GCMParameterSpec(this.keyLengthAES, iv);
            c.init(Cipher.ENCRYPT_MODE, key, parameterSpec);

            // encrypting
            byte[] encryptedData = c.doFinal(input);

            // prep for shipping
            byteBuffer = ByteBuffer.allocate(4 + iv.length + encryptedData.length);
            byteBuffer.putInt(iv.length);
            byteBuffer.put(iv);
            byteBuffer.put(encryptedData);
        }

        catch (Exception e)
        {
            System.out.println("exception encrypting data: " + e.getMessage());
        }
        return new Object[]{byteBuffer.array(), key};
    }

    /**
     * decryptData
     * @param input data input
     * @param secretKey secretKey
     * @return byte[] data
     */
    public byte[] decryptData(byte[] input, SecretKey secretKey) {
        byte[] decryptedData = null;

        try {
            // wrapping byte[] input
            ByteBuffer byteBuffer = ByteBuffer.wrap(input);

            // nonce check prep
            int nonceSize = byteBuffer.getInt();

            if (nonceSize < 12 || nonceSize > 15) {
                throw new IllegalArgumentException("nonce size incorrect; your file may have been tampered.");
            }

            // initialization vector
            byte[] iv = new byte[nonceSize];
            byteBuffer.get(iv);

            //read cipher bytes 
            byte[] cipherBytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(cipherBytes);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec parameterSpec = new GCMParameterSpec(this.keyLengthAES, iv);

            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
            
            decryptedData = cipher.doFinal(cipherBytes);
        }

        catch (Exception e)
        {
            System.out.println("exception decrypting data: " + e.getMessage());
        }

        return decryptedData;
    }

    /**
     * encryptKey
     * @param publicKey publicKey
     * @param secretKey secretKey
     * @return byte[] data
     */
    public byte[] encryptKey(PublicKey publicKey, SecretKey secretKey) {
        Cipher cipher = null;
        byte[] key = null;
    
        try {
            // initialize the cipher with the user's public key
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            key = cipher.doFinal(secretKey.getEncoded());
        }

        catch (Exception e) {
            System.out.println("exception encoding key: " + e.getMessage());
        }
        return key;
    }

    /**
     * decryptKey
     * @param privateKey privateKey
     * @param secretKey 
     * @return
     */
    public SecretKey decryptKey(byte[] input, PrivateKey privateKey) {
        Cipher cipher = null;
        SecretKey key = null;
    
        try {
            // initialize the cipher with the user's public key
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            key = new SecretKeySpec(cipher.doFinal(input), "AES");
        }

        catch (Exception e) {
            System.out.println("exception encoding key: " + e.getMessage());
        }
        return key;
    }

    /**
     * getPublicKey
     * @return PublicKey
     */
    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }

    public String getHash(byte[] fileSample) {
        StringBuilder sb = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] result = digest.digest(fileSample);

            // converting byte array to Hexadecimal String
            sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
            sb.append(String.format("%02x", b&0xff));
            }
        }

        catch (Exception e)
        {
            System.out.println("exception creating hash: " + e.getMessage());
        }

        return sb.toString();
    }
}