package pt.up.fe.android.mosaico;

import java.util.List;
import de.android1.overlaymanager.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IInterface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewDebug.IntToString;
import android.widget.AnalogClock;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class GoogleMapsView extends MapActivity {
	static final String TAG = "MapActivity";
	
	private MapView mapView;
	private MapController mapController;
	private GeoPoint point;
	private GeoPoint touchedPoint;
	OverlayManager overlayManager;
	
	AlertDialog.Builder builder;
	AlertDialog alert;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.gmaps); 
	    
	    mapView = (MapView) findViewById(R.id.mapview);
	    //MapView mapView = new MapView(this, "0cYXzPpWBPrvWY7kbfaQLfPd1drfAVJLdI0dYoA");
	    mapView.setBuiltInZoomControls(true);
	    mapController = mapView.getController();
	  
	   
	    // get the passed variables from the main activity
	    Bundle b = getIntent().getExtras();
    	double latitude = b.getDouble("LATITUDE");
    	double longitude = b.getDouble("LONGITUDE");
    	
    	// convert the long and lat into a geoPoint
    	point = new GeoPoint( (int) (latitude * 1E6), (int) (longitude * 1E6));
    	
	    mapController.animateTo(point);
	    mapController.setZoom(13);
	    
	    //Add a location marker
        MapOverlay mapOverlay = new MapOverlay();
        List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mapOverlay);        
        mapView.invalidate();
        
        overlayManager = new OverlayManager(getApplication(), mapView);
        createOverlayWithListener();
	}
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void createOverlayWithListener() {
        //Create the overlay with the pushpin
        final ManagedOverlay managedOverlay = overlayManager.createOverlay("listenerOverlay", getResources().getDrawable(R.drawable.pushpin));
        
        // the way we create the gesture listener
        managedOverlay.setOnOverlayGestureListener(new ManagedOverlayGestureDetector.OnOverlayGestureListener() {
        	
            public boolean onZoom(ZoomEvent zoom, ManagedOverlay overlay) {
                return false;
            }
 
            public boolean onDoubleTap(MotionEvent e, ManagedOverlay overlay, GeoPoint point, ManagedOverlayItem item) {
                Drawable defaultmarker = getResources().getDrawable(R.drawable.pushpin);     

                ManagedOverlay managedOverlay = overlayManager.createOverlay(defaultmarker);

                //creating some marker:
                managedOverlay.createItem(point);

                //registers the ManagedOverlayer to the MapView
                overlayManager.populate();
                Toast.makeText(getApplicationContext(), "You created a Marker!", Toast.LENGTH_LONG).show();

                return true;
            }

            public void onLongPress(MotionEvent event, ManagedOverlay arg1) {
                if (event.getPointerCount() > 1) return;
                
                touchedPoint = mapView.getProjection().fromPixels((int) event.getX(), (int) event.getY());
//	                    Toast.makeText(getBaseContext(), touchedPoint.getLatitudeE6() / 1E6 + "," + 
//	                    		touchedPoint.getLongitudeE6() /1E6 , Toast.LENGTH_SHORT).show();
                getAlertDialog().show(); 
	        	    	
            }

            public boolean onScrolled(MotionEvent arg0, MotionEvent arg1,
                    float arg2, float arg3, ManagedOverlay arg4) {
                // TODO Auto-generated method stub
                return false;
            }


            public boolean onSingleTap(MotionEvent arg0, ManagedOverlay arg1,
                    GeoPoint arg2, ManagedOverlayItem arg3) {
                // TODO Auto-generated method stub
                return false;
            }
            
            public void onLongPressFinished(MotionEvent event,
                    ManagedOverlay arg1, GeoPoint arg2, ManagedOverlayItem arg3) {
            	
            }           
        });
        overlayManager.populate();
	}
	
	/**
	 * Get the dialog for returning back to mainscreen to show the photos 
	 * @return
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
	
	// first overlay with 
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
}