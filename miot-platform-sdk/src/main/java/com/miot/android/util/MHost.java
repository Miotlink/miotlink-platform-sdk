package com.miot.android.util;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.cncrit.qiaoqiao.VspOperation;
import com.miot.android.content.NoFormatConsts;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2018/7/26 0026.
 */

public interface MHost {

   public static class HostAddress extends AsyncTask<String, Void, String>{
    @Override
    protected String doInBackground(String... params) {
        String ip_devdiv = null;
        try {
            InetAddress[] inetAddressArr = InetAddress.getAllByName(params[0]);
            if (inetAddressArr!=null){
                if (inetAddressArr.length>1){
                    for (InetAddress inetAddress:inetAddressArr){
                        if (inetAddress instanceof Inet4Address){
                            ip_devdiv=inetAddress.getHostAddress();
                        }
                    }
                }else if (inetAddressArr.length==1){
                    ip_devdiv=inetAddressArr[0].getHostAddress();
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
        return ip_devdiv;
    }

    @SuppressLint("NewApi") @Override
    protected void onPostExecute(String result) {
        try{
            if (result!=null) {
                NoFormatConsts.PLATFORM_IP = result;
                VspOperation.rsIp = NoFormatConsts.PLATFORM_IP;
            }
        }catch(Exception e){

        }
        super.onPostExecute(result);

    }
    }
}
