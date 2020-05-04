
import javax.crypto.Cipher;
import java.io.InputStream;
import java.security.*;
import java.util.Base64;

//Test Comment -Zeke

public class EncryptedData {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public EncryptedData() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        KeyPair pair = keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public void encrypt(String data) {
        
    }
}