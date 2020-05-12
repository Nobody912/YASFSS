public class Test {
    public Test()
    {
        System.out.println("TESTING!");
        testHash();
        testAES();
    }

    public void testHash()
    {
        System.out.println("### HASH TESTING ###");
        final String TEXT = "Hello World!";
        final String HASH = "ed076287532e86365e841e92bfc50d8c";
        try {
            EncryptedData test = new EncryptedData();
            String result = test.getHash(TEXT.getBytes("UTF-8"));
            System.out.println("CHECK: " + HASH + "\nHASH: " + result);
            System.out.println("RESULT VALID?: " + HASH.equals(result));
        }

        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public void testAES()
    {
        final String TEXT = "Hello World!";
        try {
            System.out.println("### ENCRYPTION/DECRYPTION (AES) TESTING ###");
            System.out.println("ORIGINAL: " + TEXT);

            EncryptedData test = new EncryptedData(2048, 128);
            byte[] encrypted = test.encryptData(TEXT.getBytes("UTF-8"));

            byte[] decrypted = test.decryptData(encrypted);
            String result = new String(decrypted);
            System.out.println("EN/DECRYPTED: " + result);

            System.out.println("RESULT VALID?: " + TEXT.equals(result));
        }

        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public static void main(String args[])
    {
        new Test();
    }
}