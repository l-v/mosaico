package pt.up.fe.android.mosaico;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 
 * Posts an update to identi.ca using HTTP POST and Basic Authentication
 *
 */
public class IdenticaAPI {
	String username;
	String password;
	boolean updateOK; // saves the status of the last update	


	public boolean isUpdateOK() {
		return updateOK;
	}

	/**
	 * Constructor
	 * @param username
	 * @param password
	 */
	public IdenticaAPI(String username, String password) {

		this.username = username;
		this.password = password;
		this.updateOK = false;
	}

	/**
	 * Posts the message.
	 * @param message
	 * @return True if post OK, false otherwise
	 */
	public void postUpdate(String message){

		try{// Creates the connection
			URL url = null;
			url = new URL(Globals.IDENTICA_URL);

			HttpURLConnection connection = null;
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			// Simple Authentication
			String val = (new StringBuffer(username).append(":").append(password)).toString();
			byte[] base = val.getBytes();
			String authorizationString = "Basic " + new String(Base64.encodeBytes(base));
			connection.setRequestProperty ("Authorization", authorizationString);

			// Generates the url parameters
			String urlParameters = "status=" + URLEncoder.encode(message,"UTF-8");
			
			connection.setDoOutput(true);
			// Sends the request
			DataOutputStream wr = new DataOutputStream (
					connection.getOutputStream ());
			wr.writeBytes (urlParameters);
			wr.flush ();
			wr.close ();

			// Gets the Response	
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer(); 
			while((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			System.out.println(response.toString());
		
			updateOK = true;


		}catch (Exception e) {

			e.printStackTrace();
			updateOK = false;
		}
	}

}
