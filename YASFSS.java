import java.util.Scanner;

/**
 *  YASFSS has the GUI and the main method. The GUI allows the user to 
 *  1) Generate Keys that will help encrypt files
 *  2) Compress and Ecrypt files using the public key from option 1
 *  3) Decrypt and Decompress files using keys from the sender as well as a key
 *   from option 1
 *  Q) Quit the program
 *  All other options will give a message saying that there was an invalid 
 *  option
 *  
 *  @author  Erik Ji, Nathan Fang, Zeke Davidson
 *  @version May 23, 2020
 *  @author  Period: 3
 *  @author  Assignment: YASFSS
 */
public class YASFSS
{
    public YASFSS()
    {
        Parcel parcel = new Parcel();
        Scanner scan = new Scanner( System.in );
        String filePath;
        String hash;

        System.out.println( "Y A S F S S" );
        System.out.println( "YET ANOTHER SECURE FILE SHARING" );

        boolean done = false;

        do
        {
            System.out.println( "Select a mode of operation:" );
            System.out.println( "1) Generate Keys");
            System.out.println( "2) Compress and Encrypt a file" );
            System.out.println( "3) Decrypt and Decompress a file" );
            System.out.println( "4) Verify file" );
            System.out.println( "Q) Quit" );
            System.out.print( "YASFSS > " );

            String response = scan.nextLine();
            if ( response.length() > 0 )
            {
                System.out.println();

                switch ( response.charAt( 0 ) )
                {
                    case '1':
                        System.out.println("Generating Keypair...");
                        parcel.generateRSAKeyPair();
                        System.out.println("Done!");
                        System.out.println();
                        break;
                    case '2':
                        System.out.print( "Input target file path > " );
                        filePath = scan.nextLine();
                        System.out.print( "Input public key path > " );
                        String publicKey = scan.nextLine();
                        System.out.println( "Compressing and Encrypting..." );
                        hash = parcel.sendData( filePath, publicKey );
                        System.out.println( "Done!" );
                        System.out.println( "MD5: " + hash );
                        System.out.println();
                        break;
                    case '3':
                        System.out.print( "Input target file path > " );
                        filePath = scan.nextLine();
                        System.out.print( "Input encrypted AES key path > " );
                        String AESKey = scan.nextLine();
                        System.out.print( "Input private key path > " );
                        String privateKey = scan.nextLine();
                        System.out.println( "Decrypting and decompressing..." );
                        parcel.receiveData( filePath, AESKey, privateKey );
                        System.out.println( "Done!" );
                        System.out.println();
                        break;
                    case '4':
                        System.out.print( "Input target file path > " );
                        filePath = scan.nextLine();
                        System.out.print( "Input MD5 checksum > " );
                        hash = scan.nextLine();
                        if (parcel.verifyHash(filePath, hash))
                        {
                            System.out.println("File integrity maintained!");
                        }
                        else
                        {
                            System.out.println("File integrity test failed!");
                        }
                        System.out.println();
                        break;
                    case 'q':
                    case 'Q':
                        done = true;
                        System.out.println();
                        break;
                    default:
                        System.out.println( "Please select a valid option." );
                        System.out.println();
                        System.out.println();
                }
            }
        } while ( !done );
        System.out.println( "Goodbye!" );
    }


    /**
     * The main method, calls the GUI and allows the user to use the program.
     * @param args arguments
     */
    public static void main( String[] args )
    {
        new YASFSS();
    }
}
