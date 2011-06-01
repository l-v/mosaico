package pt.up.fe.android.mosaico;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

public class Settings extends Activity {
	
	public static final String PREFS_NAME = "MosaicoPrefs";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
	    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    String iden_username = settings.getString("iden_username", "");
	    String iden_pass = settings.getString("iden_pass", "");
	    EditText usr = (EditText) findViewById(R.id.iden_user);
	    EditText pass = (EditText) findViewById(R.id.iden_pass);
	    usr.setText(iden_username);
	    pass.setText(iden_pass);	    
	}
	
	   @Override
	    protected void onStop(){
	      super.onStop();

		  EditText usr = (EditText) findViewById(R.id.iden_user);
		  EditText pass = (EditText) findViewById(R.id.iden_pass);
	      SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	      SharedPreferences.Editor editor = settings.edit();
	      editor.putString("iden_username", usr.getText().toString());
	      editor.putString("iden_pass", pass.getText().toString());
	      editor.commit();
	      
	      Globals.identicaUsername = usr.getText().toString();
	      Globals.identicaPassword = pass.getText().toString();
	    }
}
