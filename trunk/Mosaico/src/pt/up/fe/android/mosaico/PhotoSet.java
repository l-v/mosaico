package pt.up.fe.android.mosaico;

import java.util.ArrayList;
import java.util.LinkedList;

import android.util.Log;


/**
 * Stores a LinkedList of Photos and also the current location
 */
public class PhotoSet {
	
	private ArrayList<Photo> photoList;

	private double currentLatitude;
	private double currentLongitude;
	private double minLatitude;
	private double minLongitude;
	private double maxLatitude;
	private double maxLongitude;
	private double maxDistance;
	
	private GeoLocation currentLocation;
	
	/**
	 * Constructor
	 * @param currentLatitude current GPS position
	 * @param currentLongitude current GPS position
	 * @param minLatitude bottom left corner of the square area to show
	 * @param minLongitude bottom left corner of the square area to show
	 * @param maxLatitude top right corner of the square area to show
	 * @param maxLongitude top right corner of the square area to show
	 */
	public PhotoSet(double currentLatitude, double currentLongitude, double minLatitude, double minLongitude, double maxLatitude, double maxLongitude){
		this.currentLatitude = currentLatitude;
		this.currentLongitude = currentLongitude;
		this.minLatitude = minLatitude;
		this.minLongitude = minLongitude;
		this.maxLatitude = maxLatitude;
		this.maxLongitude = maxLongitude;
		
		this.currentLocation = GeoLocation.fromDegrees(currentLatitude, currentLongitude);
		
		photoList = new ArrayList<Photo>();
	}
	
	
	public PhotoSet(double currentLatitude, double currentLongitude, double maxDistance){
		photoList = new ArrayList<Photo>();
		
		this.currentLatitude = currentLatitude;
		this.currentLongitude = currentLongitude;
		this.maxDistance = maxDistance;
		
		currentLocation = GeoLocation.fromDegrees(currentLatitude, currentLongitude);
		
		GeoLocation[] boundingBox = currentLocation.boundingCoordinates(maxDistance, Globals.EARTH_RADIUS);
		
		this.minLatitude = boundingBox[0].getLatitudeInDegrees();
		this.minLongitude = boundingBox[0].getLongitudeInDegrees();
		
		this.maxLatitude = boundingBox[1].getLatitudeInDegrees();
		this.maxLongitude = boundingBox[1].getLongitudeInDegrees();
		
		
		Log.d("COORDINATES", "minLatitude: " + minLatitude + " , minLongitude: " + minLongitude +" , maxLatitude: " + maxLatitude +" , maxLongitude: " + maxLongitude);
	}
	
	
	
	/**
	 * Returns current latitude
	 * @return currentLatitude
	 */
	public double getCurrentLatitude(){
		return currentLatitude;
	}
	
	/**
	 * Returns current longitude
	 * @return currentLongitude
	 */
	public double getCurrentLongitude(){
		return currentLongitude;
	}
	
	/**
	 * Returns min latitude
	 * @return minLatitude
	 */
	public double getMinLatitude(){
		return minLatitude;
	}
	
	/**
	 * Returns min longitude
	 * @return minLongitude
	 */
	public double getMinLongitude(){
		return minLongitude;
	}
	
	/**
	 * Returns max latitude
	 * @return maxLatitude
	 */
	public double getMaxLatitude(){
		return maxLatitude;
	}
	
	/**
	 * Returns max longitude
	 * @return maxLongitude
	 */
	public double getMaxLongitude(){
		return maxLongitude;
	}
	
	public double getMaxDistance() {
		return maxDistance;
	}
	/**
	 * Adds a newPhoto to the photoList.
	 * @param newPhoto
	 */
	public void addPhoto(Photo newPhoto) {
		
		// TODO: put in list organized
		GeoLocation photoLocation = GeoLocation.fromDegrees(newPhoto.getLatitude(), newPhoto.getLongitude());
		
		newPhoto.setDistance( (float) photoLocation.distanceTo(currentLocation, Globals.EARTH_RADIUS));
		
		photoList.add(newPhoto);
		
		
	}
	
	/**
	 * Gets the Linked List of photos
	 * @return ArrayList<Photo>
	 */
	public ArrayList<Photo> getList(){
		return photoList;
	}
	
	/**
	 * @deprecated the correct way to add photos to the photoList is using the addPhoto method
	 */
	public void addPhotoList(LinkedList<Photo> newList) {
		
		for (int i=0; i!=newList.size(); i++) {
			addPhoto(newList.get(i));
		}
	}
	
	/**
	 * @deprecated used by Liliana for testing
	 */
	public void getPhotos() { // will get photo list results from panoramio and flikr api
		//PanoramioAPI panoramio = new PanoramioAPI();
		
		//addPhotoList(panoramio.getPictures(-180, -90, 180, 90));
	}
	
}