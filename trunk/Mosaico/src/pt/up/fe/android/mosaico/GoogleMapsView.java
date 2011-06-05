package pt.up.fe.android.mosaico;

import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class GoogleMapsView extends MapActivity implements OnGestureListener, OnDoubleTapListener{
	static final String TAG = "MapActivity";
	
	private MapView mapView;
	private MapController mapController;
	private GeoPoint point;
	private GeoPoint touchedPoint;
	
	AlertDialog.Builder builder;
	AlertDialog alert;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.gmaps); 
	    
	    mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    mapController = mapView.getController();
	  
	   
	    // get the passed variables from the main activity
	    Bundle b = getIntent().getExtras();
    	double latitude = b.getDouble("LATITUDE");
    	double longitude = b.getDouble("LONGITUDE");
    	
    	// convert the longitude and latitude into a geoPoint
    	point = new GeoPoint( (int) (latitude * 1E6), (int) (longitude * 1E6));
    	
	    mapController.animateTo(point);		// animate to the current location
	    mapController.setZoom(13);			// zoom to the current location
	    
	    //Add a location marker
        MapOverlay mapOverlay = new MapOverlay();
        List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mapOverlay);        
        mapView.invalidate();
        
	}
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * Inflate the Main menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.maps_menu, menu);
		return (super.onPrepareOptionsMenu(menu));
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.layouts:   
			return true;
		case R.id.layout_map:
			mapView.setSatellite(false);
			return true;
		case R.id.layout_satellite:
			mapView.setSatellite(true);
			return true;
		case R.id.zoom_to_current:
			mapController.animateTo(point);		// animate to the current location
		    mapController.setZoom(15);			// zoom to the current location
			return true;
		
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Get the dialog for returning back to main screen to show the photos 
	 * @return AlertDialog
	 */
	public AlertDialog getAlertDialog(){
		if (alert != null) {
			return alert;
		}
	
		builder = new AlertDialog.Builder(GoogleMapsView.this);
    	builder.setMessage(R.string.maps_question)
    	.setCancelable(false).setPositiveButton(R.string.maps_answer_yes, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// this will return back to the main activity with new data
				Bundle b = new Bundle();
				b.putDouble("LATITUDE", touchedPoint.getLatitudeE6() / 1E6);
				b.putDouble("LONGITUDE", touchedPoint.getLongitudeE6() / 1E6);
				Intent resultIntent = new Intent();
				resultIntent.putExtras(b);
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
			}
		})
		.setNegativeButton(R.string.maps_answer_no, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss(); // dismiss the alert 
			}
		});
    	
		return builder.create();
	}
	
	// first overlay with current position
	 class MapOverlay extends com.google.android.maps.Overlay 
	    {
	        @Override
	        public boolean draw(Canvas canvas, MapView mapView, 
	        boolean shadow, long when) 
	        {
	            super.draw(canvas, mapView, shadow);                   
	 
	            //translate the GeoPoint to screen pixels
	            Point screenPts = new Point();
	            mapView.getProjection().toPixels(point, screenPts);
	 
	            //add the marker
	            Bitmap bmp = BitmapFactory.decodeResource(
	                getResources(), R.drawable.pushpin);            
	            canvas.drawBitmap(bmp, screenPts.x, screenPts.y-50, null);         
	            return true;
	        }
	    }

	@Override
	public boolean onDoubleTap(MotionEvent event) {
		// get current point and show the dialog
		touchedPoint = mapView.getProjection().fromPixels((int) event.getX(), (int) event.getY());
        getAlertDialog().show();
		return true;
	}
	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}
