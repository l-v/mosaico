package pt.up.fe.android.mosaico;

/**
 * Used to store global variables for the application
 *
 */
final class Globals {
	public static final String PANORAMIO_THUMB_URL = "http://mw2.google.com/mw-panoramio/photos/square/";
	public static final int NUMBER_PHOTOS_TO_GET = 50;
	public static final double EARTH_RADIUS = 6371.01;
	public static final String IDENTICA_URL = "http://identi.ca/api/statuses/update.xml?source=Mosaico";
	public static String identicaUsername="";
	public static String identicaPassword="";
	public static int photos_range = 2;
	public static int photos_number_to_get = 50;
	public static boolean force_gps_usage = false;
	public static boolean mBusy = false;
	public static final String FLICKR_KEY = "941a4b4ce1de1b188639082b64d6ef5e";
	public static final String FLICKR_SECRET = "714103cba62ba18a";
	public static final int ACTIVITY_LOCATION_SETTINGS = 1;
	public static final int ACTIVITY_GOOGLE_MAPS = 2;
	public static final int ACTIVITY_PREFERENCES = 3;
}