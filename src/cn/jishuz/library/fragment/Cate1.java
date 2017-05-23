package cn.jishuz.library.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import cn.jishuz.library.R;
import cn.jishuz.library.activity.BookDetails;
import cn.jishuz.library.adpter.BookAdapter;
import cn.jishuz.library.db.Mydata;

public class Cate1 extends Fragment {

	private Mydata dbHelp;
	private Context mContext;

	private ArrayList<String> bookCore = new ArrayList<String>();
	private ArrayList<Bitmap> bookMap = new ArrayList<Bitmap>();
	private ArrayList<Integer> bookId = new ArrayList<Integer>();
	private Bitmap bitmap;
	private GridView girdView;
	private BookAdapter bookAdapter;
	private int key;
	private String search;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.cate1, container, false);
		getData();// 这里方法有OOM异常 写有注释 你去看看
		girdView = (GridView) view.findViewById(R.id.book_GirdView_all);
		bookAdapter = new BookAdapter(mContext, R.layout.bookadapter, bookCore, bookMap);
		girdView.setAdapter(bookAdapter);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		girdView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), BookDetails.class);
				intent.putExtra("type", "cate");
				intent.putExtra("id", bookId.get(position));
				getActivity().startActivity(intent);
				// Toast.makeText(mContext, ""+bookId.get(position)+"",
				// Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void getData() {
		dbHelp = new Mydata(mContext);
		SQLiteDatabase db = dbHelp.getReadableDatabase();
		switch (key) {
		case 1:
			gc();//回收图片
			Cursor cursor = dbHelp.queryAll("book");
			if (cursor.moveToFirst()) {
				do {
					AssetManager assets = getActivity().getAssets();
					InputStream is = null;
					try {
						is = assets.open("images/" + cursor.getString(cursor.getColumnIndex("Image")) + ".jpg");
					} catch (IOException e) {
						e.printStackTrace();
					}
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 4;
					bitmap = BitmapFactory.decodeStream(is, null, options); // 这里点快了会有OOM异常
																			// 同学来帮我解决可以？
					bookMap.add(bitmap);

					String name = cursor.getString(cursor.getColumnIndex("name"));
					String score = cursor.getString(cursor.getColumnIndex("score"));
					int bookid = cursor.getInt(cursor.getColumnIndex("_id"));

					bookId.add(bookid);
					bookCore.add(name + "\n" + score + "分");
				} while (cursor.moveToNext());
			}
			cursor.close();
			break;
		case 2:
			gc();
			Cursor cursor1= db.query("book", null, "score >= ?", new String[]{"7.5"},null, null, "_id desc");
			if (cursor1.moveToFirst()) {
				do {
					AssetManager assets = getActivity().getAssets();
					InputStream is = null;
					try {
						is = assets.open("images/" + cursor1.getString(cursor1.getColumnIndex("Image")) + ".jpg");
					} catch (IOException e) {
						e.printStackTrace();
					}
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 4;
					bitmap = BitmapFactory.decodeStream(is, null, options);

					bookMap.add(bitmap);

					String name = cursor1.getString(cursor1.getColumnIndex("name"));
					String score = cursor1.getString(cursor1.getColumnIndex("score"));
					int bookid = cursor1.getInt(cursor1.getColumnIndex("_id"));

					bookId.add(bookid);
					bookCore.add(name + "\n" + score + "分");
				} while (cursor1.moveToNext());
			}
			cursor1.close();
			db.close();
			break;
		case 3:
			gc();
			Cursor cursor2= db.query("book", null, "score < ?", new String[]{"7.5"},null, null, null);
			if (cursor2.moveToFirst()) {
				do {
					AssetManager assets = getActivity().getAssets();
					InputStream is = null;
					try {
						is = assets.open("images/" + cursor2.getString(cursor2.getColumnIndex("Image")) + ".jpg");
					} catch (IOException e) {
						e.printStackTrace();
					}
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 4;
					bitmap = BitmapFactory.decodeStream(is, null, options);

					bookMap.add(bitmap);

					String name = cursor2.getString(cursor2.getColumnIndex("name"));
					String score = cursor2.getString(cursor2.getColumnIndex("score"));
					int bookid = cursor2.getInt(cursor2.getColumnIndex("_id"));

					bookId.add(bookid);
					bookCore.add(name + "\n" + score + "分");
				} while (cursor2.moveToNext());
			}
			cursor2.close();
			db.close();
			break;
		case 4:
			gc();
			Cursor cursor4= db.query("book", null, "_id > ?", new String[]{"15"},null, null, null);
			if (cursor4.moveToFirst()) {
				do {
					AssetManager assets = getActivity().getAssets();
					InputStream is = null;
					try {
						is = assets.open("images/" + cursor4.getString(cursor4.getColumnIndex("Image")) + ".jpg");
					} catch (IOException e) {
						e.printStackTrace();
					}
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 4;
					bitmap = BitmapFactory.decodeStream(is, null, options);

					bookMap.add(bitmap);

					String name = cursor4.getString(cursor4.getColumnIndex("name"));
					String score = cursor4.getString(cursor4.getColumnIndex("score"));
					int bookid = cursor4.getInt(cursor4.getColumnIndex("_id"));

					bookId.add(bookid);
					bookCore.add(name + "\n" + score + "分");
				} while (cursor4.moveToNext());
			}
			if(cursor4.getCount() == 0){
				Toast.makeText(getActivity(), "管理员暂时还没有添加过书籍...", Toast.LENGTH_LONG).show();
			}
			cursor4.close();
			db.close();
			break;
		case 5:
			gc();
			Cursor cursor3= db.query("book", null, "_id <= ?", new String[]{"15"},null, null, "score desc");
			if (cursor3.moveToFirst()) {
				do {
					AssetManager assets = getActivity().getAssets();
					InputStream is = null;
					try {
						is = assets.open("images/" + cursor3.getString(cursor3.getColumnIndex("Image")) + ".jpg");
					} catch (IOException e) {
						e.printStackTrace();
					}
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 4;
					bitmap = BitmapFactory.decodeStream(is, null, options);

					bookMap.add(bitmap);

					String name = cursor3.getString(cursor3.getColumnIndex("name"));
					String score = cursor3.getString(cursor3.getColumnIndex("score"));
					int bookid = cursor3.getInt(cursor3.getColumnIndex("_id"));

					bookId.add(bookid);
					bookCore.add(name + "\n" + score + "分");
				} while (cursor3.moveToNext());
			}
			cursor3.close();
			db.close();
			break;
		case 6:
			gc();
			Cursor cursor5 = db.query("book", null, "name = ?", new String[]{search},null, null, null);
			if (cursor5.moveToFirst()) {
				do {
					AssetManager assets = getActivity().getAssets();
					InputStream is = null;
					try {
						is = assets.open("images/" + cursor5.getString(cursor5.getColumnIndex("Image")) + ".jpg");
					} catch (IOException e) {
						e.printStackTrace();
					}
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 4;
					bitmap = BitmapFactory.decodeStream(is, null, options);

					bookMap.add(bitmap);

					String name = cursor5.getString(cursor5.getColumnIndex("name"));
					String score = cursor5.getString(cursor5.getColumnIndex("score"));
					int bookid = cursor5.getInt(cursor5.getColumnIndex("_id"));

					bookId.add(bookid);
					bookCore.add(name + "\n" + score + "分");
				} while (cursor5.moveToNext());
			}
			if(cursor5.getCount() == 0){
				Toast.makeText(getActivity(), "查询为空...", Toast.LENGTH_LONG).show();
			}
			cursor5.close();
			db.close();
			break;
		default:
			break;
		}
		dbHelp.close();
	}

	private void gc() {
		// TODO Auto-generated method stub
		// 回收bitmap 预防OOM异常
		if (bitmap != null && !bitmap.isRecycled()) {
			// 回收并且置为null
			bitmap.recycle();
			bitmap = null;
		}
		System.gc();
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mContext = activity;
		key = getArguments().getInt("key");
		search = getArguments().getString("search");
	}

	public void onDestroy() {
		super.onDestroy();
		// 回收bitmap 预防OOM异常
		if (bitmap != null && !bitmap.isRecycled()) {
			// 回收并且置为null
			bitmap.recycle();
			bitmap = null;
		}
		System.gc();
	}

	public void onStop() {
		super.onStop();
		// 回收bitmap 预防OOM异常
		if (bitmap != null && !bitmap.isRecycled()) {
			// 回收并且置为null
			bitmap.recycle();
			bitmap = null;
		}
		System.gc();
		Log.i("hello", "sdasdsa asdasdasdasd asd asdasd qwrwe fgwq");
	}

}
