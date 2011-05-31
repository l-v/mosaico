package pt.up.fe.android.mosaico;

import android.graphics.Bitmap;


public class Photo {
	
	// delete all fields not needed later
	private int id;	
	private String title;
	private String webUrl;
	private String fileUrl;

	private String thumbUrl; // To be filled so we can get the thumbnails directly
	
	private double longitude;


	private double latitude;
	
	private int width;
	private int height;
	
	private String uploadDate;
	
	private int ownerId;
	private String ownerName;
	private String ownerUrl;
	/** Distance in km to the current position, to be set by the PhotoSet.addPhoto() method.
	 */
	private float distance;
	private int site; // 0 is Panoramio; 1 is Flicker;
	
	Bitmap thumbnail;
	/**
	 * Constructor
	 * 
	 * @param imgId
	 * @param imgTitle
	 * @param imgWebUrl
	 * @param imgFileUrl
	 * @param imgLong
	 * @param imgLat
	 * @param imgWidth
	 * @param imgHeight
	 * @param imgUpload
	 * @param imgOwnerId
	 * @param imgOwnerName
	 * @param imgOwnerUrl
	 */
	Photo (int imgId, String imgTitle, String imgWebUrl, String imgFileUrl, 
			double imgLong, double imgLat, int imgWidth, int imgHeight,
			String imgUpload, int imgOwnerId, String imgOwnerName, String imgOwnerUrl) {
		
		id = imgId;
		title = imgTitle;
		webUrl = imgWebUrl;
		fileUrl = imgFileUrl;
		
		longitude = imgLong;
		latitude = imgLat;
		
		width = imgWidth;
		height = imgHeight;
		
		uploadDate = imgUpload;
		
		ownerId = imgOwnerId;
		ownerName = imgOwnerName;
		ownerUrl = imgOwnerUrl;
		
		thumbnail = null;
		
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getOwnerName() {
		return ownerName;
	}
	
	/**
	 * Distance in km to the current position, to be set by the PhotoSet.addPhoto() method
	 */
	public float getDistance(){
		return distance;
	}
	
	/**
	 * Distance in km to the current position, to be set by the PhotoSet.addPhoto() method
	 */
	public void setDistance(float distance){
		this.distance = distance;
	}
	
	public String getCoord() {
		return "lat: " + latitude + "; long: " + longitude;
	}
	
	/**
	 * @deprecated not needed, use getPhoto or getThumb
	 *
	 */
	public String getFileUrl() {
		return fileUrl;
	}
	
	/**
	 * Fetches a photo directly from the fileUrl;
	 * @return Bitmap of the fetched photo
	 */
	public Bitmap getPhoto(){
		return ImgDownload.getImage(fileUrl);
	}
	
	/**
	 * Fetches the thumbail of a photo.
	 * If the photo is from panoramio the 60x60 thumbnail is in:
	 * http://mw2.google.com/mw-panoramio/photos/square/<id>.jpg
	 * For now I will assume all photos are from panoramio,
	 * I will change this when we add the flickr api.
	 * @return
	 */
	public Bitmap getThumb(){
		if (thumbnail == null)
			thumbnail = ImgDownload.getImage(Globals.PANORAMIO_THUMB_URL + id + ".jpg");
		
		return thumbnail;
	}
	
	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}
}