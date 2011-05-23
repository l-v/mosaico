package pt.up.fe.android.mosaico;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

import org.json.*;
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
	
	public PanoramioAPI() {
		defaultSize = "square";
		defaultNumber = 20; 
		defaultSet = "public";
	}


	// TODO: put code slightly more readable
	public LinkedList<Photo> jsonParser(String replyMsg) {

		LinkedList<Photo> photoList = new LinkedList<Photo>();

		try {

			JSONObject jsonMsg = new JSONObject(replyMsg);
			Log.v(log, "entered jsonParser");
			

			int count = Integer.parseInt(jsonMsg.getString("count"));
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
				Integer longitude = Math.round((Float.parseFloat(img.getString("longitude"))));
				Integer latitude = Math.round(Float.parseFloat(img.getString("latitude")));
				
				int width = Integer.parseInt(img.getString("width"));
				int height = Integer.parseInt(img.getString("height"));
				
				String uploadDate = img.getString("upload_date");
			
				int ownerId = Integer.parseInt(img.getString("owner_id"));
				String ownerName = img.getString("owner_name");
				String ownerUrl = img.getString("owner_url");

				Photo newPhoto = new Photo(id, title, webUrl, fileUrl, 
						longitude, latitude, width, height,
						uploadDate, ownerId, ownerName, ownerUrl);

				photoList.add(newPhoto);
			}

			Log.v(log, "left jsonParser cycle");
			return photoList;



		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}


	/**
	 * 
	 * @param minLong: minimum longitude
	 * @param minLat: minimum latitude
	 * @param maxLong: maximum longitude
	 * @param maxLat: maximun latitude
	 * @return
	 */
	public LinkedList<Photo> getPictures(int minLong, int minLat, int maxLong, int maxLat) {

		return getPictures(minLong, minLat, maxLong, maxLat, defaultSize, defaultNumber, defaultSet);

	}

	public LinkedList<Photo> getPictures(int minLong, int minLat, int maxLong, int maxLat, String size, int number, String set) {

		

		String method = "GET";
		String urlString = "http://www.panoramio.com/map/get_panoramas.php?" +
		"set=" + set + "&from=0&to=" + number +
		"&minx=" + minLong + "&miny=" + minLat + 
		"&maxx=" + maxLong + "&maxy=" + maxLat + 
		"&size=" + size + "&mapfilter=true";

		LinkedList<Photo> results = new LinkedList<Photo>();
		URL url;
		
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
			results = jsonParser(sb.toString());
			

			//return sb.toString();
			//return message;
		
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return results;
	}




}
