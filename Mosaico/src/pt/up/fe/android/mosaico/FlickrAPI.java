package pt.up.fe.android.mosaico;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import pt.up.fe.android.mosaico.Exceptions.NoPhotosFoundException;

public class FlickrAPI {
	/*
	 * http://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=4a0e553ddd58e3428eaef8f7a7edc899&min_taken_date=20100101&has_geo=1&lat=41.383263&lon=-8.780026&radius=10&extras=geo%2C+url_m%2C+url_sq&format=rest&api_sig=048215caf95438f902ee8915002b809c
	 */
	private int defaultNumber; // number of images: <=100
	private String log = "FlickrLog";
	private PhotoSet photoList; 

	public FlickrAPI(PhotoSet photoList) throws NoPhotosFoundException {
		defaultNumber = ((int) Math.floor(Globals.photos_number_to_get / 2.0)); 
		this.photoList = photoList;
		
		getPictures(photoList.getCurrentLatitude(), photoList.getCurrentLongitude(), photoList.getMaxDistance());
		
	}
	
	public FlickrAPI(PhotoSet photoList, int numberPhotos) throws NoPhotosFoundException {
		defaultNumber = numberPhotos; 
		this.photoList = photoList;
		
		getPictures(photoList.getCurrentLatitude(), photoList.getCurrentLongitude(), photoList.getMaxDistance());
		
	}
	
	public void jsonParser(String replyMsg) throws NoPhotosFoundException {


		try {
			JSONObject jsonPreMsg = new JSONObject(replyMsg);
			JSONObject jsonMsg = jsonPreMsg.getJSONObject("photos");
			Log.v(log, "entered jsonParser");

			int count = Integer.parseInt(jsonMsg.getString("total"));
			
			if (count == 0)
			{
				Log.d("FickrAPI","No Photos Found Exception");
				throw new NoPhotosFoundException();
				
			}
			JSONArray jsonArray = jsonMsg.getJSONArray("photo");

			Log.v(log, "found " + count + " photos");
			Log.v(log, "size of array stored: " + jsonArray.length());
			Log.v(log, "photos: " + jsonMsg.getString("photo"));
			
			// build picture TODO: define array limit?
			for (int i=0; i<jsonArray.length(); i++) {
				
				JSONObject img = jsonArray.getJSONObject(i);
				
				
				long id = Long.parseLong(img.getString("id"));

				String ownerId = img.getString("owner");
				String ownerUrl = "http://www.flickr.com/photos/" + ownerId;
				
				String title = img.getString("title");
				String webUrl = "http://www.flickr.com/photos/" + ownerId + "/"
				+ id;
				String fileUrl = img.getString("url_m");
				String thumbUrl = img.getString("url_sq");
				String ownerName = img.getString("ownername");
				
				//Log.v(log, "THUMB: " + thumbUrl );
				
				//TODO: verify if float coordinates are needed
				double longitude = (Double.parseDouble(img.getString("longitude")));
				double latitude = Double.parseDouble(img.getString("latitude"));
				
				int width = (int) Double.parseDouble(img.getString("width_m"));
				int height = (int) Double.parseDouble(img.getString("height_m"));
				
				String uploadDate = img.getString("dateupload");

				//System.out.println("thumb: " + thumbUrl);
				/*Photo newPhoto = new Photo(id, title, webUrl, fileUrl, 
						longitude, latitude, width, height,
						uploadDate, ownerId, ownerUrl, thumbUrl);*/
				
				Photo newPhoto = new Photo(id, title, webUrl, fileUrl, thumbUrl, 
						longitude, latitude, width, height,
						uploadDate, ownerId, ownerName, ownerUrl, 1, "Flickr");

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
	 * @throws NoPhotosFoundException 
	 * @throws NoPhotosFoundExcepton 
	 */
	public void getPictures(double curLat, double curLong, double maxDist) throws NoPhotosFoundException {

		this.getPictures(curLat, curLong, maxDist, defaultNumber);

	}

	public void getPictures(double curLat, double curLong, double maxDist, int number) throws NoPhotosFoundException {

		

		String method = "GET";
		DateFormat curYear = new SimpleDateFormat("yyyy");
		DateFormat curMonth = new SimpleDateFormat("MM");
		DateFormat curDay = new SimpleDateFormat("dd");
		Date date = new Date();
		
		StringBuilder sbdate = new StringBuilder();
		Integer year = Integer.parseInt(curYear.format(date));
		sbdate.append(year-1);
		sbdate.append(curMonth.format(date));
		sbdate.append(curDay.format(date));
				
		String urlString = "http://api.flickr.com/services/rest/?method=flickr.photos.search"
			+ "&api_key=" + Globals.FLICKR_KEY
			+  "&min_taken_date=" + sbdate
			+  "&has_geo=1"
			+  "&per_page=" + number
			+  "&lat=" + curLat
			+  "&lon=" + curLong
			
			/* To get only outdoor photos 
			 * NOTE: doesn't work, it seems flickr has not implemented it
			 */
			//+  "&geo_context=2" 
			
			/* to get more relevant photos first 
			 * and to make them not be all from the same guy
			 */
			+  "&sort=interestingness-desc" 
			+  "&radius=" + maxDist
			+  "&extras=geo,url_m,url_sq,date_upload,owner_name"
			+  "&format=json&nojsoncallback=1";	
		
		URL url;

		System.out.println("URL sent: "+ urlString);
		
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
