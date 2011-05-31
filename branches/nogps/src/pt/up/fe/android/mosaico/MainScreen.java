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

	private double currentLatitude = 9999;
	private double currentLongitude = 9999;
	
	private GridView gridview;

	ProgressDialog pd;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		gridview = (GridView) findViewById(R.id.gridview);
		
		// myPhotos = new PhotoSet(dummyLat, dummyLong, dummyLat, dummyLong,
		// maxLat, maxLong);
		// myPhotos = new PhotoSet(0, 0, 41.365968, -8.780026, 41.383263,
		// -8.764563);
		//this.currentLatitude = 42.0;
		//this.currentLongitude = -8.0;

		getLastGoodLocation();
		retrievePhotos();
	}
	
	public void retrievePhotos()
	{
		if (currentLatitude != 9999 && currentLongitude != 9999)
		{
		myPhotos = new PhotoSet(currentLatitude, currentLongitude, 3);
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
		}
	}
	/**
	 * Method invoked at startup. Getting last known location by the device
	 * 
	 */
	private void getLastGoodLocation() {
		try {
			LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			// create criteria for the provider to return
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			criteria.setAltitudeRequired(false);
			criteria.setBearingRequired(false);
			criteria.setCostAllowed(true);
			criteria.setPowerRequirement(Criteria.POWER_LOW);

			String provider = lm.getBestProvider(criteria, true); // get the provider
			Location location = lm.getLastKnownLocation(provider); // get the last known location
        
			// update the latitude and longitude
			this.currentLatitude = location.getLatitude();
			this.currentLongitude = location.getLongitude();
		}
		catch (Exception e)
		{
			// if something goes wrong in this method, try to get a fresh location
			getFreshLocation();
		}
	}
/**
 * retrieve a new location place
 */
	public void getFreshLocation()
	{
		// TODO: fix the progress dialog to actually show while new location is being retrieved
		pd = ProgressDialog.show(this, "Working... ", "Getting current location...");
		
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    LocationListener ll = new mylocationlistener(); // create the location listener
	    // get location updates from the current GPS Provider, every 1sec
	    // and notify if location changed within 100m
	    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 100.0f, ll);
	    
	    boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER); 
	    if(!isGPS)
	    {
	    	checkGPSsettings(); // show dialog for choosing to switch on GPS or not
	    }
	    //TODO: fix this progress dialog
	    pd.dismiss();
	}
	
	/**
	 * show alert to ask for gps settings 
	 */
	public void checkGPSsettings()
	{
		// create the alert that will ask the user to go back or change the GPS settings
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage("Enable your GPS or continue viewing old photos!")
    	.setCancelable(false).setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// this invokes the Location settings
				startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
			}
		})
		.setNegativeButton("Old Photos", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//MainScreen.this.finish(); // exit the application 
				dialog.dismiss(); // dismiss the alert 
			}
		});
    	
    	AlertDialog alert = builder.create(); // create the alert
    	alert.show();
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
				Intent intent = new Intent(this, GoogleMapsView.class); // create a new intent for the gmaps view
				startActivity(intent); // TODO: fix the error that gives when the activity is started (Map Activity)
				return true;
		case R.id.menu_pref:
			Toast.makeText(this, "Preferences here!", Toast.LENGTH_LONG).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();
	}
	/** location Listener for the GPS **/
	private class mylocationlistener implements LocationListener {
		
		/* overriding the default methods of the location listener that we need */
		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {
				currentLatitude = location.getLatitude();
				currentLongitude = location.getLongitude();
				
				// log the changes
				Log.d("LOCATION CHANGED", currentLatitude + "");
				Log.d("LOCATION CHANGED", currentLongitude + "");
				// show a toast with the changes - maybe to remove later
				Toast.makeText(MainScreen.this,
						currentLongitude + " " + currentLongitude,
						Toast.LENGTH_LONG).show();
	        }
			
			// refresh the photos in the grid
			 retrievePhotos();
	    }
	    @Override
	    public void onProviderDisabled(String provider) {
	    }
	    @Override
	    public void onProviderEnabled(String provider) {
	    }
	    @Override
	    public void onStatusChanged(String provider, int status, Bundle extras) {
	    }
	}

	
}