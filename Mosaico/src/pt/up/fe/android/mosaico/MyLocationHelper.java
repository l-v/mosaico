package pt.up.fe.android.mosaico;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


public class MyLocationHelper {

	private LocationManager lm;
	private LocationResult locationResult;
	private boolean isGpsEnabled = false;
	private boolean isNetworkEnabled = false;
	private final static String TAG = "MyLocationHelper";
	private AlertDialog alertDialog;
	
	
	public boolean getLocation(Context context, LocationResult result) throws Exception {
		// Use LocationResult callback class to pass location value from
		// MyLocation to user code.
		locationResult = result;
		final Handler handler = new Handler();
		 AlertDialog.Builder builder = new AlertDialog.Builder(context);
	    	builder.setMessage(R.string.location_searching).setTitle(R.string.app_name)
	    	.setCancelable(false).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// on click cancel get last known Location
					Runnable getLastLocThread = new Runnable() {
						public void run() {
							getLastKnownLocation();
						};
					};
					// on click cancel get last known Location posting the thread
					handler.post(getLastLocThread);
				}
			});
	    	 // create the dialog and return it
			 alertDialog = builder.create();
			 //alertDialog.show();
			 
			 Runnable showAlert = new Runnable() {
				 public void run() {
					 alertDialog.show();
				 };
			 };
			 // show the dialog posting it in a new thread
			 handler.post(showAlert);
		if (lm == null)
			lm = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);

		//////////////
		// this is how it will work if the force_gps_usage is true from the preferences
		/////////////
		if (Globals.force_gps_usage == true)
		{
			Log.v(TAG, "Force GPS usage is ON");
			// exceptions will be thrown if provider is not permitted.
			try {
				isGpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
			} catch (Exception ex) {
				Log.v(TAG, "isGPSEnabled: " + isGpsEnabled + ex.toString());
			}
			// now if gps in not enabled throw the exception to turn it on
			if (!isGpsEnabled) {
				throw new Exception("No Netowork or GPS Location available");
			}
			// if it is enabled start the listener
			if (isGpsEnabled)
			{
				lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 100.0f, locationListenerGps);
				Log.v(TAG, "started gps listener");
			}
			
			Log.v(TAG, "gps enabled:" + isGpsEnabled);
			return true;
		}
		///////
		/// if force_gps_usage is false  in the Preferences
		///////
		// exceptions will be thrown if provider is not permitted.
		try {
			isGpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
			Log.v(TAG, "isGPSEnabled: " + isGpsEnabled + ex.toString());
		}
		try {
			isNetworkEnabled = lm
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
			Log.v(TAG, "isNetworkEnabled: " + isNetworkEnabled + ex.toString());
		}

		// don't start listeners if no provider is enabled
		if (!isGpsEnabled && !isNetworkEnabled) {
			getLastKnownLocation();		// if the providers are disabled try to get lastKnown Location
			dismissAlertDialog();		// dismiss the alertDialog
			Log.v(TAG, "No Netowork or GPS Location available");
			throw new Exception("No Netowork or GPS Location available");
		}
		if (isGpsEnabled)
		{
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L,
					100.0f, locationListenerGps);
			Log.v(TAG, "started gps listener");
		}
		if (isNetworkEnabled)
		{
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L,
					100.0f, locationListenerNetwork);
			Log.v(TAG, "started network listener");
		}

		Log.v(TAG, "gps enabled:" + isGpsEnabled);
		Log.v(TAG, "net enabled:" + isNetworkEnabled);
		return true;
	}
	
	/**
	 * Get The last known location saved by AndroidOS
	 * will be invoked if the user does not want to wait
	 * for a fresh location to be retrieved
	 */
	public void getLastKnownLocation() {
		// stop the listeners just in case
		lm.removeUpdates(locationListenerGps);
		lm.removeUpdates(locationListenerNetwork);

		Location netLoc = null, gpsLoc = null;
		
		// get location only from GPS if force_gps_usage is true in Globals
		if (Globals.force_gps_usage == true)
		{
			// check if GPS provider is enabled get the last location in gpsLoc 
			if (isGpsEnabled)
				gpsLoc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			
			// if GPS location was retrieved
			if (gpsLoc != null) {
				locationResult.gotLocation(gpsLoc);			// pass the location
				Log.v(TAG, "last location from GPS lat:" + gpsLoc.getLatitude());
				Log.v(TAG, "last location from GPS long:" + gpsLoc.getLongitude());
				return;
			}
			// worst case scenario - there is no location 
			Log.d(TAG, "Last known gpsLoc and netLoc are null");
			locationResult.gotLocation(null);
			return;
		}
		
		
		
		
		// check which provider is enabled and get the last known location
		if (isGpsEnabled)
			gpsLoc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (isNetworkEnabled)
			netLoc = lm
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		// if there are both values use the latest one
		if (gpsLoc != null && netLoc != null) {
			if (gpsLoc.getTime() > netLoc.getTime()) {
				locationResult.gotLocation(gpsLoc);		// pass the location
				Log.v(TAG, "last location from GPS lat:" + gpsLoc.getLatitude());
				Log.v(TAG, "last location from GPS long:" + gpsLoc.getLongitude());
			}
			else
				locationResult.gotLocation(netLoc);		// pass the location
				Log.v(TAG, "last location from NET lat:" + netLoc.getLatitude());
				Log.v(TAG, "last location from NET long:" + netLoc.getLongitude());
			return;
		}

		// if only one of them has value, check which one
		if (gpsLoc != null) {
			locationResult.gotLocation(gpsLoc);			// pass the location
			Log.v(TAG, "last location from GPS lat:" + gpsLoc.getLatitude());
			Log.v(TAG, "last location from GPS long:" + gpsLoc.getLongitude());
			return;
		}
		if (netLoc != null) {
			locationResult.gotLocation(netLoc);			// pass the location
			Log.v(TAG, "last location from NET lat:" + netLoc.getLatitude());
			Log.v(TAG, "last location from NET long:" + netLoc.getLongitude());
			return;
		}

		// worst case scenario - there is no location at all
		Log.d(TAG, "Last known gpsLoc and netLoc are null");
		locationResult.gotLocation(null);
	}
	
	/**
	 * Dismiss the alert Dialog if it is shown
	 */
	private void dismissAlertDialog() {
		if (alertDialog.isShowing())	// dismiss the dialog
			alertDialog.dismiss();
	}
	
	/**
	 *  Abstract class that must be implemented
	 *  where the location must be received and handled
	 *  @author anlazarov
	 */
	public static abstract class LocationResult {
		public abstract void gotLocation(Location location);
	}
	
	//////////////////////////////////////////////////////////////
	////// Implementation of LocationListeners
	//////////////////////////////////////////////////////////////
	
	/**
	 * The GPS LocationListener
	 */
	LocationListener locationListenerGps = new LocationListener() {
		
		@Override
		public void onLocationChanged(Location location) {
			locationResult.gotLocation(location); 	// pass the location found
			dismissAlertDialog();					// dismiss the alertDialog
			Log.v(TAG, "location from GPS lat:" + location.getLatitude());
			Log.v(TAG, "location from GPS long:" + location.getLongitude());
			
			// stop both listeners
			lm.removeUpdates(this);
			lm.removeUpdates(locationListenerNetwork);
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
	};

	/**
	 * The Network Location Listener
	 */
	LocationListener locationListenerNetwork = new LocationListener() {
		
		@Override
		public void onLocationChanged(Location location) {
			locationResult.gotLocation(location); 	// pass the location found
			dismissAlertDialog();					// dismiss the alertDialog
			Log.v(TAG, "location from Net lat:" + location.getLatitude());
			Log.v(TAG, "location from Net lat:" + location.getLongitude());
			// stop both listeners
			lm.removeUpdates(this);
			lm.removeUpdates(locationListenerGps);
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
	};


}
