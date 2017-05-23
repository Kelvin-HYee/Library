package cn.jishuz.library.adpter;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jishuz.library.R;

public class BookAdapter extends BaseAdapter {

	private ArrayList<String> mBookCore = new ArrayList<String>();
	private ArrayList<Bitmap> mBookImage = new ArrayList<Bitmap>();
	private LayoutInflater mInflater;
	private Context mContext;
	LinearLayout.LayoutParams params;
	private int resourceId;

	public BookAdapter(Context context, int textViewResourceId, ArrayList<String> bookCore,
			ArrayList<Bitmap> bookImage) {
		mContext = context;
		mBookCore = bookCore;
		mBookImage = bookImage;
		mInflater = LayoutInflater.from(mContext);
		params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		resourceId = textViewResourceId;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mBookCore.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mBookCore.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		View view;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			view = mInflater.inflate(resourceId, null);
			viewHolder.bookScore = (TextView) view.findViewById(R.id.adapter_textView);
			viewHolder.bookImage = (ImageView) view.findViewById(R.id.adapter_iamge);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.bookScore.setText(mBookCore.get(position));
		viewHolder.bookImage.setImageBitmap(mBookImage.get(position));
		return view;
	}

	class ViewHolder {
		TextView bookScore;
		ImageView bookImage;
	}
}
