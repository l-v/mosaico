package pt.up.fe.android.mosaico;

import java.text.DecimalFormat;
import java.util.ArrayList;

import pt.up.fe.android.mosaico.Photo;
import pt.up.fe.android.mosaico.PhotoSet;
import pt.up.fe.android.mosaico.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

		textView.setText(new DecimalFormat("#0.0 Km").format( pics.get(position).getDistance() ));
		return convertView;
	}
}
