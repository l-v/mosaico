package pt.up.fe.android.mosaico;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.widget.Toast;

public class LocationAsyncTask extends AsyncTask<Void, String, String> {

	private Context context;
	private ProgressDialog pd;
	
	public LocationAsyncTask(Context context)
	{
		this.context = context;
	}
	
    @Override  protected void onPostExecute(String result) {
        // TODO Auto-generated method stub   
    	pd.dismiss();
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        }  

    @Override  
    protected void onPreExecute() {
        // TODO Auto-generated method stub   
        pd = ProgressDialog.show(context, "Working..", "Finding your position...");

        } 


    @Override  
    protected String doInBackground(Void... params) {  
        // TODO Auto-generated method stub 
        LocationManager locationManager;
        String context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager)this.context.getSystemService(context);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String provider = locationManager.getBestProvider(criteria, true);
        Location location =locationManager.getLastKnownLocation(provider);
        String strUpdateResult = updateWithANewLocation(location);

    //locationManager.requestLocationUpdates(provider, 1000, 10, locationListener);

        return strUpdateResult; 
        }  

    private String updateWithANewLocation(Location location) {
        // TODO Auto-generated method stub
        StringBuilder sbLocation = new StringBuilder();
        String strLocation = new String();
        //TextView myLocationText;

        String addressString = "No address found";
        String latLongString = "";

            //If there is a location
            if (location != null) {

                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    double altitude = location.getAltitude();
                    float accuracy = location.getAccuracy();
                    float bearing = location.getBearing();
                    long time = location.getTime();
                    float speed = location.getSpeed();
                    String prov = location.getProvider();
                    strLocation = "Latitude: " + latitude + "\nLongitude: " +  longitude + "\n" ;
                    publishProgress(strLocation);
            }
                    /*
                    Geocoder gc = new Geocoder(LateRunner.this, Locale.getDefault());
                    try {
                        List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);

                        //if location and then address is found
                        if (addresses.size() > 0 ) {
                            Address address = addresses.get(0);

                            for (int i=0; i <address.getMaxAddressLineIndex(); i++) {
                                sbLocation.append(address.getAddressLine(i)).append("\n");
                            }
                            strLocation= sbLocation.toString();
                            publishProgress(strLocation);
                        }
                        //if location but no address
                        else {
                            strLocation = "Latitude: " + latitude + "\nLongitude: " +  longitude + "\n";    
                            publishProgress(strLocation);
                        } //end try

                    } catch (IOException e) {
                        strLocation = "Latitude: " + latitude + "\nLongitude: " +  longitude + "\n";    
                        publishProgress(strLocation);

                    }//end catch
                }

            //If no location found
            else {
                strLocation = "Unable to find location. ";
                publishProgress(strLocation);
            } */
            return strLocation;



    }// end updateWithANewLocation()



    @Override  protected void onProgressUpdate(String... result) { 
        // TODO Auto-generated method stub 
        Toast.makeText(this.context, result[0], Toast.LENGTH_LONG).show();
        } 
 
}
