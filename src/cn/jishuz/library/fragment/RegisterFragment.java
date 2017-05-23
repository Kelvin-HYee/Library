package cn.jishuz.library.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import cn.jishuz.library.R;
import cn.jishuz.library.activity.LoginActivity;
import cn.jishuz.library.db.Mydata;

@SuppressLint("NewApi")
public class RegisterFragment extends DialogFragment {

	private EditText mUsername;
	private EditText mPassword;
	private EditText mSex;
	private EditText mPhone;

	public interface RegListener {
		void onLoginInputComplete(String username, String password);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.register, null);
		mUsername = (EditText) view.findViewById(R.id.id_username);
		mPassword = (EditText) view.findViewById(R.id.id_password);
		mSex = (EditText) view.findViewById(R.id.id_sex);
		mPhone = (EditText) view.findViewById(R.id.id_phone);

		builder.setView(view)
				// Add action buttons
				.setPositiveButton("注册", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						if(mUsername.getText().toString().equals("admin") || mUsername.getText().toString().equals("null")){
							Toast.makeText(getActivity(), "用户名不能为admin或者null...", Toast.LENGTH_SHORT).show();
						}else{
							if (mUsername.getText().toString().equals("") || mPassword.getText().toString().equals("")) {
								Toast.makeText(getActivity(), "用户名或密码不能为空", Toast.LENGTH_LONG).show();
							} else {
	                    		Mydata my =new Mydata(getActivity());
	                    		SQLiteDatabase db = my.getWritableDatabase();
	                    		db.execSQL("insert into user VALUES(1,'"+mUsername.getText().toString()+"','"+mPassword.getText().toString()+"','"+mSex.getText().toString()+"','"+mPhone.getText().toString()+"','这个人好懒,什么都没说',null,null,null)");
	                    		db.close();
	                    		my.close();
	                    		
	                        	RegListener listener = (RegListener) getActivity();  
	                            listener.onLoginInputComplete(mUsername.getText().toString(), mPassword.getText().toString());
							}
						}
					}
				}).setNegativeButton("取消", null);
		builder.setCancelable(false);
		return builder.create();
	}
}
