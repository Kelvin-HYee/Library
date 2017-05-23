package cn.jishuz.library.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import cn.jishuz.library.R;
import cn.jishuz.library.adpter.MessageAdapter;
import cn.jishuz.library.db.Mydata;
import cn.jishuz.library.until.ActivityCollector;
import cn.jishuz.library.until.Message;

public class AdminMes extends Activity{

	private Mydata dbHelp;
	private ListView listView;
	private MessageAdapter adapter;
	private ArrayList<Message> mMessage = new ArrayList<Message>();
	private List<Integer> mId = new ArrayList<Integer>();
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.admin_mes);
		ActivityCollector.addActivity(this);
		
		adapter = new MessageAdapter(this, mMessage);
		listView = (ListView) findViewById(R.id.admin_mes);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(AdminMes.this, "点击没用 你长按试试吧~~", Toast.LENGTH_SHORT).show();
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				final int p = position;
				AlertDialog.Builder builder = new AlertDialog.Builder(AdminMes.this);
				builder.setTitle("警告");
				builder.setIcon(android.R.drawable.ic_input_delete);
				builder.setMessage("你确定要删除此条记录吗？");
				builder.setPositiveButton("确定", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dbHelp = new Mydata(AdminMes.this);
						SQLiteDatabase db = dbHelp.getReadableDatabase();
						db.delete("Message", "_id = ?", new String[]{mId.get(p).toString()});
						mMessage.remove(p);
						adapter = new MessageAdapter(AdminMes.this, mMessage);
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
		setData();
	}
	
	private void setData() {
		// TODO Auto-generated method stub
		dbHelp = new Mydata(this);
		SQLiteDatabase db = dbHelp.getReadableDatabase();
		Cursor cursor = db.query("message", null, "people = ?", new String[]{"1"}, null, null, "_id desc");
		if(cursor.moveToFirst()){
			do{
				String title = cursor.getString(cursor.getColumnIndex("title"));
				String content = cursor.getString(cursor.getColumnIndex("content"));
				String time = cursor.getString(cursor.getColumnIndex("datetime"));
				mId.add(cursor.getInt(cursor.getColumnIndex("_id")));
				mMessage.add(new Message(title, content, time));
			}while(cursor.moveToNext());
		}
		if(!cursor.moveToFirst()){//没有数据
			Toast.makeText(this, "暂时没有任何消息通知!即将返回...", Toast.LENGTH_LONG).show();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					finish();
				}
			}, 3000);
		}
		dbHelp.close();
	}
	
	public void onDestroy(Activity activity){
		super.onDestroy();
		ActivityCollector.removeActivity(activity);
	}
}
