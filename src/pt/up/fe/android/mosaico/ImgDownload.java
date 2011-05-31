package pt.up.fe.android.mosaico;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/** 
 * Class that downloads images from an URL through http.
 */
public class ImgDownload {
	
	/** 
	 * Class that downloads a image file from an URL through http.
	 * @param URL The URL of the image file to be downloaded.
	 * @return the Bitmap of the image
	 */
	public static Bitmap getImage(String URL){
		Bitmap tmpBitmap =null;
		InputStream in = null;        
        try {
            in = OpenHttpConnection(URL);
            tmpBitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return tmpBitmap;
	}
	
	/**
     * resizes a Bitmap
     * @param img Original bitmap
     * @param widthNew New width
     * @param heightNew New height
     * @return resized Bitmap
     */
    public static Bitmap resize(Bitmap img, int widthNew, int heightNew){
    	int widthOrig = img.getWidth();
        int heightOrig = img.getHeight();
        
        // TODO: keep aspect ratio
        // calculate the scale - in this case = 0.4f
        float scaleWidth = ((float) widthNew) / widthOrig;
        float scaleHeight = ((float) heightNew) / heightOrig;
       
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
      
 
        // recreate the new Bitmap
        return Bitmap.createBitmap(img, 0, 0,
                          widthOrig, heightOrig, matrix, true);
        
        

    }
    
    private static InputStream OpenHttpConnection(String urlString) 
    throws IOException
    {
        InputStream in = null;
        int response = -1;
               
        URL url = new URL(urlString); 
        URLConnection conn = url.openConnection();
                 
        if (!(conn instanceof HttpURLConnection))                     
            throw new IOException("Not an HTTP connection");
        
        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect(); 

            response = httpConn.getResponseCode();                 
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();                                 
            }                     
        }
        catch (Exception ex)
        {
            throw new IOException("Error connecting");            
        }
        return in;     
    }
}
