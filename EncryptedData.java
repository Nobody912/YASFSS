import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.InputStream;
import java.nio.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;

public class EncryptedData {
    private KeyPair keyPair;
    private byte[] symmerticalKey;
    private int keyLengthAES;
    private int keyLengthRSA;

    public EncryptedData(int keyLengthRSA, int keyLengthAES) throws NoSuchAlgorithmException {
        // setting key lengths
        this.keyLengthAES = keyLengthAES;
        this.keyLengthRSA = keyLengthRSA;

        // generate RSA keys
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keyLengthRSA);
        this.keyPair = keyGen.generateKeyPair();

        // generate AES keys
        this.symmerticalKey = new byte[keyLengthAES];
        SecureRandom secureRandom = new SecureRandom(); 
        secureRandom.nextBytes(symmerticalKey);
    }

    public EncryptedData(int keyLengthRSA, int keyLengthAES, String password) throws NoSuchAlgorithmException {
        // setting key lengths
        this.keyLengthAES = keyLengthAES;
        this.keyLengthRSA = keyLengthRSA;

        // generate RSA keys
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keyLengthRSA);
        this.keyPair = keyGen.generateKeyPair();

        // use password to generate symmetrical key
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        this.symmerticalKey = Arrays.copyOfRange(password.getBytes("UTF-8"), 0, 16);
    }

    private SecretKey generateAESKey(byte[] iv) {
        KeySpec spec = new PBEKeySpec(new String(symmerticalKey, "UTF-8").toCharArray(), iv, 65536, this.keyLengthAES);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] key = secretKeyFactory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(key, "AES");
    }

    public byte[] encryptKey(byte[] input) {
        //thonk
    }

    public byte[] decryptKey(byte[] input) {
        //thonk
    }

    public byte[] encryptData(byte[] input) {
        // prepping nonce
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[12];
        secureRandom.nextBytes(iv);

        //prepping keys
        SecretKey key = generateAESKey(iv);

        // creating cipher
        Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(this.keyLengthAES, iv);
        c.init(Cipher.ENCRYPT_MODE, key, parameterSpec);

        // encrypting
        byte[] encryptedData = c.doFinal(input);
        
        // prep for shipping
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 + iv.length + encryptedData.length);
        byteBuffer.putInt(iv.length);
        byteBuffer.put(iv);
        byteBuffer.put(encryptedData);

        return byteBuffer.array();
    }

    public byte[] decryptData(byte[] input) {
        // wrapping byte[] input
        ByteBuffer byteBuffer = ByteBuffer.wrap(input);

        // nonce check prep
        int nonceSize = byteBuffer.getInt();

        if (nonceSize < 12 || nonceSize > 15)
        {
            throw new IllegalArgumentException("Nonce size incorrect.");
        }

        // initialization vector
        byte[] iv = new byte[nonceSize];
        byteBuffer.get(iv);

        // generate keys
        SecretKey secretKey = generateAESKey(iv);

        //read cipher bytes 
        byte[] cipherBytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherBytes);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(this.keyLengthAES, iv);

        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

        return cipher.doFinal(cipherBytes);
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }