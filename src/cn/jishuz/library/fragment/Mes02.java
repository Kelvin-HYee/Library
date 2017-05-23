package cn.jishuz.library.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import cn.jishuz.library.R;
import cn.jishuz.library.adpter.MessageAdapter;
import cn.jishuz.library.db.Mydata;
import cn.jishuz.library.until.Message;

public class Mes02 extends Fragment{
	
	private Mydata dbHelp;
	private Context mContext;
	private MessageAdapter adapter;
	private ArrayList<Message> mMessage = new ArrayList<Message>();
	private ListView listView;

	public View onCreateView(LayoutInflater mInflater, ViewGroup container, Bundle onBundle) {
		View view = mInflater.inflate(R.layout.mes_tab02, container, false);
		setData();
		adapter = new MessageAdapter(mContext, mMessage);
		listView = (ListView) view.findViewById(R.id.mes02_listView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				
			}
		});
		return view;
	}

	private void setData() {
		// TODO Auto-generated method stub
		dbHelp = new Mydata(mContext);
		SQLiteDatabase db = dbHelp.getReadableDatabase();
		Cursor cursor = db.query("message", null, "type = ?", new String[]{"2"}, null, null, "_id desc");
		if(cursor.moveToFirst()){
			do{
				String title = cursor.getString(cursor.getColumnIndex("title"));
				String content = cursor.getString(cursor.getColumnIndex("content"));
				String time = cursor.getString(cursor.getColumnIndex("datetime"));
				mMessage.add(new Message(title, content, time));
			}while(cursor.moveToNext());
		}
		dbHelp.close();
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mContext = activity;
	}
}
