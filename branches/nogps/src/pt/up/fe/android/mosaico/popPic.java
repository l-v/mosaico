package pt.up.fe.android.mosaico;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

class popPic extends Dialog{

	private Photo data;

	public popPic(Context context, Photo data) {
		super(context);
		this.data = data;
		this.setTitle(data.getTitle());
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.windowpic);
		ImageView imageView = (ImageView) findViewById(R.id.fullimage);
		
		Button identicabutton = (Button) findViewById(R.id.identicabutton);
		/*
		 * This runs when we press the button to send a post to identica 
		 */
		identicabutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View v) {

				final IdenticaAPI identica = new IdenticaAPI("mosaico", "m23e43");
				
				// Shows a progress dialog while it sends the update to identica
				final ProgressDialog dialog = ProgressDialog.show(v.getContext(), "", 
						"Posting update...", true);
				

				/*
				 * This handler and the following runnables allow us to call a toast from inside the tread.
				 */
				final Handler mHandler = new Handler();
				final Runnable toastPostOk = new Runnable() {
				    public void run() {
				    	Toast.makeText(v.getContext(),"identica: Update posted.", Toast.LENGTH_SHORT).show();
				    }
				};
				final Runnable toastPostFailed = new Runnable() {
				    public void run() {
				    	Toast.makeText(v.getContext(),"identica: Update failed.", Toast.LENGTH_SHORT).show();
				    }
				};
				
				/*
				 * This thread sends the update to identica and then dismisses the dialog.
				 * This has to be threaded for the progress dialog to work.
				 */
				new Thread() {
					public void run(){
						// Posts update
						identica.postUpdate("Is visiting " + data.getWebUrl());
						// Dismisses the progress dialog
						dialog.dismiss();
						// Shows a toast with the result of the update
						if(identica.isUpdateOK())
							mHandler.post(toastPostOk);
						else
							mHandler.post(toastPostFailed);
						}
				}.start();
				

			}});
		
		/* end of the identica button code */
		
		Button openmaps = (Button) findViewById(R.id.openmaps);
		/*
		 * This runs when we press the button Go To to start google maps 
		 */
		openmaps.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View v) {
				/*
				 * We can use google maps with a marker
				 */
//				Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
//						Uri.parse("geo:0,0?q="+data.getLatitude()+","+data.getLongitude()+" (" + data.getTitle() + ")"));
				/*
				 * Or call directly google navigation.
				 */
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
						Uri.parse("google.navigation:q=" + data.getLatitude()+","+data.getLongitude() ));
						v.getContext().startActivity(intent);
			}
		});
		
		imageView.setImageBitmap(data.getPhoto());
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();				
			}
		});

	}
	
	public void enddialog() {
		dismiss();
	}

}
