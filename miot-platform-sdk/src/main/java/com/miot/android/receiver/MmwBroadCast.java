package com.miot.android.receiver;

import com.miot.android.content.NoFormatConsts;
import com.miot.android.listener.MiotDeviceCallback;
import com.miot.android.listener.MiotPlatformUIListener;
import com.miot.android.listener.MmwDevice;
import com.miot.android.util.MiotlinkUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MmwBroadCast extends BroadcastReceiver {
	
	public static MmwBroadCast broadCast=null;
	
	public static MmwDevice mmwDevice=null;
	public static MiotDeviceCallback miotDeviceCallback=null;
	public static MiotPlatformUIListener miotPlatformUIListener=null;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (intent.getAction().equals(NoFormatConsts.MOIT_PU_BROADCAST_MSG_ACTION)) {
			String[] params1 = intent.getStringArrayExtra("params");
			Integer pid = Integer.valueOf(params1[0]);
			String msg = params1[1];
			String uart="";
			uart=MiotlinkUtil.doLinkBindParse(msg);
			if (mmwDevice!=null) {
				mmwDevice.reciverDevice(pid, uart);
			}
			if (miotDeviceCallback!=null) {
				miotDeviceCallback.reciverDevice(pid, uart);
			}
			if (miotPlatformUIListener!=null) {
				try {
					Log.e("onReceiverDevice", pid+"");
					miotPlatformUIListener.onReceiverDevice(pid+"", uart);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}else if (intent.getAction().equals(NoFormatConsts.MIOT_PU_STATED_CHANAGED)) {
			String[] params = intent.getStringArrayExtra("params");
			if(params!=null && params.length>2)
			if (mmwDevice!=null) {
				mmwDevice.online(params[0],params[1],params[2]);
			}
			if (miotDeviceCallback!=null) {
				miotDeviceCallback.online(params[0],params[1],params[2]);
			}
			if (miotPlatformUIListener!=null) {
				try {
					miotPlatformUIListener.onReceiverDeviceState(params[0],params[1],params[2]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else if(intent.getAction().equals(NoFormatConsts.NOTICE_LOGIN_OUT)){
			if (mmwDevice!=null) {
				mmwDevice.logout();
			}
			if (miotDeviceCallback!=null) {
				miotDeviceCallback.logout();
			}
			if (miotPlatformUIListener!=null) {
				try {
					miotPlatformUIListener.onLogot("");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else if(intent.getAction().equals("com.miot.android.MIOT_PLATFORM_RECONNECTED")){
			String[] params1 = intent.getStringArrayExtra("params");
			if (params1.length>0) {
				if (miotPlatformUIListener!=null) {
					try {
						miotPlatformUIListener.serverDisconnected(Integer.parseInt(params1[0]), "与平台断开连接", System.currentTimeMillis()+"");
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
        
	}

}
