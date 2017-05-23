package cn.jishuz.library.fragment;

import cn.jishuz.library.R;
import cn.jishuz.library.activity.About;
import cn.jishuz.library.activity.FeedBack;
import cn.jishuz.library.activity.MeDetails;
import cn.jishuz.library.activity.Mes;
import cn.jishuz.library.activity.Say;
import cn.jishuz.library.db.Mydata;
import cn.jishuz.library.until.ActivityCollector;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

public class MeFragment extends Fragment {

	private TextView username;
	private String user = "silver";
	private Mydata dbHelp;
	private Context mContext;
	private Switch cont;
	private LinearLayout exit;//退出
	private LinearLayout me;//第一个 --- me
	private LinearLayout say;
	private LinearLayout message;
	private LinearLayout feedback;
	private LinearLayout about;

	public interface OpenListener {
		void onOpenComplete(Boolean open);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab04, container, false);
		username = (TextView) view.findViewById(R.id.me_username);
		cont = (Switch) view.findViewById(R.id.open_close);
		exit = (LinearLayout) view.findViewById(R.id.exit_app);
		me = (LinearLayout) view.findViewById(R.id.me_one);
		say = (LinearLayout) view.findViewById(R.id.me_say);
		message = (LinearLayout) view.findViewById(R.id.me_message);
		feedback = (LinearLayout) view.findViewById(R.id.me_feedback);
		about = (LinearLayout) view.findViewById(R.id.me_about);
		initData();
		initEvent();
		return view;
	}

	private void initData() {
		// TODO Auto-generated method stub
		dbHelp = new Mydata(mContext);
		Cursor cursor = dbHelp.queryOne("user", "_id", new String[] { "1" });
		if (cursor.moveToFirst()) {
			user = cursor.getString(cursor.getColumnIndex("username"));
		}
		username.setText(user);

		SharedPreferences pf = getActivity().getSharedPreferences("setting",
				mContext.MODE_PRIVATE);
		Boolean open = pf.getBoolean("open", false);
		cont.setChecked(open);
	}

	private void initEvent() {
		// TODO Auto-generated method stub
		cont.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor editor = getActivity()
						.getSharedPreferences("setting", mContext.MODE_PRIVATE)
						.edit();
				if (cont.isChecked()) {// 打开
					editor.putBoolean("open", true);
					OpenListener op = (OpenListener) getActivity();
					op.onOpenComplete(true);
				} else {
					editor.putBoolean("open", false);
					OpenListener op = (OpenListener) getActivity();
					op.onOpenComplete(false);
				}
				editor.commit();
			}
		});
		
		exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setIcon(R.drawable.me_author);
				builder.setTitle("提示");
				builder.setMessage("你确定要一键退出所有activity吗？");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						new ActivityCollector().FinishAll();
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
		});
		
		me.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(),MeDetails.class);
				getActivity().startActivity(i);
			}
		});
		
		say.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(),Say.class);
				getActivity().startActivity(i);
			}
		});
		
		message.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(),Mes.class);
				getActivity().startActivity(i);
			}
		});
		
		feedback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(),FeedBack.class);
				getActivity().startActivity(i);
			}
		});
		
		about.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(),About.class);
				getActivity().startActivity(i);
			}
		});
	}
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
	}

}
