import java.util.Scanner;

// GUI STUFF


public class YASFSS
{
    public YASFSS()
    {
        Parcel parcel = new Parcel();
        Scanner scan = new Scanner( System.in );
        String filePath;
        String secret;

        boolean done = false;

        do
        {
            System.out.println( "Would You like to: " );   
            System.out.println( "1) Compress and Encrypt a file" );
            System.out.println( "2) Decrypt and Decompress a file" );
            System.out.println( "Q) Quit" );

            String response = scan.nextLine();
            if ( response.length() > 0 )
            {
                System.out.println();

                switch ( response.charAt( 0 ) )
                {
                    case '1':
                        System.out.println( "Please put in your file path: " );
                        filePath = scan.nextLine();
                        System.out.println( "What would you like the secret to be?" );
                        secret = scan.nextLine();
                        System.out.println( "Your file is being encrypted and compressed" );
                        System.out.println( "..." );
                        parcel.importData( filePath, secret );
                        System.out.println( "Done" );
                        System.out.println();
                        break;
                    case '2':
                        System.out.println( "put in your file path" );
                        filePath = scan.nextLine();
                        System.out.println( "What is the password?" );
                        secret = scan.nextLine();
                        System.out.println( "Your file is being decompressed and decrypted" );
                        System.out.println( "..." );
                        parcel.exportData( filePath, secret );
                        System.out.println( "Done" );
                        System.out.println();
                        break;
                    case 'q':
                    case 'Q':
                        done = true;
                        System.out.println();
                        break;
                    default:
                        System.out.println( "Please select a valid option" );
                        System.out.println();
                        System.out.println();
                }
            }
        } while ( !done );
        System.out.println( "Goodbye!" );
    }


    public static void main( String[] args )
    {
        new YASFSS();
    }
}
