package pt.up.fe.android.mosaico;

import java.io.*;
import java.net.*;
import org.json.*;

import pt.up.fe.android.mosaico.Exceptions.NoPhotosFoundExcepton;
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
	
	public PanoramioAPI(PhotoSet photoList) throws NoPhotosFoundExcepton {
		defaultSize = "medium";
		defaultNumber = Globals.NUMBER_PHOTOS_TO_GET; 
		defaultSet = "full";
		this.photoList = photoList;
		
		getPictures(photoList.getMinLatitude(), photoList.getMinLongitude(), photoList.getMaxLatitude(), photoList.getMaxLongitude());
		
	}
	
	public PanoramioAPI(PhotoSet photoList, int numberPhotos) throws NoPhotosFoundExcepton {
		defaultSize = "square";
		defaultNumber = numberPhotos; 
		defaultSet = "public";
		this.photoList = photoList;
		
		getPictures(photoList.getMinLatitude(), photoList.getMinLongitude(), photoList.getMaxLatitude(), photoList.getMaxLongitude());
		
	}


	// TODO: put code slightly more readable
	public void jsonParser(String replyMsg) throws NoPhotosFoundExcepton {


		try {

			JSONObject jsonMsg = new JSONObject(replyMsg);
			Log.v(log, "entered jsonParser");
			

			int count = Integer.parseInt(jsonMsg.getString("count"));
			
			if (count == 0)
			{
				throw new NoPhotosFoundExcepton(); 
			}
			JSONArray jsonArray = jsonMsg.getJSONArray("photos");

			Log.v(log, "found " + count + " photos");
			Log.v(log, "size of array stored: " + jsonArray.length());
			Log.v(log, "photos: " + jsonMsg.getString("photos"));
			
			// build picture TODO: define array limit?
			for (int i=0; i!=jsonArray.length(); i++) {
				
				JSONObject img = jsonArray.getJSONObject(i);
				
				
				int id = Integer.parseInt(img.getString("photo_id"));
				
				String title = img.getString("photo_title");
				String webUrl = img.getString("photo_url");
				String fileUrl = img.getString("photo_file_url");


				//TODO: verify if float coordinates are needed
				double longitude = (Double.parseDouble(img.getString("longitude")));
				double latitude = Double.parseDouble(img.getString("latitude"));
				
				int width = (int) Double.parseDouble(img.getString("width"));
				int height = (int) Double.parseDouble(img.getString("height"));
				
				String uploadDate = img.getString("upload_date");
			
				int ownerId = Integer.parseInt(img.getString("owner_id"));
				String ownerName = img.getString("owner_name");
				String ownerUrl = img.getString("owner_url");

				Photo newPhoto = new Photo(id, title, webUrl, fileUrl, 
						longitude, latitude, width, height,
						uploadDate, ownerId, ownerName, ownerUrl);

				photoList.addPhoto(newPhoto);
			}

			Log.v(log, "left jsonParser cycle");
	



		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * 
	 * @param minLong: minimum longitude
	 * @param minLat: minimum latitude
	 * @param maxLong: maximum longitude
	 * @param maxLat: maximun latitude
	 * @return
	 * @throws NoPhotosFoundExcepton 
	 */
	public void getPictures(double minLat, double minLong, double maxLat, double maxLong) throws NoPhotosFoundExcepton {

		this.getPictures(minLat, minLong, maxLat, maxLong, defaultSize, defaultNumber, defaultSet);

	}

	public void getPictures(double minLat, double minLong, double maxLat, double maxLong, String size, int number, String set) throws NoPhotosFoundExcepton {

		

		String method = "GET";
		String urlString = "http://www.panoramio.com/map/get_panoramas.php?" +
		"set=" + set + "&from=0&to=" + number +
		"&minx=" + minLong + "&miny=" + minLat + 
		"&maxx=" + maxLong + "&maxy=" + maxLat + 
		"&size=" + size + "&mapfilter=true";

		URL url;

		Log.d("PanoramioAPI", "URL sent: "+ urlString);
		
		try {
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



			// parse the results
			jsonParser(sb.toString());
			

			//return sb.toString();
			//return message;
		
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}




}
