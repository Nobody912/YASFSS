import java.io.*;
import java.util.zip.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


/**
 *  CompressedData is used to compress and decompress data. This is used by 
 *  Parcel along with SecuredData to secure files.
 *
 *  @author  Erik Ji, Nathan Fang, Zeke Davidson
 *  @version May 23, 2020
 *  @author  Period: 3
 *  @author  Assignment: YASFSS
 */
public class CompressedData
{
    /**
     * Compresses the file by using a Gzip file. 
     * 
     * @param file
     *            the file that the user wants to compress
     * @param gzipFile
     *            the new file that hold the compressed data
     */
    public void compressGzipFile( String file, String gzipFile )
    {
        try
        {
            FileInputStream fis = new FileInputStream( file );
            FileOutputStream fos = new FileOutputStream( gzipFile );
            GZIPOutputStream gzipOS = new GZIPOutputStream( fos )
            {
                {
                    def.setLevel( Deflater.BEST_COMPRESSION );
                }
            };
            byte[] buffer = new byte[1024];
            int len;
            while ( ( len = fis.read( buffer ) ) != -1 )
            {
                gzipOS.write( buffer, 0, len );
            }

            gzipOS.close();
            fos.close();
            fis.close();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }


    /**
     * Decompresses a given file from a Gzip format back into its original
     * format.
     * 
     * @param gzipFile
     *            The compressed file that will be decompressed
     * @param newFile
     *            The new file that holds the decompressed data
     */
    public void decompressGzipFile( String gzipFile, String newFile )
    {
        try
        {
            FileInputStream fis = new FileInputStream( gzipFile );
            GZIPInputStream gis = new GZIPInputStream( fis );
            FileOutputStream fos = new FileOutputStream( newFile );
            byte[] buffer = new byte[1024];
            int len;
            while ( ( len = gis.read( buffer ) ) != -1 )
            {
                fos.write( buffer, 0, len );
            }

            fos.close();
            gis.close();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

    }
}