package cn.jishuz.library.fragment;

import cn.jishuz.library.R;
import cn.jishuz.library.activity.BookDetails;
import cn.jishuz.library.adpter.BasketAdapter;
import cn.jishuz.library.db.Mydata;
import cn.jishuz.library.until.Basket;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class BasketFragment extends Fragment {
	
	private Mydata dbHelp;
	private List<Integer> mId = new ArrayList<Integer>();
	private ArrayList<Basket> mArr = new ArrayList<Basket>();
	private ListView listView;
	private Context mcontext;
	private BasketAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.tab03, container,false);
		setData();
		adapter = new BasketAdapter(mcontext, mArr);
		listView = (ListView) view.findViewById(R.id.mbasket_listView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), BookDetails.class);
				intent.putExtra("type", "basket");
				intent.putExtra("id", mId.get(position));
				getActivity().startActivity(intent);
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
				final int p = position;
				AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
				builder.setTitle("警告");
				builder.setIcon(android.R.drawable.ic_input_delete);
				builder.setMessage("你确定要从书蓝里删除此书吗？");
				builder.setPositiveButton("确定", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dbHelp = new Mydata(mcontext);
						SQLiteDatabase db = dbHelp.getReadableDatabase();
						db.delete("basket", "bookid = ?", new String[]{mId.get(p).toString()});
//						BasketAdapter adapter = (BasketAdapter) listView.getAdapter();
						mArr.remove(p);
						adapter = new BasketAdapter(mcontext, mArr);
						listView.setAdapter(adapter);
						db.close();
						dbHelp.close();
					}
				});
				
				builder.setNegativeButton("取消", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				});
				builder.show();
				return true;
			}
		});
		return view;
	}
	
	private void setData(){
		dbHelp = new Mydata(mcontext);
		Cursor cursor = dbHelp.queryAll("basket");
		if(cursor.moveToFirst()){
			do {
				mId.add(cursor.getInt(cursor.getColumnIndex("bookid")));
			} while (cursor.moveToNext());
		}
		cursor.close();
		Iterator it = mId.iterator();
		while(it.hasNext()){
			SQLiteDatabase sql = dbHelp.getReadableDatabase();
			Object whereString = it.next();
			Cursor cursor1 = sql.query("book", null, "_id = ?", new String[]{whereString.toString()},null, null, null);
			if(cursor1.moveToFirst()){
				String name = cursor1.getString(cursor1.getColumnIndex("name"));
				String author = cursor1.getString(cursor1.getColumnIndex("author"));
				String s = cursor1.getString(cursor1.getColumnIndex("score"));
				String image = cursor1.getString(cursor1.getColumnIndex("Image"));
				float score = Float.valueOf(s);

				AssetManager assets = getActivity().getAssets();
				InputStream is = null;
				try {
					is = assets.open("images/" + image + ".jpg");
				} catch (IOException e) {
					e.printStackTrace();
				}
				BitmapFactory.Options options = new BitmapFactory.Options();
				Bitmap bitMap = BitmapFactory.decodeStream(is, null, options);
				mArr.add(new Basket(bitMap, name, author, score, score));
			}
		}
		dbHelp.close();
	}
	
	public void onAttach(Activity activity){
		super.onAttach(activity);
		mcontext = activity;
	}
}
