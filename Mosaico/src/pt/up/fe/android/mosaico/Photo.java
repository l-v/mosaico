package pt.up.fe.android.mosaico;





public class Photo {
	
	// delete all fields not needed later
	private int id;
	private String title;
	private String webUrl;
	private String fileUrl;
	
	private int longitude;
	private int latitude;
	
	private int width;
	private int height;
	
	private String uploadDate;
	
	private int ownerId;
	private String ownerName;
	private String ownerUrl;
	
	
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
	
}