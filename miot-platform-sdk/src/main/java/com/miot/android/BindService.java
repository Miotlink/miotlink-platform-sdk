package com.miot.android;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;

import com.miot.android.content.NoFormatConsts;
import com.miot.android.receiver.MmwBroadCast;

public class BindService {
	
	
	public static BindService bindService=null;
	
	private Context context=null;
	
	private MyServiceConnection mConnection=null;
	
	public static synchronized BindService getInstance(Context context){
		if (bindService==null) {
			synchronized (BindService.class) {
				if (bindService==null) {
					bindService =new BindService(context);
				}
			}
		}
		return bindService;
		
	}
	
	private Bind bind=null;
	
	public void setBind(Bind bind) {
		this.bind = bind;
	}
	
	public Bind getBind() {
		
		return bind;
	}
	MmwBroadCast broadCast = new MmwBroadCast();
	public BindService(Context context){
		this.context=context;
		mConnection=new MyServiceConnection();
		Service.context = context;
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(NoFormatConsts.MOIT_PU_BROADCAST_MSG_ACTION);
		intentFilter.addAction(NoFormatConsts.NOTICE_LOGIN_OUT);
		intentFilter.addAction(NoFormatConsts.MIOT_PU_STATED_CHANAGED);
		intentFilter.setPriority(2147483647);
		context.registerReceiver(broadCast, intentFilter);
	}
	
	@Deprecated
	public void startBind(){
		if (bind!=null) {
			return;
		}
		if (mConnection==null) {
			mConnection=new MyServiceConnection();
		}
		Intent intent = new Intent(context.getPackageName());
		context.bindService(intent, mConnection,
				Service.BIND_AUTO_CREATE);
	}
	
	/**
	 * 兼容Android 5.0  
	 * @param packageName
	 */
	public void startBind(String packageName){
		if (mConnection==null) {
			mConnection=new MyServiceConnection();
		}
		Intent intent = new Intent(packageName);
		intent.setPackage(packageName);
		context.bindService(intent, mConnection,
				Service.BIND_AUTO_CREATE);
	}
	
	/**
	 * 兼容Android 5.0  
	 * @param packageName  包名称
	 * @param type 切换平台信息  1 杭州测试服务器 2 青岛服务器 
	 * 开发人员必须在杭州测试服务器上测试 测试成功完成后可以使用正式服务器
	 */
	public void startBind(int type,String packageName){
		NoFormatConsts.setPlarFormAddRess(type);
		if (mConnection==null) {
			mConnection=new MyServiceConnection();
		}
		if (TextUtils.isEmpty(packageName)){
			packageName=context.getPackageName();
		}
		Intent intent = new Intent(packageName);
		intent.setPackage(packageName);
		context.bindService(intent, mConnection,
				Service.BIND_AUTO_CREATE);
	}
	
	
	public void stopBind(){
		if (bind != null ) {
			context.unbindService(mConnection);
			context.stopService(new Intent(
					context.getPackageName()));
			setBind(null);
			context.unregisterReceiver(broadCast);
		}
	}
	
	
	class MyServiceConnection implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder service) {
			if (service instanceof Bind) {
				bindService.setBind((Bind) service);
			}
			
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			bindService.setBind(null);
			
		}
		
	}
	

}
