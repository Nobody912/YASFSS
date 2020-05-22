import java.util.Scanner;

//GUI STUFF


public class YASFSS
{
    public static void main(String[] args) 
    {
        Scanner scan = new Scanner( System.in );
        String filepath;
        System.out.println( "put in your file path" );
        filepath = scan.nextLine();
        CompressedData.compress( filepath );
        System.out.println("file encrypted and compressed");
    }
}
