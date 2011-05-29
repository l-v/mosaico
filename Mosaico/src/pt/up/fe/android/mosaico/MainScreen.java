package pt.up.fe.android.mosaico;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;


public class MainScreen extends Activity {
	/** Called when the activity is first created. */
	PhotoSet myPhotos;
	PanoramioAPI processPhotos;
	private GridView gridview;
	private ProgressDialog pd;
	
	private Location currentLocation;
	LocationManager lm;
	LocationListener ll;
	int gps = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		gridview = (GridView) findViewById(R.id.gridview);

	}
	
	@Override
	public void onStart() {
		super.onStart();
		/*  instantiate some stuff for the location */
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		ll = new MyLocationListener();  // create the location listener
		currentLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (currentLocation == null)
		{
			getFreshLocation();
		}
		else { 
			retrievePhotos();  // if there is last known location
		}
	}
	
	@Override	
    public void onResume() { 
        super.onResume();

	}
	@Override
	protected void onPause()
	{
		super.onPause();
		//lm.removeUpdates(ll);
		
	}
	/**
	 * retrieve a new location place
	 */
	public void getFreshLocation()
	{
	    // get location updates from the current GPS Provider, every 1sec
	    // and notify if location changed within 100m
	    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 100.0f, ll);
	    
	    // Create a ProgressDialog to let the User know that we're waiting for a GPS Fix
	    Runnable showWaitDialog = new Runnable () { 
	    	@Override
	    	public void run() {
	    		while ( currentLocation == null) // wait for the GPS fix
	    		{}
	    		// After receiving first GPS Fix dismiss the Progress Dialog
	    		pd.dismiss();
	    		retrievePhotos();
	    	};
	    };
	    pd = ProgressDialog.show(this, "Working... ", "Getting current location...", true);
	    Thread t = new Thread(showWaitDialog); // start the thread
	    t.start(); 
	}
		
	public void retrievePhotos()
	{
		if (currentLocation != null)
		{
			myPhotos = new PhotoSet(currentLocation.getLatitude(), currentLocation.getLongitude(), 3);
			processPhotos = new PanoramioAPI(myPhotos);
			gridview.setAdapter(new ImageAdapter(this, myPhotos));
			gridview.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
				    ImageAdapter ad = (ImageAdapter) arg0.getAdapter();
				    popPic dialog = new popPic(arg1.getContext(), ad.getItem(arg2));
					dialog.show();
				}
			});
		}
		else 
		{
			Toast.makeText(this, "problem with loading grid", Toast.LENGTH_LONG).show();
			getFreshLocation();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return (super.onPrepareOptionsMenu(menu));
	}

	/**
	 * The "menu" button is pressed this handler is invoked
	 * also the sub-menus are here
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_path:
			Toast.makeText(this, "Path here!", Toast.LENGTH_LONG).show();
			return true;
		case R.id.menu_fav:
			Toast.makeText(this, "Favorites Here!", Toast.LENGTH_LONG).show();
			return true;
		case R.id.menu_history:
			Toast.makeText(this, "History here!", Toast.LENGTH_LONG).show();
			return true;
		case R.id.menu_gps:
			return true;
			case R.id.gps_current:
				getFreshLocation(); // get a new location from the gps device
				return true;
			case R.id.gps_another:
				Intent intent = new Intent(); // create a new intent for the gmaps view
				intent.setClass(this, GoogleMapsView.class);
				startActivity(intent); // TODO: fix the error that gives when the activity is started (Map Activity)
				return true;
		case R.id.menu_pref:
			Toast.makeText(this, "Preferences here!", Toast.LENGTH_LONG).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/** location Listener for the GPS **/
	private class MyLocationListener implements LocationListener {
		
		/* when the location is changed update the photos */
		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {
				currentLocation = location;
				retrievePhotos();
				lm.removeUpdates(this);
				// log the changes
				Log.d("LOCATION CHANGED", location.getLatitude() + "");
				Log.d("LOCATION CHANGED", location.getLongitude() + "");
				// show a toast with the changes - maybe to remove later
				Toast.makeText(MainScreen.this,
						location.getLatitude() + " " + location.getLongitude(),
						Toast.LENGTH_LONG).show();
	        }
	    }
		// when the provider is disabled - ask the user to turn on the GPS
	    @Override
	    public void onProviderDisabled(String provider) {
	    	// create the alert that will ask the user to go back or change the GPS settings
			AlertDialog.Builder builder = new AlertDialog.Builder(MainScreen.this);
	    	builder.setMessage(R.string.gps_disabled_question)
	    	.setCancelable(false).setPositiveButton(R.string.gps_disabled_answer_yes, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// this invokes the Location settings
					startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
				}
			})
			.setNegativeButton(R.string.gps_disabled_answer_no, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					MainScreen.this.finish(); // exit the application 
					//dialog.dismiss(); // dismiss the alert 
				}
			});
	    	
	    	AlertDialog alert = builder.create(); // create the alert
	    	alert.show(); // show the dialog asking for turning on the GPS
	    }
	    @Override
	    public void onProviderEnabled(String provider) {
	    }
	    @Override
	    public void onStatusChanged(String provider, int status, Bundle extras) {
	    }
	}
}