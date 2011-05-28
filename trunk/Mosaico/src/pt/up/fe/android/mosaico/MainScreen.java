package pt.up.fe.android.mosaico;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

class popPic extends Dialog {
	/**
	 * @param context
	 */
	Photo data;

	public popPic(Context context, Photo data) {
		super(context);
		this.data = data;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.windowpic);
		ImageView imageView = (ImageView) findViewById(R.id.fullimage);
		imageView.setImageBitmap(data.getPhoto());
	}

}

public class MainScreen extends Activity {
	/** Called when the activity is first created. */
	PhotoSet myPhotos;
	PanoramioAPI processPhotos;
	
	/** Defines the format for the distance. */
	DecimalFormat distanceFormat = new DecimalFormat("#0.0 Km");

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
	private void getLastGoodLocation() {
		try {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String provider = lm.getBestProvider(criteria, true);
        Location location = lm.getLastKnownLocation(provider);
        
        this.currentLatitude = location.getLatitude();
        this.currentLongitude = location.getLongitude();
		}
		catch (Exception e)
		{
			getMyLocation();
		}
	}
/**
 * retrieve a new location place
 */
	public void getMyLocation()
	{
		// this dialog doesn't seem to work but maybe its not worthed
		pd = ProgressDialog.show(this, "Working... ", "Getting current location...");
		
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    LocationListener ll = new mylocationlistener();
	    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 1.0f, ll);
	    
	    boolean isGPS = lm.isProviderEnabled (LocationManager.GPS_PROVIDER);
	    if(!isGPS)
	    {
	    	checkGPSsettings();
	    }
	   //this.currentLatitude = currentLocation.getLatitude();
	    //this.currentLongitude = currentLocation.getLongitude();
	    Log.d("Coordinates changed", currentLongitude + " " + currentLatitude );
	    pd.dismiss();
	}
	
	/**
	 * show alert to ask for gps settings 
	 */
	public void checkGPSsettings()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage("Enable your GPS or continue viewing old photos!")
    	.setCancelable(false).setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
			}
		})
		.setNegativeButton("Old Photos", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//MainScreen.this.finish();
				dialog.dismiss();
			}
		});
    	
    	AlertDialog alert = builder.create();
    	alert.show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return (super.onPrepareOptionsMenu(menu));
	}

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
			getMyLocation(); // get the new location
			return true;
		case R.id.gps_another:

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
	
	private class mylocationlistener implements LocationListener {
		
		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {
	        Log.d("LOCATION CHANGED", location.getLatitude() + "");
	        Log.d("LOCATION CHANGED", location.getLongitude() + "");
	        Toast.makeText(MainScreen.this,
	            location.getLatitude() + "" + location.getLongitude(),
	            Toast.LENGTH_LONG).show();
	        }
			currentLatitude = location.getLatitude();
			currentLongitude = location.getLongitude();
			 retrievePhotos();
			//currentLocation = location;
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

	public class ImageAdapter extends BaseAdapter {
		private Context mContext;
		ArrayList<Photo> pics;

		public ImageAdapter(Context c, PhotoSet data) {
			mContext = c;
			pics = data.getList();
		}

		public int getCount() {
			return pics.size();
		}

		public Photo getItem(int position) {
			return pics.get(position);
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {

			if (convertView == null) { // recycled view is null so create it.
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.gridelement, parent,
						false);
			}
			final ImageView imageView = (ImageView) convertView
					.findViewById(R.id.thumbimg);
			TextView textView = (TextView) convertView
					.findViewById(R.id.thumbdist);

			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8, 8, 8, 8);

			textView.setPadding(8, 0, 0, 0);

			new Thread(new Runnable() {
				public void run() {
					final Bitmap b = pics.get(position).getThumb();
					imageView.post(new Runnable() {
						public void run() {
							imageView.setImageBitmap(b);
						}
					});
				}
			}).start();

			// imageView.setImageBitmap(pics.get(position).getThumb());

			textView.setText(distanceFormat.format( pics.get(position).getDistance() ));
			return convertView;
		}
	}
}