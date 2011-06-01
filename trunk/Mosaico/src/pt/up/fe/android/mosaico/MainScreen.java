package pt.up.fe.android.mosaico;

import pt.up.fe.android.mosaico.MyLocation.LocationResult;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.os.Bundle;
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
	
	private Location currentLocation;
	MyLocation myLocation = new MyLocation();
	
	public static final String PREFS_NAME = "MosaicoPrefs"; //Preferences file
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
	    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    Globals.identicaUsername = settings.getString("iden_username", "");
	    Globals.identicaPassword = settings.getString("iden_pass", "");
	    
		gridview = (GridView) findViewById(R.id.gridview);
		
		/*
		 * This is not used anymore.
		 */
		//Double currentLatitude = 41.383263;
		//Double currentLongitude = -8.780026;
		
		//myPhotos = new PhotoSet(currentLatitude, currentLongitude, 3);
		
//		processPhotos = new PanoramioAPI(myPhotos);
//		gridview.setAdapter(new ImageAdapter(this, myPhotos));
//		gridview.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				ImageAdapter ad = (ImageAdapter) arg0.getAdapter();
//				popPic dialog = new popPic(arg1.getContext(), ad.getItem(arg2));
//				dialog.show();
//			}
//		});
		
		/*
		 * this gets a location and 
		 * afterwards call the function locationResult
		 */
		boolean gotLoc = myLocation.getLocation(this, locationResult);
		if (!gotLoc)
			Toast.makeText(this, "no location", Toast.LENGTH_LONG).show();

	}
	
	/*
	 *  This is what to do when we got a location.
	 *  Here is were we retrieve the photos.
	 *  implementation of the abstract class
	 */
	public LocationResult locationResult = new LocationResult() {
	    @Override
	    public void gotLocation(final Location location) {
	        //Got the location!
	    	if(location!= null){
	    		currentLocation = location;
	    		retrievePhotos();
	    	}
	    };
	};
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override	
    public void onResume() { 
        super.onResume();

	}
	@Override
	protected void onPause()
	{
		super.onPause();
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
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return (super.onPrepareOptionsMenu(menu));
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode) { 
	    	case (1) : { 
	    		if (resultCode == Activity.RESULT_OK)
	    		{
	    			Bundle b = data.getExtras();
	    			currentLocation.setLatitude(b.getDouble("LATITUDE"));
	    			currentLocation.setLongitude(b.getDouble("LONGITUDE"));
	    			retrievePhotos();
	    		}
	    		break;
	    	}
	    }
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
				/*
				 * Tries to get the location and if location OK retrieves photos
				 */
				boolean locOK = myLocation.getLocation(this, locationResult); // get a new location from the gps device
				if(!locOK)
					Toast.makeText(this, "no location", Toast.LENGTH_LONG).show();
				return true;
			case R.id.gps_another:
				// create a new intent for the gmaps view
				Intent intent = new Intent(MainScreen.this, GoogleMapsView.class);
				Bundle b = new Bundle();
				b.putDouble("LATITUDE", currentLocation.getLatitude());
				b.putDouble("LONGITUDE", currentLocation.getLongitude());
				intent.putExtras(b);
				MainScreen.this.startActivityForResult(intent, 1);
				return true;
		case R.id.menu_pref:
			Intent pref = new Intent(this, Settings.class);
			startActivity(pref);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}