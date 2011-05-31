package pt.up.fe.android.mosaico;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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
		Button identica = (Button) findViewById(R.id.identicabutton);
		identica.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getContext(), "Click!", Toast.LENGTH_LONG).show();
				// Chamar método p.e identica(data.getLatitude(), data.getLongitude());				
			}});
		
		imageView.setImageBitmap(data.getPhoto());
		imageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();				
			}});
		
	}
	public void enddialog() {
		dismiss();
	}

}
