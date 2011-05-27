package pt.up.fe.android.mosaico;

import java.util.LinkedList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainScreen extends Activity {
    /** Called when the activity is first created. */
	PhotoSet myPhotos;
	PanoramioAPI processPhotos;
	
	private double currentLatitude;
	private double currentLongitude;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //myPhotos = new PhotoSet(dummyLat, dummyLong, dummyLat, dummyLong, maxLat, maxLong); 
        myPhotos = new PhotoSet(0, 0, 41.365968,  -8.780026,  41.383263, -8.764563);
        processPhotos = new PanoramioAPI(myPhotos);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
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
        	getMyLocation();	// fill in the coordinates
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
    /**
     * Fill in the location coordinates
     */
    private void getMyLocation() {
    	
    	ProgressDialog pd = ProgressDialog.show(this, "Working..", "Finding your position...");
    	LocationManager locationManager;
        String context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager)getSystemService(context);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);
        
        this.currentLatitude = location.getLatitude();
        this.currentLongitude = location.getLongitude();
		
        Toast.makeText(this, currentLatitude + " : " + currentLongitude, Toast.LENGTH_LONG).show();
        pd.dismiss();
	}


	public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        //File photogrid = new File("/sdcard/PhotoGrid");
        //File[] thumbs = photogrid.listFiles();
        LinkedList<Photo> pics = myPhotos.getList();

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            //return thumbs.length;
        	System.out.println("Size of vector: " + pics.size());
        	return pics.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        /*public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            TextView distance;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                distance = new TextView(mContext);
                //imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
                distance = (TextView) convertView;
            }

            Uri imgUri=Uri.fromFile(thumbs[position]);
            imageView.setImageURI(imgUri);
            
            distance.setText("12343");
            return imageView;
        }*/
        public View getView(int position, View convertView, ViewGroup parent) {
        	
        	if(convertView == null) { // recycled view is null so create it.
        		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        		convertView = inflater.inflate(R.layout.gridelement, parent, false);
           }
           ImageView imageView = (ImageView) convertView.findViewById(R.id.thumbimg);
           TextView textView = (TextView) convertView.findViewById(R.id.thumbdist);
           
           imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
           imageView.setPadding(8, 8, 8, 8); 
           
           textView.setPadding(8, 0, 0, 0);
           
           //Uri imgUri=Uri.fromFile(thumbs[position]);
           //imageView.setImageURI(imgUri);
           imageView.setImageBitmap(pics.get(position).getThumb());
           
           textView.setText(pics.get(position).getTitle());
           return convertView;
        }
    }
}