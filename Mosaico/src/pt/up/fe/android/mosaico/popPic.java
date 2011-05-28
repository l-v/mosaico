package pt.up.fe.android.mosaico;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

class popPic extends Dialog {
	/**
	 * @param context
	 */
	private Photo data;

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
	public void enddialog() {
		dismiss();
	}
}
