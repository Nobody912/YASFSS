import java.io.*;
import java.util.zip.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressedData
{
    
    private static void decompressGzipFile(String gzipFile, String newFile) {
        try {
            FileInputStream fis = new FileInputStream(gzipFile);
            GZIPInputStream gis = new GZIPInputStream(fis);
            FileOutputStream fos = new FileOutputStream(newFile);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            //close resources
            fos.close();
            gis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    private static void compressGzipFile(String file, String gzipFile) {
        try {
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(gzipFile);
            GZIPOutputStream gzipOS = new GZIPOutputStream(fos){{def.setLevel(Deflater.BEST_COMPRESSION);}};
            byte[] buffer = new byte[1024];
            int len;
            while ((len=fis.read(buffer)) != -1) {
                gzipOS.write(buffer, 0, len);
            }
            //close resources
            gzipOS.close();
            fos.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
 
    public static void main(String[] args) 
    {
        String FILENAME = "Abed's Uncontrollable Christmas.mp4";
        String file = FILENAME;
        String gzipFile = FILENAME + ".gz";
        String newFile = "new_" + FILENAME;
        compressGzipFile(file, gzipFile);
        decompressGzipFile(gzipFile, newFile);
    }
}