# Photo #
_saves the info for a single photo._
```
public class Photo{

	private int id;
	private String title;
	private String webUrl;
	private String fileUrl;
	
	private float longitude;
	private float latitude;
	
	private int width;
	private int height;
	
	private String uploadDate;
	
	private int ownerId;
	private String ownerName;
	private String ownerUrl;

        private Bitmap photo; // The bitmap of the photo.



	Photo();
	
	Photo (int imgId, String imgTitle, String imgWebUrl, String imgFileUrl, 
		int imgLong, int imgLat, int imgWidth, int imgHeight,
		String imgUpload, int imgOwnerId, String imgOwnerName, 
                String imgOwnerUrl) ;

	public String getTitle();
	
	public String getOwnerName();
	
	public String getCoord();
	
	public String getFileUrl();

        public Bitmap getPhoto(); // Returns the photo // TODO Maybe in a default size?

        public Bitmap getThumb(); // Returns the thumbnail of the photo.


}
```

# PhotoSet #
_list of Photos_
```
public class PhotoSet{

        private LinkedList<Photo> photoList;
	
	
	PhotoSet();
	
	public void addPhoto(Photo newPhoto);
	
	public void addPhotoList(LinkedList<Photo> newList);
	
	// will get photo list results from panoramio and flickr api
	public void getPhotos();
	

}
```