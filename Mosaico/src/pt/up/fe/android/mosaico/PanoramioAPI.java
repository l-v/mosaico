package pt.up.fe.android.mosaico;

import java.io.*;
import java.net.*;
import org.json.*;

import pt.up.fe.android.mosaico.Exceptions.NoPhotosFoundException;
/*
import org.json.*;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
 */

import android.util.Log;


public class PanoramioAPI {

	private String defaultSize; // size of the image: original, medium, small, thumbnail, square, mini_square
	private int defaultNumber; // number of images: <=100
	private String defaultSet; // public (popular photos), full (all photos), userIDnumber
	private String log = "panoramioLog";
	private PhotoSet photoList;
	
   
   /**
	 * PanoramioAPI Constructor
	 * 
	 * @param photoList
	 * @throws NoPhotosFoundException
	 */
	public PanoramioAPI(PhotoSet photoList) throws NoPhotosFoundException {
		defaultSize = "medium";
		if ((Globals.photos_number_to_get % 2) != 0) {
			defaultNumber = ((int) (Globals.photos_number_to_get / 2) + 1); 
		}
		else {
			defaultNumber = ((int) Math.floor(Globals.photos_number_to_get / 2.0));
		}
		 
		defaultSet = "full";
		this.photoList = photoList;
		
		getPictures(photoList.getMinLatitude(), photoList.getMinLongitude(), photoList.getMaxLatitude(), photoList.getMaxLongitude());
		
	}
	
    /**
	 * PanoramioAPI Constructor
	 * 
	 * @param photoList
	 * @throws NoPhotosFoundException
	 */
	public PanoramioAPI(PhotoSet photoList, int numberPhotos) throws NoPhotosFoundException {
		defaultSize = "square";
		defaultNumber = numberPhotos; 
		defaultSet = "public";
		this.photoList = photoList;
		
		getPictures(photoList.getMinLatitude(), photoList.getMinLongitude(), photoList.getMaxLatitude(), photoList.getMaxLongitude());
		
	}


	  /**
	 * Parser of json message returned by Panoramio
	 * 
	 * @param replyMsg
	 * @throws NoPhotosFoundException
	 */
	public void jsonParser(String replyMsg) throws NoPhotosFoundException {


		try {

      // Retrieves picture count and photo array 
			JSONObject jsonMsg = new JSONObject(replyMsg);
			Log.v(log, "entered jsonParser");
			
			int count = Integer.parseInt(jsonMsg.getString("count"));
			if (count == 0) {
				throw new NoPhotosFoundException(); 
			}
         
			JSONArray jsonArray = jsonMsg.getJSONArray("photos");



			Log.v(log, "found " + count + " photos");
			Log.v(log, "size of array stored: " + jsonArray.length());
			Log.v(log, "photos: " + jsonMsg.getString("photos"));
			
         
         
         
			// build picture TODO: define array limit?
      // Processes data from pictures array 
			for (int i=0; i!=jsonArray.length(); i++) {
				
				JSONObject img = jsonArray.getJSONObject(i);
				
				
				int id = Integer.parseInt(img.getString("photo_id"));
				
				String title = img.getString("photo_title");
				String webUrl = img.getString("photo_url");
				String fileUrl = img.getString("photo_file_url");



				double longitude = (Double.parseDouble(img.getString("longitude")));
				double latitude = Double.parseDouble(img.getString("latitude"));
				
				int width = (int) Double.parseDouble(img.getString("width"));
				int height = (int) Double.parseDouble(img.getString("height"));
				
				String uploadDate = img.getString("upload_date");
			
				String ownerId = img.getString("owner_id");
				String ownerName = img.getString("owner_name");
				String ownerUrl = img.getString("owner_url");


        // constructs photo object 
				String thumbUrl = Globals.PANORAMIO_THUMB_URL + id + ".jpg";
				Photo newPhoto = new Photo(id, title, webUrl, fileUrl, 
						thumbUrl, longitude, latitude, width, height,
						uploadDate, ownerId, ownerName, ownerUrl, 0, "Panoramio");
 
        // adds photo to list 
				photoList.addPhoto(newPhoto);
			}
         
			Log.v(log, "left jsonParser cycle");


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
   * Gets pictures for a given location (uses default settings)
	 * 
	 * @param minLong: minimum longitude
	 * @param minLat: minimum latitude
	 * @param maxLong: maximum longitude
	 * @param maxLat: maximun latitude
	 * @return
	 * @throws NoPhotosFoundException 
	 */
	public void getPictures(double minLat, double minLong, double maxLat, double maxLong) throws NoPhotosFoundException {

		this.getPictures(minLat, minLong, maxLat, maxLong, defaultSize, defaultNumber, defaultSet);

	}


	/**
	 * Gets pictures for a location 
	 * 
	 * @param minLong: minimum longitude
	 * @param minLat: minimum latitude
	 * @param maxLong: maximum longitude
	 * @param maxLat: maximun latitude
	 * @return
	 * @throws NoPhotosFoundException 
	 */
	public void getPictures(double minLat, double minLong, double maxLat, double maxLong, String size, int number, String set) throws NoPhotosFoundException {

	 
    // Constructs Panoramio url
		String method = "GET";
		String urlString = "http://www.panoramio.com/map/get_panoramas.php?" +
		"set=" + set + "&from=0&to=" + number +
		"&minx=" + minLong + "&miny=" + minLat + 
		"&maxx=" + maxLong + "&maxy=" + maxLat + 
		"&size=" + size + "&mapfilter=true";

		URL url;
		Log.d("PanoramioAPI", "URL sent: "+ urlString);
		
		try {
      
      // Establishes connection to Panoramio (GET)
			url = new URL(urlString);

			HttpURLConnection connection;

			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(method);


			if (connection.getResponseCode() != 200) {
				throw new IOException(connection.getResponseMessage());
			}
         
         

			// Buffer the result into a string
			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();

			connection.disconnect();



			// parse the results (json format)
			jsonParser(sb.toString());
			
		
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}




}
