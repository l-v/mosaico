package pt.up.fe.android.mosaico;

import java.net.UnknownHostException; 

import pt.up.fe.android.mosaico.MyLocationHelper.LocationResult;

import pt.up.fe.android.mosaico.Exceptions.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
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
	private static final String TAG = "MainScreen";
	 
	/* Fields accessed in the class */
	private PhotoSet myPhotos;
	private GridView gridview;
	
	private Location currentLocation; 
	private MyLocationHelper myLocationHelper = new MyLocationHelper();
	
	public static final String PREFS_NAME = "MosaicoPrefs"; //Preferences file
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
	    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    Globals.identicaUsername = settings.getString("iden_username", "");
	    Globals.identicaPassword = settings.getString("iden_pass", "");
	    
	    // inflate the main grid
		gridview = (GridView) findViewById(R.id.gridview);
		
		
		 // if internet connection is present at startup - check for location
		if (hasInternetConnection()){
			getLocation(); // call to get location
		}
	}
	
	
	/** this is called when the screen rotates.
	 * (onCreate is no longer called when screen rotates due to manifest, see: android:configChanges)
	 * 
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
	    super.onConfigurationChanged(newConfig);
	    setContentView(R.layout.main);
	    gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(new ImageAdapter(MainScreen.this, myPhotos));
	    gridview.setOnItemClickListener(new OnItemClickListener() { 
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			    ImageAdapter ad = (ImageAdapter) arg0.getAdapter();
			    popPic dialog = new popPic(arg1.getContext(), ad.getItem(arg2));
				dialog.show();
			}
		});
	}	
	
	
	/*
	 * this gets the location returned in locationResult from LocationResult class
	 * implemented here in the Main Activity. 
	 */
	 public void getLocation()
	 {	
		 try {
			myLocationHelper.getLocation(this, locationResult);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(MainScreen.this, e.getMessage(), Toast.LENGTH_LONG).show();
			Log.d(TAG,"getLocation(): Problem occured while looking for a new location");
			
			//show dialog for changing Android Location Settings
			showSettingsDialog(); 	
		}
	 }
	 
	/*
	 *  This is what to do when we got a location.
	 *  Here is were we retrieve the photos.
	 *  implementation of the abstract class
	 */
	private LocationResult locationResult = new LocationResult() {
	    @Override
	    public void gotLocation(final Location location) {
	        //Got the location!
	    	if(location!= null){
	    		currentLocation = location;
	    		retrievePhotos();
	    	}
	    };
	};
	/**
	 * Check if Internet connection is available and is connected
	 * @return boolean
	 */
	 private boolean hasInternetConnection() {
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    // test for connection
	    if (cm.getActiveNetworkInfo() != null
	            && cm.getActiveNetworkInfo().isAvailable()
	            && cm.getActiveNetworkInfo().isConnected()) {
	    	return true;
	    }
	    else
	    {
	    	Toast.makeText(this	, R.string.internet_no_connection, Toast.LENGTH_LONG).show();
	        Log.v(TAG, "Internet Connection Not Present");
	        return false;
	    }
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override	
    public void onResume() { 
        super.onResume();

	}
	@Override
	protected void onPause(){
		super.onPause();
	}
	
	// TODO Here it may be made a bit simpler with AsyncTask 
	public void retrievePhotos()
	{
		try {
		final ProgressDialog pd = ProgressDialog.show(this, "Mosaico","Loading Photos...", true);
		
		if (currentLocation != null)
		{
			Handler handler = new Handler();
			Thread t = new Thread() { 
				public void run() {
					try {	
						myPhotos = new PhotoSet(currentLocation.getLatitude(), currentLocation.getLongitude(), 3);
						new PanoramioAPI(myPhotos);
						gridview.setAdapter(new ImageAdapter(MainScreen.this, myPhotos));
						gridview.setOnItemClickListener(new OnItemClickListener() { 
							
							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							    ImageAdapter ad = (ImageAdapter) arg0.getAdapter();
							    popPic dialog = new popPic(arg1.getContext(), ad.getItem(arg2));
								dialog.show();
							}
						});
						pd.dismiss(); // dismiss the loading photos screen
						} catch (NoPhotosFoundExcepton e)
						{
							pd.dismiss(); // dismiss the loading photos screen
							Log.d(TAG, e.getMessage());
							Log.v(TAG, "Coldn't retrieve photos");
							// cannot put toast here - this method is called from another thread
						}
				};
			}; // end of thread 
			handler.post(t); // start the thread
		}
		else 
		{
			pd.dismiss(); // dismiss the progress dialog "loading photos"
			Toast.makeText(this, R.string.photos_not_loaded, Toast.LENGTH_LONG).show();
		}
		} catch (Exception e)
		{
			Toast.makeText(this, "Couldn't show progress dialog", Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Inflate the Main menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return (super.onPrepareOptionsMenu(menu));
	}
	
	/**
	 * Data handled when coming back from activities started from this Activity
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode) { 
			// when coming back from Google Maps
	    	case (Globals.ACTIVITY_GOOGLE_MAPS) : { 
	    		if (resultCode == Activity.RESULT_OK)
	    		{
	    			Bundle b = data.getExtras();
	    			currentLocation.setLatitude(b.getDouble("LATITUDE"));
	    			currentLocation.setLongitude(b.getDouble("LONGITUDE"));
	    			retrievePhotos();
	    		}
	    		break;
	    	}
	    	// when coming back from Location Settings
	    	case (Globals.ACTIVITY_LOCATION_SETTINGS) : {
	    		getLocation();	// refresh the location
	    	}
	    }
	}

	/**
	 * Invoked when Menu Item is selected
	 * also the sub-menu items are here
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_refresh:   
			retrievePhotos(); // refresh the photos
			return true;
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
				getLocation();
				return true;
			case R.id.gps_another:
				// create a new intent for the Google maps view
				// and pass the current latitude and longitude in a Bundle
				Intent intent = new Intent(MainScreen.this, GoogleMapsView.class);
				Bundle b = new Bundle();
				b.putDouble("LATITUDE", currentLocation.getLatitude());
				b.putDouble("LONGITUDE", currentLocation.getLongitude());
				intent.putExtras(b);
				MainScreen.this.startActivityForResult(intent, Globals.ACTIVITY_GOOGLE_MAPS);
				return true;
		case R.id.menu_pref:
			Intent pref = new Intent(this, Settings.class);
			startActivity(pref);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Helping method for showing the Settings Dialog
	 */
	private void showSettingsDialog()
	{
		// create the alert that will ask the user to go to Location Settings
        AlertDialog.Builder builder = new AlertDialog.Builder(MainScreen.this);
		builder.setMessage(R.string.gps_disabled_question).setTitle(R.string.app_name)
		.setCancelable(false).setPositiveButton(R.string.gps_disabled_answer_yes, new DialogInterface.OnClickListener() {
		                
			@Override
			public void onClick(DialogInterface dialog, int which) {
					// this invokes the Location settings
					startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), Globals.ACTIVITY_LOCATION_SETTINGS);
			}
		        }).setNegativeButton(R.string.gps_disabled_answer_no, new DialogInterface.OnClickListener() {
		                @Override
		                public void onClick(DialogInterface dialog, int which) {
		                        MainScreen.this.finish(); // if there is no location 
		                }
		        });
		
		AlertDialog alert = builder.create(); // create the alert
		alert.show(); // show the dialog 
	}

}