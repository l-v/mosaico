package pt.up.fe.android.mosaico;

import java.util.Timer;
import java.util.TimerTask;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class MyLocation {
	Timer timer1;
	LocationManager lm;
	LocationResult locationResult;
	boolean isGpsEnabled = false;
	boolean isNetworkEnabled = false;
	final static String TAG = "MyLocation";

	ProgressDialog pd;

	public boolean getLocation(Context context, LocationResult result) {
		// Use LocationResult callback class to pass location value from
		// MyLocation to user code.
		locationResult = result;
		if (lm == null)
			lm = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);

		// exceptions will be thrown if provider is not permitted.
		try {
			isGpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			isNetworkEnabled = lm
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		// don't start listeners if no provider is enabled
		if (!isGpsEnabled && !isNetworkEnabled)
			// TODO must inform the user for nothing enabled
			return false;

		if (isGpsEnabled)
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L,
					100.0f, locationListenerGps);
		if (isNetworkEnabled)
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L,
					100.0f, locationListenerNetwork);
		timer1 = new Timer();
		timer1.schedule(new GetLastLocation(), 10000); // schedule the timer for 10 secs

		pd = ProgressDialog.show(context, "Working... ",
				"Getting current location...", true);

		Log.v("MyLocation", "gps enabled:" + isGpsEnabled);
		Log.v("MyLocation", "net enabled:" + isNetworkEnabled);
		return true;
	}

	// the location listener for GPS
	LocationListener locationListenerGps = new LocationListener() {
		public void onLocationChanged(Location location) {
			timer1.cancel();
			pd.dismiss(); // dismiss the progress dialog
			locationResult.gotLocation(location); // set the location found
			Log.v("MyLocation", "location from GPS lat:" + location.getLatitude());
			Log.v("MyLocation", "location from GPS lat:" + location.getLongitude());
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
	// the location listener for Network
	LocationListener locationListenerNetwork = new LocationListener() {
		public void onLocationChanged(Location location) {
			timer1.cancel();
			pd.dismiss();  // dismiss the progress dialog
			locationResult.gotLocation(location);  // set the location found
			Log.v("MyLocation", "location from Net lat:" + location.getLatitude());
			Log.v("MyLocation", "location from Net lat:" + location.getLongitude());
			// stop both listeners
			lm.removeUpdates(this);
			lm.removeUpdates(locationListenerGps);
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	class GetLastLocation extends TimerTask {
		@Override
		public void run() {
			// stop the listeners just in case
			lm.removeUpdates(locationListenerGps);
			lm.removeUpdates(locationListenerNetwork);

			Location netLoc = null, gpsLoc = null;
			// check which provider is enabled and get the last known location from it
			if (isGpsEnabled)
				gpsLoc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (isNetworkEnabled)
				netLoc = lm
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			// if there are both values use the latest one
			if (gpsLoc != null && netLoc != null) {
				if (gpsLoc.getTime() > netLoc.getTime())
				{
					locationResult.gotLocation(gpsLoc);
				}
					
				else
					locationResult.gotLocation(netLoc);
				return;
			}

			// if only one of them has value, check which one
			if (gpsLoc != null) {
				locationResult.gotLocation(gpsLoc);
				Log.v("MyLocation", "location from GPS lat:" + gpsLoc.getLatitude());
				return;
			}
			if (netLoc != null) {
				locationResult.gotLocation(netLoc);
				Log.v("MyLocation", "location from GPS lat:" + netLoc.getLatitude());
				return;
			}
			
			// worst case scenario - there is no location at all
			Log.d(TAG, "gpsLoc and netLoc are null");
			locationResult.gotLocation(null);
			pd.dismiss(); // dismiss the progress dialog
		}
	}

	public static abstract class LocationResult {
		public abstract void gotLocation(Location location);
	}
}
