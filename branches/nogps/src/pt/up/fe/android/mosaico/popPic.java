package pt.up.fe.android.mosaico;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.content.DialogInterface.OnClickListener;;

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
		imageView.setImageBitmap(data.getPhoto());
		
	}
	public void enddialog() {
		dismiss();
	}

}
