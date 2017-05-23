package cn.jishuz.library.until;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class LibraryTool {

	//时间戳转换为yyyy-MM-dd  HH:mm:ss格式
	public static String getDateToString(String timeStamp) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");  
        long lcc = Long.valueOf(timeStamp);  
        int i = Integer.parseInt(timeStamp);  
        String times = sdr.format(new Date(lcc * 1000L));  
		return times;
	}
	
	//掉此方法输入所要转换的时间输入例如（"2014年06月14日16时09分00秒"）返回时间戳  
    public static String data(String time) {  
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒",  
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
     * 判断某个服务是否正在运行的方法 
     *  
     * @param mContext 
     * @param serviceName 
     *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService） 
     * @return true代表正在运行，false代表服务没有正在运行 
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
