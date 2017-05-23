package cn.jishuz.library.adpter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import cn.jishuz.library.R;
import cn.jishuz.library.until.Basket;

public class BasketAdapter extends BaseAdapter {
	
	private ArrayList<Basket> basket;
	private LayoutInflater mInflater;
	private Context mContext;
	
	public BasketAdapter(Context context,ArrayList<Basket> basket) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		this.basket = basket;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return basket.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return basket.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.basketadapter, null);
			viewHolder.mImage = (ImageView) convertView.findViewById(R.id.basket_image);
			viewHolder.mTitle = (TextView) convertView.findViewById(R.id.basket_title);
			viewHolder.mAuthor = (TextView) convertView.findViewById(R.id.basket_author);
			viewHolder.mRating = (RatingBar) convertView.findViewById(R.id.basket_score_r);
			viewHolder.mScore = (TextView) convertView.findViewById(R.id.basket_score);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Basket mBasket = basket.get(position);
		viewHolder.mImage.setImageBitmap(mBasket.mBookImage);
		viewHolder.mTitle.setText(mBasket.mBookTitile);
		viewHolder.mAuthor.setText(mBasket.mBookAuthor);
		viewHolder.mRating.setRating(mBasket.core_r);
		viewHolder.mScore.setText(mBasket.core+"");
		return convertView;
	}
	
	class ViewHolder{
		public ImageView mImage;
		public TextView mTitle;
		public TextView mAuthor;
		public RatingBar mRating;
		public TextView mScore;
	}
}
