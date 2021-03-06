package com.cncrit.qiaoqiao.vsp;

import android.util.Log;

import com.cncrit.qiaoqiao.VspOperation;
import com.miot.android.Service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;



public class TcpSocket<IReceiver> implements Runnable{
	public static String tag = TcpSocket.class.getName();
    public static String DefaultCharSetName = "ISO-8859-1"; // "UTF-8"; // "ISO-8859-1";



	TcpSocket(IReceiver tsr){
		this.tsr = tsr;
	}
	protected void finalize() {
		this.disconnect();
	}
	
	public boolean hasConnect()	{
		return sc.isConnected();
	}
	private IReceiver tsr = null;
	private Socket sc = null;
	private InputStream is = null;
	private DataOutputStream os = null;
	private Thread recvThread = null;
	boolean isRunning = false;
	byte[] recvBuff = new byte[10240];
	
	private String ip=null;
	
	private int port=0;

	public boolean send(String sendString) {
		try {			
			return send(sendString.getBytes(DefaultCharSetName),sendString.length());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Socket getSc() {
		return sc;
	}

	public boolean send(byte[] sendBuff,int length) {
		try {
			synchronized (this)
			{ 
				if( sc != null && os != null ){
					os.write(sendBuff, 0, length);
					os.flush();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	private boolean isConnect=false;
	
	private int i=0;

	
	private static Boolean isNetWorkOk=null;
	private static byte[] syn = new byte[0];
	public static synchronized Boolean isNetWorkOk(){
		return isNetWorkOk;
	}
	public boolean connect(String strServerIP, int nPort) {
		try {
			this.ip=strServerIP;
			this.port=nPort;
			sc = new Socket();
			SocketAddress socketAddress = new InetSocketAddress(strServerIP,nPort);
			sc.connect(socketAddress, 10000);
			isConnect=true;
			synchronized (syn) {
				isNetWorkOk = true;
			}
			is = sc.getInputStream();
			os = new DataOutputStream(sc.getOutputStream());
			recvThread = new Thread(null, this);
			recvThread.start();
			
		} catch (UnknownHostException e) {
			isConnect=false;
			e.printStackTrace();
			VspOperation.loginFailCode=-1;
			VspOperation.loginFailErrorMess="连接服务器失败,检查网络";
			return false;
		} catch (IOException e) {
			isConnect=false;
			e.printStackTrace();
			VspOperation.loginFailErrorMess="连接服务器失败,检查网络";
			VspOperation.loginFailCode=-1;
			return false;
		} catch (Exception e) {
			isConnect=false;
			e.printStackTrace();
			VspOperation.loginFailErrorMess="连接服务器失败,检查网络";
			VspOperation.loginFailCode=-1;
			return false;
		}
		return true;
	}

	void finnalize(){
		this.disconnect();
	}
	
	public void disconnect() {
		isConnect=false;
		isRunning = false;
		try {
			if ( sc != null ) {
				if (sc.isConnected()) {
					sc.shutdownInput();			
					synchronized (this) { 
						sc.shutdownOutput();
						sc.close();
					}
				}
				sc = null;
				is = null;
				os = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		isRunning = true;
		while (isRunning) {
			i++;
			try {
				int recvLen = is.read(recvBuff);
				byte[] a = recvBuff;
				if (recvLen <= 0) {
					Thread.sleep(2);
				} else {
					if(tsr != null){
						((com.cncrit.qiaoqiao.vsp.IReceiver) tsr).onReceive(recvBuff, recvLen);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
			
		}
		if (isConnect) {
			Service.sendBroadcast("com.miot.android.MIOT_PLATFORM_RECONNECTED", new String []{"1",""});
			isConnect=false;
		 }

	}
}