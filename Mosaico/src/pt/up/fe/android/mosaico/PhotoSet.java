package pt.up.fe.android.mosaico;

import java.util.LinkedList;



public class PhotoSet {
	
	private LinkedList<Photo> photoList;
	
	
	PhotoSet() {
		
	}
	
	public void addPhoto(Photo newPhoto) {
		
		// TODO: put in list organized
		photoList.add(newPhoto);
	}
	
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