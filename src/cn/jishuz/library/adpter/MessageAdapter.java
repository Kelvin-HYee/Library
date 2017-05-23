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
import cn.jishuz.library.until.Message;

public class MessageAdapter extends BaseAdapter{

	private ArrayList<Message> mMessage;
	private LayoutInflater mInflater;
	private Context mContext;
	
	public MessageAdapter(Context mContext,ArrayList<Message> mMessage) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.mMessage = mMessage;
		mInflater = LayoutInflater.from(mContext);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mMessage.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mMessage.get(position);
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
			convertView = mInflater.inflate(R.layout.messageadapter, null);
			viewHolder.mTitle = (TextView) convertView.findViewById(R.id.mesAdapter_title);
			viewHolder.mContent = (TextView) convertView.findViewById(R.id.mesAdapter_content);
			viewHolder.mTime = (TextView) convertView.findViewById(R.id.mesAdapter_time);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Message msg = mMessage.get(position);
		viewHolder.mTitle.setText(msg.mTitile);
		viewHolder.mContent.setText(msg.mContent);
		viewHolder.mTime.setText(msg.mTime);
		return convertView;
	}
	
	class ViewHolder{
		public TextView mTitle;
		public TextView mContent;
		public TextView mTime;
	}

}
