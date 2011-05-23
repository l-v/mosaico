package pt.up.fe.android.mosaico;

import java.util.LinkedList;



public class PhotoSet {
	
	public LinkedList<Photo> photoList;
	
	
	PhotoSet() {
		
	}
	
	public void addPhoto(Photo newPhoto) {
		
		// TODO: put in list organized
		photoList.add(newPhoto);
	}
	
	/**
	 * @deprecated the correct way to add photos to the photoList is using the addPhoto method
	 */
	public void addPhotoList(LinkedList<Photo> newList) {
		
		for (int i=0; i!=newList.size(); i++) {
			addPhoto(newList.get(i));
		}
	}
	
	// will get photo list results from panoramio and flikr api
	public void getPhotos() {
		//PanoramioAPI panoramio = new PanoramioAPI();
		
		//addPhotoList(panoramio.getPictures(-180, -90, 180, 90));
	}
	
}