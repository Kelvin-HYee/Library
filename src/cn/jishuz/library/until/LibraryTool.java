package cn.jishuz.library.until;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class LibraryTool {

	//ʱ���ת��Ϊyyyy-MM-dd  HH:mm:ss��ʽ
	public static String getDateToString(String timeStamp) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");  
        long lcc = Long.valueOf(timeStamp);  
        int i = Integer.parseInt(timeStamp);  
        String times = sdr.format(new Date(lcc * 1000L));  
		return times;
	}
	
	//���˷���������Ҫת����ʱ���������磨"2014��06��14��16ʱ09��00��"������ʱ���  
    public static String data(String time) {  
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy��MM��dd��HHʱmm��ss��",  
                Locale.CHINA);  
        Date date;  
        String times = null;  
        try {  
            date = sdr.parse(time);  
            long l = date.getTime();  
            String stf = String.valueOf(l);  
            times = stf.substring(0, 10);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return times;  
    }  
    
    /** 
     * �ж�ĳ�������Ƿ��������еķ��� 
     *  
     * @param mContext 
     * @param serviceName 
     *            �ǰ���+��������������磺net.loonggg.testbackstage.TestService�� 
     * @return true�����������У�false�������û���������� 
     */  
    public boolean isServiceWork(Context mContext, String serviceName) {  
        boolean isWork = false;  
        ActivityManager myAM = (ActivityManager) mContext  
                .getSystemService(Context.ACTIVITY_SERVICE);  
        List<RunningServiceInfo> myList = myAM.getRunningServices(40);  
        if (myList.size() <= 0) {  
            return false;  
        }  
        for (int i = 0; i < myList.size(); i++) {  
            String mName = myList.get(i).service.getClassName().toString();  
            if (mName.equals(serviceName)) {  
                isWork = true;  
                break;  
            }  
        }  
        return isWork;  
    }  
    
}
