/*
 * 
 */

package com.miot.android;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.cncrit.qiaoqiao.VspOperation;
import com.cncrit.qiaoqiao.WebService;
import com.miot.android.content.NoFormatConsts;
import com.miot.android.util.ACache;
import com.miot.android.util.Mhost;

// TODO: Auto-generated Javadoc
/**
 * The Class Service.
 * 
 * @ClassName: Service
 * @Description:
 * @author 作者 E-mail <a href="mailto:yubo@51box.cn">禹波</a>
 * @version 创建时间：2014-3-14 10:18:44 Service.
 */
public class Service extends android.app.Service {

	/**
	 * @ClassName: Service
	 * @Description:
	 * @author 作者 E-mail <a href="qiaozhuang@miotlink.com">qiaozhuang</a>
	 * @version 创建时间：2015-8-19 10:18:44 Bind type.
	 */

	/** The bind. */
	private Bind bind = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		if(ACache.get(this).getAsString(NoFormatConsts.PLATFORM_EXTERNAL_IP)==null){
			new Mhost.HostAddress().execute(NoFormatConsts.PLATFORM_EXTERNAL_IP);
		}else{
			Log.e("onCreate", ACache.get(this).getAsString(NoFormatConsts.PLATFORM_EXTERNAL_IP));
			VspOperation.rsIp=ACache.get(this).getAsString(NoFormatConsts.PLATFORM_EXTERNAL_IP);
		}
	}



	public static Context context;

	public static void sendBroadcast(String action, String[] params) {
		Log.e("", "sendBroadcast: Context=" + context + ",action='" + action
				+ "',params=" + params[0].toString() + ","
				+ (params.length > 1 ? params[1] : ""));
		if (context != null) {
			Intent intent = new Intent();
			intent.setAction(action);
			intent.putExtra("params", params);
			context.sendBroadcast(intent);
		}
	}

	public static void sendBroadcast(Context context, String action,
			String[] params) {
		if (context != null) {
			Intent intent = new Intent();
			intent.setAction(action);
			intent.putExtra("params", params);
			context.sendBroadcast(intent);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		WebService webService = new WebService(NoFormatConsts.NAMESPACE,
				NoFormatConsts.ENDPOINT);
		bind = new PlatformBind(webService);
		return bind;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
	}
	
	

}
