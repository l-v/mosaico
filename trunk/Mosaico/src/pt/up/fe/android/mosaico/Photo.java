package pt.up.fe.android.mosaico;

import android.graphics.Bitmap;



public class Photo {
	
	// delete all fields not needed later
	private int id;
	private String title;
	private String webUrl;
	private String fileUrl;
	
	private String thumbUrl; // To be filled so we can get the thumbnails directly
	
	private int longitude;
	private int latitude;
	
	private int width;
	private int height;
	
	private String uploadDate;
	
	private int ownerId;
	private String ownerName;
	private String ownerUrl;
	
	private int site; // 0 is Panoramio; 1 is Flicker;
	
	
	Photo() {
		
	}
	
	Photo (int imgId, String imgTitle, String imgWebUrl, String imgFileUrl, 
			int imgLong, int imgLat, int imgWidth, int imgHeight,
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
		
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getOwnerName() {
		return ownerName;
	}
	
	public String getCoord() {
		return "lat: " + latitude + "; long: " + longitude;
	}
	
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
		return ImgDownload.getImage(Globals.PANORAMIO_THUMB_URL + id + ".jpg");		
	}
}