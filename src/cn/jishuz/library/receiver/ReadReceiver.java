package cn.jishuz.library.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import cn.jishuz.library.service.ReadService;

public class ReadReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent i = new Intent(context,ReadService.class);
		context.startService(i);
	}

}
